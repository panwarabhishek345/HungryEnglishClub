package app.com.HungryEnglish.Activity.Teacher;


import com.squareup.okhttp.Call;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.ForgotPassword;
import app.com.HungryEnglish.Activity.LoginActivity;
import app.com.HungryEnglish.Model.ForgotPassord.ForgotPasswordModel;
import app.com.HungryEnglish.Model.Profile.TeacherProfileMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherProfileMain;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Services.DownloadService;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Mail;
import app.com.HungryEnglish.Util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import static app.com.HungryEnglish.Util.Constant.BASEURL;
import static app.com.HungryEnglish.Util.Constant.SHARED_PREFS.KEY_USER_ID;
import static app.com.HungryEnglish.Util.Constant.SHARED_PREFS.KEY_USER_ROLE;
import static app.com.HungryEnglish.Util.Utils.getPath;
import static app.com.HungryEnglish.Util.Utils.getRealPathFromURI;


/**
 * Created by Rujul on 6/30/2017.
 */

public class TeacherProfileActivity extends BaseActivity implements
        View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private ImageView profileImage, idProofImage, ivCVFileStatus, ivAudioFileStatus, ivViewCv, ivViewAudio;
    final int SELECT_PHOTO = 100;
    final int SELECT_ID_PROOF = 200;
    final int SELECT_FILE = 300;
    final int SELECT_AUDIO = 400;
    private EditText btnCvUpload, btnAudioFile, userNameEdit, emailEdit, currnetPlaceEdit, fullNameTeacherEdit, avaibilityDateTeacherEdit, specialSkillTeacherEdit, etMobileOrWechatId;

    private String pathProfilePic = null, pathCvDoc = null, pathIdProofPic = null, pathAudioFile = null;
    private Button btnSubmiTeacherProfile;
    private String id = "", role = "";
    String cvFileName, audioFileName, audioPath, resumePath;
    ProgressDialog mProgressDialog;
    String CallFrom = "";
    Context mContext;
    RelativeLayout layoutIdProof;
    LinearLayout layoutCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        mContext = TeacherProfileActivity.this;
        getDataFromIntent();
        idMapping();

        getProfile();
    }

    private void getDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            role = extras.getString("role");
            CallFrom = extras.getString("callFrom");
