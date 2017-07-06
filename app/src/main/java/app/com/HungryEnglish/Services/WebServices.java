package app.com.HungryEnglish.Services;


import java.util.Map;

import android.telecom.Call;

import app.com.HungryEnglish.Model.Profile.StudentProfileMainResponse;
import app.com.HungryEnglish.Model.Profile.TeacherProfileMainResponse;
import app.com.HungryEnglish.Model.TeacherList.TeacherListMainResponse;
import app.com.HungryEnglish.Model.login.LoginMainResponse;
import app.com.HungryEnglish.Model.register.RegisterMainResponse;
import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;


public interface WebServices {

    @POST("/login.php")
    public void getLogin(@QueryMap Map<String, String> map, Callback<LoginMainResponse> callback);

    @POST("/register.php")
    public void getRegister(@QueryMap Map<String, String> map, Callback<RegisterMainResponse> callback);

    @POST("/student_profile.php")
    public void getParentProfile(@QueryMap Map<String, String> map, Callback<StudentProfileMainResponse> callback);

    @POST("/teacher_profile.php")
    public void getTeacherProfile(@QueryMap Map<String, String> map, Callback<TeacherProfileMainResponse> callback);

    @POST("/getuserbystatus.php")
    public void getTeacherList(@QueryMap Map<String, String> map, Callback<TeacherListMainResponse> callback);

    @Multipart
    @POST("/teacher_profile.php")
//    void createTeacherProfile(@QueryMap Map<String, String> map, @Part("idProof") TypedFile idProof, @Part("proImage") TypedFile proImage, @Part("resume") TypedFile resume, Callback<TeacherProfileMainResponse> callback);
    public void createTeacherProfile(@QueryMap Map<String, String> map, @Part("idProof") TypedFile idProof, @Part("proImage") TypedFile proImage, @Part("resume") TypedFile resume, Callback<TeacherProfileMainResponse> callback);
}








