package app.com.HungryEnglish.Activity.Admin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.LoginActivity;
import app.com.HungryEnglish.Activity.Student.StudentListActivity;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;

/**
 * Created by R'jul on 7/14/2017.
 */

public class AdminDashboardActivity extends BaseActivity {

    LinearLayout llStudentList, llTeacherList, llAddImageOrLink, llLogout;

    AlphaAnimation click;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        click = new AlphaAnimation(1F, 0.5F);

        setContentView(R.layout.admin_dashboard_activity);

        idMapping();


        setOnClick();

    }


    private void idMapping() {
        llStudentList = (LinearLayout) findViewById(R.id.llStudentList);

        llTeacherList = (LinearLayout) findViewById(R.id.llTeacherList);

        llAddImageOrLink = (LinearLayout) findViewById(R.id.llAddImageOrLink);

        llLogout = (LinearLayout) findViewById(R.id.llLogout);

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

}
