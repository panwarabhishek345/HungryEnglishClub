package app.com.raivatshikhar.Activity;

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
import app.com.raivatshikhar.Model.login.LoginMainResponse;
import app.com.raivatshikhar.R;
import app.com.raivatshikhar.Services.ApiHandler;
import app.com.raivatshikhar.Util.Constant;
import app.com.raivatshikhar.Util.Utils;
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

        idMapping();


        utils = new Utils(LoginActivity.this);

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
                v.startAnimation(click);
                startActivity(RegisterActivity.class);
                break;
            case R.id.activity_login_btn:
                v.startAnimation(click);
//                if (getText(emailEdt).length() > 4) {
//                    if (String.valueOf(passwordEdt.getText()).length() > 5) {
//                        if (utils.isConnectingToInternet()) {
//                            utils.ShowProgressDialog(true);
//
//                            String loginUrl = Constant.BASEURL + "login.php?u_name=" + String.valueOf(emailEdt.getText()) + "&u_pass=" + String.valueOf(passwordEdt.getText());
//                            try {
//                                utils.ShowProgressDialog(false);
//                                JSONObject object = new JSONObject(utils.getResponseofGet(loginUrl));
//                                if (object.getString("status").equalsIgnoreCase("true")) {
//
//                                    startActivity(StudentProfileActivity.class);
//                                    this.finish();
//                                } else {
//                                    toast(getApplicationContext().getString(R.string.loginfail));
//                                }
//                            } catch (JSONException e) {
//                                utils.ShowProgressDialog(false);
//                                e.printStackTrace();
//                            }
//
//                        } else {
//                            toast(getApplicationContext().getString(R.string.no_internet));
//                        }
//                    } else {
//                        passwordEdt.setError(getApplicationContext().getString(R.string.password_validation));
//                        passwordEdt.requestFocus();
//                    }
//                } else {
//                    emailEdt.setError(getApplicationContext().getString(R.string.user_name_validation));
//                    emailEdt.requestFocus();
//                }


                if (emailEdt.getText().toString().equalsIgnoreCase("")) {
                    emailEdt.setError("Enter Email Address");
                    emailEdt.requestFocus();
//                    Toast.makeText(LoginActivity.this, "Please Enter User Email.", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (!Patterns.EMAIL_ADDRESS.matcher(
//                        emailEdt.getText().toString()).matches()) {
//                    emailEdt.setError("Enter Valid Email Address");
//                    emailEdt.requestFocus();
////                    Toast.makeText(LoginActivity.this, "Please enter valid  Email Address", Toast.LENGTH_SHORT).show();
//                    return;
//                }


                if (passwordEdt.getText().toString().equalsIgnoreCase("")) {
                    passwordEdt.setError("Enter Password");
                    passwordEdt.requestFocus();
//                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwordEdt.getText().toString().trim().length() < 6) {
                    passwordEdt.setError("Password must be minimun 6 character");
                    passwordEdt.requestFocus();

//                    Toast.makeText(LoginActivity.this, "Password must be minimun 6 character", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwordEdt.getText().toString().length() > 15) {
                    passwordEdt.setError("Password must be maximum 15 character");
                    passwordEdt.requestFocus();
//                    Toast.makeText(LoginActivity.this, "Password must be maximum 15 character", Toast.LENGTH_SHORT).show();
                    return;
                }

                // CALL LOGIN API
                callLoginApi();

//                startActivity(StudentProfileActivity.class);
//                this.finish();
                break;
        }
    }


    // CALL LOGIN API HERE
    private void callLoginApi() {

        if (!Utils.checkNetwork(LoginActivity.this)) {

            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), LoginActivity.this);

            return;
        } else {
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
                        Toast.makeText(getApplicationContext(), "" + loginUser.getMsg(), Toast.LENGTH_SHORT).show();
                        Utils.WriteSharePrefrence(LoginActivity.this, Constant.SHARED_PREFS.KEY_USER_ID, loginUser.getData().getId());
                        Utils.WriteSharePrefrence(LoginActivity.this, Constant.SHARED_PREFS.KEY_USER_ROLE, role);
                        Utils.WriteSharePrefrence(LoginActivity.this, Constant.SHARED_PREFS.KEY_USER_NAME, loginUser.getData().getUsername());
                        Utils.WriteSharePrefrence(LoginActivity.this, Constant.SHARED_PREFS.KEY_IS_LOGGED_IN, "1");

                        if (role.equalsIgnoreCase("teacher") && loginUser.getData().getIsActive().equals("2") ) {
                            startActivity(new Intent(LoginActivity.this, TeacherProfileActivity.class));
                        } else if (role.equalsIgnoreCase("student")&& loginUser.getData().getIsActive().equals("0")) {
                            startActivity(new Intent(LoginActivity.this, StudentProfileActivity.class));
                        }else if(role.equalsIgnoreCase("student")){
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }


                        finish();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private Map<String, String> getLoginDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("u_pass", "" + passwordEdt.getText().toString());
        map.put("u_name", "" + emailEdt.getText());

        Log.e("map", "LOGIN " + map);
        return map;
    }
}
