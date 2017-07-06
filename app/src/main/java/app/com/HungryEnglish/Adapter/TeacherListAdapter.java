package app.com.HungryEnglish.Adapter;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.HungryEnglish.Activity.MainActivity;
import app.com.HungryEnglish.Activity.TeacherListActivity;
import app.com.HungryEnglish.Model.TeacherList.TeacherListResponse;
import app.com.HungryEnglish.R;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.MyViewHolder> {

    private List<TeacherListResponse> teacherList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvEmail, tvMobileNo, tvTeacherAvaibility;
        public ImageView ivProfilePic;


        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            tvTeacherAvaibility = (TextView) view.findViewById(R.id.tvTeacherAvaibility);
            ivProfilePic = (ImageView) view.findViewById(R.id.ivTeacherProfilePic);
        }
    }


    public TeacherListAdapter(TeacherListActivity mainActivity, List<TeacherListResponse> teacherList) {

        this.teacherList = teacherList;
        this.mContext = mainActivity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_teacher_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        Movie movie = teacherList.get(position);
        holder.tvTeacherName.setText(teacherList.get(position).getUsername());

        holder.tvEmail.setText("Email : "+teacherList.get(position).getEmail());

        holder.tvMobileNo.setText("Mobile No : "+teacherList.get(position).getMobNo());


//        Picasso.with(mContext).load(R.drawable.ic_user_default).into(holder.ivProfilePic);
//        holder.tvGender.setText("Gender : " + teacherList.get(position).get("gender"));
//        holder.tvExperience.setText("Experience : " + teacherList.get(position).get("experience"));
//        holder.tvTeacherAvaibility.setText("Avaibility : " + teacherList.get(position).get("avaibility"));
    }

    @Override
    public int getItemCount() {
        Log.e("COUNT","@@ "+teacherList.size());
        return teacherList.size();
    }
}