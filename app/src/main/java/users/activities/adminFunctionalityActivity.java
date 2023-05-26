package users.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.ppl.R;
import serviceprovider.activities.getAllServicesActivity;

public class adminFunctionalityActivity extends AppCompatActivity {
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_functionality);

        Button usersButton = findViewById(R.id.btn_edit_users);
        Button servicesButton = findViewById(R.id.btn_edit_services);

        usersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminFunctionalityActivity.this, getAllUsersActivity.class);
                startActivity(intent);
            }
        });
        servicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminFunctionalityActivity.this, getAllServicesActivity.class);
                startActivity(intent);
            }
        });
        Button logout = findViewById(R.id.btn_logout);
        sessionManager = new SessionManager(getApplicationContext());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
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