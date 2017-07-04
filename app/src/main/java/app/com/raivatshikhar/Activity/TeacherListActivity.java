package app.com.raivatshikhar.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import app.com.raivatshikhar.Adapter.TeacherListAdapter;
import app.com.raivatshikhar.R;
import app.com.raivatshikhar.Util.Constant;
import app.com.raivatshikhar.Util.Utils;

/**
 * Created by Rujul on 7/1/2017.
 */

public class TeacherListActivity extends BaseActivity {

    RecyclerView recyclerTearcherList;
    private List<HashMap<String, String>> teacherList;
    private TeacherListAdapter teacherListAdapter;
    ImageView imgListHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);


        teacherList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> data = new HashMap<>();
            data.put("image", "https://image.freepik.com/free-photo/smiling-teacher-with-blackboard-background_1098-866.jpg");
            data.put("name", "Teacher Shah");
            data.put("gender", "Male");
            data.put("experience", String.valueOf(i + 1));
            data.put("avaibility", "Any Time");
            teacherList.add(data);
        }

        idMapping();
    }

    private void idMapping() {

        recyclerTearcherList = (RecyclerView) findViewById(R.id.recyclerTearcherList);
        imgListHeader = (ImageView) findViewById(R.id.image_teacher_list_header);

        teacherListAdapter = new TeacherListAdapter(TeacherListActivity.this, teacherList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTearcherList.setLayoutManager(mLayoutManager);
        recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
        recyclerTearcherList.setAdapter(teacherListAdapter);


//        callTeacherListApi();
    }


//    // CALL TEACHER LIST API HERE
//    private void callTeacherListApi() {
//
//        if (!Utils.checkNetwork(TeacherListActivity.this)) {
//
//            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), TeacherListActivity.this);
//
//            return;
//        } else {
//            Utils.showDialog(TeacherListActivity.this);
//            ApiHandler.getApiService().getTeacherList(getLoginDetail(), new retrofit.Callback<TeacherListMainResponse>() {
//
//                @Override
//                public void success(TeacherListMainResponse teacherListMainResponse, Response response) {
//                    Utils.dismissDialog();
//                    if (teacherListMainResponse == null) {
//                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (teacherListMainResponse.getStatus() == null) {
//                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (teacherListMainResponse.getStatus().equals("false")) {
//
//                        Toast.makeText(getApplicationContext(), "" + teacherListMainResponse.getMsg(), Toast.LENGTH_SHORT).show();
//
//                        return;
//                    }
//                    if (teacherListMainResponse.getStatus().equals("true")) {
//
//
//                        startActivity(new Intent(TeacherListActivity.this, MainActivity.class));
//
//
//                        finish();
//                    }
//
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    error.printStackTrace();
//                    error.getMessage();
//                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//
//    }
//
//    private Map<String, String> getLoginDetail() {
//        Map<String, String> map = new HashMap<>();
//        map.put("u_pass", "" + passwordEdt.getText().toString());
//        map.put("u_name", "" + emailEdt.getText());
//
//        Log.e("map", "LOGIN " + map);
//        return map;
//    }

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
