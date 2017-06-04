package app.com.raivatshikhar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerBtn = (Button) findViewById(R.id.login_register);
        registerBtn.setOnClickListener(this);
        setTitle("Login");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
                startActivity(Register.class);
                break;
        }
    }
}
