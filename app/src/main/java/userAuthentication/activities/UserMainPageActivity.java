package userAuthentication.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import userAuthentication.dbo.DBHelper;
import userAuthentication.dbo.UserDbo;
import userAuthentication.models.User;

import com.example.ppl.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserMainPageActivity extends AppCompatActivity {
    DBHelper DB;
    SessionManager sessionManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final UserDbo userDbo = new DBHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page);

        Button logout = findViewById(R.id.btn_logout);
        // DB = new DBHelper(this);
         sessionManager = new SessionManager(getApplicationContext());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        EditText nameInput = findViewById(R.id.value_name);
        EditText surnameInput = findViewById(R.id.value_surname);
        EditText emailInput = findViewById(R.id.value_email);
        EditText numberInput = findViewById(R.id.value_number);
        if (sessionManager != null) {

            String email = sessionManager.getEmail();
            if (email != null) {
                executorService.execute(() -> {
                    User user = userDbo.findUserByEmail(email);
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
                        }
                    });
                });
            }
        }
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