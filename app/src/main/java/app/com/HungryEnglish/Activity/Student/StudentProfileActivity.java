package app.com.HungryEnglish.Activity.Student;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import app.com.HungryEnglish.Activity.Admin.AdminDashboardActivity;
import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.LoginActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherListActivity;
import app.com.HungryEnglish.Model.Profile.StudentGetProfileMainResponse;
import app.com.HungryEnglish.Model.Profile.StudentProfileMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherProfileMain;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.Util.Constant.BASEURL;
import static app.com.HungryEnglish.Util.Constant.SHARED_PREFS.KEY_USER_ID;
import static app.com.HungryEnglish.Util.Constant.SHARED_PREFS.KEY_USER_ROLE;


/**
 * Created by Bhadresh Chavada on 27-06-2017.
 */

public class StudentProfileActivity extends BaseActivity {

    private EditText fullNameStudentEdit, ageEdit, nearRailwayStationEdit, avaibilityStudentEdit;
    private RadioGroup radioSex;
    private RadioButton radioMale, radioFemale;
    private RadioButton radioButton;
    private Button login_register;
    private String sex = "";
    private String id = "", role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        idMapping();
        getDataFromIntent();
        setOnClick();
        getProfile();
    }

    private void getDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            role = extras.getString("role");
        }
    }


    private void idMapping() {

        fullNameStudentEdit = (EditText) findViewById(R.id.fullNameStudentEdit);

        ageEdit = (EditText) findViewById(R.id.ageEdit);

        nearRailwayStationEdit = (EditText) findViewById(R.id.nearRailwayStationEdit);

        avaibilityStudentEdit = (EditText) findViewById(R.id.avaibilityStudentEdit);

        login_register = (Button) findViewById(R.id.login_register);

        radioSex = (RadioGroup) findViewById(R.id.radioSex);

        radioMale = (RadioButton) findViewById(R.id.radioMale);

        radioFemale = (RadioButton) findViewById(R.id.radioFemale);


    }


    private void setOnClick() {

        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fullNameStudentEdit.getText().toString().equalsIgnoreCase("")) {
                    fullNameStudentEdit.setError("Enter Full Name");
                    fullNameStudentEdit.requestFocus();
                    return;
                }

                if (ageEdit.getText().toString().equalsIgnoreCase("")) {
                    ageEdit.setError("Enter Age");
                    ageEdit.requestFocus();
                    return;
                }

                if (nearRailwayStationEdit.getText().toString().equalsIgnoreCase("")) {
                    nearRailwayStationEdit.setError("Enter Nearest Railway Station");
                    nearRailwayStationEdit.requestFocus();
                    return;
                }

                if (avaibilityStudentEdit.getText().toString().equalsIgnoreCase("")) {
                    avaibilityStudentEdit.setError("Enter Avaibility");
                    avaibilityStudentEdit.requestFocus();
                    return;
                }


                // get selected radio button from radioGroup
                int selectedId = radioSex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

                sex = radioButton.getText().toString();

                callParentProfileApi();

            }
        });

    }

    private void callParentProfileApi() {
        if (!Utils.checkNetwork(StudentProfileActivity.this)) {

            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), StudentProfileActivity.this);

            return;
        }

        //  SHOW PROGRESS DIALOG
        Utils.showDialog(StudentProfileActivity.this);

        ApiHandler.getApiService().getParentProfile(getParentProfileDetail(), new retrofit.Callback<StudentProfileMainResponse>() {

            @Override
            public void success(StudentProfileMainResponse parentProfileMainResponse, Response response) {
                Utils.dismissDialog();
                if (parentProfileMainResponse == null) {
                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (parentProfileMainResponse.getStatus() == null) {
                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (parentProfileMainResponse.getStatus().equals("false")) {
                    Toast.makeText(getApplicationContext(), "" + parentProfileMainResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    return;
                }
                if (parentProfileMainResponse.getStatus().equals("true")) {

                    Toast.makeText(getApplicationContext(), "User Profile Get successfully", Toast.LENGTH_SHORT).show();

                    Utils.WriteSharePrefrence(StudentProfileActivity.this, Constant.SHARED_PREFS.KEY_IS_ACTIVE, "1");
                    if (!role.equals("")) {
                        finish();

                    } else {
                        startActivity(new Intent(StudentProfileActivity.this, TeacherListActivity.class));
                    }


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

    private Map<String, String> getParentProfileDetail() {
        Map<String, String> map = new HashMap<>();
        if (role.equalsIgnoreCase("")) {
            String userId = Utils.ReadSharePrefrence(StudentProfileActivity.this, Constant.SHARED_PREFS.KEY_USER_ID);
            map.put("uId", "" + userId);
        } else {
            map.put("uId", id);
        }

        map.put("fullname", "" + String.valueOf(fullNameStudentEdit.getText()));
        map.put("available_time", String.valueOf(avaibilityStudentEdit.getText()));
        map.put("age", String.valueOf(ageEdit.getText()));
        map.put("sex", String.valueOf(sex));
        map.put("station", String.valueOf(nearRailwayStationEdit.getText()));
        return map;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:

                final Dialog dialog = new Dialog(StudentProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_logout);
                dialog.setCancelable(false);
                TextView tvLogout = (TextView) dialog.findViewById(R.id.btLogoutPopupLogout);
                TextView tvCancel = (TextView) dialog.findViewById(R.id.btCancelPopupLogout);
                tvLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.ClearaSharePrefrence(StudentProfileActivity.this);

//                        startActivity(LoginActivity.class, true);

                        Utils.WriteSharePrefrence(StudentProfileActivity.this, Constant.SHARED_PREFS.KEY_IS_LOGGED_IN, "0");
                        Intent intent = new Intent(StudentProfileActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

        }
        return true;
    }

    private void getProfile() {
        if (!Utils.checkNetwork(StudentProfileActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), StudentProfileActivity.this);
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        if (role.equalsIgnoreCase("")) {
            map.put("uId", read(KEY_USER_ID));
            map.put("role", read(KEY_USER_ROLE));
        } else {
            map.put("uId", id);
            map.put("role", role);
        }
        ApiHandler.getApiService().getStudentProfile(map, new Callback<StudentGetProfileMainResponse>() {
            @Override
            public void success(StudentGetProfileMainResponse studentGetProfileMainResponse, Response response) {
                toast(studentGetProfileMainResponse.getMsg());
                fullNameStudentEdit.setText(studentGetProfileMainResponse.getData().getFullName());

                if (studentGetProfileMainResponse.getInfo() != null) {

                    avaibilityStudentEdit.setText(studentGetProfileMainResponse.getInfo().getAvailableTime());
                    ageEdit.setText(studentGetProfileMainResponse.getInfo().getAge());

                    if (studentGetProfileMainResponse.getInfo().getSex().equalsIgnoreCase("male")) {
                        radioMale.setChecked(true);
                    } else {
                        radioFemale.setChecked(true);
                    }

                    nearRailwayStationEdit.setText(studentGetProfileMainResponse.getInfo().getStation());


                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

}