//            Toast.makeText(mContext, CallFrom, Toast.LENGTH_SHORT).show();
        }
    }

    private void idMapping() {
        profileImage = (ImageView) findViewById(R.id.profile_image);
        idProofImage = (ImageView) findViewById(R.id.idProofImage);
        ivCVFileStatus = (ImageView) findViewById(R.id.ivCVFileStatus);
        ivAudioFileStatus = (ImageView) findViewById(R.id.ivAudioFileStatus);
        ivViewCv = (ImageView) findViewById(R.id.ivViewCv);
        ivViewAudio = (ImageView) findViewById(R.id.ivViewAudio);

        etMobileOrWechatId = (EditText) findViewById(R.id.etMobileOrWechatId);
        currnetPlaceEdit = (EditText) findViewById(R.id.currnetPlaceEdit);
        fullNameTeacherEdit = (EditText) findViewById(R.id.fullNameTeacherEdit);
        avaibilityDateTeacherEdit = (EditText) findViewById(R.id.avaibilityDateTeacherEdit);
        specialSkillTeacherEdit = (EditText) findViewById(R.id.specialSkillTeacherEdit);
        btnAudioFile = (EditText) findViewById(R.id.btn_audio_file);
        btnCvUpload = (EditText) findViewById(R.id.btn_cv_file);
        userNameEdit = (EditText) findViewById(R.id.usernameStudentEdit);
        layoutCV = (LinearLayout) findViewById(R.id.layout_cv);
        layoutIdProof = (RelativeLayout) findViewById(R.id.layout_idproof);

        emailEdit = (EditText) findViewById(R.id.emailEdit);
        btnSubmiTeacherProfile = (Button) findViewById(R.id.btnSubmiTeacherProfile);

        String role = Utils.ReadSharePrefrence(TeacherProfileActivity.this, Constant.SHARED_PREFS.KEY_USER_ROLE);
        if (role.equalsIgnoreCase("admin")) {
            ivViewCv.setVisibility(View.VISIBLE);
            ivViewAudio.setVisibility(View.VISIBLE);
        }


        if (CallFrom.equals("Student")) {

            btnSubmiTeacherProfile.setText(TeacherProfileActivity.this.getString(R.string.requst_teacher));

            TextInputLayout wechatLayout = (TextInputLayout) findViewById(R.id.wechat_id_textinput);
            TextInputLayout cvLayout = (TextInputLayout) findViewById(R.id.text_input_layout_uploadCV);
            btnSubmiTeacherProfile.setOnClickListener(this);
            ivViewCv.setOnClickListener(this);
            ivViewAudio.setOnClickListener(this);

            btnCvUpload.setKeyListener(null);
            btnAudioFile.setKeyListener(null);
            userNameEdit.setKeyListener(null);
            emailEdit.setKeyListener(null);
            currnetPlaceEdit.setKeyListener(null);
            fullNameTeacherEdit.setKeyListener(null);
            avaibilityDateTeacherEdit.setKeyListener(null);
            specialSkillTeacherEdit.setKeyListener(null);
            etMobileOrWechatId.setKeyListener(null);
            wechatLayout.setVisibility(View.GONE);
            cvLayout.setVisibility(View.GONE);
            ivCVFileStatus.setVisibility(View.GONE);
            ivViewCv.setVisibility(View.GONE);
            layoutIdProof.setVisibility(View.GONE);
            layoutCV.setVisibility(View.GONE);


        } else if (CallFrom.equals("Admin")) {
            btnSubmiTeacherProfile.setText(TeacherProfileActivity.this.getString(R.string.submit));

            profileImage.setOnClickListener(this);
            idProofImage.setOnClickListener(this);
            btnCvUpload.setOnClickListener(this);
            currnetPlaceEdit.setOnClickListener(this);
            btnSubmiTeacherProfile.setOnClickListener(this);
            btnAudioFile.setOnClickListener(this);
            ivViewCv.setOnClickListener(this);
            ivViewAudio.setOnClickListener(this);
        } else {
            btnSubmiTeacherProfile.setText(TeacherProfileActivity.this.getString(R.string.submit));

            profileImage.setOnClickListener(this);
            idProofImage.setOnClickListener(this);
            btnCvUpload.setOnClickListener(this);
            currnetPlaceEdit.setOnClickListener(this);
            btnSubmiTeacherProfile.setOnClickListener(this);
            btnAudioFile.setOnClickListener(this);
            ivViewCv.setOnClickListener(this);
            ivViewAudio.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivViewCv:

                downloadCv();
                break;

            case R.id.ivViewAudio:
                downloadAudio();
                break;

            case R.id.profile_image:
                uploadImage(SELECT_PHOTO);
                break;
            case R.id.idProofImage:
                uploadImage(SELECT_ID_PROOF);
                break;
            case R.id.btn_cv_file:
                uploadFile();
                break;
            case R.id.btn_audio_file:
                uploadAudio();
                break;
            case R.id.btnSubmiTeacherProfile:
                if (CallFrom.equals("Student")) {
                    RequestToTeacher();
                } else {

                    if (fullNameTeacherEdit.getText().toString().equals("")) {
                        fullNameTeacherEdit.setError("Enter Name");
                        fullNameTeacherEdit.requestFocus();
                        return;
                    }
                    if (avaibilityDateTeacherEdit.getText().toString().equals("")) {
                        avaibilityDateTeacherEdit.setError("Enter Avaibility");
                        avaibilityDateTeacherEdit.requestFocus();
                        return;
                    }
                    if (specialSkillTeacherEdit.getText().toString().equals("")) {
                        specialSkillTeacherEdit.setError("Enter Special Skills");
                        specialSkillTeacherEdit.requestFocus();
                        return;
                    }

                    if (etMobileOrWechatId.getText().toString().equals("")) {
                        etMobileOrWechatId.setError("Enter Mobile No. or Wechat Id");
                        etMobileOrWechatId.requestFocus();
                    }

//                if (pathProfilePic.equalsIgnoreCase("")) {
//                    Toast.makeText(TeacherProfileActivity.this, "Please Select Profile Image", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (pathIdProofPic.equalsIgnoreCase("")) {
//                    Toast.makeText(TeacherProfileActivity.this, "Please Select id proof Image", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (pathCvDoc.equalsIgnoreCase("")) {
//                    Toast.makeText(TeacherProfileActivity.this, "Please Select CV document image", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (pathAudioFile.equalsIgnoreCase("")) {
//                    Toast.makeText(TeacherProfileActivity.this, "Please Select Audio file", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                    callTeacherProfileApi();
                    break;
                }
        }
    }

    private void downloadCv() {

        if (resumePath != null) {
            mProgressDialog = new ProgressDialog(TeacherProfileActivity.this);
            mProgressDialog.setMessage("Please wait Download Resume");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
            Log.d("PATH", resumePath);
            Toast.makeText(this, "Download start", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DownloadService.class);
            intent.putExtra("url", resumePath);
            intent.putExtra("receiver", new DownloadReceiver(new Handler()));
            startService(intent);

        } else {
            Toast.makeText(this, "Path not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadAudio() {

        if (audioPath != null) {
            mProgressDialog = new ProgressDialog(TeacherProfileActivity.this);
            mProgressDialog.setMessage("Please wait Download Audio");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
            Toast.makeText(this, "Download start", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DownloadService.class);
            intent.putExtra("url", audioPath);
            intent.putExtra("receiver", new DownloadReceiver(new Handler()));
            startService(intent);
            Log.d("PATH", audioPath);
        } else {
            Toast.makeText(this, "Path not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("*/*");
        startActivityForResult(photoPickerIntent, SELECT_FILE);
    }


    private void uploadAudio() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("audio/*");
        startActivityForResult(photoPickerIntent, SELECT_AUDIO);
    }

    private void uploadImage(int PHOTO_CONSTANT) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PHOTO_CONSTANT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    toast(R.string.sucess_external_storage_msg);

                } else {
                    toast(R.string.error_permission_msg);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
//        getRealPathFromURI(this, data.getData());
        if (resultCode == Activity.RESULT_OK) {
            switch (reqCode) {
                case SELECT_PHOTO:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathProfilePic = getRealPathFromURI(this, data.getData());
                    } else {
                        pathProfilePic = getPath(this, data.getData());
                    }
                    Picasso.with(TeacherProfileActivity.this).load(Uri.fromFile(new File(pathProfilePic))).error(R.drawable.ic_user_default).into(profileImage);
                    break;
                case SELECT_ID_PROOF:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathIdProofPic = getRealPathFromURI(this, data.getData());
                    } else {
                        pathIdProofPic = getPath(this, data.getData());
                    }
                    Picasso.with(TeacherProfileActivity.this).load(Uri.fromFile(new File(pathIdProofPic))).error(R.drawable.ic_user_default).into(idProofImage);
                    break;
                case SELECT_FILE:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathCvDoc = getRealPathFromURI(this, data.getData());
                    } else {
                        pathCvDoc = getPath(this, data.getData());
                    }
                    String[] spiltArray = pathCvDoc.split("/");
                    cvFileName = spiltArray[spiltArray.length - 1];
                    btnCvUpload.setText(cvFileName);
                    if (!pathCvDoc.equals("")) {
                        Picasso.with(TeacherProfileActivity.this).load(R.drawable.ic_file).into(ivCVFileStatus);
                    }
                    break;
                case SELECT_AUDIO:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathAudioFile = getRealPathFromURI(this, data.getData());
                    } else {
                        pathAudioFile = getPath(this, data.getData());
                    }
                    String[] spiltAudioArray = pathCvDoc.split("/");
                    audioFileName = spiltAudioArray[spiltAudioArray.length - 1];
                    btnAudioFile.setText(audioFileName);
                    if (!btnAudioFile.equals("")) {
                        Picasso.with(TeacherProfileActivity.this).load(R.drawable.ic_file).into(ivAudioFileStatus);
                    }
                    break;


            }
        } else {
            toast("You cancel current task.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {

            }

        } else {

        }

    }

    private void getProfile() {
        if (!Utils.checkNetwork(TeacherProfileActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), TeacherProfileActivity.this);
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
        ApiHandler.getApiService().getTeacherProfile(map, new Callback<TeacherProfileMain>() {
            @Override
            public void success(TeacherProfileMain teacherProfileMain, Response response) {
                fullNameTeacherEdit.setText(teacherProfileMain.getData().getFullName());
                if (teacherProfileMain.getInfo() != null) {
                    avaibilityDateTeacherEdit.setText(teacherProfileMain.getInfo().getAvailableTime());
                    currnetPlaceEdit.setText(teacherProfileMain.getInfo().getAddress());
                    specialSkillTeacherEdit.setText(teacherProfileMain.getInfo().getSkills());
                    Picasso.with(TeacherProfileActivity.this).load(BASEURL + teacherProfileMain.getInfo().getProfileImage()).into(profileImage);
                    Log.e("ID IMG", "" + BASEURL + teacherProfileMain.getInfo().getIdImage());
                    Picasso.with(TeacherProfileActivity.this).load(BASEURL + teacherProfileMain.getInfo().getIdImage()).into(idProofImage);
                    resumePath = teacherProfileMain.getInfo().getResume();
                    String[] cvFileArray = resumePath.split("/");
                    if (cvFileArray.length > 1) {
                        btnCvUpload.setText(cvFileArray[cvFileArray.length - 1]);
                        Picasso.with(TeacherProfileActivity.this).load(R.drawable.ic_file).into(ivCVFileStatus);
                    }

                    audioPath = teacherProfileMain.getInfo().getAudioFile();
                    String[] audioFileArray = audioPath.split("/");
                    if (audioFileArray.length > 1) {
                        btnAudioFile.setText(audioFileArray[audioFileArray.length - 1]);
                        Picasso.with(TeacherProfileActivity.this).load(R.drawable.ic_file).into(ivAudioFileStatus);
                    }

                    emailEdit.setText(teacherProfileMain.getData().getUsername());
                    userNameEdit.setText(teacherProfileMain.getData().getUsername());
                    etMobileOrWechatId.setText(String.valueOf(teacherProfileMain.getData().getMobNo()));
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void callTeacherProfileApi() {
        if (!Utils.checkNetwork(TeacherProfileActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), TeacherProfileActivity.this);
            return;
        }
//        if (pathProfilePic != null && pathIdProofPic != null && pathIdProofPic != null && pathIdProofPic != null) {
        //  SHOW PROGRESS DIALOG
        Utils.showDialog(TeacherProfileActivity.this);
        TypedFile proImage = null, idProof = null, resume = null, audiofile = null;

        Map<String, TypedFile> files = new HashMap<String, TypedFile>();
//        files.put("my file number one", new TypedFile("image/jpg", new File(filename)));
//        files.put("my file number two", new TypedFile("image/jpg", new File(filename)));


        if (pathProfilePic != null) {
            proImage = new TypedFile("multipart/form-data", new File(pathProfilePic));
            files.put("proImage", proImage);
        }

        if (pathIdProofPic != null) {
            idProof = new TypedFile("multipart/form-data", new File(pathIdProofPic));
            files.put("idProof", idProof);
        }
        if (pathCvDoc != null) {
            resume = new TypedFile("multipart/form-data", new File(pathCvDoc));
            files.put("resume", resume);
        }
        if (pathAudioFile != null) {
            audiofile = new TypedFile("multipart/form-data", new File(pathAudioFile));
        }



        ApiHandler.getApiService().createTeacherProfile(getTeacherProfileDetail(), files, new Callback<TeacherProfileMainResponse>() {

            @Override
            public void success(TeacherProfileMainResponse teacherProfileMainResponse, Response response) {
                Utils.dismissDialog();
                if (teacherProfileMainResponse == null) {
                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (teacherProfileMainResponse.getStatus() == null) {
                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (teacherProfileMainResponse.getStatus().equals("false")) {
                    Toast.makeText(getApplicationContext(), "" + teacherProfileMainResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (teacherProfileMainResponse.getStatus().equals("true")) {
                    Toast.makeText(getApplicationContext(), "" + teacherProfileMainResponse.getMsg(), Toast.LENGTH_SHORT).show();
//                        startActivity(MainActivity.class);
                    finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                error.printStackTrace();
                toast("Something Wrong");
            }
        });
//        }
//        else {
//            Utils.dismissDialog();
//            toast("Please select all the files");
//        }
    }

    private Map<String, String> getTeacherProfileDetail() {
        Map<String, String> map = new HashMap<>();
        if (role.equalsIgnoreCase("")) {
            String userId = Utils.ReadSharePrefrence(TeacherProfileActivity.this, KEY_USER_ID);
            map.put("uId", userId);
        } else {
            map.put("uId", id);
        }
        map.put("fullname", String.valueOf(fullNameTeacherEdit.getText()));
        map.put("available_time", String.valueOf(avaibilityDateTeacherEdit.getText()));
        map.put("address", String.valueOf(currnetPlaceEdit.getText()));
        map.put("skill", String.valueOf(specialSkillTeacherEdit.getText()));
        map.put("mob_no", String.valueOf(etMobileOrWechatId.getText()));
        return map;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                final Dialog dialog = new Dialog(TeacherProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_logout);
                dialog.setCancelable(false);
                TextView tvLogout = (TextView) dialog.findViewById(R.id.btLogoutPopupLogout);
                TextView tvCancel = (TextView) dialog.findViewById(R.id.btCancelPopupLogout);
                tvLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clear();
                        write(Constant.SHARED_PREFS.KEY_IS_LOGGED_IN, "0");
                        Intent intent = new Intent(TeacherProfileActivity.this, LoginActivity.class);
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

    public class DownloadReceiver extends ResultReceiver {

        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    mProgressDialog.dismiss();
                }
            }
        }
    }

    private void RequestToTeacher() {


        Utils.showDialog(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("teacherId", id);
        map.put("studentId", Utils.ReadSharePrefrence(mContext, Constant.SHARED_PREFS.KEY_USER_ID));

//        http://smartsquad.16mb.com/HungryEnglish/api/add_request.php?teacherId=1&studentId=2
        ApiHandler.getApiService().addRequest(map, new retrofit.Callback<ForgotPasswordModel>() {


            @Override
            public void success(ForgotPasswordModel forgotPasswordModel, Response response) {

                Utils.dismissDialog();


                if (forgotPasswordModel.getStatus().toString().equals("true")) {
                    Toast.makeText(mContext, forgotPasswordModel.getMsg(), Toast.LENGTH_SHORT).show();
                    sendEmail(Utils.ReadSharePrefrence(mContext, Constant.SHARED_PREFS.KEY_USER_NAME), String.valueOf(fullNameTeacherEdit.getText()));

                    onBackPressed();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                Toast.makeText(mContext, "Try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendEmail(String studentName, String TeacherName) {

        String message = "Welcome to Hungry English Club " + studentName + "Enquiry for Teacher  " + TeacherName;

        String[] recipients = {"idigi@live.com"};
        SendEmailAsyncTask email = new SendEmailAsyncTask();
//        email.activity = mContext;
        email.m = new Mail("hungryenglishclub@gmail.com", "rujulgandhi");
        email.m.set_from("hungryenglishclub@gmail.com");
        email.m.setBody(message);
        email.m.set_to(recipients);
        email.m.set_subject("Hungry English CLUB");
        email.execute();


    }


    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Mail m;
        ForgotPassword activity;

        public SendEmailAsyncTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (m.send()) {
//                    Toast.makeText(activity, "Email sent.", Toast.LENGTH_SHORT).show();


                } else {
//                Toast.makeText(activity, "Email failed to send.", Toast.LENGTH_SHORT).show();
//                    onBackPressed();
                }

                return true;
            } catch (AuthenticationFailedException e) {
                Log.e("EmailError", "Bad account details");
                e.printStackTrace();

//            Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                return false;
            } catch (MessagingException e) {
                Log.e("EmailError", "Email failed");
                e.printStackTrace();

//            Toast.makeText(activity, "Email failed to send.", Toast.LENGTH_SHORT).show();
                return false;
            } catch (Exception e) {
                e.printStackTrace();

//            Toast.makeText(activity, "Unexpected error occured.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

}