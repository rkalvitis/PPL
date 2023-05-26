package users.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.ppl.R;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import serviceprovider.dao.ServiceDao;
import serviceprovider.dao.ServiceDaoImpl;
import serviceprovider.models.Service;
import users.dao.UserDao;
import users.dao.UserDaoImpl;
import users.models.User;

public class changePasswordActivity extends AppCompatActivity {
    private ExecutorService executorService;
    private EditText userPassword;
    private EditText userPasswordRepeat;
    private final UserDao userDao = new UserDaoImpl();
    private final ServiceDao serviceDao = new ServiceDaoImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPassword = findViewById(R.id.firstPassword);
                userPasswordRepeat = findViewById(R.id.secondPassword);
                EditText oldPassword = findViewById(R.id.oldPassword);
                String oldPasswordText = oldPassword.getText().toString().trim();
                String passwordText = userPassword.getText().toString().trim();

                int userId = getIntent().getIntExtra("lietotajs_ID", -1);
                executorService = Executors.newSingleThreadExecutor();

                executorService.execute(() -> {
                    User user = userDao.getUser(userId);
                    if (user != null) {
                        if (checkOldPassword(user, oldPasswordText)) {
                            if (validateInputs(userPassword, userPasswordRepeat)) {
                                // Update the user object with the new values
                                boolean success = userDao.updatePassword(userId, passwordText);
                                runOnUiThread(() -> {
                                    if (success) {
                                        Toast.makeText(changePasswordActivity.this, "Parole tika veiksmīgi nomainīta!", Toast.LENGTH_SHORT).show();
                                        finish(); // Close the activity after updating the password
                                    } else {
                                        Toast.makeText(changePasswordActivity.this, "Neizdevās nomainīt paroli!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(changePasswordActivity.this, "Patreizējā parole nav pareiza!", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else if (user == null) {
                        int serviceId = getIntent().getIntExtra("servisasniedzejs_ID", -1);
                        executorService.execute(() -> {
                            Service service = serviceDao.getServiceProvider(serviceId);
                            if (service != null) {
                                if (checkOldPasswordSevice(service, oldPasswordText)) {
                                    if (validateInputs(userPassword, userPasswordRepeat)) {
                                        // Update the user object with the new values
                                        boolean success = serviceDao.updatePassword(serviceId, passwordText);
                                        runOnUiThread(() -> {
                                            if (success) {
                                                Toast.makeText(changePasswordActivity.this, "Parole tika veiksmīgi nomainīta!", Toast.LENGTH_SHORT).show();
                                                finish(); // Close the activity after updating the password
                                            } else {
                                                Toast.makeText(changePasswordActivity.this, "Neizdevās nomainīt paroli!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(() -> {
                                        Toast.makeText(changePasswordActivity.this, "Patreizējā parole nav pareiza!", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
        ImageButton backButton = findViewById(R.id.returnBackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous activity
                finish();
            }
        });
    }
    private boolean checkOldPassword(User user, String oldPassword) {
        return user.getParole().equals(oldPassword);
    }
    private boolean checkOldPasswordSevice(Service service, String oldPassword) {
        return service.getParole().equals(oldPassword);
    }
    private boolean validateInputs(EditText password, EditText confirmPassword) {
        String passwordText = password.getText().toString().trim();
        String passwordTextAgain = confirmPassword.getText().toString().trim();
        if (passwordText.isEmpty() || passwordTextAgain.isEmpty()) {
            runOnUiThread(() -> {
                Toast.makeText(changePasswordActivity.this, "Nav aizpildīti visi lauki!", Toast.LENGTH_SHORT).show();
            });
            return false;
        }
        if (!passwordText.equals(passwordTextAgain)) {
            runOnUiThread(() -> {
                Toast.makeText(changePasswordActivity.this, "Ievadītās paroles nesakrīt!", Toast.LENGTH_SHORT).show();
            });
            return false;
        }
        if (passwordText.length() < 8 || !passwordText.matches(".*\\d.*") || !passwordText.matches(".*[A-Z].*")) {
            runOnUiThread(() -> {
                Toast.makeText(changePasswordActivity.this, "Parolei jāsatur vismaz 8 simboli, vismaz viens cipars un lielais burts!", Toast.LENGTH_SHORT).show();
            });
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

