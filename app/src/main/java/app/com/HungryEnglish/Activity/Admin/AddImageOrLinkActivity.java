package app.com.HungryEnglish.Activity.Admin;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Model.admin.AddInfoResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.Util.Utils.getPath;
import static app.com.HungryEnglish.Util.Utils.getRealPathFromURI;

/**
 * Created by R'jul on 7/14/2017.
 */

public class AddImageOrLinkActivity extends BaseActivity {
    final int SELECT_PHOTO = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

    private LinearLayout llAddMoreLayout;

    private TextView tvAddMoreBtn;

    private List<EditText> allEds;

    private StringBuilder commaSepValueBuilder;
    public int cnt = 0;
    EditText etAddMoreLink;

    private ImageView ivSelectImage;
    private TextView tvSubmitLink;

    private String pathPic = "";
    private String Link1 = "", Link2 = "", Link3 = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_or_link);

        idMapping();

        setOnClick();
        allEds = new ArrayList<EditText>();
        addDynamicContactText();
    }


    private void idMapping() {

        llAddMoreLayout = (LinearLayout) findViewById(R.id.llAddMoreLayout);

        tvAddMoreBtn = (TextView) findViewById(R.id.tvAddMoreBtn);

        ivSelectImage = (ImageView) findViewById(R.id.ivSelectImage);

        tvSubmitLink = (TextView) findViewById(R.id.tvSubmitLink);
    }

    private void setOnClick() {

        tvAddMoreBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allEds.size() > 0 && allEds.size() <= 2) {

                    addDynamicContactText();
                }
            }
        });

        ivSelectImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(SELECT_PHOTO);
            }
        });

        tvSubmitLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pathPic.equalsIgnoreCase("")) {

                    Toast.makeText(AddImageOrLinkActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (allEds.size() > 0) {
                    if (allEds.get(0).getText().toString().equalsIgnoreCase("")) {

                        Toast.makeText(AddImageOrLinkActivity.this, "Please Enter Link1", Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    if (allEds.get(1).getText().toString().equalsIgnoreCase("")) {
//
//                        Toast.makeText(AddImageOrLinkActivity.this, "Please Enter Link2", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    if (allEds.get(2).getText().toString().equalsIgnoreCase("")) {
//
//                        Toast.makeText(AddImageOrLinkActivity.this, "Please Enter Link3", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                } else {
                    Toast.makeText(AddImageOrLinkActivity.this, "Please Enter Link", Toast.LENGTH_SHORT).show();
                    return;
                }

                Link1 = String.valueOf(allEds.get(0).getText());

                if (allEds.size() == 2) {
                    Link2 = String.valueOf(allEds.get(1).getText());
                }
                if (allEds.size() == 3) {
                    Link2 = String.valueOf(allEds.get(1).getText());
                    Link3 = String.valueOf(allEds.get(2).getText());
                }
                callSubmitImageAndLInkApi();
            }
        });

    }

    private void callSubmitImageAndLInkApi() {


        if (!Utils.checkNetwork(AddImageOrLinkActivity.this)) {

            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), AddImageOrLinkActivity.this);

            return;
        } else {
            Utils.showDialog(AddImageOrLinkActivity.this);
            ApiHandler.getApiService().addInfo(addInfoDetail(), new retrofit.Callback<AddInfoResponse>() {

                @Override
                public void success(AddInfoResponse addInfoResponse, Response response) {
                    Utils.dismissDialog();
                    if (addInfoResponse == null) {
                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (addInfoResponse.getStatus() == null) {
                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (addInfoResponse.getStatus().equals("false")) {

                        Toast.makeText(getApplicationContext(), "" + addInfoResponse.getMsg(), Toast.LENGTH_SHORT).show();

                        return;
                    }
                    if (addInfoResponse.getStatus().equals("true")) {
                        Toast.makeText(getApplicationContext(), "" + addInfoResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        startActivity(AdminDashboardActivity.class);
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

    }

    private Map<String, String> addInfoDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("image", pathPic);
        map.put("link1", Link1);
        map.put("link2", Link2);
        map.put("link3", Link3);

        Log.e("map", "TEACHER LIST " + map);
        return map;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(AdminDashboardActivity.class);
        finish();
    }

    private void addDynamicContactText() {
        cnt = cnt + 1;
        TextView tvLabel = new TextView(AddImageOrLinkActivity.this);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(Math.round(getResources().getDimension(R.dimen._5sdp)), 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        tvLabel.setLayoutParams(llp);
        tvLabel.setPadding(Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)));
        tvLabel.setText("Link " + cnt);
        tvLabel.setTextColor(ContextCompat.getColor(AddImageOrLinkActivity.this, R.color.colorPrimaryDark));
        tvLabel.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        tvLabel.setTextSize(12);
        llAddMoreLayout.addView(tvLabel);

        etAddMoreLink = new EditText(AddImageOrLinkActivity.this);
        allEds.add(etAddMoreLink);
//            etAddMoreLink.setId(id);
        LinearLayout.LayoutParams llpET = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llpET.setMargins(Math.round(getResources().getDimension(R.dimen._5sdp)), 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        etAddMoreLink.setLayoutParams(llpET);
        etAddMoreLink.setPadding(Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)));
        etAddMoreLink.setTextSize(12);
        etAddMoreLink.setBackground(ContextCompat.getDrawable(AddImageOrLinkActivity.this, R.drawable.bg_edt));
        etAddMoreLink.setBackground(null);
        etAddMoreLink.setInputType(InputType.TYPE_CLASS_TEXT);
        etAddMoreLink.setHint("Link " + cnt);
        llAddMoreLayout.addView(etAddMoreLink);

        View view = new View(AddImageOrLinkActivity.this);
        LinearLayout.LayoutParams llparamView = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2);
        view.setLayoutParams(llparamView);
        view.setBackgroundColor(ContextCompat.getColor(AddImageOrLinkActivity.this, R.color.colorPrimaryDark));
        llAddMoreLayout.addView(view);
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
        Log.d("reqCode", String.valueOf(reqCode));
//        getRealPathFromURI(this, data.getData());

        if (resultCode == Activity.RESULT_OK) {
            switch (reqCode) {
                case SELECT_PHOTO:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathPic = getRealPathFromURI(this, data.getData());
                    } else {
                        pathPic = getPath(this, data.getData());
                    }
                    Picasso.with(AddImageOrLinkActivity.this).load(Uri.fromFile(new File(pathPic))).error(R.drawable.ic_user_default).into(ivSelectImage);
                    Log.e("PATH", "Image Path : " + pathPic);
                    break;

            }
        } else {
            toast("You cancel current task.");
        }
    }


}
