package EventModule.actions;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppl.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import EventModule.dao.EventDao;
import EventModule.dao.EventDaoImpl;
import EventModule.models.Event;
import users.activities.SessionManager;

public class GetEventActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView timeTextView;
    private TextView locationTextView;
    private TextView photosTextView;
    private int eventId = 1;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final EventDao eventDao = new EventDaoImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        nameTextView = findViewById(R.id.nosaukums);
        timeTextView = findViewById(R.id.datums);
        locationTextView = findViewById(R.id.adrese);
        ImageButton backButton = findViewById(R.id.returnBackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous activity
                finish();
            }
        });

        executorService.execute(() -> {
            Event event = eventDao.getEvent(eventId);
            runOnUiThread(() -> {
                if (event != null) {
                    if (nameTextView != null) {
                        nameTextView.setText(String.valueOf(event.getNosaukums()));
                    }
                    if (timeTextView != null) {
                        timeTextView.setText((event.getSakumaLaiks().toString()));
                    }
                    if (locationTextView != null) {
                        locationTextView.setText(String.valueOf(event.getLokacija()));
                    }
                }
            });
        });
    }
}
