package au.edu.federation.itech3107.studentattendance30395716;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<Attendance> attendances;
    private StudentDao studentDao;

    public AttendanceAdapter(Context context, List<Attendance> attendances) {
        this.attendances = attendances;
        Database database = Database.getDatabase(context);
        studentDao = database.studentCourseDao();
        Log.e("AttendanceAdapter", "studentDao is: " + (studentDao == null ? "null" : "not null"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Attendance currentAttendance = attendances.get(position);

        new Thread(() -> {
            int studentId = currentAttendance.getStudentId();
            Log.e("studentId: ", studentId+"");
            StudentBean studentCourse = studentDao.getStudentCourseById(studentId);

            Log.e("studentCourse: ", studentCourse.toString()+"");
            new Handler(Looper.getMainLooper()).post(() -> {
                if (studentCourse != null) {
                    holder.studentNameTextView.setText(studentCourse.getStudentId()+"");
                    holder.attendanceStatusTextView.setText(currentAttendance.isPresent() ? "Attendance" : "NO Attendance");
                }
            });
        }).start();
    }

    @Override
    public int getItemCount() {
        return attendances.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView studentNameTextView;
        public TextView attendanceStatusTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
            attendanceStatusTextView = itemView.findViewById(R.id.attendanceStatusTextView);
        }
    }
}
