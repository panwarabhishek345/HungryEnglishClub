package app.com.raivatshikhar.Services;


import java.util.Map;

import app.com.raivatshikhar.Model.Profile.StudentProfileMainResponse;
import app.com.raivatshikhar.Model.Profile.TeacherProfileMainResponse;
import app.com.raivatshikhar.Model.login.LoginMainResponse;
import app.com.raivatshikhar.Model.register.RegisterMainResponse;
import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.QueryMap;

public interface WebServices {

    @POST("/login.php")
    public void getLogin(@QueryMap Map<String, String> map, Callback<LoginMainResponse> callback);

    @POST("/register.php")
    public void getRegister(@QueryMap Map<String, String> map, Callback<RegisterMainResponse> callback);

    @POST("/student_profile.php")
    public void getParentProfile(@QueryMap Map<String, String> map, Callback<StudentProfileMainResponse> callback);

    @POST("/teacher_profile.php")
    public void getTeacherProfile(@QueryMap Map<String, String> map, Callback<TeacherProfileMainResponse> callback);
}








