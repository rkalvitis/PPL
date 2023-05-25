package serviceprovider.activities;

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

import serviceprovider.adapters.ServiceUserAdapter;
import serviceprovider.dao.ServiceDaoImpl;
import serviceprovider.models.Service;
import users.activities.adminFunctionalityActivity;

public class getAllServicesActivity extends AppCompatActivity implements ServiceUserAdapter.OnItemClickListener {
    private ServiceDaoImpl serviceDaoImpl;
    private RecyclerView recyclerView;
    private ServiceUserAdapter serviceAdapter;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_services);
        serviceDaoImpl = new ServiceDaoImpl();

        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceUserAdapter();
        serviceAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(serviceAdapter);

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            List<Service> services = serviceDaoImpl.getAllServices();
            runOnUiThread(() -> {
                // Update the adapter with the retrieved users
                serviceAdapter.setServices(services);
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
    public void onItemClick(Service service) {
        Intent intent = new Intent(getAllServicesActivity.this, adminEditServiceActivity.class);
        intent.putExtra("servisasniedzejs_ID", service.getServisasniedzejs_ID());
        startActivity(intent);
    }
}
