package app.com.HungryEnglish.Activity;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
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
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_ID = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_FILE = 2;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_AUDIOFILE = 4;
    ImageView profileImage, idProofImage;
    final int SELECT_PHOTO = 100;
    final int SELECT_ID_PROOF = 200;
    final int SELECT_FILE = 300;
    final int SELECT_AUDIO = 400;
    Button btnCvUpload;
    TextView tvUploadCV;
    EditText currnetPlaceEdit, fullNameTeacherEdit, avaibilityDateTeacherEdit, specialSkillTeacherEdit;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;
    String mLastUpdateTime, pathProfilePic, pathCvDoc, pathQualification, pathIdProofPic, pathAudioFile;
    private double lat = 0.0, lng = 0.0;
    private Button btnSubmiTeacherProfile, btnAudioFile;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available
//        if (!isGooglePlayServicesAvailable()) {
//            finish();
//        }


        setContentView(R.layout.activity_teacher_profile);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        idMapping();
        getLocation();


    }

    void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            getLocationRequest(MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // ...
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            currnetPlaceEdit.setText(getCompleteAddressString(lat, lng));
                        }
                    }
                });
    }

    private void idMapping() {

        profileImage = (ImageView) findViewById(R.id.profile_image);
        idProofImage = (ImageView) findViewById(R.id.id_image);
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
                uploadImage(SELECT_PHOTO, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                break;
            case R.id.id_image:
                uploadImage(SELECT_ID_PROOF, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_ID);
                break;
            case R.id.btn_cv_file:
                uploadFile(MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_FILE);
                break;

            case R.id.currnetPlaceEdit:
                getLocationRequest(MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//                createLocationRequest();
                break;
            case R.id.btn_audio_file:
                uploadAudio(MY_PERMISSIONS_REQUEST_ACCESS_AUDIOFILE);

            case R.id.btnSubmiTeacherProfile:

                if (fullNameTeacherEdit.getText().toString().equals("")) {
                    fullNameTeacherEdit.setError("Enter Name");
                    fullNameTeacherEdit.requestFocus();
                    return;
                }

//                if (lat == 0.0) {
//                    currnetPlaceEdit.setError("Select Location");
//                    currnetPlaceEdit.requestFocus();
//                    return;
//                }
//
//
//                if (lat == 0.0) {
//                    currnetPlaceEdit.setError("Select Location");
//                    currnetPlaceEdit.requestFocus();
//                    return;
//                }

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


    private void getLocationRequest(int myPermissionsRequestAccessFineLocation) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, myPermissionsRequestAccessFineLocation);

        if (ActivityCompat.checkSelfPermission(TeacherProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(TeacherProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherProfileActivity.this);
                builder.setTitle("Need Location Permission");
                builder.setMessage("This app needs location permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(TeacherProfileActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }

        } else {
            getLocation();
        }
    }

    private void uploadFile(int CONSTANT) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(TeacherProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(TeacherProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(TeacherProfileActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CONSTANT);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
//                ActivityCompat.requestPermissions(TeacherProfileActivity.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        CONSTANT);

                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("*/*");
                startActivityForResult(photoPickerIntent, SELECT_FILE);
            }
        } else {

            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("*/*");
            startActivityForResult(photoPickerIntent, SELECT_FILE);
        }
    }


    private void uploadAudio(int CONSTANT) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(TeacherProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(TeacherProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(TeacherProfileActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CONSTANT);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
//                ActivityCompat.requestPermissions(TeacherProfileActivity.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        CONSTANT);

                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("audio/*");
                startActivityForResult(photoPickerIntent, SELECT_AUDIO);
            }
        } else {

            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("audio/*");
            startActivityForResult(photoPickerIntent, SELECT_AUDIO);
        }
    }

    private void uploadImage(int PHOTO_CONSTANT, int CONSTANT) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(TeacherProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(TeacherProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(TeacherProfileActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CONSTANT);
                }
            } else {
//                ActivityCompat.requestPermissions(TeacherProfileActivity.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        CONSTANT);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PHOTO_CONSTANT);
            }
        } else {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, PHOTO_CONSTANT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_ID_PROOF);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_FILE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    photoPickerIntent.setType("*/*");
                    startActivityForResult(photoPickerIntent, SELECT_FILE);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_ACCESS_AUDIOFILE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    photoPickerIntent.setType("audio/*");
                    startActivityForResult(photoPickerIntent, SELECT_AUDIO);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getLocation();
                } else {
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

        switch (reqCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    onSelectFromGalleryResult(data);

//                    try {
//                        final Uri imageUri = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        profileImage.setImageBitmap(selectedImage);
//                        pathProfilePic = imageUri.getPath();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                }
                break;
            case SELECT_ID_PROOF:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        idProofImage.setImageBitmap(selectedImage);
                        pathIdProofPic = imageUri.getPath();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case SELECT_FILE:
                if (resultCode == SELECT_FILE) {
                    if (data != null) {
                        final Uri fileUri = data.getData();
                        String Path = fileUri.getPath();
                        btnCvUpload.setText(Path);
                        pathCvDoc = Path;
                    }
                }
                break;
            case SELECT_AUDIO:
                if (resultCode == SELECT_FILE) {
                    final Uri fileUri = data.getData();
                    String Path = fileUri.getPath();
                    btnAudioFile.setText(Path);
                    pathAudioFile = fileUri.getPath();
                }
                break;
        }
    }




    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        Log.e("PICTURE LINK", ">> " + selectedImageUri.getPath());
        // OI FILE Manager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            // Do something for 19 and above versions
            // OI FILE Manager
            String wholeID = DocumentsContract.getDocumentId(selectedImageUri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                pathIdProofPic = cursor.getString(columnIndex);
            }
            cursor.close();
            Picasso.with(TeacherProfileActivity.this).load(selectedImageUri).error(R.drawable.ic_user_default).into(profileImage);
            Log.e("PATH", "Image Path : " + pathIdProofPic);

