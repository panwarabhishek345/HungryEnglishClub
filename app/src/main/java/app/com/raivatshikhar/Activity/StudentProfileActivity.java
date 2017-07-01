package app.com.raivatshikhar.Activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import app.com.raivatshikhar.Model.Profile.StudentProfileMainResponse;
import app.com.raivatshikhar.R;
import app.com.raivatshikhar.Services.ApiHandler;
import app.com.raivatshikhar.Util.Constant;
import app.com.raivatshikhar.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.raivatshikhar.R.id.avaibilityEdit;

/**
 * Created by Bhadresh Chavada on 27-06-2017.
 */

public class StudentProfileActivity extends BaseActivity {

    private EditText fullNameStudentEdit, ageEdit, yearOfExpEdit, nearRailwayStationEdit, addressEdit, specialSkillsEdit, avaibilityStudentEdit;
    private RadioGroup radioSex;
    private RadioButton radioButton;
    private Button login_register;
    private String sex = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        idMapping();

        setOnClick();

    }


    private void idMapping() {

        fullNameStudentEdit = (EditText) findViewById(R.id.fullNameStudentEdit);

        ageEdit = (EditText) findViewById(R.id.ageEdit);

        yearOfExpEdit = (EditText) findViewById(R.id.yearOfExpEdit);

        nearRailwayStationEdit = (EditText) findViewById(R.id.nearRailwayStationEdit);

        addressEdit = (EditText) findViewById(R.id.addressEdit);

        avaibilityStudentEdit = (EditText) findViewById(avaibilityEdit);

        specialSkillsEdit = (EditText) findViewById(R.id.specialSkillsEdit);

        login_register = (Button) findViewById(R.id.login_register);

        radioSex = (RadioGroup) findViewById(R.id.radioSex);

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

                if (yearOfExpEdit.getText().toString().equalsIgnoreCase("")) {
                    yearOfExpEdit.setError("Enter Year Of English Experience");
                    yearOfExpEdit.requestFocus();
                    return;
                }


                if (nearRailwayStationEdit.getText().toString().equalsIgnoreCase("")) {
                    nearRailwayStationEdit.setError("Enter Nearest Railway Station");
                    nearRailwayStationEdit.requestFocus();
                    return;
                }

                if (addressEdit.getText().toString().equalsIgnoreCase("")) {
                    addressEdit.setError("Enter Address");
                    addressEdit.requestFocus();
                    return;
                }


                if (avaibilityStudentEdit.getText().toString().equalsIgnoreCase("")) {
                    avaibilityStudentEdit.setError("Enter Avaibility");
                    avaibilityStudentEdit.requestFocus();
                    return;
                }


                if (specialSkillsEdit.getText().toString().equalsIgnoreCase("")) {
                    specialSkillsEdit.setError("Enter Special Skills");
                    specialSkillsEdit.requestFocus();
                    return;
                }


                // get selected radio button from radioGroup
                int selectedId = radioSex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

                sex = radioButton.getText().toString();

                Toast.makeText(StudentProfileActivity.this,
                        radioButton.getText(), Toast.LENGTH_SHORT).show();

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

                    Toast.makeText(getApplicationContext(), "" + parentProfileMainResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(StudentProfileActivity.this, TeacherProfileActivity.class));

                    finish();
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
        String userId = Utils.ReadSharePrefrence(StudentProfileActivity.this, Constant.SHARED_PREFS.KEY_USER_ID);
        map.put("uId", "" + userId);
        map.put("fullname", "" + String.valueOf(fullNameStudentEdit.getText()));
        map.put("available_time", String.valueOf(avaibilityStudentEdit.getText()));
        map.put("age", String.valueOf(ageEdit.getText()));
        map.put("sex", String.valueOf(sex));
        map.put("experience", String.valueOf(yearOfExpEdit.getText()));
        map.put("station", String.valueOf(nearRailwayStationEdit.getText()));
        map.put("address", String.valueOf(addressEdit.getText().toString()));
        map.put("skill", String.valueOf(specialSkillsEdit.getText().toString()));

        Log.e("map", "LOGIN " + map);
        return map;
    }

}
