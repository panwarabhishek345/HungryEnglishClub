package app.com.raivatshikhar.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import app.com.raivatshikhar.R;
import app.com.raivatshikhar.Util.Constant;
import app.com.raivatshikhar.Util.Utils;

public class MainActivity extends BaseActivity {
    public TextView msgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msgTv = (TextView) findViewById(R.id.main_msg);
        msgTv.setText("Hello " + read(Constant.SHARED_PREFS.KEY_USER_NAME) + " your Role " + read(Constant.SHARED_PREFS.KEY_USER_ROLE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String role = Utils.ReadSharePrefrence(MainActivity.this, Constant.SHARED_PREFS.KEY_USER_ROLE);
        switch (item.getItemId()) {
            case R.id.logout:
                Utils.ClearaSharePrefrence(MainActivity.this);
                startActivity(LoginActivity.class, true);

                break;
            case R.id.profile:
                if (role.equalsIgnoreCase("student"))
                    startActivity(StudentProfileActivity.class);
                else if (role.equalsIgnoreCase("teacher"))
                    startActivity(TeacherProfileActivity.class);
        }
        return true;
    }
}
