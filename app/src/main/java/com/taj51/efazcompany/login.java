package com.taj51.efazcompany;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.LoginDetailsPOJO;
import com.taj51.efazcompany.pojo.LoginPOJO;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {
    SharedPreferences remPreference;
    private LoginPOJO loginData;
    private TextView fbook, sin, sup, login;//acc,
    private EditText mal, pswd;
    private SharedPreferences visible;
    private boolean isVisible;
    private ImageButton visibleBtn;
    private CheckBox remmeberMe;
    private String emailT = "";
    private String passwordT = "";

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void changeLang(String lan){
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("lang", MODE_PRIVATE).edit();
        editor.putString("ar","ar");
        editor.apply();
        recreate();
    }

    public void loadLang(){
        SharedPreferences preferences = getSharedPreferences("lang",MODE_PRIVATE);
        String l = preferences.getString("ar","en");
        changeLang(l);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loadLang();
        setContentView(R.layout.activity_login);

        remPreference = getSharedPreferences("remmber", MODE_PRIVATE);
        boolean remmberBool = remPreference.getBoolean("remm", false);

        if (remmberBool) {
            String email = remPreference.getString("email", "").trim();
            String password = remPreference.getString("password", "").trim();
            logIn(email, password, 1, 0);
        }

        //save = getSharedPreferences("login", MODE_PRIVATE);
        visible = getSharedPreferences("visible", MODE_PRIVATE);
        sup = (TextView) findViewById(R.id.sup);
        sin = (TextView) findViewById(R.id.sin);
        login = (TextView) findViewById(R.id.sinnp);
        mal = (EditText) findViewById(R.id.mal);
        pswd = (EditText) findViewById(R.id.pswd);
        remmeberMe = (CheckBox) findViewById(R.id.remmeberMe);

        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(login.this, signup.class);
                startActivity(it);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLang("ar");
                if (mal.getText().toString().trim().equals("") || mal.getText().toString().trim().equals(null) || !isValidEmail(mal.getText().toString().trim().toLowerCase())) {
                    mal.setError(getResources().getString(R.string.email_error));
                    mal.requestFocus();
                } else {
                    if (pswd.getText().toString().trim().equals("") || pswd.getText().toString().trim().equals(null)) {
                        pswd.setError(getResources().getString(R.string.password_error));
                        pswd.requestFocus();
                    } else {
                        String email = mal.getText().toString().trim();
                        String password = pswd.getText().toString().trim();
                        emailT = email;
                        passwordT = password;
                        final ProgressDialog progressDialog = new ProgressDialog(login.this);
                        progressDialog.setCancelable(false); // set cancelable to false
                        progressDialog.setMessage("Please Wait"); // set message
                        progressDialog.show();
                        //logIn(email, password, 1, 0);
                        new ActiveAsync().execute();
                        progressDialog.dismiss();
                    }
                }


            }
        });

        visibleBtn = (ImageButton) findViewById(R.id.login_v_b);
        visibleBtn.setEnabled(false);
        visibleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisible = visible.getBoolean("v", false);

                if (isVisible) {
                    pswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    SharedPreferences.Editor vi1 = visible.edit();
                    vi1.putBoolean("v", false);
                    vi1.commit();
                    visibleBtn.setImageDrawable(getResources().getDrawable(R.drawable.show));

                } else {
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
                if (pswd.getText().toString().trim().equals("")) {
                    visibleBtn.setEnabled(false);
                } else {
                    visibleBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void logIn(final String user_email, final String user_password, int is_active, final int login_type) {

        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(login.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        final LoginPOJO loginPOJO = new LoginPOJO(user_email, user_password, is_active, login_type);

        Api.getClient().isLogged(loginPOJO).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {


                try {
                    boolean isFound;
                    if (response.body() == null) {
                        isFound = false;
                        remmeberMe.setChecked(false);
                    } else {
                        isFound = response.body();
                    }

                    if (isFound) {


                        Api.getClient().getLoggedId(loginPOJO).enqueue(new Callback<LoginPOJO>() {
                            @Override
                            public void onResponse(Call<LoginPOJO> call, Response<LoginPOJO> response) {
                                //Toast.makeText(getApplicationContext(), response.body()+"", Toast.LENGTH_LONG).show();
                                loginData = response.body();

                                Api.getClient().CheckProfileExist(loginData.getLogin_id()).enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        int result = response.body();
                                        if (result > 0) {

                                            String dates = getDateFor();

                                            Timestamp time = getTimeStamp(dates);
                                            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                                            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                                            LoginDetailsPOJO loginDetailsPOJO = new LoginDetailsPOJO(loginData.getLogin_id(), 0, dates, ip, 1);
                                            Api.getClient().addLoginDetails(loginDetailsPOJO).enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    SharedPreferences.Editor editor = remPreference.edit();
                                                    editor.putBoolean("remm", remmeberMe.isChecked());
                                                    editor.putString("email", loginData.getUser_email());
                                                    editor.putString("password", user_password);
                                                    //editor2.putString("pass", pswd.getText().toString().trim());
                                                    editor.commit();

                                                    Api.getClient().activeLogin(loginData.getLogin_id()).enqueue(new Callback<Void>() {
                                                        @Override
                                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                                            Log.d("active", "response");
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Void> call, Throwable t) {
                                                            Log.d("active", "fail");
                                                        }
                                                    });


                                                    //new ActiveAsync().execute();



                                                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                                                    intent.putExtra("id", loginData.getLogin_id());
                                                    intent.putExtra("email", loginData.getUser_email());
                                                    intent.putExtra("password", loginData.getUser_password());
                                                    intent.putExtra("active", loginData.getIs_active());
                                                    intent.putExtra("type", loginData.getLogin_type());
                                                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    progressDialog.dismiss();
                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {

                                                }
                                            });
                                        } else if (result == 0) {
                                            //Toast.makeText(getApplicationContext(), "Complete your Profile", Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(getBaseContext(), CompleteProfileActivity.class);
                                            Log.d("test", loginData.getLogin_id() + "");
                                            intent.putExtra("id", loginData.getLogin_id());
                                            intent.putExtra("email", loginData.getUser_email());
                                            SharedPreferences.Editor editor = remPreference.edit();
                                            editor.putBoolean("remm", true);
                                            editor.putString("email", loginData.getUser_email());
                                            editor.putString("password", loginData.getUser_password());
                                            editor.commit();
                                            Api.getClient().activeLogin(loginData.getLogin_id()).enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    Log.d("active", "response");
                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {
                                                    Log.d("active", "fail");
                                                }
                                            });
                                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {

                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<LoginPOJO> call, Throwable t) {

                            }
                        });


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Email not found Or may be not Conformed", Toast.LENGTH_LONG).show();
                        //pswd.setError(getResources().getString(R.string.password_error));
                        pswd.requestFocus();
                    }

                } catch (IllegalStateException e) {
                    Toast.makeText(getApplicationContext(), loginData.getLogin_id(), Toast.LENGTH_LONG).show();
                    Log.d("test", loginData.getLogin_id() + "    dsfsdfsdffffff");
                }


            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private boolean validateEmail() {
        String email = mal.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            mal.setError(getResources().getString(R.string.email_valid));
            mal.requestFocus();
            return false;
        }

        return true;
    }

    public String getDateFor() {
        Date javaUtilDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        System.out.println(formatter.format(javaUtilDate));
        String dates = formatter.format(javaUtilDate);
        return dates;
    }

    public Timestamp getTimeStamp(String date) {
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


    public class ActiveAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {


            final LoginPOJO loginPOJO = new LoginPOJO(emailT, passwordT, 1, 0);

            Api.getClient().isLogged(loginPOJO).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {


                    try {
                        boolean isFound;
                        if (response.body() == null) {
                            isFound = false;
                            remmeberMe.setChecked(false);
                        } else {
                            isFound = response.body();
                        }

                        if (isFound) {


                            Api.getClient().getLoggedId(loginPOJO).enqueue(new Callback<LoginPOJO>() {
                                @Override
                                public void onResponse(Call<LoginPOJO> call, Response<LoginPOJO> response) {
                                    //Toast.makeText(getApplicationContext(), response.body()+"", Toast.LENGTH_LONG).show();
                                    loginData = response.body();

                                    Api.getClient().CheckProfileExist(loginData.getLogin_id()).enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            int result = response.body();
                                            if (result > 0) {

                                                String dates = getDateFor();

                                                Timestamp time = getTimeStamp(dates);
                                                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                                                String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                                                LoginDetailsPOJO loginDetailsPOJO = new LoginDetailsPOJO(loginData.getLogin_id(), 0, dates, ip, 1);
                                                Api.getClient().addLoginDetails(loginDetailsPOJO).enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        SharedPreferences.Editor editor = remPreference.edit();
                                                        editor.putBoolean("remm", remmeberMe.isChecked());
                                                        editor.putString("email", loginData.getUser_email());
                                                        editor.putString("password", passwordT);
                                                        //editor2.putString("pass", pswd.getText().toString().trim());
                                                        editor.commit();

                                                        Api.getClient().activeLogin(loginData.getLogin_id()).enqueue(new Callback<Void>() {
                                                            @Override
                                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                                Log.d("active", "response");
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Void> call, Throwable t) {
                                                                Log.d("active", "fail");
                                                            }
                                                        });


                                                        //new ActiveAsync().execute();



                                                        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                                                        intent.putExtra("id", loginData.getLogin_id());
                                                        intent.putExtra("email", loginData.getUser_email());
                                                        intent.putExtra("password", loginData.getUser_password());
                                                        intent.putExtra("active", loginData.getIs_active());
                                                        intent.putExtra("type", loginData.getLogin_type());
                                                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
//                                                        progressDialog.dismiss();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {

                                                    }
                                                });
                                            } else if (result == 0) {
                                                Toast.makeText(getApplicationContext(), "Complete your Profile", Toast.LENGTH_LONG).show();

                                                Intent intent = new Intent(getBaseContext(), CompleteProfileActivity.class);
                                                Log.d("test", loginData.getLogin_id() + "");
                                                intent.putExtra("id", loginData.getLogin_id());
                                                intent.putExtra("email", loginData.getUser_email());
                                                SharedPreferences.Editor editor = remPreference.edit();
                                                editor.putBoolean("remm", true);
                                                editor.putString("email", loginData.getUser_email());
                                                editor.putString("password", loginData.getUser_password());
                                                editor.commit();
                                                startActivity(intent);
                                                //progressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {

                                        }
                                    });

                                }

                                @Override
                                public void onFailure(Call<LoginPOJO> call, Throwable t) {

                                }
                            });


                        } else {
                            //progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Email not found Or may be not Conformed", Toast.LENGTH_LONG).show();
                            //pswd.setError(getResources().getString(R.string.password_error));
                            pswd.requestFocus();
                        }

                    } catch (IllegalStateException e) {
                        Toast.makeText(getApplicationContext(), loginData.getLogin_id(), Toast.LENGTH_LONG).show();
                        Log.d("test", loginData.getLogin_id() + "    dsfsdfsdffffff");
                    }


                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });




            return null;
        }
    }
}
