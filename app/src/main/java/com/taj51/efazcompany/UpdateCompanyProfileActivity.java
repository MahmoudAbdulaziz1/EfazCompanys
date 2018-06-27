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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.taj51.efazcompany.pojo.GetProfilePojo;
import com.taj51.efazcompany.pojo.ProfilePOJO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCompanyProfileActivity extends Fragment {


    private final int PICK_IMAGE_REQUEST = 71;
    List<String> categoryNames;
    private ImageButton editBtn;
    private Uri filePath;
    private ImageView img;
    private EditText nameTxt;
    private EditText addressTxt;
    //private EditText categoryTxt;
    private EditText youtubeTxt;
    private EditText websiteTxt;
    private TextView saveBtn;
    private Spinner spin;
    private byte[] image;
    private String byts = "";
    private String name = "";
    private String address = "";
    private String category = "";
    private String youtube = "";
    private String website = "";
    private String byts2 = "";
    private String cat2 = "";
    private List<CategoryPojo> categoryPojos;

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

    // method for base64 to bitmap
    public static Bitmap decodeBase64(byte[] decodedByte) {
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private static boolean isValidUrl(String yout) {
        return !TextUtils.isEmpty(yout) && Patterns.WEB_URL.matcher(yout).matches();
    }

    private static boolean isValidWebsite(String yout) {
        return !TextUtils.isEmpty(yout) && Patterns.DOMAIN_NAME.matcher(yout).matches();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_update_company_profile, container, false);
        img = (ImageView) view.findViewById(R.id.addProfile_u);
        nameTxt = (EditText) view.findViewById(R.id.profile_name_u);
        addressTxt = (EditText) view.findViewById(R.id.profile_address_u);
        youtubeTxt = (EditText) view.findViewById(R.id.profile_youtube_u);
        websiteTxt = (EditText) view.findViewById(R.id.profile_website_u);
        saveBtn = (TextView) view.findViewById(R.id.profile_btn_u);
        editBtn = (ImageButton) view.findViewById(R.id.editImageBtn_u);
        spin = (Spinner) view.findViewById(R.id.category_spinner_u);


        //for category spinner
        categoryPojos = new ArrayList<CategoryPojo>();
        Api.getClient().getCategories().enqueue(new Callback<List<CategoryPojo>>() {
            @Override
            public void onResponse(Call<List<CategoryPojo>> call, Response<List<CategoryPojo>> response) {
                categoryPojos = response.body();
                categoryNames = new ArrayList<String>();
                categoryNames.add("Select Your Category");
                for (int i = 0; i < categoryPojos.size(); i++) {
                    categoryNames.add(categoryPojos.get(i).getCategory_name());
                    Log.d("testsssssssssss", categoryPojos.get(i).getCategory_name());
                }
                Log.d("testsssssssssss", categoryPojos.size() + " ");

                ArrayAdapter adapter = new ArrayAdapter(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, categoryNames);
                spin.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CategoryPojo>> call, Throwable t) {

            }
        });


        new myAsync().execute();


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

                        if (position == 0) {
                            category = "";
                        } else {
                            category = parent.getItemAtPosition(position).toString();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                if (category.trim().equals("")){
                    category= cat2;
                }

                if (name.equals("")) {
                    nameTxt.setError(getResources().getString(R.string.org_name));
                    nameTxt.requestFocus();
                } else {
                    if (address.equals("")) {
                        addressTxt.setError(getResources().getString(R.string.org_add));
                        addressTxt.requestFocus();
                    } else {
                        if (category.equals("")) {
                            spin.setFocusable(true);
                            Toast.makeText(getActivity().getBaseContext(), "Select Category", Toast.LENGTH_LONG).show();
                        } else {
                            if (website.equals("")) {
                                websiteTxt.setError(getResources().getString(R.string.website_error));
                                websiteTxt.requestFocus();
                            } else {

                                if (byts.equals("")) {
                                   byts = byts2;
                                   if (category.trim().equals("")){
                                       category= cat2;
                                   }
                                   Log.d("TestAPiUpdate", cat2);
                                    if (youtube.equals("")) {
                                        Log.d("TestAPiUpdate", "youtube");

                                        if (validateWebsite()) {
                                            Log.d("TestAPiUpdate", website);
                                            final int id = getArguments().getInt("id", 0);
                                            final String email = getArguments().getString("email");
                                            ProfilePOJO pojo = new ProfilePOJO(getArguments().getInt("id", 0), name, Base64.decode(byts, 0),
                                                    address, category, youtube, website);
                                            Api.getClient().updateProfile(pojo).enqueue(new Callback<Integer>() {
                                                @Override
                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                    Log.d("TestAPiUpdate", "updated");
                                                    Intent move = new Intent(getActivity().getBaseContext(), HomeActivity.class);
                                                    move.putExtra("id", id);
                                                    move.putExtra("email", email);
                                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                                    FragmentTransaction trans = manager.beginTransaction();
                                                    trans.remove(new UpdateCompanyProfileActivity());
                                                    trans.commit();
                                                    manager.popBackStack();                                                    startActivity(move);
                                                }

                                                @Override
                                                public void onFailure(Call<Integer> call, Throwable t) {
                                                    Log.d("TestAPiUpdate", "not updated" + t.getMessage());

                                                }
                                            });
                                        } else {
                                            websiteTxt.setError(getResources().getString(R.string.website_error));
                                            websiteTxt.requestFocus();
                                        }
                                    } else {
                                        if (validateYoutubeUrl()) {
                                            if (validateWebsite()) {
                                                ProfilePOJO pojo = new ProfilePOJO(getArguments().getInt("id", 0), name, Base64.decode(byts, 0),
                                                        address, category, youtube, website);
                                                Api.getClient().updateProfile(pojo).enqueue(new Callback<Integer>() {
                                                    @Override
                                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                        final int id = getArguments().getInt("id", 0);
                                                        final String email = getArguments().getString("email");
                                                        Intent intent1 = new Intent(getActivity().getBaseContext(), HomeActivity.class);
                                                        intent1.putExtra("id", id);
                                                        intent1.putExtra("email", email);
                                                        FragmentManager manager = getActivity().getSupportFragmentManager();
                                                        FragmentTransaction trans = manager.beginTransaction();
                                                        trans.remove(new UpdateCompanyProfileActivity());
                                                        trans.commit();
                                                        manager.popBackStack();                                                        startActivity(intent1);
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Integer> call, Throwable t) {

                                                    }
                                                });
                                            } else {
                                                websiteTxt.setError(getResources().getString(R.string.website_error));
                                                websiteTxt.requestFocus();
                                            }

                                        } else {
                                            youtubeTxt.setError(getResources().getString(R.string.org_youtube));
                                            youtubeTxt.requestFocus();
                                        }

                                    }
                                } else {



                                    if (youtube.equals("")) {

                                        if (validateWebsite()) {
                                            final int id = getArguments().getInt("id", 0);
                                            final String email = getArguments().getString("email");
                                            ProfilePOJO pojo = new ProfilePOJO(getArguments().getInt("id", 0), name, Base64.decode(byts, 0),
                                                    address, category, youtube, website);
                                            Api.getClient().updateProfile(pojo).enqueue(new Callback<Integer>() {
                                                @Override
                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                    Intent move = new Intent(getActivity().getBaseContext(), HomeActivity.class);
                                                    move.putExtra("id", id);
                                                    move.putExtra("email", email);
                                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                                    FragmentTransaction trans = manager.beginTransaction();
                                                    trans.remove(new UpdateCompanyProfileActivity());
                                                    trans.commit();
                                                    manager.popBackStack();
                                                    startActivity(move);
                                                }

                                                @Override
                                                public void onFailure(Call<Integer> call, Throwable t) {

                                                }
                                            });
                                        } else {
                                            websiteTxt.setError(getResources().getString(R.string.website_error));
                                            websiteTxt.requestFocus();
                                        }
                                    } else {
                                        if (validateYoutubeUrl()) {
                                            if (validateWebsite()) {
                                                ProfilePOJO pojo = new ProfilePOJO(getArguments().getInt("id", 0), name, Base64.decode(byts, 0),
                                                        address, category, youtube, website);
                                                Api.getClient().AddUserProfile(pojo).enqueue(new Callback<Integer>() {
                                                    @Override
                                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                        final int id = getArguments().getInt("id", 0);
                                                        final String email = getArguments().getString("email");
                                                        Intent intent1 = new Intent(getActivity().getBaseContext(), HomeActivity.class);
                                                        intent1.putExtra("id", id);
                                                        intent1.putExtra("email", email);
                                                        FragmentManager manager = getActivity().getSupportFragmentManager();
                                                        FragmentTransaction trans = manager.beginTransaction();
                                                        trans.remove(new UpdateCompanyProfileActivity());
                                                        trans.commit();
                                                        manager.popBackStack();
                                                        startActivity(intent1);
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Integer> call, Throwable t) {

                                                    }
                                                });
                                            } else {
                                                websiteTxt.setError(getResources().getString(R.string.website_error));
                                                websiteTxt.requestFocus();
                                            }

                                        } else {
                                            youtubeTxt.setError(getResources().getString(R.string.org_youtube));
                                            youtubeTxt.requestFocus();
                                        }

                                    }
                                }


                            }
                        }
                    }
                }


            }
        });


        return view;
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
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            img.setImageBitmap(bitmap);
            image = encodeTobase64(bitmap);
            byts = Base64.encodeToString(image, Base64.DEFAULT);
        }
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

    private boolean validateWebsite() {
        String web = websiteTxt.getText().toString().trim();

        if (web.isEmpty() || !isValidWebsite(web)) {
            websiteTxt.setError(getResources().getString(R.string.website_error));
            websiteTxt.requestFocus();
            return false;
        }

        return true;
    }

    public class myAsync extends AsyncTask<Void, Void, Void> {

        final int id = getArguments().getInt("id");

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("UpdateProfile", id +"");
            Api.getClient().getProfile(id).enqueue(new Callback<GetProfilePojo>() {
                @Override
                public void onResponse(Call<GetProfilePojo> call, Response<GetProfilePojo> response) {


                    GetProfilePojo profilePojo = response.body();
                    byts2 = profilePojo.getCompany_logo_image();
                    img.setImageBitmap(decodeBase64(profilePojo.getCompany_logo_image()));
                    nameTxt.setText(profilePojo.getCompany_name());
                    addressTxt.setText(profilePojo.getCompany_address());
                    websiteTxt.setText(profilePojo.getCompany_website_url());
                    youtubeTxt.setText(profilePojo.getCompany_link_youtube());
                    cat2 = profilePojo.getCompany_service_desc();
                    Log.d("TstApiUpdate", "cat= " +cat2);
                    int indexCategory = categoryNames.indexOf(cat2);
                    spin.setSelection(indexCategory);

                }

                @Override
                public void onFailure(Call<GetProfilePojo> call, Throwable t) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


}
