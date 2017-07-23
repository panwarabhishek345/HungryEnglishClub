package app.com.HungryEnglish.Activity.Admin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.LoginActivity;
import app.com.HungryEnglish.Activity.Student.StudentListActivity;
import app.com.HungryEnglish.Adapter.StudentListAdapter;
import app.com.HungryEnglish.Adapter.TeacherApprovedAdapter;
import app.com.HungryEnglish.Model.StudentList.StudentData;
import app.com.HungryEnglish.Model.StudentList.StudentListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.Model.admin.CountListMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by R'jul on 7/14/2017.
 */

public class AdminDashboardActivity extends BaseActivity {

    LinearLayout llStudentList, llTeacherList, llAddImageOrLink, llLogout;

    AlphaAnimation click;
    int teacherCount = 0, studentCount = 0;
    private TextView tvTeacherCountAdmin, tvStudentCountAdmin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        click = new AlphaAnimation(1F, 0.5F);
        setContentView(R.layout.admin_dashboard_activity);
        idMapping();

        setOnClick();

        callGetCountApi();

    }

    private void idMapping() {
        llStudentList = (LinearLayout) findViewById(R.id.llStudentList);
        llTeacherList = (LinearLayout) findViewById(R.id.llTeacherList);
        llAddImageOrLink = (LinearLayout) findViewById(R.id.llAddImageOrLink);
        llLogout = (LinearLayout) findViewById(R.id.llLogout);
        tvTeacherCountAdmin = (TextView) findViewById(R.id.tvTeacherCountAdmin);
        tvStudentCountAdmin = (TextView) findViewById(R.id.tvStudentCountAdmin);
    }

    private void setOnClick() {
        llStudentList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(StudentListActivity.class);
            }
        });
        llTeacherList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AdminTeacherList.class);
            }
        });
        llAddImageOrLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddImageOrLinkActivity.class);
            }
        });
        llLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(AdminDashboardActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_logout);
                dialog.setCancelable(false);
                TextView tvLogout = (TextView) dialog.findViewById(R.id.btLogoutPopupLogout);
                TextView tvCancel = (TextView) dialog.findViewById(R.id.btCancelPopupLogout);
                tvLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.WriteSharePrefrence(AdminDashboardActivity.this, Constant.SHARED_PREFS.KEY_IS_LOGGED_IN, "0");
                        Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }


    // CALL GET COUNT API HERE
    private void callGetCountApi() {
        if (!Utils.checkNetwork(AdminDashboardActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), AdminDashboardActivity.this);
            return;
        } else {
            Utils.showDialog(AdminDashboardActivity.this);
            ApiHandler.getApiService().getCountList(getTeacherDetail(), new retrofit.Callback<CountListMainResponse>() {
                @Override
                public void success(CountListMainResponse countListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (countListMainResponse == null) {
                        toast("Something Wrong");
                        return;
                    }
                    if (countListMainResponse.getStatus() == null) {
                        toast("Something Wrong");
                        return;
                    }
                    if (countListMainResponse.getStatus().equals("false")) {
                        toast(countListMainResponse.getMsg());
                        return;
                    }
                    if (countListMainResponse.getStatus().equals("true")) {
                        teacherCount = Integer.parseInt(countListMainResponse.getTeacherCount());
                        studentCount = Integer.parseInt(countListMainResponse.getStudentCount());
                        tvTeacherCountAdmin.setText(String.valueOf(teacherCount));
                        tvStudentCountAdmin.setText(String.valueOf(studentCount));
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Utils.dismissDialog();
                    Toast.makeText(AdminDashboardActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private Map<String, String> getTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }




}
