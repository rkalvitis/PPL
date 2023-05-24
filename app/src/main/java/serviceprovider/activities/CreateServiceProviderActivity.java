package serviceprovider.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ppl.BaseActivity;
import com.example.ppl.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import serviceprovider.dao.ServiceDao;
import serviceprovider.dao.ServiceDaoImpl;
import serviceprovider.models.Service;

public class CreateServiceProviderActivity extends BaseActivity {
    private EditText nameEditText;
    private EditText emailEditText;
    // other EditTexts for other fields
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ServiceDao serviceDao = new ServiceDaoImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service_provider);

//        nameEditText = findViewById(R.id.editTextServiceName);
//        emailEditText = findViewById(R.id.editTextServiceEmail);
        // find other EditTexts

//        Button createButton = findViewById(R.id.buttonCreate);
//        createButton.setOnClickListener(v -> createServiceProvider());
    }

    private void createServiceProvider() {
        Service newService = new Service();
        newService.setNosaukums(nameEditText.getText().toString());
        newService.setEpasts(emailEditText.getText().toString());
        // set other fields on newService

        executorService.execute(() -> {
            boolean success = serviceDao.createServiceProvider(newService);

            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(this, "Service provider created successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to create service provider.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}

