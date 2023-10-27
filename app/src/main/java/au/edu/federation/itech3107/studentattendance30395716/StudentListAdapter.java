package au.edu.federation.itech3107.studentattendance30395716;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;




public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private final List<StudentBean> students;
    private final StudentDao daoInstance;
    private Activity activity ;

    public StudentListAdapter(Activity context, List<StudentBean> students) {
        this.students = students;
        this.activity = context;
        daoInstance = Database.getDatabase(context).studentCourseDao();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pupil_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentBean currentStudent = students.get(position);
        holder.nameLabel.setText(currentStudent.getStudentName());
        holder.removeButton.setOnClickListener(v -> removeStudent(position, currentStudent.getStudentId()));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    private void removeStudent(int position, int studentId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                daoInstance.deleteByStudentIdAndCourseId(studentId);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (position < students.size()) {
                            students.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameLabel;
        final Button removeButton;

        ViewHolder(@NonNull View view) {
            super(view);
            nameLabel = view.findViewById(R.id.studentNameLabel);
            removeButton = view.findViewById(R.id.studentRemoveButton);
        }
    }
}