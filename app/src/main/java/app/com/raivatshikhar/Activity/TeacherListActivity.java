package app.com.raivatshikhar.Activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import app.com.raivatshikhar.Adapter.TeacherListAdapter;
import app.com.raivatshikhar.R;

/**
 * Created by Rujul on 7/1/2017.
 */

public class TeacherListActivity extends Activity {

    RecyclerView recyclerTearcherList;
    private List<Movie> teacherList = new ArrayList<>();
    private TeacherListAdapter teacherListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        idMapping();
    }

    private void idMapping() {

        recyclerTearcherList = (RecyclerView) findViewById(R.id.recyclerTearcherList);

        teacherListAdapter = new TeacherListAdapter(TeacherListActivity.this,teacherList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTearcherList.setLayoutManager(mLayoutManager);
        recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
        recyclerTearcherList.setAdapter(teacherListAdapter);

    }
}
