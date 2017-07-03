package app.com.raivatshikhar.Adapter;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import app.com.raivatshikhar.Activity.TeacherListActivity;
import app.com.raivatshikhar.R;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.MyViewHolder> {

    private List<HashMap<String, String>> teacherList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvGender, tvExperience, tvTeacherAvaibility;
        public ImageView ivProfilePic;

        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvGender = (TextView) view.findViewById(R.id.tvGender);
            tvExperience = (TextView) view.findViewById(R.id.tvExperience);
            tvTeacherAvaibility = (TextView) view.findViewById(R.id.tvTeacherAvaibility);
            ivProfilePic = (ImageView) view.findViewById(R.id.ivTeacherProfilePic);
        }
    }


    public TeacherListAdapter(TeacherListActivity teacherListActivity, List<HashMap<String, String>> teacherList) {

        this.teacherList = teacherList;

        this.mContext = teacherListActivity;
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
        holder.tvTeacherName.setText(teacherList.get(position).get("name"));

        Picasso.with(mContext).load(teacherList.get(position).get("image")).into(holder.ivProfilePic);
        holder.tvGender.setText("Gender : " + teacherList.get(position).get("gender"));
        holder.tvExperience.setText("Experience : " + teacherList.get(position).get("experience"));
        holder.tvTeacherAvaibility.setText("Avaibility : " + teacherList.get(position).get("avaibility"));
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }
}