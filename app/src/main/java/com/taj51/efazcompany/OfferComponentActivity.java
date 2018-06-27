package com.taj51.efazcompany;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taj51.efazcompany.api_classes.Api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferComponentActivity extends AppCompatActivity {

    long progress = 0;
    private ImageView logo;
    private TextView title;
    private TextView explain;
    private TextView cost;
    private TextView displayDate;
    private TextView expiredDate;

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String encodedImage) {
        byte[] decodedByte = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_component);
        Toolbar toolbar = (Toolbar) findViewById(R.id.offer_component_toolbar);
        setSupportActionBar(toolbar);

        logo = (ImageView) findViewById(R.id.offer_component_image);
        title = (TextView) findViewById(R.id.offer_component_product_name);
        explain = (TextView) findViewById(R.id.offer_component_product_explain);
        cost = (TextView) findViewById(R.id.offer_component_product_cost);
        displayDate = (TextView) findViewById(R.id.display_date);
        expiredDate = (TextView) findViewById(R.id.offer_component_expire_date);

//        Api.getClient().getCompanyOfferData(getIntent().getIntExtra("product_id", 0)).enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//
//                try {
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
//                    Date parsedDate2 = dateFormat.parse(response.body().get(3));
//                    Timestamp timestamp1 = new java.sql.Timestamp(parsedDate2.getTime());
//                    Date parsedDate3 = dateFormat.parse(response.body().get(4));
//                    Timestamp timestamp2 = new java.sql.Timestamp(parsedDate3.getTime());
//                    java.sql.Date date = new java.sql.Date(timestamp1.getTime());
//                    java.sql.Date date2 = new java.sql.Date(timestamp2.getTime());
//                    displayDate.setText(date.toString());
//                    expiredDate.setText(date2.toString());
//                    final long milliseconds1 = timestamp1.getTime();
//                    final long milliseconds2 = timestamp2.getTime();
//                    long diff = milliseconds2 - milliseconds1;
//                    long diffDays = diff / (24 * 60 * 60 * 1000);
//                    ProgressBar seekBar = (ProgressBar) findViewById(R.id.seekBar2);
//                    seekBar.setMax(0);
//                    seekBar.setMax((int) diffDays);
//                    seekBar.setProgress(Integer.parseInt(response.body().get(0)));
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable t) {
//
//            }
//        });

        new myAsync().execute();

        Intent intent = getIntent();
        //Toast.makeText(getApplicationContext(), intent.getDoubleExtra("product_cost", 0) + " " + intent.getStringExtra("product_explain"), Toast.LENGTH_LONG).show();

        logo.setImageBitmap(decodeBase64(intent.getStringExtra("product_image")));
        title.setText(intent.getStringExtra("product_title"));
        explain.setText(intent.getStringExtra("product_explain"));
        cost.setText(intent.getDoubleExtra("product_cost", 0) + "");

        String date1 = "2018-06-26 11:12:00.000";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

        try {
            Date parsedDate2 = dateFormat.parse(date1);
            Timestamp timestamp2 = new java.sql.Timestamp(parsedDate2.getTime());
            Date parsedDate3 = dateFormat.parse("2018-06-29 11:52:00.000");
            Timestamp timestamp3 = new java.sql.Timestamp(parsedDate3.getTime());

            long milliseconds1 = timestamp2.getTime();
            final long milliseconds2 = timestamp3.getTime();
            long diff = milliseconds2 - milliseconds1;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long rem = diff % (24 * 60 * 60 * 1000);
            long diffHours = rem / (60 * 60 * 1000);
            long rem2 = rem % (60 * 60 * 1000);
            final long diffMinutes = rem2 / (60 * 1000);
            Log.d("progressbar", diffMinutes + " ss");



        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    public class myAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Api.getClient().getCompanyOfferData(getIntent().getIntExtra("product_id", 0)).enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                        Date parsedDate2 = dateFormat.parse(response.body().get(3));
                        Timestamp timestamp1 = new java.sql.Timestamp(parsedDate2.getTime());
                        Date parsedDate3 = dateFormat.parse(response.body().get(4));
                        Timestamp timestamp2 = new java.sql.Timestamp(parsedDate3.getTime());
                        java.sql.Date date = new java.sql.Date(timestamp1.getTime());
                        java.sql.Date date2 = new java.sql.Date(timestamp2.getTime());
                        displayDate.setText(date.toString());
                        expiredDate.setText(date2.toString());
                        final long milliseconds1 = timestamp1.getTime();
                        final long milliseconds2 = timestamp2.getTime();
                        long diff = milliseconds2 - milliseconds1;
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        ProgressBar seekBar = (ProgressBar) findViewById(R.id.seekBar2);
                        seekBar.setMax(0);
                        seekBar.setMax((int) diffDays);
                        seekBar.setProgress(Integer.parseInt(response.body().get(0)));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {

                }
            });

            return null;
        }
    }


}
