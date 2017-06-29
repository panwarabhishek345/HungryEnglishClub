package app.com.raivatshikhar.Activity;

import android.os.Bundle;
import android.widget.TextView;
import app.com.raivatshikhar.R;
import app.com.raivatshikhar.Util.Constant;

public class MainActivity extends BaseActivity {
    public TextView msgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msgTv = (TextView) findViewById(R.id.main_msg);
        msgTv.setText("Hello " + read(Constant.SHARED_PREFS.KEY_USER_NAME) + " your Role " + read(Constant.SHARED_PREFS.KEY_USER_ROLE));
    }
}
