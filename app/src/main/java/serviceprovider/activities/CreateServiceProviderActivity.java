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
import users.activities.SessionManager;

public class CreateServiceProviderActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText cityEditText;
    private EditText adressEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    private EditText numberEditText;
    private CountryCodePicker countryCode;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ServiceDao serviceDao = new ServiceDaoImpl();
    private CountryCodePicker countryCodePicker;
    private String selectedCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service_provider);

        ImageButton backButton = findViewById(R.id.returnBackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous activity
                finish();
            }
        });
        nameEditText = findViewById(R.id.nosaukums);
        cityEditText = findViewById(R.id.city);
        adressEditText = findViewById(R.id.adress);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.passwordAgain);
        numberEditText = findViewById(R.id.number);
        registerButton = findViewById(R.id.registerBtn);
        countryCode = findViewById(R.id.countryCodePicker);

        // handle user choice of country number for phone number
        countryCodePicker = findViewById(R.id.countryCodePicker);
        selectedCountryCode = countryCodePicker.getDefaultCountryCode();
        countryCodePicker.setOnCountryChangeListener(() -> {
            selectedCountryCode = countryCodePicker.getSelectedCountryCode();
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String city = cityEditText.getText().toString().trim();
                String adress = adressEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                String numberString = numberEditText.getText().toString().trim();
                String countryCode = selectedCountryCode;
                int countryCodeInt = Integer.parseInt(countryCode);
                SessionManager sessionManager = new SessionManager(getApplicationContext());

                try {
                    if (validateInputs(name, city, adress, email, password, confirmPassword, numberString)) {
                        // Check if the numberString is not empty before parsing
                        if (!numberString.isEmpty()) {
                            Integer number = Integer.parseInt(numberString);
                            // Check if the email is not already registered
                            executorService.execute(() -> {
                                Service service = serviceDao.findServiceByEmail(email);
                                runOnUiThread(() -> {
                                    if (service != null) {
                                        Toast.makeText(CreateServiceProviderActivity.this, "Servisa sniedzējs ar šādu e-pastu jau eksistē", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Service newService = new Service();
                                        newService.setNosaukums(name);
                                        newService.setEpasts(email);
                                        newService.setPilseta(city);
                                        newService.setAdrese(adress);
                                        newService.setTelefons(number);
                                        newService.setValstsKods(countryCodeInt);
                                        newService.setParole(password);
                                        executorService.execute(() -> {
                                            boolean success = serviceDao.createServiceProvider(newService);
                                            runOnUiThread(() -> {
                                                if (success) {
                                                    sessionManager.saveEmail(email);
                                                    Toast.makeText(CreateServiceProviderActivity.this, "Reģistrēšanās ir veiksmīga!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), GetServiceProviderActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(CreateServiceProviderActivity.this, "Reģistrēšanās ir neveiksmīga!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        });
                                    }
                                });
                            });
                        } else {
                            Toast.makeText(CreateServiceProviderActivity.this, "Radās neparedzēta kļūda. Lūdzu, mēģiniet vēlreiz!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(CreateServiceProviderActivity.this, "Nepareiz numura formāts!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (Exception e) {
                    Toast.makeText(CreateServiceProviderActivity.this, "Radās neparedzēta kļūda. Lūdzu, mēģiniet vēlreiz!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            private boolean validateInputs(String name, String city, String adress, String email, String password, String confirmPassword, String number) {
                if (name.isEmpty() || city.isEmpty() || adress.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || number.isEmpty()) {
                    Toast.makeText(CreateServiceProviderActivity.this, "Nav aizpildīti visi obligātie ievadlauki!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!name.matches("[a-zA-Z\\s]+")  || !city.matches("[a-zA-Z]+")) {
                    Toast.makeText(CreateServiceProviderActivity.this, "Servisa nosaukumam un pilsētas nosaukumam ir jāsatur tikai burti!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (name.length() > 50 || city.length() > 50 || adress.length() > 50) {
                    Toast.makeText(CreateServiceProviderActivity.this, "Ievadītais servisa nosaukums vai pilsētas, vai adreses nosaukums ir pārāk garšs!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(CreateServiceProviderActivity.this, "Nekorekts e-pasta formāts!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!Patterns.PHONE.matcher(number).matches() || number.length() !=8) {
                    Toast.makeText(CreateServiceProviderActivity.this, "Nekorekts telefona numura formāts!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[A-Z].*")) {
                    Toast.makeText(CreateServiceProviderActivity.this, "Parolei jāsatur vismaz 8 simboli, vismaz viens cipars un lielais burts!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(CreateServiceProviderActivity.this, "Ievadītās paroles nesakrīt!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });
    }
}