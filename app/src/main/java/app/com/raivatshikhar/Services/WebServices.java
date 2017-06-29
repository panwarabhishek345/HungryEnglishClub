package app.com.raivatshikhar.Services;


import java.util.Map;

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

}








