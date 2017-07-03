package app.com.raivatshikhar.Services;


import java.util.Map;

import android.telecom.Call;
import app.com.raivatshikhar.Model.Profile.StudentProfileMainResponse;
import app.com.raivatshikhar.Model.Profile.TeacherProfileMainResponse;
import app.com.raivatshikhar.Model.login.LoginMainResponse;
import app.com.raivatshikhar.Model.register.RegisterMainResponse;
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

    @Multipart
    @POST("/teacher_profile.php")
//    void createTeacherProfile(@QueryMap Map<String, String> map, @Part("idProof") TypedFile idProof, @Part("proImage") TypedFile proImage, @Part("resume") TypedFile resume, Callback<TeacherProfileMainResponse> callback);
    Call createTeacherProfile(@QueryMap Map<String, String> map, @Part("idProof") TypedFile idProof, @Part("proImage") TypedFile proImage, @Part("resume") TypedFile resume, Callback<TeacherProfileMainResponse> callback);
}








