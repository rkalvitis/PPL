package users.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.example.ppl.R;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import users.dao.UserDaoImpl;
import users.models.User;

public class getAllUsersActivity extends AppCompatActivity implements userAdapter.OnItemClickListener {
    private UserDaoImpl userDaoImpl;
    private RecyclerView recyclerView;
    private userAdapter userAdapter;
    private ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_users);
        userDaoImpl = new UserDaoImpl();

        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new userAdapter();
        userAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(userAdapter);

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            List<User> users = userDaoImpl.getAllUsersWithIsAdminZero();
            runOnUiThread(() -> {
                // Update the adapter with the retrieved users
                userAdapter.setUsers(users);
            });
        });
        ImageButton backButton = findViewById(R.id.returnBackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), adminFunctionalityActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the executor service when the activity is destroyed.
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
    @Override
    public void onItemClick(User user) {
        Intent intent = new Intent(getAllUsersActivity.this, adminEditUserActivity.class);
        intent.putExtra("lietotajs_ID", user.getLietotajs_ID());
        startActivity(intent);
    }
}
