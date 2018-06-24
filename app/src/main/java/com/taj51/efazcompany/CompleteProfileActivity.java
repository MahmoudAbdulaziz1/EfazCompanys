package com.taj51.efazcompany;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.CategoryPojo;
import com.taj51.efazcompany.pojo.ProfilePOJO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteProfileActivity extends AppCompatActivity {


    private ImageButton editBtn;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageView img;
    private EditText nameTxt;
    private EditText addressTxt;
    //private EditText categoryTxt;
    private EditText youtubeTxt;
    private EditText websiteTxt;
    private TextView saveBtn;
    private Spinner spin;

    private byte[] image ;
    private String byts = "";
    private String name = "";
    private String address = "";
    private String category = "";
    private String youtube = "";
    private String website = "";

    private List<CategoryPojo> categoryPojos;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivty_coimplete_profile);


        img = (ImageView)findViewById(R.id.addProfile);
        nameTxt = (EditText) findViewById(R.id.profile_name);
        addressTxt = (EditText) findViewById(R.id.profile_address);
        //categoryTxt = (EditText) findViewById(R.id.profile_service);
        youtubeTxt = (EditText) findViewById(R.id.profile_youtube);
        websiteTxt = (EditText) findViewById(R.id.profile_website);
        saveBtn = (TextView) findViewById(R.id.profile_btn);
        editBtn = (ImageButton) findViewById(R.id.editImageBtn);
        spin = (Spinner) findViewById(R.id.category_spinner);


        categoryPojos = new ArrayList<CategoryPojo>();
        Api.getClient().getCategories().enqueue(new Callback<List<CategoryPojo>>() {
            @Override
            public void onResponse(Call<List<CategoryPojo>> call, Response<List<CategoryPojo>> response) {
                categoryPojos = response.body();
                List<String> categoryNames = new ArrayList<String>();
                categoryNames.add("Select Your Category");
                for (int i=0 ; i< categoryPojos.size(); i++){
                    categoryNames.add(categoryPojos.get(i).getCategory_name());
                }

                ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_item, categoryNames);
                spin.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CategoryPojo>> call, Throwable t) {

            }
        });


        final Intent intent = getIntent();
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImageStr();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = nameTxt.getText().toString().trim();
                address = addressTxt.getText().toString().trim();
                youtube = youtubeTxt.getText().toString().trim();
                website = websiteTxt.getText().toString().trim();
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if(position == 0){
                            category = "";
                        }else {
                            category = parent.getItemAtPosition(position).toString();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




                if (byts.equals("")){
                    img.setFocusable(true);
                    Toast.makeText(getBaseContext(), "Select Image Please", Toast.LENGTH_LONG).show();
                }else {
                    if (name.equals("") ){
                        nameTxt.setError(getResources().getString(R.string.org_name));
                        nameTxt.requestFocus();
                    }else {
                        if (address.equals("")){
                            addressTxt.setError(getResources().getString(R.string.org_add));
                            addressTxt.requestFocus();
                        }else {
                            if (category.equals("")){
                                spin.setFocusable(true);
                                Toast.makeText(getBaseContext(), "Select Category", Toast.LENGTH_LONG).show();
                            }else {
                                if (website.equals("")){
                                    websiteTxt.setError(getResources().getString(R.string.website_error));
                                    websiteTxt.requestFocus();
                                }else {
                                    if (youtube.equals("")){
                                        if (validateWebsite()) {
                                            final int id = intent.getIntExtra("id", 0);
                                            final String email = intent.getStringExtra("email");
                                            ProfilePOJO pojo = new ProfilePOJO(intent.getIntExtra("id", 0), name, Base64.decode(byts, 0),
                                                    address, category, youtube, website);
                                            Api.getClient().AddUserProfile(pojo).enqueue(new Callback<Integer>() {
                                                @Override
                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                    Intent move = new Intent(getBaseContext(), ProfileActivity.class);
                                                    move.putExtra("id", id);
                                                    move.putExtra("email", email);
                                                    startActivity(move);
                                                }

                                                @Override
                                                public void onFailure(Call<Integer> call, Throwable t) {

                                                }
                                            });
                                        }else {
                                            websiteTxt.setError(getResources().getString(R.string.website_error));
                                            websiteTxt.requestFocus();
                                        }
                                    }else {
                                        if (validateYoutubeUrl()){
                                            if (validateWebsite()){
                                                ProfilePOJO pojo = new ProfilePOJO(intent.getIntExtra("id", 0), name, Base64.decode(byts, 0),
                                                        address, category, youtube, website);
                                                Api.getClient().AddUserProfile(pojo).enqueue(new Callback<Integer>() {
                                                    @Override
                                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                        Intent intent1 = new Intent(getBaseContext(), HomeActivity.class);
                                                        startActivity(intent1);
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Integer> call, Throwable t) {

                                                    }
                                                });
                                            }else {
                                                websiteTxt.setError(getResources().getString(R.string.website_error));
                                                websiteTxt.requestFocus();
                                            }

                                        }else {
                                            youtubeTxt.setError(getResources().getString(R.string.org_youtube));
                                            youtubeTxt.requestFocus();
                                        }

                                    }
                                }
                            }
                        }
                    }
                }


//                if (byts.equals("") || name.equals("") || address.equals("") || category.equals("") || website.equals("")){
//                    Toast.makeText(getBaseContext(), "Fill all data please", Toast.LENGTH_LONG).show();
//                }else {
//
//
//                }


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            filePath = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            img.setImageBitmap(bitmap);
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



    private boolean validateYoutubeUrl() {
        String ytube = youtubeTxt.getText().toString().trim();

        if (ytube.isEmpty() || !isValidUrl(ytube)) {
            youtubeTxt.setError(getResources().getString(R.string.youtube_error));
            youtubeTxt.requestFocus();
            return false;
        }

        return true;
    }

    private static boolean isValidUrl(String yout) {
        return !TextUtils.isEmpty(yout) && Patterns.WEB_URL.matcher(yout).matches();
    }

    private boolean validateWebsite() {
        String web = websiteTxt.getText().toString().trim();

        if (web.isEmpty() || !isValidWebsite(web)) {
            websiteTxt.setError(getResources().getString(R.string.website_error));
            websiteTxt.requestFocus();
            return false;
        }

        return true;
    }

    private static boolean isValidWebsite(String yout) {
        return !TextUtils.isEmpty(yout) && Patterns.DOMAIN_NAME.matcher(yout).matches();
    }


}
