package app.com.HungryEnglish.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import app.com.HungryEnglish.Activity.Admin.AdminDashboardActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Activity.Teacher.MainActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherListActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherProfileActivity;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;

/**
 * Created by Vnnovate on 6/29/2017.
 */

public class SplashActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String isLoggedIn = Utils.ReadSharePrefrence(SplashActivity.this, Constant.SHARED_PREFS.KEY_IS_LOGGED_IN);
                String isActiveStatue = Utils.ReadSharePrefrence(SplashActivity.this, Constant.SHARED_PREFS.KEY_IS_ACTIVE);
                String role = Utils.ReadSharePrefrence(SplashActivity.this, Constant.SHARED_PREFS.KEY_USER_ROLE);
                if (isLoggedIn.equalsIgnoreCase("1")) {

                    if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("0")) {

                        Intent i = new Intent(SplashActivity.this, StudentProfileActivity.class);
                        startActivity(i);
                        finish();

                    } else if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("1")) {

                        Intent i = new Intent(SplashActivity.this, TeacherListActivity.class);
                        startActivity(i);
                        finish();

                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("0")) {

                        Intent i = new Intent(SplashActivity.this, TeacherProfileActivity.class);
                        startActivity(i);
                        finish();

                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("1")) {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("2")) {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }else if (role.equalsIgnoreCase("admin")) {
                        Intent i = new Intent(SplashActivity.this, AdminDashboardActivity.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);
    }
}
