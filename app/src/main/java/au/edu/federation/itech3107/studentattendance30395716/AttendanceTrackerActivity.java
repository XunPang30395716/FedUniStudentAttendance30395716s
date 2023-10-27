package au.edu.federation.itech3107.studentattendance30395716;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;



public class AttendanceTrackerActivity extends AppCompatActivity {

    private RecyclerView listViewAttendanceRecords;
    private AttendanceDao attendanceRepository;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance2);

        initializeUIComponents();
    }

    private void initializeUIComponents() {
        listViewAttendanceRecords = findViewById(R.id.attendanceListView);
        listViewAttendanceRecords.setLayoutManager(new LinearLayoutManager(this));
         Database databaseInstance =  Database.getDatabase(this);
        attendanceRepository = databaseInstance.attendanceDao();

        initDatePickerDialog();
        setDatePickerListener();
    }

    private void initDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                   //String formattedDate = formatSelectedDate(year, monthOfYear, dayOfMonth);
                    String dateString = String.format("%d-%02d-%02d", year, monthOfYear+1, dayOfMonth);
                    Log.e("year：",dateString.toString().trim());
                    updateAttendanceList(dateString.trim());
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void setDatePickerListener() {

        findViewById(R.id.btnOpenDatePicker).setOnClickListener(v -> datePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog"));
    }

    private String formatSelectedDate(int year, int month, int day) {
        return year + "-" + (month + 1) + "-" + day;
    }

    private void updateAttendanceList(String date) {
        new Thread(() -> {
            List<Attendance> dailyAttendanceList = attendanceRepository.getAttendanceByDate(date);

            Log.e("Data size: ", String.valueOf(dailyAttendanceList.size()));
            runOnUiThread(() -> {
                AttendanceAdapter listAdapter = new AttendanceAdapter(this, dailyAttendanceList);
                listViewAttendanceRecords.setAdapter(listAdapter);
            });

        }).start();
    }
}

