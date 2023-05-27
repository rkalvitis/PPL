package users.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ppl.R;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import serviceprovider.activities.GetServiceProviderActivity;
import serviceprovider.dao.ServiceDao;
import serviceprovider.dao.ServiceDaoImpl;
import serviceprovider.models.Service;
import users.dao.UserDaoImpl;
import users.dao.UserDao;
import users.models.User;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private ExecutorService executorService;
    private final UserDao userDao = new UserDaoImpl();
    private final ServiceDao serviceDao = new ServiceDaoImpl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openResetPasswordButton = findViewById(R.id.forgotPassword);
        Button openRegisterButton = findViewById(R.id.btn_register);
        Button openLoginButton = findViewById(R.id.btn_send);

        openResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to launch the new activity
                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        openRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to launch the new activity
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        openLoginButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                email = findViewById(R.id.userEmail);
                password = findViewById(R.id.userPassword);

                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                SessionManager sessionManager = new SessionManager(getApplicationContext());
                if (userEmail.equals("") || userPassword.equals("")) {
                    Toast.makeText(MainActivity.this, "Lūdzu ievadiet visus laukus", Toast.LENGTH_SHORT).show();
                } else {
                    executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(() -> {
                        try {
                            String email = userEmail;
                            String password = userPassword;
                            User user = userDao.findRegisteredUser(email, password);
                            if (user != null) {
                                if (userEmail != null) {
                                    sessionManager.saveEmail(userEmail);
                                }
                                int admin = user.getIsAdmin();
                                runOnUiThread(() -> {
                                    Toast.makeText(MainActivity.this, "Pieteikšanās noritēja veiksmīgi!", Toast.LENGTH_SHORT).show();
                                    Intent intent = (admin == 1) ?
                                            new Intent(getApplicationContext(), adminFunctionalityActivity.class) :
                                            new Intent(getApplicationContext(), UserMainPageActivity.class);
                                    startActivity(intent);
                                });
                            } else {
                                Service service = serviceDao.findRegisteredService(email, password);
                                if (service != null) {
                                    if (userEmail != null) {
                                        sessionManager.saveEmail(userEmail);
                                    }
                                    runOnUiThread(() -> {
                                        Toast.makeText(MainActivity.this, "Pieteikšanās noritēja veiksmīgi!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), GetServiceProviderActivity.class);
                                        startActivity(intent);
                                    });
                                } else {
                                    runOnUiThread(() ->
                                            Toast.makeText(MainActivity.this, "Nepareizi pieteikšanās dati!", Toast.LENGTH_SHORT).show()
                                    );
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    // Remember to shutdown the executor service when it's no longer needed.
                    executorService.shutdown();
                }
            }
        });
    }
}