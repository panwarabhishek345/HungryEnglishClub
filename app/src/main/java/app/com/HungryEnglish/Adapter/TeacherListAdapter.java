package app.com.HungryEnglish.Adapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.HungryEnglish.Fragment.TeacherApprovedListFragment;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Constant;

import static app.com.HungryEnglish.Fragment.TeacherApprovedListFragment.callRemoveTeacherFromListApi;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.MyViewHolder> {

    private List<TeacherListResponse> teacherList;
    private Context mContext;
    TeacherApprovedListFragment activity;
    private OnRemoveTeacherClickListener mListener;
    private int pos;

    public TeacherListAdapter(Context mContext, List<TeacherListResponse> teacherList) {
        this.mContext = mContext;
        this.teacherList = teacherList;
    }

    public interface OnRemoveTeacherClickListener {
        public void onItemClick(View view, int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvClosestStation, tvTeacherAvaibility, tvSpecialSkills;
        public ImageView ivProfilePic, ivRemove;


        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvClosestStation = (TextView) view.findViewById(R.id.tvClosestStation);
            tvSpecialSkills = (TextView) view.findViewById(R.id.tvSpecialSkills);
            tvTeacherAvaibility = (TextView) view.findViewById(R.id.tvTeacherAvaibility);
            ivProfilePic = (ImageView) view.findViewById(R.id.ivTeacherProfilePic);
            ivRemove = (ImageView) view.findViewById(R.id.ivRemove);
        }

        public void bind(TeacherListResponse teacherListResponse, OnRemoveTeacherClickListener mListener) {


        }
    }


    public TeacherListAdapter(Context mainActivity, List<TeacherListResponse> teacherList, OnRemoveTeacherClickListener onRemoveTeacherClickListener) {

        this.teacherList = teacherList;
        this.mContext = mainActivity;
        this.mListener = onRemoveTeacherClickListener;

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
        pos = position;
        holder.bind(teacherList.get(position), mListener);

        holder.tvTeacherName.setText(teacherList.get(position).getUsername());

        if (teacherList.get(position).getTeacherInfo() != null) {

            holder.tvSpecialSkills.setText(mContext.getString(R.string.special_skils_label) + " " + teacherList.get(position).getTeacherInfo().getSkills());

            holder.tvTeacherAvaibility.setText(mContext.getString(R.string.teacher_avaibility_label) + " " + teacherList.get(position).getTeacherInfo().getAvailableTime());

            holder.tvClosestStation.setText(teacherList.get(position).getTeacherInfo().getAddress());

            String profilePicUrl = Constant.BASEURL + "" + teacherList.get(position).getTeacherInfo().getProfileImage();
            Log.e("URL", "" + profilePicUrl);
            Picasso.with(mContext).load(profilePicUrl).error(R.drawable.ic_user_default).into(holder.ivProfilePic);

            holder.ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callRemoveTeacherFromListApi(pos);

                }
            });
        }


//        holder.tvGender.setText("Gender : " + teacherList.get(position).get("gender"));
//        holder.tvExperience.setText("Experience : " + teacherList.get(position).get("experience"));
//        holder.tvTeacherAvaibility.setText("Avaibility : " + teacherList.get(position).get("avaibility"));
    }

    @Override
    public int getItemCount() {
        Log.e("COUNT", "@@ " + teacherList.size());
        return teacherList.size();
    }
}