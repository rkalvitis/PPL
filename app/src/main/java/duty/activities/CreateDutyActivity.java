package duty.activities;

import com.example.ppl.R;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import duty.dbo.DbRequests;
import duty.dbo.IDbRequests;
import duty.models.Duty;

public class CreateDutyActivity extends AppCompatActivity{
    private EditText aprakstsEditText;
    private EditText nosaukumsEditText;
    private CheckBox paveiktsCheckBox;
    private Button createDutyBtn;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final IDbRequests _dbRequests = new DbRequests();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_duty);

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
        createDutyBtn = findViewById(R.id.buttonCreateDuty);

        createDutyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nosaukums = nosaukumsEditText.getText().toString().trim();
                String apraksts = aprakstsEditText.getText().toString().trim();
                boolean paveikts = paveiktsCheckBox.isChecked();

                try{
                    if(!nosaukums.isEmpty()){
                        Duty newDuty = new Duty();
                        newDuty.setPaveikts(paveikts);
                        newDuty.setApraksts(apraksts);
                        newDuty.setNosaukums(nosaukums);
                        executorService.execute(() -> {
                            boolean success = _dbRequests.createDuty(newDuty);
                            runOnUiThread(() -> {
                                if (success) {
                                    Toast.makeText(CreateDutyActivity.this, "Pienākuma izveidošana ir veiksmīga!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), ViewAllDutiesActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(CreateDutyActivity.this, "Pienākuma izveidošana ir neveiksmīga!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    }
                    else
                        Toast.makeText(CreateDutyActivity.this, "Nav ievadīts nosaukums", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e) {
                    Toast.makeText(CreateDutyActivity.this, "Radās neparedzēta kļūda. Lūdzu, mēģiniet vēlreiz!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });



    }





}
