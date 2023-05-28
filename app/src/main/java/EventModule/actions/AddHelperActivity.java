package EventModule.actions;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppl.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import EventModule.dao.EventOrganizerDao;
import EventModule.dao.EventOrganizerDaoImpl;
import EventModule.models.EventOrganizers;

public class AddHelperActivity extends AppCompatActivity {
    private EditText emailEditText;
    private int eventId;
    private Button addHelperBtn;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final EventOrganizerDao eventOrgDao = new EventOrganizerDaoImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_helper_to_event);
        ImageButton backButton = findViewById(R.id.returnBackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous activity
                finish();
            }
        });
        emailEditText = findViewById(R.id.email);
        addHelperBtn = findViewById(R.id.addHelperBtn);

        addHelperBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = emailEditText.getText().toString().trim();

                EventOrganizers eventOrg = new EventOrganizers();
                //eventOrg.set
                //executorService.execute(() -> {
                 //   int id = eventOrgDao.addHelper()
                //});
            }
        });

    }
}
