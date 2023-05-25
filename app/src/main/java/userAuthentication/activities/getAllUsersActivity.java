package userAuthentication.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;


import com.example.ppl.R;

import java.util.List;

import userAuthentication.dbo.DBHelper;
import userAuthentication.models.User;

public class getAllUsersActivity extends AppCompatActivity implements userAdapter.OnItemClickListener {
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private userAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_users);
        dbHelper = new DBHelper();

        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new userAdapter();
        userAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(userAdapter);

        // Execute the database query on a background thread
        new QueryUsersTask().execute();
    }

    private class QueryUsersTask extends AsyncTask<Void, Void, List<User>> {
        @Override
        protected List<User> doInBackground(Void... voids) {
            return dbHelper.getAllUsersWithIsAdminZero();
        }

        @Override
        protected void onPostExecute(List<User> users) {
            // Update the adapter with the retrieved users
            userAdapter.setUsers(users);
        }
    }

    @Override
    public void onItemClick(User user) {
        // Handle item click, e.g., navigate to another activity
        Intent intent = new Intent(getAllUsersActivity.this, adminEditUserActivity.class);
        intent.putExtra("lietotajs_ID", user.getLietotajs_ID());
        startActivity(intent);
    }
}