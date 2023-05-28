package serviceprovider.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.ppl.BaseActivity;
import com.example.ppl.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import serviceprovider.dao.BookingDao;
import serviceprovider.dao.BookingDaoImpl;
import serviceprovider.dao.ServiceDao;
import serviceprovider.dao.ServiceDaoImpl;
import serviceprovider.models.Booking;
import serviceprovider.models.Service;

public class BookServiceProviderActivity extends BaseActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private BookingDao bookingDao = new BookingDaoImpl();
    private ServiceDao serviceDao = new ServiceDaoImpl();
    private int serviceProviderId;

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView cityTextView;
    private TextView addressTextView;
    private TextView phoneTextView;
    private CalendarView calendarView;
    private EditText editTextDateFrom;
    private EditText editTextDateTo;

    private List<Date> bookedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_service_provider);

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        cityTextView = findViewById(R.id.cityTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        editTextDateFrom = findViewById(R.id.bookDateFromEditText);
        editTextDateTo = findViewById(R.id.bookDateToEditText);

        serviceProviderId = getIntent().getIntExtra("SERVISASNIEDZEJS_ID", 2);
        Service service = serviceDao.getServiceProvider(serviceProviderId);

        nameTextView.setText(service.getNosaukums());
        emailTextView.setText(service.getEpasts());
        cityTextView.setText(service.getPilseta());
        addressTextView.setText(service.getAdrese());
        phoneTextView.setText(service.getTelefons());

        bookedDates = bookingDao.getBookedDates(serviceProviderId);
        com.applandeo.materialcalendarview.CalendarView calendarView = findViewById(R.id.calendar_view);

        // set booked dates in calendar
        List<Calendar> calendars = new ArrayList<>();
        for (Date date : bookedDates) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendars.add(calendar);
        }
        calendarView.setDisabledDays(calendars);

        // choose booking dates
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            int clickCounter = 0;
            @Override
            public void onDayClick(EventDay eventDay) {

                //validation
                Toast toast = Toast.makeText(BookServiceProviderActivity.this, "This date is booked", Toast.LENGTH_SHORT);
                toast.cancel();
                Calendar clickedDayCalendar = eventDay.getCalendar();
                for (Date bookedDate : bookedDates) {
                    if (isSameDay(bookedDate, clickedDayCalendar.getTime())) {
                        toast.show();
                        break;
                    }
                }

                // switch between dateFrom and dateTo when clicking calendar
                if (clickCounter == 0){
                    Calendar calendar = eventDay.getCalendar();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(BookServiceProviderActivity.this,
                            (view1, selectedYear, selectedMonth, selectedDay) -> {
                                TimePickerDialog timePickerDialog = new TimePickerDialog(BookServiceProviderActivity.this,
                                        (view2, hourOfDay, minute) -> {
                                            calendar.set(selectedYear, selectedMonth, selectedDay, hourOfDay, minute);
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                            editTextDateFrom.setText(format.format(calendar.getTime()));
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                                timePickerDialog.show();
                            }, year, month, day);
                    datePickerDialog.show();
                    clickCounter = 1;
                } else {
                    Calendar calendar = eventDay.getCalendar();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(BookServiceProviderActivity.this,
                            (view1, selectedYear, selectedMonth, selectedDay) -> {
                                TimePickerDialog timePickerDialog = new TimePickerDialog(BookServiceProviderActivity.this,
                                        (view2, hourOfDay, minute) -> {
                                            calendar.set(selectedYear, selectedMonth, selectedDay, hourOfDay, minute);
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                            editTextDateTo.setText(format.format(calendar.getTime()));
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                                timePickerDialog.show();
                            }, year, month, day);
                    datePickerDialog.show();
                    clickCounter = 0;
                }

            }
        });

        Button bookButton = findViewById(R.id.btn_book);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date dateFrom = null;
                Date dateTo = null;
                if (!editTextDateFrom.getText().toString().isEmpty()) {
                    try {
                        dateFrom = format.parse(editTextDateFrom.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (!editTextDateTo.getText().toString().isEmpty()) {
                    try {
                        dateTo = format.parse(editTextDateTo.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

                //validation
                if (dateFrom != null && dateTo == null) {
                    Toast.makeText(BookServiceProviderActivity.this, "Please provide the 'Date To' parameter", Toast.LENGTH_LONG).show();
                    return;
                } else if (dateFrom != null && dateTo != null && dateFrom.compareTo(dateTo) > 0) {
                    Toast.makeText(BookServiceProviderActivity.this, "'Date From' can't be later than 'Date To'", Toast.LENGTH_LONG).show();
                    return;
                } else if (dateTo.compareTo(new Date()) < 0 || dateFrom.compareTo(new Date()) < 0 ){
                    Toast.makeText(BookServiceProviderActivity.this, "Cannot book in the past.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (dateFrom != null && dateTo != null) {

                    for (Date bookedDate : bookedDates) {
                        Toast toast = Toast.makeText(BookServiceProviderActivity.this, "This date is booked", Toast.LENGTH_SHORT);
                        toast.cancel();
                        if (isSameDay(bookedDate, dateFrom) || isSameDay(bookedDate, dateTo)){
                            toast.show();
                            break;
                        }
                    }


                    bookServiceProvider(dateFrom, dateTo);
                }
            }
        });
    }

    private void bookServiceProvider(Date datumsNo, Date datumsLIdz) {
        Booking booking = new Booking();
        booking.setServisasniedzejs_ID(serviceProviderId);
        booking.setDatumsNo(datumsNo);
        booking.setDatumsLidz(datumsLIdz);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                boolean success = bookingDao.bookServiceProvider(booking);

                if(success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BookServiceProviderActivity.this, "Service provider booked successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), BookServiceProviderActivity.class);
                            intent.putExtra("SERVISASNIEDZEJS_ID", serviceProviderId);
                            startActivity(intent);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BookServiceProviderActivity.this, "Failed to book service provider.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
