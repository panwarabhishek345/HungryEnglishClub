package app.com.HungryEnglish.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import app.com.HungryEnglish.Fragment.TeacherPendingListFragment;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;

import static app.com.HungryEnglish.Fragment.TeacherApprovedListFragment.callRemoveTeacherFromListApi;

/**
 * Created by Vnnovate on 7/19/2017.
 */

public class TeacherApprovedAdapter extends RecyclerView.Adapter<TeacherApprovedAdapter.MyViewHolder> {
    int pos;
    private List<TeacherListResponse> teacherList;
    private Context mContext;
    AdapterView.OnItemClickListener mOnItemClickLister;
    private TeacherPendingAdapter.OnRemoveTeacherClickListener mListener;


    public interface OnRemoveTeacherClickListener {
        public void onItemClick(View view, int position);
    }

    public TeacherApprovedAdapter(Context activity, List<TeacherListResponse> teacherList) {
        this.mContext = activity;
        this.teacherList = teacherList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvEmail, tvMobileNo, tvTeacherAvaibility;
        public ImageView ivProfilePic, ivRemove;
        public LinearLayout llEditDel;


        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            tvTeacherAvaibility = (TextView) view.findViewById(R.id.tvTeacherAvaibility);
            ivProfilePic = (ImageView) view.findViewById(R.id.ivTeacherProfilePic);
            ivRemove = (ImageView) view.findViewById(R.id.ivRemove);
            llEditDel = (LinearLayout) view.findViewById(R.id.llEditDel);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_teacher_approved_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        pos = position;
        //        Movie movie = teacherList.get(position);
        holder.tvTeacherName.setText(teacherList.get(pos).getUsername());

        holder.tvEmail.setText("Email : " + teacherList.get(pos).getEmail());

        holder.tvMobileNo.setText("Mobile No : " + teacherList.get(pos).getMobNo());
        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callRemoveTeacherFromListApi(pos,teacherList.get(pos).getId(),teacherList.get(pos).getRole());

            }
        });

//        Picasso.with(mContext).load(R.drawable.ic_user_default).into(holder.ivProfilePic);
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
