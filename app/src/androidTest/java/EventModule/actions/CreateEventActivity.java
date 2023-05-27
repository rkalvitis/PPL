package EventModule.actions;

import android.os.Bundle;
import android.widget.EditText;

import com.example.ppl.BaseActivity;
import com.example.ppl.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import EventModule.dao.EventDao;
import EventModule.dao.EventDaoImpl;
import EventModule.models.Event;

public class CreateEventActivity extends BaseActivity {
    private EditText pasakumaNosaukums;
    private EditText laiks;
    private EditText lokacija;
    private EditText linksFoto;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final EventDao eventDao = new EventDaoImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    private void createEvent(){
        Event newEvent = new Event();
        newEvent.setNosaukums();
    }
}
