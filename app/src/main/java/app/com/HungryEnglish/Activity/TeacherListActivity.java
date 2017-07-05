package app.com.HungryEnglish.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import app.com.HungryEnglish.Adapter.TeacherListAdapter;
import app.com.HungryEnglish.Model.TeacherList.TeacherListMainResponse;
import app.com.HungryEnglish.Model.TeacherList.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rujul on 7/1/2017.
 * TEACHER LIST
 * http://smartsquad.16mb.com/HungryEnglish/api/getuserbystatus.php?role=teacher&status=0
 */

public class TeacherListActivity extends BaseActivity {

    RecyclerView recyclerTearcherList;
    private TeacherListAdapter teacherListAdapter;
    ImageView imgListHeader;
    List<TeacherListResponse> teacherList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        idMapping();

        if (Utils.ReadSharePrefrence(TeacherListActivity.this, Constant.SHARED_PREFS.KEY_USER_ROLE).equalsIgnoreCase("student")) {

            callTeacherListApi();
        }
    }

    private void idMapping() {

        recyclerTearcherList = (RecyclerView) findViewById(R.id.recyclerTearcherList);
        imgListHeader = (ImageView) findViewById(R.id.image_teacher_list_header);


    }


    // CALL TEACHER LIST API HERE
    private void callTeacherListApi() {

        if (!Utils.checkNetwork(TeacherListActivity.this)) {

            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), TeacherListActivity.this);

            return;
        } else {
            Utils.showDialog(TeacherListActivity.this);
            ApiHandler.getApiService().getTeacherList(getTeacherDetail(), new retrofit.Callback<TeacherListMainResponse>() {

                @Override
                public void success(TeacherListMainResponse teacherListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherListMainResponse == null) {
                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus() == null) {
                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("false")) {

                        Toast.makeText(getApplicationContext(), "" + teacherListMainResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("true")) {

                        teacherList = new ArrayList<TeacherListResponse>();
                        teacherList = teacherListMainResponse.getData();

                        Log.e("TeacherList", "" + teacherList.size());
                        Log.e("TeacherList", "" + teacherList);

                        teacherListAdapter = new TeacherListAdapter(TeacherListActivity.this, teacherList);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerTearcherList.setLayoutManager(mLayoutManager);
                        recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
                        recyclerTearcherList.setAdapter(teacherListAdapter);

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

    private Map<String, String> getTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("role", Utils.ReadSharePrefrence(TeacherListActivity.this, Constant.SHARED_PREFS.KEY_USER_ROLE));
        map.put("status", Utils.ReadSharePrefrence(TeacherListActivity.this, Constant.SHARED_PREFS.KEY_IS_ACTIVE));

        Log.e("map", "TEACHER LIST " + map);
        return map;
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
        String role = Utils.ReadSharePrefrence(TeacherListActivity.this, Constant.SHARED_PREFS.KEY_USER_ROLE);
        switch (item.getItemId()) {
            case R.id.logout:
                Utils.ClearaSharePrefrence(TeacherListActivity.this);
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
