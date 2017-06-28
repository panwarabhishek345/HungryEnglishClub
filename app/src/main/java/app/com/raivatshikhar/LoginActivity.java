package app.com.raivatshikhar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import app.com.raivatshikhar.Utils.Constant;
import app.com.raivatshikhar.Utils.Utils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    Button registerBtn, loginBtn;
    EditText emailEdt, passwordEdt;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerBtn = (Button) findViewById(R.id.login_register);
        loginBtn = (Button) findViewById(R.id.activity_login_btn);
        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        emailEdt = (EditText) findViewById(R.id.activity_login_email);
        passwordEdt = (EditText) findViewById(R.id.activity_login_password);
        setTitle("Login");

        utils = new Utils(LoginActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
                startActivity(Register.class);
                break;
            case R.id.activity_login_btn:
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
//                                    startActivity(ParentRegistrationActivity.class);
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
                startActivity(ParentRegistrationActivity.class);
                this.finish();
                break;
        }
    }
}
