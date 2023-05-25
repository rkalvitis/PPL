package serviceprovider.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppl.BaseActivity;
import com.example.ppl.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import serviceprovider.adapters.ServiceSearchAdapter;
import serviceprovider.dao.ServiceDao;
import serviceprovider.dao.ServiceDaoImpl;
import serviceprovider.models.Service;

public class SearchServicesProviderActivity extends BaseActivity {

    private EditText editTextKeyword;
    private EditText editTextCategory;
    private EditText editTextDateFrom;
    private EditText editTextDateTo;
    private Spinner spinnerOrderBy;
    private Button buttonSearch;
    private RecyclerView recyclerView;
    private TextView tvEmptyState;
    private ProgressBar progressBar;
    private ServiceSearchAdapter serviceAdapter;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ServiceDao serviceDao = new ServiceDaoImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_service_provider);

        editTextKeyword = findViewById(R.id.searchKeywordEditText);
        editTextCategory = findViewById(R.id.searchCategoryEditText);
        editTextDateFrom = findViewById(R.id.searchDateFromEditText);
        editTextDateTo = findViewById(R.id.searchDateToEditText);
        spinnerOrderBy = findViewById(R.id.spinnerOrderBy);
        buttonSearch = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_bar);
        tvEmptyState = findViewById(R.id.tv_empty_state);  // Empty state TextView

        // Configure the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceSearchAdapter();
        recyclerView.setAdapter(serviceAdapter);

        // OnClickListener for the editTextDateFrom
        editTextDateFrom.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(SearchServicesProviderActivity.this,
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(SearchServicesProviderActivity.this,
                                (view2, hourOfDay, minute) -> {
                                    calendar.set(selectedYear, selectedMonth, selectedDay, hourOfDay, minute);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                    editTextDateFrom.setText(format.format(calendar.getTime()));
                                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                        timePickerDialog.show();
                    }, year, month, day);
            datePickerDialog.show();
        });

        // OnClickListener for the editTextDateTo
        editTextDateTo.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(SearchServicesProviderActivity.this,
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(SearchServicesProviderActivity.this,
                                (view2, hourOfDay, minute) -> {
                                    calendar.set(selectedYear, selectedMonth, selectedDay, hourOfDay, minute);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                    editTextDateTo.setText(format.format(calendar.getTime()));
                                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                        timePickerDialog.show();
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Populate the spinner
        String[] orderByOptions = {"name", "rating", "ratingCount"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, orderByOptions);
        spinnerOrderBy.setAdapter(adapter);

        buttonSearch.setOnClickListener(v -> {
            tvEmptyState.setVisibility(View.VISIBLE);
            tvEmptyState.setText("Loading...");
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            String keyword = editTextKeyword.getText().toString();
            String category = editTextCategory.getText().toString();
            String orderBy = spinnerOrderBy.getSelectedItem().toString();

            // get dateFrom/dateTo
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date dateFrom = null;
            Date dateTo = null;
            if(!editTextDateFrom.getText().toString().isEmpty()) {
                try {
                    dateFrom = format.parse(editTextDateFrom.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            if(!editTextDateTo.getText().toString().isEmpty()) {
                try {
                    dateTo = format.parse(editTextDateTo.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            // Validate date edge cases
            Date finalDateFrom = dateFrom;
            Date finalDateTo = dateTo;
            if (dateFrom != null && dateTo == null) {
                Toast.makeText(this, "Please provide the 'Date To' parameter", Toast.LENGTH_LONG).show();
                return;
            } else if (dateFrom != null && dateTo != null && dateFrom.compareTo(dateTo) > 0) {
                Toast.makeText(this, "'Date From' can't be later than 'Date To'", Toast.LENGTH_LONG).show();
                return;
            }

            executorService.execute(() -> {
                List<Service> services = serviceDao.searchServices(keyword, category, finalDateFrom, finalDateTo, orderBy);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (services.isEmpty()) {
                        tvEmptyState.setVisibility(View.GONE);
                        Toast.makeText(this, "No service provider found with given parameters", Toast.LENGTH_SHORT).show();
                    } else {
                        tvEmptyState.setVisibility(View.GONE);
                        serviceAdapter.setServices(services);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
            });
        });
    }

}
