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
import com.example.ppl.R;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import serviceprovider.activities.getAllServicesActivity;
import users.dao.UserDao;
import users.dao.UserDaoImpl;
import users.models.User;

public class adminEditUserActivity extends AppCompatActivity {
    private EditText userName;
    private EditText userSurname;
    private EditText userNumber;
    private EditText userEmail;
    private CountryCodePicker countryCode;
    private final UserDao userDao = new UserDaoImpl();
    private ExecutorService executorService;
    private Button Services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_users);
        Services = findViewById(R.id.btn_serivces);
        ImageButton backButton = findViewById(R.id.returnBackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), getAllUsersActivity.class);
                startActivity(intent);
            }
        });
        Services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), getAllServicesActivity.class);
                startActivity(intent);
            }
        });
        userName = findViewById(R.id.value_name);
        userSurname = findViewById(R.id.value_city);
        userNumber = findViewById(R.id.value_number);
        userEmail = findViewById(R.id.value_email);
        countryCode = findViewById(R.id.countryCodePicker);

        int userId = getIntent().getIntExtra("lietotajs_ID", -1);

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            User user = userDao.getUser(userId);
            runOnUiThread(() -> {
                if (user != null) {
                    if (userName != null) {
                        userName.setText(String.valueOf(user.getVards()));
                    }
                    if (userSurname != null) {
                        userSurname.setText(String.valueOf(user.getUzvards()));
                    }
                    if (userEmail != null) {
                        userEmail.setText(String.valueOf(user.getEpasts()));
                    }
                    if (userNumber != null) {
                        userNumber.setText(String.valueOf(user.getTelefons()));
                    }
                    if (countryCode != null) {
                        countryCode.setCountryForPhoneCode(user.getValstsKods());
                    }
                }
            });
        });
         Button saveButton = findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validateInputs(userName, userSurname, userEmail, userNumber)) {
                        executorService.execute(() -> {
                            User user = userDao.getUser(userId);
                            if (user != null) {
                                // Update the user object with the new values
                                user.setVards(userName.getText().toString().trim());
                                user.setUzvards(userSurname.getText().toString().trim());
                                user.setEpasts(userEmail.getText().toString().trim());
                                user.setTelefons(Integer.parseInt(userNumber.getText().toString().trim()));
                                user.setValstsKods(countryCode.getSelectedCountryCodeAsInt());

                                boolean success = userDao.updateUser(user);
                                runOnUiThread(() -> {
                                    if (success) {
                                        Toast.makeText(adminEditUserActivity.this, "Lietotāja informācija veiksmīgi atjaunota!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(adminEditUserActivity.this, "Lietotāja informācijas atjaunošana bija neveiksmīga!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(adminEditUserActivity.this, "Radās neparedzēta kļūda. Lūdzu, mēģiniet vēlreiz!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        Button deleteButton = findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(() -> {
                    boolean success = userDao.deleteUser(userId);
                    runOnUiThread(() -> {
                        if (success) {
                            Toast.makeText(adminEditUserActivity.this, "Lietotājs veiksmīgi izdzēsts!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(adminEditUserActivity.this, getAllUsersActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(adminEditUserActivity.this, "Neizdevās izdzēst lietotāju!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });
    }
    private boolean validateInputs(EditText name, EditText surname, EditText email, EditText number) {
        String nameText = name.getText().toString().trim();
        String surnameText = surname.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String numberText = number.getText().toString().trim();

        if (nameText.isEmpty() || surnameText.isEmpty() || emailText.isEmpty() || numberText.isEmpty()) {
            Toast.makeText(adminEditUserActivity.this, "Nav aizpildīti visi obligātie ievadlauki!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!nameText.matches("[a-zA-Z]+") || !surnameText.matches("[a-zA-Z]+")) {
            Toast.makeText(adminEditUserActivity.this, "Vārdam un uzvārdam ir jāsatur tikai burti!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(adminEditUserActivity.this, "Nekorekts e-pasta formāts!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.PHONE.matcher(numberText).matches() || number.length() !=8) {
            Toast.makeText(adminEditUserActivity.this, "Nekorekts telefona numura formāts!", Toast.LENGTH_SHORT).show();
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
