package app.com.HungryEnglish.Activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.HungryEnglish.Adapter.TeacherListAdapter;
import app.com.HungryEnglish.Model.TeacherList.TeacherListMainResponse;
import app.com.HungryEnglish.Model.TeacherList.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity {
    public TextView msgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
