package app.com.raivatshikhar;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.com.raivatshikhar.Utils.Utils;

public class Register extends BaseActivity implements View.OnClickListener {

    private Button loginBtn, signupBtn;
    EditText fullNameEdt, usernameEdt, passwordEdt, emailEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Sign up");

        loginBtn = (Button) findViewById(R.id.register_login);
        signupBtn = (Button) findViewById(R.id.activity_register_btn);
        fullNameEdt = (EditText) findViewById(R.id.activity_register_fullname);
        usernameEdt = (EditText) findViewById(R.id.activity_register_username);
        passwordEdt = (EditText) findViewById(R.id.activity_register_password);
        emailEdt = (EditText) findViewById(R.id.activity_register_email);
        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_login:
                startActivity(LoginActivity.class);
                break;
            case R.id.activity_register_btn:


        }
    }

    boolean checkValidation() {
        if (getText(fullNameEdt).length() < 4) {
            fullNameEdt.setError(getApplicationContext().getString(R.string.full_name_validation));
            fullNameEdt.requestFocus();
            return false;
        } else if (getText(usernameEdt).length() < 4) {
            usernameEdt.setError(getApplicationContext().getString(R.string.user_name_validation));
            usernameEdt.requestFocus();
            return false;
        } else if (Utils.emailValidator(String.valueOf(emailEdt.getText()))) {
            emailEdt.setError(getApplicationContext().getString(R.string.email_validation));
            emailEdt.requestFocus();
            return false;
        } else if (getText(passwordEdt).length() < 6) {
            passwordEdt.setError(getApplicationContext().getString(R.string.password_validation));
            passwordEdt.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}
