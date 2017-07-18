package app.com.HungryEnglish.Adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import app.com.HungryEnglish.Model.StudentList.StudentData;
import app.com.HungryEnglish.R;

/**
 * Created by R'jul on 7/14/2017.
 */

public class StudentListAdapter  extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {

    private List<StudentData> studentList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvEmail, tvMobileNo, tvTeacherAvaibility, etEditStudentProfile;
        public ImageView ivProfilePic;


        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            tvTeacherAvaibility = (TextView) view.findViewById(R.id.tvTeacherAvaibility);
            ivProfilePic = (ImageView) view.findViewById(R.id.ivTeacherProfilePic);
            etEditStudentProfile = (TextView) view.findViewById(R.id.etEditStudentProfile);
        }
    }




    public StudentListAdapter(Context mainActivity, List<StudentData> studentList) {

        this.studentList = studentList;
        this.mContext = mainActivity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_student_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        Movie movie = teacherList.get(position);
        holder.tvTeacherName.setText(studentList.get(position).getUsername());

        holder.tvEmail.setText("Email : "+studentList.get(position).getEmail());

        holder.tvMobileNo.setText("Mobile No : "+studentList.get(position).getMobNo());

        holder.etEditStudentProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


//        Picasso.with(mContext).load(R.drawable.ic_user_default).into(holder.ivProfilePic);
//        holder.tvGender.setText("Gender : " + teacherList.get(position).get("gender"));
//        holder.tvExperience.setText("Experience : " + teacherList.get(position).get("experience"));
//        holder.tvTeacherAvaibility.setText("Avaibility : " + teacherList.get(position).get("avaibility"));
    }

    @Override
    public int getItemCount() {
        Log.e("COUNT","@@ "+studentList.size());
        return studentList.size();
    }
}
