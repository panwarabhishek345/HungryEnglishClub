package app.com.HungryEnglish.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.HungryEnglish.Activity.LoginActivity;
import app.com.HungryEnglish.Activity.StudentProfileActivity;
import app.com.HungryEnglish.Activity.TeacherListActivity;
import app.com.HungryEnglish.Activity.TeacherProfileActivity;
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
 * Created by Vnnovate on 7/5/2017.
 */

public class TeacherApprovedListFragment extends Fragment {
   View mView;
    private RecyclerView recyclerTearcherList;
    List<TeacherListResponse> teacherList;
    private TeacherListAdapter teacherListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_teacher_aproved_list, container, false);
        idMapping();
        callTeacherListApi();
        return  mView;
    }

    private void idMapping() {
        recyclerTearcherList = (RecyclerView)mView. findViewById(R.id.recyclerTearcherList);
    }

    // CALL TEACHER LIST API HERE
    private void callTeacherListApi() {
        if (!Utils.checkNetwork(getActivity())) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), getActivity());
            return;
        } else {
            Utils.showDialog(getActivity());
            ApiHandler.getApiService().getTeacherList(getTeacherDetail(), new retrofit.Callback<TeacherListMainResponse>() {
                @Override
                public void success(TeacherListMainResponse teacherListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherListMainResponse == null) {
                        Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus() == null) {
                        Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("false")) {
                        Toast.makeText(getActivity(), "" + teacherListMainResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("true")) {
                        teacherList = new ArrayList<TeacherListResponse>();
                        teacherList = teacherListMainResponse.getData();

                        teacherListAdapter = new TeacherListAdapter(getActivity(), teacherList);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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
                    Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private Map<String, String> getTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("role", "teacher");
        map.put("status", "1");
        return map;
    }
}
