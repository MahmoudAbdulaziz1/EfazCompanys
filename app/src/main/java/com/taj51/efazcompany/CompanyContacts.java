package com.taj51.efazcompany;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.LoginDetailsPOJO;
import com.taj51.efazcompany.pojo.LoginPOJO;
import com.taj51.efazcompany.pojo.SignUpPOJO;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyContacts extends AppCompatActivity {

    private LoginPOJO loginData;
    private EditText companyWebsite;
    private EditText companyPhone;
    private TextView signUpBTN;
    private String [] five={"08111"};
    private String [] four={"0570","0571","0572","0576","0577","0578"};
    private String [] three={"011","012","013","014","016","017","050","051","053","054","055","056","058","059"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_contacts);

        Intent intent = getIntent();
        final String mail        = intent.getStringExtra("email");
        final String password    = intent.getStringExtra("password");
        final String companyName = intent.getStringExtra("company");
        final String userName    = intent.getStringExtra("userName");
        final String address     = intent.getStringExtra("address");




        companyWebsite = (EditText)findViewById(R.id.organization_website);
        companyPhone   = (EditText)findViewById(R.id.organization_phone);
        signUpBTN      = (TextView)findViewById(R.id.company_data_nxt);
        signUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateWebsite() && validatePhone()){
                    String website = companyWebsite.getText().toString().trim();
                    String phone   = companyPhone.getText().toString().trim();
                    signUp(mail, password, userName,phone , companyName, address,website, 0, 1);
                }
            }
        });

    }

    private boolean validateWebsite() {
        String website = companyWebsite.getText().toString().trim();

        if (website.isEmpty() || !isValidWebsite(website)) {
            companyWebsite.setError(getResources().getString(R.string.website_error));
            companyWebsite.requestFocus();
            return false;
        }

        return true;
    }

    private static boolean isValidWebsite(String website) {
        return !TextUtils.isEmpty(website) && Patterns.DOMAIN_NAME.matcher(website).matches();
    }

    private boolean validatePhone() {
        String phone = companyPhone.getText().toString().trim();

        if (phone.isEmpty() || !isValidPhone(phone)) {
            companyPhone.setError(getResources().getString(R.string.phone_error));
            companyPhone.requestFocus();
            return false;
        }

        String fiveStr = phone.substring(0,Math.min(phone.length(), 5));
        if (Arrays.asList(five).contains(fiveStr)){
            return true;
        }else {
            String fourStr = fiveStr.substring(0, Math.min(fiveStr.length(), 4));
            if (Arrays.asList(four).contains(fourStr)){
                return true;
            }else {
                String threeStr = fourStr.substring(0, Math.min(fiveStr.length(), 3));
                if (Arrays.asList(three).contains(threeStr)){
                    return true;
                }else {
                    companyPhone.setError(getResources().getString(R.string.phone_error));
                    companyPhone.requestFocus();
                    return false;
                }
            }
        }
    }

    private static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }


    private void signUp(final String registeration_email, final String registeration_password, String registeration_username,
                        String registeration_phone_number, String registration_organization_name,
                        String registration_address_desc, String registration_website_url, final int registration_is_school,
                        final int registration_isActive) {


        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(CompanyContacts.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        // Api is a class in which we define a method getClient() that returns the API Interface class object
        // registration is a POST request type method in which we are sending our field's data
        // enqueue is used for callback response and error
        SignUpPOJO signUpPOJO = new SignUpPOJO(registeration_email, registeration_password, registeration_username,
                registeration_phone_number, registration_organization_name, registration_address_desc, registration_website_url,
                registration_is_school, registration_isActive);
        Api.getClient().registration(signUpPOJO).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //add to login table
                LoginPOJO loginPOJO = new LoginPOJO(registeration_email, registeration_password, registration_isActive, registration_is_school);
                Api.getClient().login(loginPOJO).enqueue(new Callback<LoginPOJO>() {
                    @Override
                    public void onResponse(Call<LoginPOJO> call, Response<LoginPOJO> response) {
                        loginData = response.body();
                        String dates = getDateFor();

                        Timestamp time = getTimeStamp(dates);
                        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                        LoginDetailsPOJO loginDetailsPOJO = new LoginDetailsPOJO(loginData.getLogin_id(), 0, dates, ip, 1);
                        Api.getClient().addLoginDetails(loginDetailsPOJO).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.d("test", loginData.getLogin_id()+"");
                                Intent intent = new Intent(getBaseContext(), CompleteProfileActivity.class);
                                intent.putExtra("id", loginData.getLogin_id());
                                intent.putExtra("email", loginData.getUser_email());
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<LoginPOJO> call, Throwable t) {

                    }
                });
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Log.d("response", t.getMessage().toString());
                progressDialog.dismiss();
            }
        });

    }

    public String getDateFor(){
        Date javaUtilDate= new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        System.out.println(formatter.format(javaUtilDate));
        String dates = formatter.format(javaUtilDate);
        return dates;
    }

    public Timestamp getTimeStamp(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
        return timestamp;
    }


}