package com.taj51.efazcompany;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.AddCompanyOfferPOJO;
import com.taj51.efazcompany.pojo.GetCompanyOfferPOJO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateOfferActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    private ImageButton editProductImage;
    private ImageView productImage;
    private EditText productTitle;
    private EditText productExplanation;
    private EditText productCost;
    private EditText expireDate;
    private EditText deliverDate;
    private TextView updateProduct;
    private byte[] image;
    private String byts = "";
    private Uri filePath;
    private int product_id;
    private int companyId;
    private Timestamp timestamp;
    private String logo;

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String encodedImage) {
        byte[] decodedByte = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    // method for bitmap to base64
    public static byte[] encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        //String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        //Log.d("Image Log:", imageEncoded);
        return b;//imageEncoded;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_offer);

        productImage = (ImageView) findViewById(R.id.product_image_u);
        productTitle = (EditText) findViewById(R.id.product_title_u);
        productExplanation = (EditText) findViewById(R.id.product_explanation_u);
        productCost = (EditText) findViewById(R.id.product_cost_u);
        updateProduct = (TextView) findViewById(R.id.product_save_u);
        expireDate = (EditText) findViewById(R.id.expire_days_u);
        deliverDate = (EditText) findViewById(R.id.deliver_days_u);

        editProductImage = (ImageButton) findViewById(R.id.edit_product_image_u);


        product_id = getIntent().getIntExtra("id", 0);
        Log.d("UPDATE", product_id + " ");
        new getAsync().execute();

        editProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageStr();
            }
        });

        updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleStr = productTitle.getText().toString().trim();
                String explanationStr = productExplanation.getText().toString().trim();
                String costStr = productCost.getText().toString().trim();
                String expireStr = expireDate.getText().toString().trim();
                String deliverStr = deliverDate.getText().toString().trim();


                if (titleStr.equals("") || titleStr.equals(null)) {
                    productTitle.setError(getResources().getString(R.string.pass_confirm));
                    productTitle.requestFocus();
                } else {
                    if (explanationStr.equals("") || explanationStr.equals(null)) {
                        productExplanation.setError(getResources().getString(R.string.pass_confirm));
                        productExplanation.requestFocus();
                    } else {
                        if (costStr.equals("") || costStr.equals(null)) {
                            productCost.setError(getResources().getString(R.string.pass_confirm));
                            productCost.requestFocus();
                        } else {
                            if (expireStr.equals("") || expireStr.equals(null)) {
                                expireDate.setError(getResources().getString(R.string.pass_confirm));
                                expireDate.requestFocus();
                            } else {
                                if (deliverStr.equals("") || deliverStr.equals(null)) {
                                    deliverDate.setError(getResources().getString(R.string.pass_confirm));
                                    deliverDate.requestFocus();
                                } else {
//                                    if (byts.equals("") || byts.equals(null)) {
//                                        productImage.requestFocus();
//                                        //editText.requestFocus();
//                                    } else {

                                    new myAsync().execute();
                                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                                    intent.putExtra("id", companyId);
                                    startActivity(intent);


                                    //}
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    private void chooseImageStr() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            productImage.setImageBitmap(bitmap);
            image = encodeTobase64(bitmap);
            byts = Base64.encodeToString(image, Base64.DEFAULT);

        }
    }

    public class getAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Api.getClient().getSingleCompanyOffer(product_id).enqueue(new Callback<GetCompanyOfferPOJO>() {
                @Override
                public void onResponse(Call<GetCompanyOfferPOJO> call, Response<GetCompanyOfferPOJO> response) {
                    GetCompanyOfferPOJO pojo = response.body();
                    logo = pojo.getOffer_logo();

                    productImage.setImageBitmap(decodeBase64(pojo.getOffer_logo()));
                    productTitle.setText(pojo.getOffer_title());
                    productExplanation.setText(pojo.getOffer_explaination());
                    productCost.setText(pojo.getOffer_cost() + " ");
                    String display = pojo.getOffer_display_date();
                    String deliver = pojo.getOffer_deliver_date();
                    String expire = pojo.getOffer_expired_date();
                    companyId = pojo.getCompany_id();
                    String hours;
                    String min;
                    String sec;

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                        Date parsedDate = dateFormat.parse(display);
                        timestamp = new java.sql.Timestamp(parsedDate.getTime());
                        Date parsedDate2 = dateFormat.parse(expire);
                        Timestamp timestamp2 = new java.sql.Timestamp(parsedDate2.getTime());
                        Date parsedDate3 = dateFormat.parse(deliver);
                        Timestamp timestamp3 = new java.sql.Timestamp(parsedDate3.getTime());


                        long milliseconds1 = timestamp.getTime();
                        long milliseconds2 = timestamp2.getTime();
                        Long milliSeconds3 = timestamp3.getTime();
                        long diff = milliseconds2 - milliseconds1;
                        long diff2 = milliSeconds3 - milliseconds1;
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        long diffDays2 = diff2 / (24 * 60 * 60 * 1000);
                        long rem = diff % (24 * 60 * 60 * 1000);
                        long diffHours = rem / (60 * 60 * 1000);
                        long rem2 = rem % (60 * 60 * 1000);
                        long diffMinutes = rem2 / (60 * 1000);
                        hours = String.valueOf(diffDays);
                        min = String.valueOf(diffHours);
                        sec = String.valueOf(diffMinutes);
                        expireDate.setText(String.valueOf(diffDays));
                        deliverDate.setText(String.valueOf(diffDays2));

                        AddCompanyOfferPOJO offer = new AddCompanyOfferPOJO();
                        Api.getClient().updateCompanyOffer(offer);


                    } catch (Exception e) { //this generic but you can control another types of exception
                        // look the origin of excption
                    }


                }

                @Override
                public void onFailure(Call<GetCompanyOfferPOJO> call, Throwable t) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public class myAsync extends AsyncTask<Void, Void, Integer> {
        int id;

        @Override
        protected Integer doInBackground(Void... voids) {


            String titleStr = productTitle.getText().toString().trim();
            String explanationStr = productExplanation.getText().toString().trim();
            String costStr = productCost.getText().toString().trim();
            String expireStr = expireDate.getText().toString().trim();
            String deliverStr = deliverDate.getText().toString().trim();


            java.sql.Timestamp display = timestamp;

            java.sql.Timestamp expire = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(expire);
            cal.add(Calendar.DAY_OF_WEEK, Integer.parseInt(expireStr));
            expire.setTime(cal.getTime().getTime()); // or
            expire = new Timestamp(cal.getTime().getTime());

            java.sql.Timestamp deliver = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(deliver);
            cal2.add(Calendar.DAY_OF_WEEK, Integer.parseInt(deliverStr));
            deliver.setTime(cal2.getTime().getTime()); // or
            deliver = new Timestamp(cal2.getTime().getTime());

            Log.d("test add product", "= " + expire);
            if (byts.equals("")){
                byts = logo;
                AddCompanyOfferPOJO pojos = new AddCompanyOfferPOJO(product_id ,Base64.decode(byts, 0), titleStr, explanationStr, Double.parseDouble(costStr), display.toString(),
                        expire.toString(), deliver.toString(), companyId);
                Api.getClient().updateCompanyOffer(pojos).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {


                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.d("test add product", "4" + t.getMessage());

                    }
                });
            }else {
                //byts = logo;
                AddCompanyOfferPOJO pojos = new AddCompanyOfferPOJO(Base64.decode(byts, 0), titleStr, explanationStr, Double.parseDouble(costStr), display.toString(),
                        expire.toString(), deliver.toString(), companyId);
                Api.getClient().updateCompanyOffer(pojos).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {


                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.d("test add product", "4" + t.getMessage());

                    }
                });

            }

            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }
}
