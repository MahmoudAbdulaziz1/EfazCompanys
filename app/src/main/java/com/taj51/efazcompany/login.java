package com.taj51.efazcompany;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.LoginPOJO;

import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity
{
    private LoginPOJO loginData;
    private TextView fbook,acc,sin,sup,login;
    private EditText mal,pswd;
    private SharedPreferences save;
    private SharedPreferences visible;
    private boolean isVisible;
    private ImageButton visibleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        save = getSharedPreferences("login", MODE_PRIVATE);
        visible = getSharedPreferences("visible", MODE_PRIVATE);
        String reEmail = save.getString("email", "");
        String rePassword = save.getString("pass", "");
        SharedPreferences.Editor editor = save.edit();
        editor.putString("email", "");
        editor.putString("pass", "");
        editor.commit();
        sup = (TextView) findViewById(R.id.sup);
        sin = (TextView) findViewById(R.id.sin);
        login = (TextView) findViewById(R.id.sinnp);
        fbook = (TextView) findViewById(R.id.fboook);
        acc = (TextView) findViewById(R.id.act);
        mal = (EditText) findViewById(R.id.mal);
        pswd = (EditText) findViewById(R.id.pswd);


        mal.setText(reEmail);
        pswd.setText(rePassword);
        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor2 = save.edit();
                editor2.putString("email", mal.getText().toString().trim());
                editor2.putString("pass", pswd.getText().toString().trim());
                editor2.commit();
                Intent it = new Intent(login.this, signup.class);
                startActivity(it);
            }
        });
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(login.this, signup.class);
                SharedPreferences.Editor editor2 = save.edit();
                editor2.putString("email", mal.getText().toString().trim());
                editor2.putString("pass", pswd.getText().toString().trim());
                editor2.commit();
                startActivity(it);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mal.getText().toString().trim();
                String password = pswd.getText().toString().trim();
                logIn(email, password, 1, 1);
            }
        });

        visibleBtn = (ImageButton) findViewById(R.id.login_v_b);
        visibleBtn.setEnabled(false);
        visibleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisible = visible.getBoolean("v", false);

                if (isVisible){
                    pswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    SharedPreferences.Editor vi1 = visible.edit();
                    vi1.putBoolean("v", false);
                    vi1.commit();
                    visibleBtn.setImageDrawable(getResources().getDrawable(R.drawable.show));

                }else {
                    pswd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    SharedPreferences.Editor vi2 = visible.edit();
                    vi2.putBoolean("v", true);
                    vi2.commit();
                    visibleBtn.setImageDrawable(getResources().getDrawable(R.drawable.hide));

                }

            }
        });




        pswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pswd.getText().toString().trim().equals("")){
                    visibleBtn.setEnabled(false);
                }else {
                    visibleBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    public void logIn(String user_email, String user_password, int is_active, int login_type){

        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(login.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        LoginPOJO loginPOJO = new LoginPOJO(user_email, user_password, is_active, login_type);

        Api.getClient().login(loginPOJO).enqueue(new Callback<LoginPOJO>() {
            @Override
            public void onResponse(retrofit2.Call<LoginPOJO> call, Response<LoginPOJO> response) {
                loginData = response.body();
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(retrofit2.Call<LoginPOJO> call, Throwable t) {
                Log.d("response", t.getMessage().toString());
                progressDialog.dismiss();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = save.edit();
        editor.putString("email", "");
        editor.putString("pass", "");
        editor.commit();
        SharedPreferences s= getSharedPreferences("signup", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = s.edit();
        editor2.putString("email", "");
        editor2.putString("pass", "");
        editor2.putString("pass2", "");
        editor2.commit();
    }
}
