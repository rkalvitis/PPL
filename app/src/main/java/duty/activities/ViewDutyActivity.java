package duty.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppl.BaseActivity;
import com.example.ppl.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import duty.dbo.DbRequests;
import duty.dbo.IDbRequests;
import duty.models.Duty;

public class ViewDutyActivity extends BaseActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TextView nosaukumsTextView;
    private TextView aprakstsTextView;

    private Button deleteBtn;
    private Button updateBtn;
    private final IDbRequests _dbRequests = new DbRequests();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_duty);

        // TODO SET UP BACK BUTTON
        ImageButton backBtn = findViewById(R.id.returnBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        nosaukumsTextView =findViewById(R.id.textViewNosaukums);
        aprakstsTextView = findViewById(R.id.textViewApraksts);

        int dutyId = getIntent().getIntExtra("PIENAKUMS_ID", 2);
        Duty duty = _dbRequests.getDuty(dutyId);

        nosaukumsTextView.setText(duty.getNosaukums());
        aprakstsTextView.setText(duty.getApraksts());

        deleteBtn = findViewById(R.id.deleteDuty);
        updateBtn = findViewById(R.id.updateDuty);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    executorService.execute(() -> {
                        boolean success = _dbRequests.deleteDuty(dutyId);
                        if (success) {
                            Toast.makeText(ViewDutyActivity.this, "Pienākums izdzēsts", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ViewDutyActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                catch (Exception ex){
                    Toast.makeText(ViewDutyActivity.this, "Radās neparedzēta kļūda. Lūdzu, mēģiniet vēlreiz!", Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateDutyActivity.class);
                intent.putExtra("PIENAKUMS_ID", dutyId);
                startActivity(intent);
            }
        });

    }



}
