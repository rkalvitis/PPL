package serviceprovider.activities;

import com.example.ppl.R;
import com.hbb20.CountryCodePicker;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import serviceprovider.dao.ServiceDao;
import serviceprovider.dao.ServiceDaoImpl;
import serviceprovider.models.Service;
import users.activities.MainActivity;
import users.activities.SessionManager;
import users.activities.changePasswordActivity;

public class GetServiceProviderActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ServiceDao serviceDao = new ServiceDaoImpl();
    private CountryCodePicker countryCode;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_service_provider);

        Button logout = findViewById(R.id.btn_logout);
        sessionManager = new SessionManager(getApplicationContext());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        EditText nameInput = findViewById(R.id.value_name);
        EditText cityInput = findViewById(R.id.value_city);
        EditText adressInput = findViewById(R.id.value_adress);
        EditText emailInput = findViewById(R.id.value_email);
        EditText numberInput = findViewById(R.id.value_number);
        countryCode = findViewById(R.id.countryCodePicker);

        if (sessionManager != null) {
            String email = sessionManager.getEmail();
            if (email != null) {
                executorService.execute(() -> {
                    Service service = serviceDao.findServiceByEmail(email);
                    runOnUiThread(() -> {
                        if (service != null) {
                            if (nameInput != null) {
                                nameInput.setText(String.valueOf(service.getNosaukums()));
                            }
                            if (cityInput != null) {
                                cityInput.setText(String.valueOf(service.getPilseta()));
                            }
                            if (adressInput != null) {
                                adressInput.setText(String.valueOf(service.getAdrese()));
                            }
                            if (emailInput != null) {
                                emailInput.setText(String.valueOf(service.getEpasts()));
                            }
                            if (numberInput != null) {
                                numberInput.setText(String.valueOf(service.getTelefons()));
                            }
                            if (countryCode != null) {
                                countryCode.setCountryForPhoneCode(service.getValstsKods());
                                // Disable click functionality and make it non-focusable
                                countryCode.setCcpClickable(false);
                                countryCode.setClickable(false);
                                countryCode.setEnabled(false);
                                countryCode.setFocusable(false);
                                countryCode.setFocusableInTouchMode(false);
                            }
                            userId = service.getServisasniedzejs_ID();
                        }
                    });
                });
            }
        }
        Button changePasswordButton = findViewById(R.id.changePassword);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), changePasswordActivity.class);
                intent.putExtra("servisasniedzejs_ID", userId);
                startActivity(intent);
            }
        });
    }

    private void logoutUser() {
        // Clear user session
        sessionManager.clearSession();

        // Navigate to the login screen
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}