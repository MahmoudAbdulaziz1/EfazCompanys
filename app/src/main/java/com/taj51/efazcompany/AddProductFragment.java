package com.taj51.efazcompany;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.AddCompanyOfferPOJO;
import com.taj51.efazcompany.pojo.CompanyOfferPOJO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductFragment extends Fragment {

    private ImageButton editProductImage;
    private ImageView productImage;
    private EditText  productTitle;
    private EditText  productExplanation;
    private EditText  productCost;
    private EditText  expireDate;
    private EditText  deliverDate;
    private TextView  saveProduct;
    private byte[] image ;
    private String byts = "";
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        productImage       = (ImageView)view.findViewById(R.id.product_image);
        productTitle       = (EditText)view.findViewById(R.id.product_title);
        productExplanation = (EditText)view.findViewById(R.id.product_explanation);
        productCost        = (EditText)view.findViewById(R.id.product_cost);
        saveProduct        = (TextView)view.findViewById(R.id.product_save);
        expireDate         = (EditText)view.findViewById(R.id.expire_days);
        deliverDate        = (EditText)view.findViewById(R.id.deliver_days);

        editProductImage   = (ImageButton)view.findViewById(R.id.edit_product_image);

        editProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageStr();
            }
        });


        saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String titleStr       = productTitle.getText().toString().trim();
                String explanationStr = productExplanation.getText().toString().trim();
                String costStr        = productCost.getText().toString().trim();
                String expireStr      = expireDate.getText().toString().trim();
                String deliverStr     = deliverDate.getText().toString().trim();






                if (titleStr.equals("")|| titleStr.equals(null)){
                    productTitle.setError(getResources().getString(R.string.pass_confirm));
                    productTitle.requestFocus();
                }else {
                    if (explanationStr.equals("")|| explanationStr.equals(null)){
                        productExplanation.setError(getResources().getString(R.string.pass_confirm));
                        productExplanation.requestFocus();
                    }else {
                        if (costStr.equals("")|| costStr.equals(null)){
                            productCost.setError(getResources().getString(R.string.pass_confirm));
                            productCost.requestFocus();
                        }else{
                            if (expireStr.equals("")|| expireStr.equals(null)){
                                expireDate.setError(getResources().getString(R.string.pass_confirm));
                                expireDate.requestFocus();
                            }else {
                                if (deliverStr.equals("")|| deliverStr.equals(null)){
                                    deliverDate.setError(getResources().getString(R.string.pass_confirm));
                                    deliverDate.requestFocus();
                                }else {
                                    if (byts.equals("")|| byts.equals(null)){
                                        productImage.requestFocus();
                                        //editText.requestFocus();
                                    }else {

                                        new myAsync().execute();

                                    }
                                }
                            }
                        }
                    }
                }
            }
        });


        Api.getClient().getCompanyOffers().enqueue(new Callback<List<CompanyOfferPOJO>>() {
            @Override
            public void onResponse(Call<List<CompanyOfferPOJO>> call, Response<List<CompanyOfferPOJO>> response) {
                Log.d("test product","1");
                List<CompanyOfferPOJO> data = response.body();
                Log.d("test product","2");
                Toast.makeText(getActivity(), data.get(0).getOffer_expired_date() + "  ", Toast.LENGTH_LONG).show();
                Log.d("test product","3");
            }

            @Override
            public void onFailure(Call<List<CompanyOfferPOJO>> call, Throwable t) {
                Log.d("test product","4" + t.getMessage());
            }
        });

        return view;
    }


    public class myAsync extends AsyncTask<Void, Void, Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {

            final int[] id = new int[1];

            String titleStr       = productTitle.getText().toString().trim();
            String explanationStr = productExplanation.getText().toString().trim();
            String costStr        = productCost.getText().toString().trim();
            String expireStr      = expireDate.getText().toString().trim();
            String deliverStr     = deliverDate.getText().toString().trim();








                                    java.sql.Timestamp display = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

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

                                    Log.d("test add product","= " + expire);
                                    AddCompanyOfferPOJO pojo = new AddCompanyOfferPOJO(Base64.decode(byts, 0), titleStr, explanationStr, Double.parseDouble(costStr), display.toString(),
                                            expire.toString(), deliver.toString(), 24);
                                    Api.getClient().addCompanyOffer(pojo).enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            Log.d("test add product","1");

                                            id[0] = response.body();

                                            Log.d("test add product","2");


                                            Log.d("test add product","3");

                                            productImage.setImageResource(R.drawable.backgrd);
                                            productTitle.setText("");
                                            productExplanation.setText("");
                                            productCost.setText("");
                                            expireDate.setText("");
                                            deliverDate.setText("");



                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            Log.d("test add product","4"+ t.getMessage());

                                        }
                                    });

            return id[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            Toast.makeText(getActivity(), integer + "  num", Toast.LENGTH_LONG).show();
        }
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
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            productImage.setImageBitmap(bitmap);
            image = encodeTobase64(bitmap);
            byts = Base64.encodeToString(image, Base64.DEFAULT);
//            SharedPreferences byteImage = getSharedPreferences("byte", MODE_PRIVATE);
//            SharedPreferences.Editor editor = byteImage.edit();
//            editor.putString("b", Base64.encodeToString(encodeTobase64(bitmap), Base64.DEFAULT));
//            editor.commit();
            //Picasso.with(getBaseContext()).load(filePath).fit().into(img);

        }
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

    // method for base64 to bitmap
    public static Bitmap decodeBase64(byte[] decodedByte) {
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }


}
