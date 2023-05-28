package users.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import serviceprovider.activities.SearchServicesProviderActivity;
import EventModule.actions.GetEventActivity;
import users.dao.UserDaoImpl;
import users.dao.UserDao;
import users.models.User;
import com.example.ppl.R;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserMainPageActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final UserDao userDao = new UserDaoImpl();
    private CountryCodePicker countryCode;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page);

        Button logout = findViewById(R.id.btn_logout);
        sessionManager = new SessionManager(getApplicationContext());
        
        Button searchServices = findViewById(R.id.btn_search_sevice_provider);
        addRedirectListener(searchServices, SearchServicesProviderActivity.class);

        Button getEvent = findViewById(R.id.btn_get_event);
        addRedirectListener(getEvent, GetEventActivity.class);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        EditText nameInput = findViewById(R.id.value_name);
        EditText surnameInput = findViewById(R.id.value_city);
        EditText emailInput = findViewById(R.id.value_email);
        EditText numberInput = findViewById(R.id.value_number);
        countryCode = findViewById(R.id.countryCodePicker);

        if (sessionManager != null) {
            String email = sessionManager.getEmail();
            if (email != null) {
                executorService.execute(() -> {
                    User user = userDao.findUserByEmail(email);
                    runOnUiThread(() -> {
                        if (user != null) {
                            if (nameInput != null) {
                                nameInput.setText(String.valueOf(user.getVards()));
                            }
                            if (surnameInput != null) {
                                surnameInput.setText(String.valueOf(user.getUzvards()));
                            }
                            if (emailInput != null) {
                                emailInput.setText(String.valueOf(user.getEpasts()));
                            }
                            if (numberInput != null) {
                                numberInput.setText(String.valueOf(user.getTelefons()));
                            }
                            if (countryCode != null) {
                                countryCode.setCountryForPhoneCode(user.getValstsKods());
                                // Disable click functionality and make it non-focusable
                                countryCode.setCcpClickable(false);
                                countryCode.setClickable(false);
                                countryCode.setEnabled(false);
                                countryCode.setFocusable(false);
                                countryCode.setFocusableInTouchMode(false);
                            }
                            userId = user.getLietotajs_ID();
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
                intent.putExtra("lietotajs_ID", userId);
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

    private void addRedirectListener(Button b, Class<?> cls){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainPageActivity.this, cls);
                startActivity(intent);
            }
        });

    }
}