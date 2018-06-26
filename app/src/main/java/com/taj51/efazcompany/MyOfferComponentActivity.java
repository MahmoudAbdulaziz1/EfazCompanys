package com.taj51.efazcompany;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.GetCompanyOfferPOJO;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOfferComponentActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView title;
    private TextView explain;
    private TextView cost;
    private TextView displayDate;
    private TextView expiredDate;
    private Button update;
    private Button delete;

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String encodedImage) {
        byte[] decodedByte = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reresent_my_offer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_offer_component_toolbar);
        logo = (ImageView) findViewById(R.id.my_offer_component_image);
        title = (TextView) findViewById(R.id.my_offer_component_product_name);
        explain = (TextView) findViewById(R.id.my_offer_component_product_explain);
        cost = (TextView) findViewById(R.id.my_offer_component_product_cost);
        displayDate = (TextView) findViewById(R.id.my_display_date);
        expiredDate = (TextView) findViewById(R.id.my_offer_component_expire_date);
        update = (Button) findViewById(R.id.update_offer);
        delete = (Button) findViewById(R.id.delete_offer);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "test nav", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        Api.getClient().getSingleCompanyOffer(getIntent().getIntExtra("product_id", 0)).enqueue(new Callback<GetCompanyOfferPOJO>() {
            @Override
            public void onResponse(Call<GetCompanyOfferPOJO> call, Response<GetCompanyOfferPOJO> response) {

                GetCompanyOfferPOJO offer = response.body();
                Log.d("trrrrrrrrr", getIntent().getIntExtra("product_id", 0) + "");
                logo.setImageBitmap(decodeBase64(offer.getOffer_logo()));
                title.setText(offer.getOffer_title());
                explain.setText(offer.getOffer_explaination());
                cost.setText(offer.getOffer_cost() + "");
                //final ProgressBar seekBar = (ProgressBar) findViewById(R.id.my_seekBar2);

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
                            ProgressBar seekBar = (ProgressBar) findViewById(R.id.my_seekBar2);
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



//                String date1 = "2018-06-29 11:12:00.000";
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
//
//
//                try {
//                    Date parsedDate2 = dateFormat.parse(date1);
//                    Timestamp timestamp2 = new java.sql.Timestamp(parsedDate2.getTime());
//                    Date parsedDate3 = dateFormat.parse("2018-06-29 11:52:00.000");
//                    Timestamp timestamp3 = new java.sql.Timestamp(parsedDate3.getTime());
//
//                    long milliseconds1 = timestamp2.getTime();
//                    final long milliseconds2 = timestamp3.getTime();
//                    long diff = milliseconds2 - milliseconds1;
//                    long diffDays = diff / (24 * 60 * 60 * 1000);
//                    long rem = diff % (24 * 60 * 60 * 1000);
//                    long diffHours = rem / (60 * 60 * 1000);
//                    long rem2 = rem % (60 * 60 * 1000);
//                    final long diffMinutes = rem2 / (60 * 1000);
//
//                    // you should define max in xml, but if you need to do this by code, you must set max as 0 and then your desired value. this is because a bug in SeekBar (issue 12945) (don't really checked if it was corrected)
//                    seekBar.setMax(0);
//                    seekBar.setMax((int) diffMinutes);
//                    seekBar.setProgress(1);
//
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public void onFailure(Call<GetCompanyOfferPOJO> call, Throwable t) {

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdate = new Intent(getBaseContext(), UpdateOfferActivity.class);
                intentUpdate.putExtra("id", getIntent().getIntExtra("product_id", 0));
                startActivity(intentUpdate);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        MyOfferComponentActivity.this);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        Api.getClient().deleteCompanyOffer(getIntent().getIntExtra("product_id", 0)).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {

                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
                        dialog.dismiss();
                        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                        intent.putExtra("id", getIntent().getIntExtra("company_id", 0) + "");
                        startActivity(intent);

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

                alert.show();

            }
        });
    }
}
