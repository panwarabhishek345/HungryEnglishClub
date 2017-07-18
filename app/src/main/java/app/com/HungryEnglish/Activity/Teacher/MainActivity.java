package app.com.HungryEnglish.Activity.Teacher;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.LoginActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Model.Teacher.InfoMainResponse;
import app.com.HungryEnglish.Model.Teacher.InfoResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity {
    private int cnt = 0;
    private LinearLayout llLinkList;
    InfoResponse infoList;
    ImageView image_teacher_list_header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idMapping();
        callGetInfoApi();
    }

    private void idMapping() {
        image_teacher_list_header = (ImageView) findViewById(R.id.image_teacher_list_header);
        llLinkList = (LinearLayout) findViewById(R.id.llLinkList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String role = Utils.ReadSharePrefrence(MainActivity.this, Constant.SHARED_PREFS.KEY_USER_ROLE);
        switch (item.getItemId()) {
            case R.id.logout:
                final Dialog dialog = new Dialog(MainActivity.this);
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
                        startActivity(LoginActivity.class, true);
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
            case R.id.profile:
                if (role.equalsIgnoreCase("student"))
                    startActivity(StudentProfileActivity.class);
                else if (role.equalsIgnoreCase("teacher"))
                    startActivity(TeacherProfileActivity.class);
                break;


        }
        return true;
    }

    private void callGetInfoApi() {
        if (!Utils.checkNetwork(MainActivity.this)) {

            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), MainActivity.this);

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
                        return;
                    }
                    if (infoMainResponse.getStatus().equals("true")) {

                        infoList = new InfoResponse();
                        infoList = infoMainResponse.getInfo();
                        String imgUrl = Constant.BASEURL + infoList.getImage();
                        Picasso.with(MainActivity.this).load(imgUrl).error(R.drawable.gredient_green).into(image_teacher_list_header);

                        if (!infoList.getLink1().equalsIgnoreCase("")) {
                            addDynamicContactText(infoList.getLink1());
                        }

                        if (!infoList.getLink2().equalsIgnoreCase("")) {
                            addDynamicContactText(infoList.getLink2());
                        }

                        if (!infoList.getLink3().equalsIgnoreCase("")) {
                            addDynamicContactText(infoList.getLink3());
                        }

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


    private void addDynamicContactText(final String link1) {
        cnt = cnt + 1;
        TextView tvLabel = new TextView(MainActivity.this);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(Math.round(getResources().getDimension(R.dimen._5sdp)), 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        tvLabel.setLayoutParams(llp);
        tvLabel.setPadding(Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)));
        tvLabel.setText(link1);
        tvLabel.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
        tvLabel.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        tvLabel.setTextSize(12);
        llLinkList.addView(tvLabel);
        tvLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "";
                if (!link1.startsWith("http://") && !link1.startsWith("https://")) {
                    url = "http://" + link1;
                } else {
                    url = link1;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }

        });
    }

}
