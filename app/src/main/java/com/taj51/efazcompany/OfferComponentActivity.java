package com.taj51.efazcompany;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taj51.efazcompany.api_classes.Api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OfferComponentActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView title;
    private TextView explain;
    private TextView cost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_component);
        Toolbar toolbar = (Toolbar)findViewById(R.id.offer_component_toolbar);
        setSupportActionBar(toolbar);

        logo = (ImageView) findViewById(R.id.offer_component_image);
        title = (TextView) findViewById(R.id.offer_component_product_name);
        explain = (TextView) findViewById(R.id.offer_component_product_explain);
        cost = (TextView) findViewById(R.id.offer_component_product_cost);



        Intent intent = getIntent();
        Toast.makeText(getApplicationContext(), intent.getDoubleExtra("product_cost",0)+ " "+ intent.getStringExtra("product_explain"), Toast.LENGTH_LONG).show();

        logo.setImageBitmap(decodeBase64(intent.getStringExtra("product_image")));
        title.setText(intent.getStringExtra("product_title"));
        explain.setText(intent.getStringExtra("product_explain"));
        cost.setText(intent.getDoubleExtra("product_cost",0)+"");

        final Handler seekBarHandler = new Handler(); // must be created in the same thread that created the SeekBar
        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar2);
        String date1 = "2018-06-29 11:12:00.000";
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

            // you should define max in xml, but if you need to do this by code, you must set max as 0 and then your desired value. this is because a bug in SeekBar (issue 12945) (don't really checked if it was corrected)
            seekBar.setMax(0);
            seekBar.setMax((int)diffMinutes);
            seekBar.setProgress(1);

            seekBarHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (seekBar != null) {
                        seekBar.setMax(0);
                        seekBar.setMax((int) diffMinutes);
                        java.sql.Timestamp display = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                        long t = display.getTime();
                        long diff2 = milliseconds2 - t;
                        long min2 = diff2 / (60*1000);
                        long a = diffMinutes - diff2;
                        seekBar.setProgress((int) a);
                    }
                }
            });




        } catch (ParseException e) {
            e.printStackTrace();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "test nav", Toast.LENGTH_LONG).show();
            }
        });


    }


    // method for base64 to bitmap
    public static Bitmap decodeBase64(String encodedImage) {
        byte[] decodedByte = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }


}
