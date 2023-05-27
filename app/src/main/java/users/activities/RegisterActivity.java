package users.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import serviceprovider.activities.CreateServiceProviderActivity;
import users.dao.UserDaoImpl;
import users.dao.UserDao;
import users.models.User;
import com.example.ppl.R;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    private Button register_serviss_Button;
    private EditText numberEditText;
    private CountryCodePicker countryCode;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final UserDao userDao = new UserDaoImpl();
    private CountryCodePicker countryCodePicker;
    private String selectedCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageButton backButton = findViewById(R.id.returnBackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous activity
                finish();
            }
        });

        nameEditText = findViewById(R.id.nosaukums);
        surnameEditText = findViewById(R.id.city);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.passwordAgain);
        numberEditText = findViewById(R.id.number);
        registerButton = findViewById(R.id.registerBtn);
        countryCode = findViewById(R.id.countryCodePicker);

        // for services registration
        register_serviss_Button = findViewById(R.id.register_serviss);
        register_serviss_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to launch the new activity
                Intent intent = new Intent(RegisterActivity.this, CreateServiceProviderActivity.class);
                startActivity(intent);
            }
        });
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
                String surname = surnameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                String numberString = numberEditText.getText().toString().trim();
                String countryCode = selectedCountryCode;
                int countryCodeInt = Integer.parseInt(countryCode);
                SessionManager sessionManager = new SessionManager(getApplicationContext());

                try {
                    if (validateInputs(name, surname, email, password, confirmPassword, numberString)) {
                        // Check if the numberString is not empty before parsing
                        if (!numberString.isEmpty()) {
                            Integer number = Integer.parseInt(numberString);
                            // Check if the email is not already registered
                            executorService.execute(() -> {
                                User user = userDao.findUserByEmail(email);
                                runOnUiThread(() -> {
                                    if (user != null) {
                                        Toast.makeText(RegisterActivity.this, "Lietotājs ar šādu e-pastu jau eksistē", Toast.LENGTH_SHORT).show();
                                    } else {
                                        User newUser = new User();
                                        newUser.setVards(name);
                                        newUser.setUzvards(surname);
                                        newUser.setEpasts(email);
                                        newUser.setParole(password);
                                        newUser.setTelefons(number);
                                        newUser.setIsAdmin(0);
                                        newUser.setValstsKods(countryCodeInt);
                                        executorService.execute(() -> {
                                            boolean success = userDao.insertUser(newUser);
                                            runOnUiThread(() -> {
                                                if (success) {
                                                    sessionManager.saveEmail(email);
                                                    Toast.makeText(RegisterActivity.this, "Reģistrēšanās ir veiksmīga!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), UserMainPageActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Reģistrēšanās ir neveiksmīga!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        });
                                    }
                                });
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Radās neparedzēta kļūda. Lūdzu, mēģiniet vēlreiz!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(RegisterActivity.this, "Nepareiz numura formāts!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "Radās neparedzēta kļūda. Lūdzu, mēģiniet vēlreiz!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            private boolean validateInputs(String name, String surname, String email, String password, String confirmPassword, String number) {
                if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || number.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Nav aizpildīti visi obligātie ievadlauki!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!name.matches("[a-zA-Z]+") || !surname.matches("[a-zA-Z]+")) {
                    Toast.makeText(RegisterActivity.this, "Vārdam un uzvārdam ir jāsatur tikai burti!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (name.length() > 20 || name.length() > 20) {
                    Toast.makeText(RegisterActivity.this, "Ievadītāis vārds un/vai uzvārds ir pārāk garšs!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this, "Nekorekts e-pasta formāts!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!Patterns.PHONE.matcher(number).matches() || number.length() !=8) {
                    Toast.makeText(RegisterActivity.this, "Nekorekts telefona numura formāts!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[A-Z].*")) {
                    Toast.makeText(RegisterActivity.this, "Parolei jāsatur vismaz 8 simboli, vismaz viens cipars un lielais burts!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Ievadītās paroles nesakrīt!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });
    }
}
