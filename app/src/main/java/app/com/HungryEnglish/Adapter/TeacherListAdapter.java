package app.com.HungryEnglish.Adapter;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import app.com.HungryEnglish.Activity.ForgotPassword;
import app.com.HungryEnglish.Fragment.TeacherApprovedListFragment;
import app.com.HungryEnglish.Model.ForgotPassord.ForgotPasswordModel;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Mail;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.Fragment.TeacherApprovedListFragment.callRemoveTeacherFromListApi;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.MyViewHolder> {

    private List<TeacherListResponse> teacherList;
    private Context mContext;
    TeacherApprovedListFragment activity;
    //    private OnRemoveTeacherClickListener mListener;
    private int pos;

    public TeacherListAdapter(Context mContext, List<TeacherListResponse> teacherList) {
        this.mContext = mContext;
        this.teacherList = teacherList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvClosestStation, tvTeacherAvaibility, tvSpecialSkills, tvReportTeacher;
        public ImageView ivProfilePic;


        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvClosestStation = (TextView) view.findViewById(R.id.tvClosestStation);
            tvSpecialSkills = (TextView) view.findViewById(R.id.tvSpecialSkills);
            tvTeacherAvaibility = (TextView) view.findViewById(R.id.tvTeacherAvaibility);
            ivProfilePic = (ImageView) view.findViewById(R.id.ivTeacherProfilePic);
            if (Utils.ReadSharePrefrence(mContext, Constant.SHARED_PREFS.KEY_USER_ROLE).equals("student")) {
                tvReportTeacher = (TextView) view.findViewById(R.id.tvReportTeacher);
            }


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_teacher_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        pos = position;
        holder.tvTeacherName.setText(teacherList.get(position).getUsername());

        if (teacherList.get(position).getTeacherInfo() != null) {

            holder.tvSpecialSkills.setText(mContext.getString(R.string.special_skils_label) + " " + teacherList.get(position).getTeacherInfo().getSkills());

            holder.tvTeacherAvaibility.setText(mContext.getString(R.string.teacher_avaibility_label) + " " + teacherList.get(position).getTeacherInfo().getAvailableTime());

            holder.tvClosestStation.setText(teacherList.get(position).getTeacherInfo().getAddress());

            String profilePicUrl = Constant.BASEURL + "" + teacherList.get(position).getTeacherInfo().getProfileImage();
            Log.e("URL", "" + profilePicUrl);
            Picasso.with(mContext).load(profilePicUrl).error(R.drawable.ic_user_default).into(holder.ivProfilePic);

            if (Utils.ReadSharePrefrence(mContext, Constant.SHARED_PREFS.KEY_USER_ROLE).equals("student")) {
                holder.tvReportTeacher.setVisibility(View.VISIBLE);
            } else {
                holder.tvReportTeacher.setVisibility(View.GONE);
            }

            holder.tvReportTeacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, "Send Mail To Teacher Under Development", Toast.LENGTH_SHORT).show();

                    RequestToTeacher(position);
                }
            });
        }
    }

    private void RequestToTeacher(final int position) {


        Utils.showDialog(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("teacherId", teacherList.get(position).getId());
        map.put("studentId", Utils.ReadSharePrefrence(mContext, Constant.SHARED_PREFS.KEY_USER_ID));

//        http://smartsquad.16mb.com/HungryEnglish/api/add_request.php?teacherId=1&studentId=2
        ApiHandler.getApiService().addRequest(map, new retrofit.Callback<ForgotPasswordModel>() {


            @Override
            public void success(ForgotPasswordModel forgotPasswordModel, Response response) {

                Utils.dismissDialog();


                if (forgotPasswordModel.getStatus().toString().equals("true")) {
                    Toast.makeText(mContext, forgotPasswordModel.getMsg(), Toast.LENGTH_SHORT).show();
                    sendEmail(Utils.ReadSharePrefrence(mContext, Constant.SHARED_PREFS.KEY_USER_NAME),teacherList.get(position).getFullName());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                Toast.makeText(mContext, "Try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.e("COUNT", "@@ " + teacherList.size());
        return teacherList.size();
    }

    private void sendEmail(String studentName, String TeacherName) {

        String message = "Welcome to Hungry English Club " + studentName + "Enquiry for Teacher  " + TeacherName;

        String[] recipients = {"idigi@live.com"};
        SendEmailAsyncTask email = new SendEmailAsyncTask();
//        email.activity = mContext;
        email.m = new Mail("hungryenglishclub@gmail.com", "rujulgandhi");
        email.m.set_from("hungryenglishclub@gmail.com");
        email.m.setBody(message);
        email.m.set_to(recipients);
        email.m.set_subject("Hungry English CLUB");
        email.execute();


    }


    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Mail m;
        ForgotPassword activity;

        public SendEmailAsyncTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (m.send()) {
//                    Toast.makeText(activity, "Email sent.", Toast.LENGTH_SHORT).show();


                } else {
//                Toast.makeText(activity, "Email failed to send.", Toast.LENGTH_SHORT).show();
                }

                return true;
            } catch (AuthenticationFailedException e) {
                Log.e("EmailError", "Bad account details");
                e.printStackTrace();

//            Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                return false;
            } catch (MessagingException e) {
                Log.e("EmailError", "Email failed");
                e.printStackTrace();

//            Toast.makeText(activity, "Email failed to send.", Toast.LENGTH_SHORT).show();
                return false;
            } catch (Exception e) {
                e.printStackTrace();

//            Toast.makeText(activity, "Unexpected error occured.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
}