//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
//                    String imageName = path.substring(path.lastIndexOf('/') + 1);
//                        String imageName = System.currentTimeMillis() + ".png";
//                        edtProductName.setText(imageName);
//                    setImageFromIntent(filePath);
//                Picasso.with(getActivity()).load(selectedImageUri).error(R.drawable.default_img).into(ivContenteThumb);
        } else {
            pathIdProofPic = getPathFromURI(selectedImageUri);
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




    @Override
    public void onStart() {
        super.onStart();
        Log.d("", "onStart fired ..............");
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        Log.d("", "Location update stopped .......................");
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("", "onStop fired ..............");
    }


    private void callTeacherProfileApi() {
        if (!Utils.checkNetwork(TeacherProfileActivity.this)) {

            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), TeacherProfileActivity.this);

            return;
        }
//        else {
//            Toast.makeText(getApplicationContext(), "Teacher Profile Update successfully", Toast.LENGTH_SHORT).show();
//
//            startActivity(new Intent(TeacherProfileActivity.this, TeacherListActivity.class));
//
//            finish();
//        }

        //  SHOW PROGRESS DIALOG
        Utils.showDialog(TeacherProfileActivity.this);

        Log.e("proImage ","@@ "+pathProfilePic);

        TypedFile proImage = new TypedFile("multipart/form-data", new File(pathProfilePic));
        TypedFile idProof = new TypedFile("multipart/form-data", new File(pathIdProofPic));
        TypedFile resume = new TypedFile("multipart/form-data", new File(pathIdProofPic));
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
//        map.put("resume", "");
//        map.put("idProof", "");
//        map.put("proImage", "");
        map.put("address", String.valueOf(currnetPlaceEdit.getText()));
        map.put("latitude", String.valueOf(lat));
        map.put("longitude", String.valueOf(lng));
        map.put("skill", String.valueOf(specialSkillTeacherEdit.getText()));

        Log.e("map", "Teacher Profile " + map);
        return map;
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w(" address", "" + strReturnedAddress.toString());
            } else {
                Log.w("address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("address", "Canont get Address!");
        }
        return strAdd;
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
                Utils.ClearaSharePrefrence(TeacherProfileActivity.this);
                startActivity(LoginActivity.class, true);

                break;

        }
        return true;
    }
}
