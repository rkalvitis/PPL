package serviceprovider.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.ppl.R;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import serviceprovider.dao.ServiceDao;
import serviceprovider.dao.ServiceDaoImpl;
import serviceprovider.models.Service;
import users.activities.getAllUsersActivity;
public class adminEditServiceActivity extends AppCompatActivity {
private Button Users;
    private EditText name;
    private EditText city;
    private EditText address;
    private EditText number;
    private EditText email;
    private CountryCodePicker countryCode;
    private final ServiceDao serviceDao = new ServiceDaoImpl();
    private ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_service);
        Users = findViewById(R.id.btn_users);
        ImageButton backButton = findViewById(R.id.returnBackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), getAllServicesActivity.class);
                startActivity(intent);
            }
        });
        Users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), getAllUsersActivity.class);
                startActivity(intent);
            }
        });
        name = findViewById(R.id.value_name);
        city = findViewById(R.id.value_city);
        address = findViewById(R.id.value_adress);
        number = findViewById(R.id.value_number);
        email = findViewById(R.id.value_email);
        countryCode = findViewById(R.id.countryCodePicker);

        int serviceId = getIntent().getIntExtra("servisasniedzejs_ID", -1);

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Service service = serviceDao.getServiceProvider(serviceId);
            runOnUiThread(() -> {
                if (service != null) {
                    if (name != null) {
                        name.setText(String.valueOf(service.getNosaukums()));
                    }
                    if (city != null) {
                        city.setText(String.valueOf(service.getPilseta()));
                    }
                    if (address != null) {
                        address.setText(String.valueOf(service.getAdrese()));
                    }
                    if (email != null) {
                        email.setText(String.valueOf(service.getEpasts()));
                    }
                    if (number != null) {
                        number.setText(String.valueOf(service.getTelefons()));
                    }
                    if (countryCode != null) {
                        countryCode.setCountryForPhoneCode(service.getValstsKods());
                    }
                }
            });
        });
        Button saveButton = findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validateInputs(name, city,address, email, number)) {
                        executorService.execute(() -> {
                            Service service = serviceDao.getServiceProvider(serviceId);
                            if (service != null) {
                                // Update the user object with the new values
                                service.setNosaukums(name.getText().toString().trim());
                                service.setPilseta(city.getText().toString().trim());
                                service.setAdrese(address.getText().toString().trim());
                                service.setEpasts(email.getText().toString().trim());
                                service.setTelefons(Integer.parseInt(number.getText().toString().trim()));
                                service.setValstsKods(countryCode.getSelectedCountryCodeAsInt());

                                boolean success = serviceDao.updateServiceProvider(service);
                                runOnUiThread(() -> {
                                    if (success) {
                                        Toast.makeText(adminEditServiceActivity.this, "Servisa sniedzēja informācija veiksmīgi atjaunota!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(adminEditServiceActivity.this, "Servisa sniedzēja informācijas atjaunošana bija neveiksmīga!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(adminEditServiceActivity.this, "Radās neparedzēta kļūda. Lūdzu, mēģiniet vēlreiz!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        Button deleteButton = findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(() -> {
                    boolean success = serviceDao.deleteServiceProvider(serviceId);
                    runOnUiThread(() -> {
                        if (success) {
                            Toast.makeText(adminEditServiceActivity.this, "Servisa sniedzējs veiksmīgi izdzēsts!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(adminEditServiceActivity.this, getAllServicesActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(adminEditServiceActivity.this, "Neizdevās izdzēst servisa sniedzēju!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });
    }
    private boolean validateInputs(EditText name, EditText city, EditText address, EditText email, EditText number) {
        String nameText = name.getText().toString().trim();
        String cityText = city.getText().toString().trim();
        String addressText = address.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String numberText = number.getText().toString().trim();

        if (nameText.isEmpty() || cityText.isEmpty() || addressText.isEmpty() || emailText.isEmpty() || numberText.isEmpty()) {
            Toast.makeText(adminEditServiceActivity.this, "Nav aizpildīti visi obligātie ievadlauki!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!nameText.matches("[a-zA-Z\\s]+")  || !cityText.matches("[a-zA-Z]+")) {
            Toast.makeText(adminEditServiceActivity.this, "Servisa nosaukumam un pilsētas nosaukumam ir jāsatur tikai burti!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (nameText.length() > 50 || cityText.length() > 50 || addressText.length() > 50) {
            Toast.makeText(adminEditServiceActivity.this, "Ievadītais servisa nosaukums vai pilsētas, vai adreses nosaukums ir pārāk garšs!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(adminEditServiceActivity.this, "Nekorekts e-pasta formāts!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.PHONE.matcher(numberText).matches() || number.length() !=8) {
            Toast.makeText(adminEditServiceActivity.this, "Nekorekts telefona numura formāts!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the executor service when the activity is destroyed.
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}