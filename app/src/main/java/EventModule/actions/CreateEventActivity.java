package EventModule.actions;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ppl.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import EventModule.dao.EventDao;
import EventModule.dao.EventDaoImpl;
import EventModule.models.Event;
import users.activities.SessionManager;

public class CreateEventActivity extends AppCompatActivity {
    private EditText eventNameEditText;
    private LocalDate dateTime;
    private EditText timeEditText;
    private EditText addressEditText;
    private EditText photosEditText;
    private Button saveEventBtn;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final EventDao eventDao = new EventDaoImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        ImageButton backButton = findViewById(R.id.returnBackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous activity
                finish();
            }
        });
        eventNameEditText = findViewById(R.id.nosaukums);
        timeEditText = findViewById(R.id.dateTime);
        //addressEditText = findViewById(R.id.adress);
        photosEditText = findViewById(R.id.photos);
        saveEventBtn = findViewById(R.id.saveEventBtn);
        //dateTime = LocalDate.now();

        saveEventBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String name = eventNameEditText.getText().toString().trim();
                String time = timeEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String photos = photosEditText.getText().toString().trim();
                SessionManager sessionManager = new SessionManager(getApplicationContext());

                timeEditText.setOnClickListener(view -> {
                    final Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                            (view1, selectedYear, selectedMonth, selectedDay) -> {
                                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventActivity.this,
                                        (view2, hourOfDay, minute) -> {
                                            calendar.set(selectedYear, selectedMonth, selectedDay, hourOfDay, minute);
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                            timeEditText.setText(format.format(calendar.getTime()));
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                                timePickerDialog.show();
                            }, year, month, day);
                    datePickerDialog.show();
                });

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date = new Date();
                if(!timeEditText.getText().toString().isEmpty()) {
                    try {
                        date = format.parse(timeEditText.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

                    if(validateInputs(name, address, photos)){
                        Event newEvent = new Event();
                        newEvent.setNosaukums(name);
                        newEvent.setSakumaLaiks(date);
                        newEvent.setLokacija(address);
                        newEvent.setLinksUzFoto(photos);

                    }
            }

            private boolean validateInputs(String name, String address, String photos)
            {
                if(name.isEmpty())
                {
                    Toast.makeText(CreateEventActivity.this, "Nav aizpildīti visi obligātie ievadlauki!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(address.length() > 250)
                {
                    Toast.makeText(CreateEventActivity.this, "Adresei ir jābūt īsakai par 250 simboliem!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(photos.length() > 250)
                {
                    Toast.makeText(CreateEventActivity.this, "Linkam ir jābūt īsakai par 250 simboliem!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });

    }
}
