package app.com.HungryEnglish.Activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.com.HungryEnglish.Activity.Admin.AdminDashboardActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Activity.Teacher.MainActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherListActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherProfileActivity;
import app.com.HungryEnglish.Model.login.LoginMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    Button registerBtn, loginBtn;
    Context context;
    EditText emailEdt, passwordEdt;
    Utils utils;
    AlphaAnimation click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        click = new AlphaAnimation(1F, 0.5F);
        utils = new Utils(LoginActivity.this);
        idMapping();
    }

    private void idMapping() {
        registerBtn = (Button) findViewById(R.id.login_register);

        loginBtn = (Button) findViewById(R.id.activity_login_btn);

        emailEdt = (EditText) findViewById(R.id.activity_login_email);

        passwordEdt = (EditText) findViewById(R.id.activity_login_password);

        setTitle("Login");


        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.activity_login_btn:

                if (emailEdt.getText().toString().equalsIgnoreCase("")) {
                    emailEdt.setError("Enter Email Address");
                    emailEdt.requestFocus();
                    return;
                }
                if (passwordEdt.getText().toString().equalsIgnoreCase("")) {
                    passwordEdt.setError("Enter Password");
                    passwordEdt.requestFocus();
                    return;
                }
                if (passwordEdt.getText().toString().trim().length() < 6) {
                    passwordEdt.setError("Password must be minimun 6 character");
                    passwordEdt.requestFocus();
                    return;
                }
                // CALL LOGIN API
                callLoginApi();
                break;
        }
    }


    // CALL LOGIN API HERE
    private void callLoginApi() {

        if (!Utils.checkNetwork(LoginActivity.this)) {
            Utils.showCustomDialog(getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), LoginActivity.this);
            return;
        }

        Utils.showDialog(LoginActivity.this);
        ApiHandler.getApiService().getLogin(getLoginDetail(), new retrofit.Callback<LoginMainResponse>() {
            @Override
            public void success(LoginMainResponse loginUser, Response response) {
                Utils.dismissDialog();
                if (loginUser == null) {
                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (loginUser.getStatus() == null) {
                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (loginUser.getStatus().equals("false")) {
                    Toast.makeText(getApplicationContext(), "" + loginUser.getMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (loginUser.getStatus().equals("true")) {
                    String role = loginUser.getData().getRole();
                    Log.e("ROLE",""+role);

                    String isActiveStatue = loginUser.getData().getIsActive();
                    Toast.makeText(getApplicationContext(), "" + loginUser.getMsg(), Toast.LENGTH_SHORT).show();
                    Utils.WriteSharePrefrence(LoginActivity.this, Constant.SHARED_PREFS.KEY_USER_ID, loginUser.getData().getId());
                    Utils.WriteSharePrefrence(LoginActivity.this, Constant.SHARED_PREFS.KEY_USER_ROLE, role);
                    Utils.WriteSharePrefrence(LoginActivity.this, Constant.SHARED_PREFS.KEY_USER_NAME, loginUser.getData().getUsername());
                    Utils.WriteSharePrefrence(LoginActivity.this, Constant.SHARED_PREFS.KEY_IS_LOGGED_IN, "1");
                    Utils.WriteSharePrefrence(LoginActivity.this, Constant.SHARED_PREFS.KEY_IS_ACTIVE, isActiveStatue);

                    if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("0")) {
                        Intent i = new Intent(LoginActivity.this, StudentProfileActivity.class);
                        startActivity(i);
                        finish();
                    } else if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("1")) {
                        Intent i = new Intent(LoginActivity.this, TeacherListActivity.class);
                        startActivity(i);
                        finish();
                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("0")) {
                        Intent i = new Intent(LoginActivity.this, TeacherProfileActivity.class);
                        startActivity(i);
                        finish();
                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("1")) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("2")) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else if (role.equalsIgnoreCase("admin")) {
                        Intent i = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Map<String, String> getLoginDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("u_pass", "" + passwordEdt.getText().toString());
        map.put("u_name", "" + emailEdt.getText());
        return map;
    }
}
