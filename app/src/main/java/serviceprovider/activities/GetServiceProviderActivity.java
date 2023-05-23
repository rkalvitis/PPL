package serviceprovider.activities;

import com.example.ppl.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import serviceprovider.dao.ServiceDao;
import serviceprovider.dao.ServiceDaoImpl;
import serviceprovider.models.Service;

public class GetServiceProviderActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView emailTextView;
    // other TextViews for other fields
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ServiceDao serviceDao = new ServiceDaoImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_service_provider);

        nameTextView = findViewById(R.id.textViewServiceName);
        emailTextView = findViewById(R.id.textViewServiceEmail);
        // find other TextViews

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        executorService.execute(() -> {
            Service service = serviceDao.getServiceProvider(2);
            runOnUiThread(() -> {
                if (service != null) {
                    nameTextView.setText(service.getNosaukums());
                    emailTextView.setText(service.getEpasts());
                    // set text on other TextViews
                } else {
                    // display a message to the user
                    Toast.makeText(this, "No service provider found with the given ID.", Toast.LENGTH_SHORT).show();
                }
            });
        });

////          Example for create
//        executorService.execute(() -> {
//            Service newService = new Service();
//            // set properties on newService
//
//            boolean success = serviceDao.createServiceProvider(newService);
//
//            runOnUiThread(() -> {
//                if (success) {
//                    Toast.makeText(this, "Service provider created successfully.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Failed to create service provider.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}

