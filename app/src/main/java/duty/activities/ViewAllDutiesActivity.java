package duty.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.ppl.R;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import duty.adapters.dutyAdapter;
import duty.dbo.DbRequests;
import duty.dbo.IDbRequests;
import duty.models.Duty;

public class ViewAllDutiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final IDbRequests _dbRequests = new DbRequests();
    private final dutyAdapter dutyAdap = new dutyAdapter();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_view_duties);
        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //dutyAdap.setOnItemClickListener(this);
        recyclerView.setAdapter(dutyAdap);

        executorService.execute(() -> {
            // TODO
            //List<Duty> users = _dbRequests.getAllDuties();
            runOnUiThread(() -> {
                // Update the adapter with the retrieved users
                // TODO
            });
        });
        ImageButton backButton = findViewById(R.id.returnBackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewDutyActivity.class);
                //intent.putExtra("PIENAKUMS_ID", dutyId);
                startActivity(intent);
            }
        });


    }
}
