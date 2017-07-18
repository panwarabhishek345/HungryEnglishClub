package app.com.HungryEnglish.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Adapter.TeacherListAdapter;
import app.com.HungryEnglish.Adapter.TeacherPendingAdapter;
import app.com.HungryEnglish.Interface.OnRemoveTeacherClickListener;
import app.com.HungryEnglish.Model.Teacher.TeacherListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.Fragment.TeacherPendingListFragment.mContext;
import static app.com.HungryEnglish.Util.Constant.SHARED_PREFS.KEY_USER_ID;
import static app.com.HungryEnglish.Util.Constant.SHARED_PREFS.KEY_USER_ROLE;

/**
 * Created by Vnnovate on 7/5/2017.
 */

public class TeacherApprovedListFragment extends Fragment {
    View mView;
    private static RecyclerView recyclerTearcherList;
    static List<TeacherListResponse> teacherList;
    private static TeacherListAdapter teacherListAdapter;
    OnRemoveTeacherClickListener onRemoveTeacherClickListener;
    public static Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_teacher_aproved_list, container, false);
        mContext = getActivity();
        idMapping();
        callTeacherListApi();
        return mView;
    }

    private void idMapping() {
        recyclerTearcherList = (RecyclerView) mView.findViewById(R.id.recyclerTearcherList);
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

                        teacherListAdapter = new TeacherListAdapter(getActivity(), teacherList, (TeacherListAdapter.OnRemoveTeacherClickListener) onRemoveTeacherClickListener);
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

//    http://smartsquad.16mb.com/HungryEnglish/api/delete_user.php?id=19&role=student

    // CALL DELETE TEACHER FROM LIST API HERE
    public static void callRemoveTeacherFromListApi(final int pos) {
        if (!Utils.checkNetwork(mContext)) {
            Utils.showCustomDialog("Internet Connection !", mContext.getResources().getString(R.string.internet_connection_error), (Activity) mContext);
            return;
        } else {
            Utils.showDialog(mContext);
            ApiHandler.getApiService().getRemoveTeacherFromList(removeTeacherDetail(), new retrofit.Callback<TeacherListMainResponse>() {
                @Override
                public void success(TeacherListMainResponse teacherListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherListMainResponse == null) {
                        Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus() == null) {
                        Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("false")) {
                        Toast.makeText(mContext, "" + teacherListMainResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("true")) {
                        Toast.makeText(mContext, teacherListMainResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        teacherListAdapter = new TeacherListAdapter(mContext, teacherList);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerTearcherList.setLayoutManager(mLayoutManager);
                        recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
                        recyclerTearcherList.setAdapter(teacherListAdapter);
                        teacherList.remove(pos);
                        teacherListAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private static Map<String, String> removeTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("role", Utils.ReadSharePrefrence(mContext, KEY_USER_ROLE));
        map.put("id", Utils.ReadSharePrefrence(mContext, KEY_USER_ID));
        return map;
    }

}
