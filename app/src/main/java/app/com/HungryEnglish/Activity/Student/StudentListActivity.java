package app.com.HungryEnglish.Activity.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import app.com.HungryEnglish.Activity.Admin.AdminDashboardActivity;
import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Adapter.StudentListAdapter;
import app.com.HungryEnglish.Model.StudentList.StudentData;
import app.com.HungryEnglish.Model.StudentList.StudentListMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by R'jul on 7/14/2017.
 */

public class StudentListActivity extends BaseActivity {

    private RecyclerView recyclerStudentList;
    List<StudentData> studentList;
    private StudentListAdapter studentListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        idMapping();

        callStudentListApi();
    }

    private void idMapping() {
        recyclerStudentList = (RecyclerView) findViewById(R.id.recyclerStudentList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(AdminDashboardActivity.class);
        finish();

    }

    // CALL TEACHER LIST API HERE
    private void callStudentListApi() {
        if (!Utils.checkNetwork(StudentListActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), StudentListActivity.this);
            return;
        } else {
            Utils.showDialog(StudentListActivity.this);
            ApiHandler.getApiService().getStudentList(getStudentDetail(), new retrofit.Callback<StudentListMainResponse>() {
                @Override
                public void success(StudentListMainResponse studentListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (studentListMainResponse == null) {
                        Toast.makeText(StudentListActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (studentListMainResponse.getStatus() == null) {
                        Toast.makeText(StudentListActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (studentListMainResponse.getStatus().equals("false")) {
                        Toast.makeText(StudentListActivity.this, "" + studentListMainResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (studentListMainResponse.getStatus().equals("true")) {
                        studentList = new ArrayList<StudentData>();
                        studentList = studentListMainResponse.getData();

                        studentListAdapter = new StudentListAdapter(StudentListActivity.this, studentList);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(StudentListActivity.this);
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerStudentList.setLayoutManager(mLayoutManager);
                        recyclerStudentList.setItemAnimator(new DefaultItemAnimator());
                        recyclerStudentList.setAdapter(studentListAdapter);
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Toast.makeText(StudentListActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private Map<String, String> getStudentDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("role", "student");
        map.put("status", "1");
        return map;
    }
}
