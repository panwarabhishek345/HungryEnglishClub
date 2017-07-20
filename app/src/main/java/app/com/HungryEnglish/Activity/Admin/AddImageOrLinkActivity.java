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
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Model.Teacher.InfoMainResponse;
import app.com.HungryEnglish.Model.Teacher.InfoResponse;
import app.com.HungryEnglish.Model.admin.AddInfoResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

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
    public int cnt = 0;
    EditText etAddMoreLink;
    private ImageView ivSelectImage;
    private TextView tvSubmitLink;
    private String pathPic = "";
    public static int maxLinks = 2;
    public ArrayList<String> links;
    InfoResponse infoList;
    private LinearLayout llLinkList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_or_link);
        idMapping();
        setOnClick();
        links = new ArrayList<>();
        allEds = new ArrayList<EditText>();

        callGetInfoApi();





    }

    private void hideAddMoreButton() {
        if (allEds.size() == 3) {
            tvAddMoreBtn.setVisibility(View.GONE);
        }
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
                if (allEds.size() > 0 && allEds.size() <= maxLinks) {
                    addDynamicContactText("");
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

                for (int i = 0; i < allEds.size(); i++) {
                    String edtText = getText(allEds.get(i));
                    if (edtText.equalsIgnoreCase("")) {
                        toast("Please Enter Link" + (i + 1));
                        return;
                    } else {
                        links.add(edtText);
                    }
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
            TypedFile imageTypeFile = new TypedFile("multipart/form-data", new File(pathPic));
            ApiHandler.getApiService().addInfo(addInfoDetail(), imageTypeFile, new retrofit.Callback<AddInfoResponse>() {
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
        for (int i = 0; i < links.size(); i++) {
            map.put("link" + (i + 1), links.get(i));
        }
        return map;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(AdminDashboardActivity.class);
        finish();
    }

    private void addDynamicContactText(String link1) {
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
        etAddMoreLink.setText(link1);
        llAddMoreLayout.addView(etAddMoreLink);

        View view = new View(AddImageOrLinkActivity.this);
        LinearLayout.LayoutParams llparamView = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2);
        view.setLayoutParams(llparamView);
        view.setBackgroundColor(ContextCompat.getColor(AddImageOrLinkActivity.this, R.color.colorPrimaryDark));
        llAddMoreLayout.addView(view);

        hideAddMoreButton();

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
        if (resultCode == Activity.RESULT_OK) {
            switch (reqCode) {
                case SELECT_PHOTO:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathPic = getRealPathFromURI(this, data.getData());
                    } else {
                        pathPic = getPath(this, data.getData());
                    }
                    ivSelectImage.setScaleType(ScaleType.CENTER_CROP);
                    Picasso.with(AddImageOrLinkActivity.this).load(Uri.fromFile(new File(pathPic))).error(R.drawable.ic_user_default).into(ivSelectImage);
                    break;

            }
        } else {
            toast("Select Image");
        }
    }


    private void callGetInfoApi() {
        if (!Utils.checkNetwork(AddImageOrLinkActivity.this)) {

            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), AddImageOrLinkActivity.this);

            return;
        } else {

            ApiHandler.getApiService().getInfo(getInfo(), new retrofit.Callback<InfoMainResponse>() {

                @Override
                public void success(InfoMainResponse infoMainResponse, Response response) {

                    if (infoMainResponse == null) {
                        toast("Something Wrong");
                        return;
                    }
                    if (infoMainResponse.getStatus() == null) {
                        toast("Something Wrong");
                        return;
                    }
                    if (infoMainResponse.getStatus().equals("false")) {
                        toast(infoMainResponse.getMsg());
                        allEds.clear();
                        tvAddMoreBtn.setVisibility(View.VISIBLE);
                        addDynamicContactText("");
                        return;
                    }
                    if (infoMainResponse.getStatus().equals("true")) {

                        infoList = new InfoResponse();
                        infoList = infoMainResponse.getInfo();
                        String imgUrl = Constant.BASEURL + infoList.getImage();
                        ivSelectImage.setScaleType(ScaleType.CENTER_CROP);
                        Picasso.with(AddImageOrLinkActivity.this).load(imgUrl).placeholder(R.drawable.gredient_green).error(R.drawable.gredient_green).into(ivSelectImage);
                        if (!infoList.getLink1().equalsIgnoreCase("")) {
                            addDynamicContactText(infoList.getLink1());
                        }

                        if (!infoList.getLink2().equalsIgnoreCase("")) {
                            addDynamicContactText(infoList.getLink2());
                        }

                        if (!infoList.getLink3().equalsIgnoreCase("")) {
                            addDynamicContactText(infoList.getLink3());
                        }
                        hideAddMoreButton();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    toast("Something Wrong");
                }
            });

        }

    }


    private Map<String, String> getInfo() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

//    private void showDynamicContactText(final String link1) {
//        cnt = cnt + 1;
//        TextView tvLabel = new TextView(AddImageOrLinkActivity.this);
//        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        llp.setMargins(Math.round(getResources().getDimension(R.dimen._5sdp)), 0, 0, 0); // llp.setMargins(left, top, right, bottom);
//        tvLabel.setLayoutParams(llp);
//        tvLabel.setPadding(Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)));
//        tvLabel.setText(link1);
//        tvLabel.setTextColor(ContextCompat.getColor(AddImageOrLinkActivity.this, R.color.colorPrimaryDark));
//        tvLabel.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
//        tvLabel.setTextSize(12);
//        llAddMoreLayout.addView(tvLabel);
//        tvLabel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String url = "";
//                if (!link1.startsWith("http://") && !link1.startsWith("https://")) {
//                    url = "http://" + link1;
//                } else {
//                    url = link1;
//                }
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(browserIntent);
//            }
//
//        });
//    }

}
