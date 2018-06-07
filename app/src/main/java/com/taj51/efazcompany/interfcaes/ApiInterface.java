package com.taj51.efazcompany.interfcaes;

import com.taj51.efazcompany.pojo.LoginPOJO;
import com.taj51.efazcompany.pojo.SignUpPOJO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

//    @FormUrlEncoded // annotation used in POST type requests
    @Headers("Content-Type: application/json")
    @POST("/register/add")
        // API's endpoints
    Call<SignUpPOJO> registration(@Body SignUpPOJO signUpPOJO);

    @GET("/register/getAll")
    Call<List<SignUpPOJO>> getAllRegisters();

    @Headers("Content-Type: application/json")
    @POST("/login/loginUser")
    Call<LoginPOJO>login(@Body LoginPOJO loginPOJO);

}