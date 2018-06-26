package com.taj51.efazcompany.interfcaes;

import com.taj51.efazcompany.pojo.AddCompanyOfferPOJO;
import com.taj51.efazcompany.pojo.CategoryPojo;
import com.taj51.efazcompany.pojo.CompanyOfferPOJO;
import com.taj51.efazcompany.pojo.GetCompanyOfferPOJO;
import com.taj51.efazcompany.pojo.GetProfilePojo;
import com.taj51.efazcompany.pojo.LoginDetailsPOJO;
import com.taj51.efazcompany.pojo.LoginPOJO;
import com.taj51.efazcompany.pojo.ProfilePOJO;
import com.taj51.efazcompany.pojo.SignUpPOJO;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

//    @FormUrlEncoded // annotation used in POST type requests
    @Headers("Content-Type: application/json")
    @POST("/register/add")
        // API's endpoints
    Call<Void> registration(@Body SignUpPOJO signUpPOJO);

    @GET("/register/getAll")
    Call<List<SignUpPOJO>> getAllRegisters();

    @Headers("Content-Type: application/json")
    @POST("/login/loginUser")
    Call<LoginPOJO>login(@Body LoginPOJO loginPOJO);
    @GET("/profile/getProfile/{id}")
    Call<GetProfilePojo> getProfile(@Path("id") int id);
    @PUT("/profile/updateProfile")
    Call<Integer>updateProfile(@Body ProfilePOJO pojo);

    @Headers("Content-Type: application/json")
    @POST("/profile/addProfile")
    Call<Integer>AddUserProfile(@Body ProfilePOJO profilePOJO);

    @GET("/cat/getCategories")
    Call<List<CategoryPojo>> getCategories();

    @Headers("Content-Type: application/json")
    @POST("/details/add")
    Call<Void>addLoginDetails(@Body LoginDetailsPOJO pojo);

    @GET("/details/getAll")
    Call<List<LoginDetailsPOJO>> getLoginDetails();

    @POST("/login/isLogged")
    Call<Boolean> isLogged(@Body LoginPOJO pojo);

    @POST("/login/getLoginId")
    Call<LoginPOJO> getLoggedId(@Body LoginPOJO pojo);



    @GET("/companyOffer/getOffers")
    Call<List<GetCompanyOfferPOJO>> getCompanyOffers();
    @GET("/companyOffer/getData/{id}")
    Call<List<String>> getCompanyOfferData(@Path("id") int id);

    @GET("/companyOffer/getOffers/{id}")
    Call<List<GetCompanyOfferPOJO>> getSingleCompanyOffers(@Path("id") int id);
    @GET("/companyOffer/getOffer/{id}")
    Call<GetCompanyOfferPOJO> getSingleCompanyOffer(@Path("id") int id);


    @PUT("/companyOffer/updateOffer")
    Call<Integer> updateCompanyOffer(@Body AddCompanyOfferPOJO model);

    @PUT("/companyOffer/deleteOffer/{id}")
    Call<Integer> deleteCompanyOffer(@Path("id") int id);

    @POST("/companyOffer/addOffer")
    Call<Integer> addCompanyOffer(@Body AddCompanyOfferPOJO pojo);

    @GET("/profile/profileExist/{id}")
    Call<Integer> CheckProfileExist(@Path("id") int id);

    @PUT("/login/activeUser/{id}")
    Call<Void> activeLogin(@Path("id") int id);
    @PUT("/login/inActiveUser/{id}")
    Call<Void> inActiveLogin(@Path("id") int id);

}