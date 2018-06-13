package com.taj51.efazcompany;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.ProfilePOJO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        final int id = intent.getIntExtra("id", 0);
        String email = intent.getStringExtra("email");

        Api.getClient().getProfile(id).enqueue(new Callback<ProfilePOJO>() {
            @Override
            public void onResponse(Call<ProfilePOJO> call, Response<ProfilePOJO> response) {
                Log.d("TestAPI", "hskdjgaskjd   "+id);
                ProfilePOJO pojo = response.body();
                Log.d("TestAPI", pojo.getCompany_name());
                Toast.makeText(getApplicationContext(), pojo.getCompany_name(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ProfilePOJO> call, Throwable t) {
                Log.d("TestAPI", t.getMessage()+t.getLocalizedMessage());

            }
        });


    }
}
