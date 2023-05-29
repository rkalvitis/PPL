package duty.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppl.BaseActivity;
import com.example.ppl.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import duty.dbo.DbRequests;
import duty.dbo.IDbRequests;
import duty.models.Duty;

public class UpdateDutyActivity extends AppCompatActivity {
    private EditText aprakstsEditText;
    private EditText nosaukumsEditText;
    private CheckBox paveiktsCheckBox;
    private Button updateDutyBtn;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final IDbRequests _dbRequests = new DbRequests();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_duty);

        ImageButton backBtn = findViewById(R.id.returnBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        nosaukumsEditText = findViewById(R.id.editTextNosaukums);
        paveiktsCheckBox = findViewById(R.id.checkBoxPaveikts);
        aprakstsEditText = findViewById(R.id.editTextApraksts);
        updateDutyBtn = findViewById(R.id.buttonUpdateDuty);

        updateDutyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nosaukums = nosaukumsEditText.getText().toString().trim();
                String apraksts = aprakstsEditText.getText().toString().trim();
                boolean paveikts = paveiktsCheckBox.isChecked();

                try{
                    int dutyId = getIntent().getIntExtra("PIENAKUMS_ID", 2);
                    Duty duty = _dbRequests.getDuty(dutyId);
                    if(!nosaukums.isEmpty()){
                        Duty newDuty = new Duty();
                        newDuty.setPienakums_ID(dutyId);
                        newDuty.setPaveikts(paveikts);
                        newDuty.setApraksts(apraksts);
                        newDuty.setNosaukums(nosaukums);
                        executorService.execute(() -> {
                            boolean success = _dbRequests.updateDuty(newDuty);
                            runOnUiThread(() -> {
                                if (success) {
                                    Toast.makeText(UpdateDutyActivity.this, "Pienākums atjaunināts veiksmīgi!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), ViewAllDutiesActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(UpdateDutyActivity.this, "Pienākuma atjaunināšana neveiksmīga!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    }
                    else
                        Toast.makeText(UpdateDutyActivity.this, "Nav ievadīts nosaukums", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e) {
                    Toast.makeText(UpdateDutyActivity.this, "Radās neparedzēta kļūda. Lūdzu, mēģiniet vēlreiz!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

    }
}
