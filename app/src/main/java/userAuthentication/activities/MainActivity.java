package userAuthentication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import userAuthentication.dbo.DBHelper;
import userAuthentication.dbo.UserDbo;
import userAuthentication.models.User;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final UserDbo userDbo = new DBHelper();
    private final ServiceDao serviceDao = new ServiceDaoImpl();

    //DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openResetPasswordButton = findViewById(R.id.forgotPassword);
        Button openRegisterButton = findViewById(R.id.btn_register);
        Button openLoginButton = findViewById(R.id.btn_login);

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
                    new AsyncTask<String, Void, User>() {
                        @Override
                        protected User doInBackground(String... params) {
                            String email = params[0];
                            String password = params[1];
                            return userDbo.findRegisteredUser(email, password);
                        }

                        @Override
                        protected void onPostExecute(User user) {
                            if (user != null) {
                                if (userEmail != null) {
                                    sessionManager.saveEmail(userEmail);
                                }
                                int admin = user.getIsAdmin();

                                if (admin == 1) {
                                    Toast.makeText(MainActivity.this, "Pieteikšanās noritēja veiksmīgi!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), adminFunctionalityActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Pieteikšanās noritēja veiksmīgi!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), UserMainPageActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                new AsyncTask<String, Void, Service>() {
                                    @Override
                                    protected Service doInBackground(String... params) {
                                        String email = params[0];
                                        String password = params[1];
                                        return serviceDao.findRegisteredService(email, password);
                                    }

                                    @Override
                                    protected void onPostExecute(Service service) {
                                        if (service != null) {
                                            if (userEmail != null) {
                                                sessionManager.saveEmail(userEmail);
                                            }
                                            Toast.makeText(MainActivity.this, "Pieteikšanās noritēja veiksmīgi!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), GetServiceProviderActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(MainActivity.this, "Nepareizi pieteikšanās dati!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }.execute(userEmail, userPassword);
                            }
                        }
                    }.execute(userEmail, userPassword);
                }
            }
        });

    }
}