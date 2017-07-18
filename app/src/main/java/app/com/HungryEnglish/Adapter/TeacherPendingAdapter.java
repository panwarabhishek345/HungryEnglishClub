package app.com.HungryEnglish.Adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import app.com.HungryEnglish.Fragment.TeacherPendingListFragment;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;

/**
 * Created by R'jul on 7/11/2017.
 */

public class TeacherPendingAdapter extends RecyclerView.Adapter<TeacherPendingAdapter.MyViewHolder> {
    int pos;
    private List<TeacherListResponse> teacherList;
    private Context mContext;
    OnItemClickListener mOnItemClickLister;


    public TeacherPendingAdapter(Context activity, List<TeacherListResponse> teacherList) {
        this.mContext = activity;
        this.teacherList = teacherList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvEmail, tvMobileNo, tvTeacherAvaibility, tvAcceptInvitation;
        public ImageView ivProfilePic;


        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            tvTeacherAvaibility = (TextView) view.findViewById(R.id.tvTeacherAvaibility);
            ivProfilePic = (ImageView) view.findViewById(R.id.ivTeacherProfilePic);
            tvAcceptInvitation = (TextView) view.findViewById(R.id.tvAcceptInvitation);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_teacher_pending_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        pos = position;
        //        Movie movie = teacherList.get(position);
        holder.tvTeacherName.setText(teacherList.get(pos).getUsername());

        holder.tvEmail.setText("Email : " + teacherList.get(pos).getEmail());

        holder.tvMobileNo.setText("Mobile No : " + teacherList.get(pos).getMobNo());

        holder.tvAcceptInvitation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                TeacherPendingListFragment.callTeacherAcceptInvitationApi( pos, teacherList.get(pos).getId(), teacherList.get(pos).getIsActive());
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
