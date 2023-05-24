package userAuthentication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ppl.R;

import userAuthentication.dbo.DBHelper;
import userAuthentication.dbo.UserDbo;
import userAuthentication.models.User;

public class adminEditUserActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private EditText userName;
    private EditText userSurname;
    private EditText userNumber;
    private EditText userEmail;
    private final DBHelper userDbo = new DBHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_users);

        Button logout = findViewById(R.id.btn_logout);
        sessionManager = new SessionManager(getApplicationContext());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        userName = findViewById(R.id.value_name);
        userSurname = findViewById(R.id.value_surname);
        userNumber = findViewById(R.id.value_number);
        userEmail = findViewById(R.id.value_email);

        int userId = getIntent().getIntExtra("lietotajs_ID", -1);

        new GetUserTask().execute(userId);
    }

    private void logoutUser() {
        sessionManager.clearSession();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private class GetUserTask extends AsyncTask<Integer, Void, User> {
        @Override
        protected User doInBackground(Integer... params) {
            int userId = params[0];
            return userDbo.getUser(userId);
        }

        @Override
        protected void onPostExecute(User user) {
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
            }
        }
    }
}
