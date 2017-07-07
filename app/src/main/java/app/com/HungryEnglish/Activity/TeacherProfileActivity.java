package app.com.HungryEnglish.Activity;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import app.com.HungryEnglish.Model.Profile.TeacherProfileMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


/**
 * Created by Rujul on 6/30/2017.
 */

public class TeacherProfileActivity extends BaseActivity implements
        View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    ImageView profileImage, idProofImage;
    final int SELECT_PHOTO = 100;
    final int SELECT_ID_PROOF = 200;
    final int SELECT_FILE = 300;
    final int SELECT_AUDIO = 400;
    Button btnCvUpload;
    EditText currnetPlaceEdit, fullNameTeacherEdit, avaibilityDateTeacherEdit, specialSkillTeacherEdit;
    String mLastUpdateTime, pathProfilePic, pathCvDoc, pathQualification, pathIdProofPic, pathAudioFile;
    private double lat = 0.0, lng = 0.0;
    private Button btnSubmiTeacherProfile, btnAudioFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        idMapping();
    }


    private void idMapping() {
        profileImage = (ImageView) findViewById(R.id.profile_image);
        idProofImage = (ImageView) findViewById(R.id.idProofImage);
        btnCvUpload = (Button) findViewById(R.id.btn_cv_file);

        currnetPlaceEdit = (EditText) findViewById(R.id.currnetPlaceEdit);

        fullNameTeacherEdit = (EditText) findViewById(R.id.fullNameTeacherEdit);

        avaibilityDateTeacherEdit = (EditText) findViewById(R.id.avaibilityDateTeacherEdit);

        specialSkillTeacherEdit = (EditText) findViewById(R.id.specialSkillTeacherEdit);

        btnSubmiTeacherProfile = (Button) findViewById(R.id.btnSubmiTeacherProfile);

        btnAudioFile = (Button) findViewById(R.id.btn_audio_file);

        profileImage.setOnClickListener(this);
        idProofImage.setOnClickListener(this);
        btnCvUpload.setOnClickListener(this);
        currnetPlaceEdit.setOnClickListener(this);
        btnSubmiTeacherProfile.setOnClickListener(this);
        btnAudioFile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                callTeacherProfileApi();
                break;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        if (data != null) {
            onSelectFromGalleryResult(data, reqCode);
        } else {
            Toast.makeText(TeacherProfileActivity.this, "Yopu didn't pick anything", Toast.LENGTH_SHORT).show();
        }
//        switch (reqCode) {
//            case SELECT_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    onSelectFromGalleryResult(data, SELECT_PHOTO);
//                }
//                break;
//            case SELECT_ID_PROOF:
//                if (resultCode == RESULT_OK) {
//                    onSelectFromGalleryResult(data, SELECT_ID_PROOF);
////                    try {
////                        final Uri imageUri = data.getData();
////                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
////                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
////                        idProofImage.setImageBitmap(selectedImage);
////                        pathIdProofPic = imageUri.getPath();
////                    } catch (FileNotFoundException e) {
////                        e.printStackTrace();
////                    }
//                }
//                break;
//            case SELECT_FILE:
//                if (resultCode == SELECT_FILE) {
//                    if (data != null) {
////                        final Uri fileUri = data.getData();
////                        String Path = fileUri.getPath();
////                        btnCvUpload.setText(Path);
////                        pathCvDoc = Path;
//                        onSelectFromGalleryResult(data, SELECT_FILE);
//                    }
//                }
//                break;
//            case SELECT_AUDIO:
//                if (resultCode == SELECT_FILE) {
////                    final Uri fileUri = data.getData();
////                    String Path = fileUri.getPath();
////                    btnAudioFile.setText(Path);
////                    pathAudioFile = fileUri.getPath();
//
//                    onSelectFromGalleryResult(data, SELECT_AUDIO);
//                }
//                break;
//        }
    }


    private void onSelectFromGalleryResult(Intent data, int OPTION) {
        Uri selectedImageUri = data.getData();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            // Do something for 19 and above versions
            // OI FILE Manager
            String[] column = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, null, null, null);
            int columnIndex = cursor.getColumnIndex(column[0]);

            switch (OPTION) {

                case SELECT_PHOTO:
                    if (cursor.moveToFirst()) {
                        pathProfilePic = cursor.getString(columnIndex);
                    }
                    cursor.close();
                    Picasso.with(TeacherProfileActivity.this).load(selectedImageUri).error(R.drawable.ic_user_default).into(profileImage);
                    Log.e("PATH", "Image Path : " + pathProfilePic);
                    break;

                case SELECT_ID_PROOF:
                    if (cursor.moveToFirst()) {
                        pathIdProofPic = cursor.getString(columnIndex);
                    }
                    cursor.close();
                    Picasso.with(TeacherProfileActivity.this).load(selectedImageUri).error(R.drawable.ic_user_default).into(idProofImage);
                    Log.e("PATH", "Image Path : " + pathIdProofPic);
                    break;


                case SELECT_FILE:
                    if (cursor.moveToFirst()) {
                        pathCvDoc = cursor.getString(columnIndex);
                    }
                    cursor.close();
                    Log.e("PATH", "Image Path : " + pathCvDoc);
                    break;

                case SELECT_AUDIO:
                    if (cursor.moveToFirst()) {
                        pathAudioFile = cursor.getString(columnIndex);
                    }
                    cursor.close();
                    Log.e("PATH", "Image Path : " + pathAudioFile);
                    break;
            }


        } else {


            switch (OPTION) {

                case SELECT_PHOTO:
                    pathProfilePic = getPathFromURI(selectedImageUri);
                    Picasso.with(TeacherProfileActivity.this).load(selectedImageUri).error(R.drawable.ic_user_default).into(profileImage);
                    Log.e("PATH", "Image Path : " + pathProfilePic);
                    break;

                case SELECT_ID_PROOF:
                    pathIdProofPic = getPathFromURI(selectedImageUri);
                    Picasso.with(TeacherProfileActivity.this).load(selectedImageUri).error(R.drawable.ic_user_default).into(idProofImage);
                    Log.e("PATH", "Image Path : " + pathIdProofPic);
                    break;


                case SELECT_FILE:
                    pathCvDoc = getPathFromURI(selectedImageUri);
                    Log.e("PATH", "Image Path : " + pathCvDoc);
                    break;

                case SELECT_AUDIO:
                    pathAudioFile = getPathFromURI(selectedImageUri);
                    Log.e("PATH", "Image Path : " + pathAudioFile);
                    break;
            }

            Log.e("File Path", ">> " + pathIdProofPic);
            String imageName = pathIdProofPic.substring(pathIdProofPic.lastIndexOf('/') + 1);
            Picasso.with(TeacherProfileActivity.this).load(selectedImageUri).error(R.drawable.ic_user_default).into(profileImage);
        }


    }


    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(proj[0]);
            res = cursor.getString(columnIndex);
        }
        cursor.close();
        return res;

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onStart() {
        super.onStart();
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

            toast("Granted");

        }
    }

    private void callTeacherProfileApi() {
        if (!Utils.checkNetwork(TeacherProfileActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), TeacherProfileActivity.this);
            return;
        }

        //  SHOW PROGRESS DIALOG
        Utils.showDialog(TeacherProfileActivity.this);
        TypedFile proImage = new TypedFile("multipart/form-data", new File(pathProfilePic));
        TypedFile idProof = new TypedFile("multipart/form-data", new File(pathIdProofPic));
        TypedFile resume = new TypedFile("multipart/form-data", new File(pathCvDoc));
        TypedFile audiofile = new TypedFile("multipart/form-data", new File(pathAudioFile));

        ApiHandler.getApiService().createTeacherProfile(getTeacherProfileDetail(), idProof, proImage, resume, new Callback<TeacherProfileMainResponse>() {

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

                    startActivity(new Intent(TeacherProfileActivity.this, TeacherProfileActivity.class));

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

    private Map<String, String> getTeacherProfileDetail() {
        Map<String, String> map = new HashMap<>();
        String userId = Utils.ReadSharePrefrence(TeacherProfileActivity.this, Constant.SHARED_PREFS.KEY_USER_ID);
        map.put("uId", "" + userId);
        map.put("fullname", "" + String.valueOf(fullNameTeacherEdit.getText()));
        map.put("available_time", String.valueOf(avaibilityDateTeacherEdit.getText()));
        map.put("address", String.valueOf(currnetPlaceEdit.getText()));
        map.put("latitude", String.valueOf(lat));
        map.put("longitude", String.valueOf(lng));
        map.put("skill", String.valueOf(specialSkillTeacherEdit.getText()));
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
                Utils.ClearaSharePrefrence(TeacherProfileActivity.this);
                startActivity(LoginActivity.class, true);
                break;
        }
        return true;
    }
}
