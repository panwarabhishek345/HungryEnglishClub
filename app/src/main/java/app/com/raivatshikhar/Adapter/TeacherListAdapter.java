package app.com.raivatshikhar.Adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import app.com.raivatshikhar.Activity.TeacherListActivity;
import app.com.raivatshikhar.R;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.MyViewHolder> {

    private List<Movie> teacherList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvGender, tvExperience;

        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvGender = (TextView) view.findViewById(R.id.tvGender);
            tvExperience = (TextView) view.findViewById(R.id.tvExperience);
        }
    }


    public TeacherListAdapter(TeacherListActivity teacherListActivity, List<Movie> teacherList) {
        
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
        Movie movie = teacherList.get(position);
//        holder.tvTeacherName.setText(movie.getTitle());
//        holder.tvGender.setText(movie.getGenre());
//        holder.tvExperience.setText(movie.getYear());
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }
}