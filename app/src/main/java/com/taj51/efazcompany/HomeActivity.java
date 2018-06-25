package com.taj51.efazcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.GetProfilePojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout dLayout;
    SharedPreferences prf;

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String encodedImage) {
        byte[] decodedByte = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.home_toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        toolbar.setTitle(getResources().getString(R.string.app_name)); // setting a title for this Toolbar
        prf = getSharedPreferences("ids", MODE_PRIVATE);
        int t = prf.getInt("id",0);
        if (t==0){
            SharedPreferences.Editor edit = prf.edit();
            edit.putInt("id", getIntent().getIntExtra("id", 0));
            edit.commit();
        }

//        android.support.v4.app.FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
//        Intent intent = getIntent();
//        Bundle bundle = new Bundle();
//        bundle.putString("", "From Activity");
//        bundle.putInt("id", intent.getIntExtra("id", 0));
//        bundle.putString("email", intent.getStringExtra("email"));
//        bundle.putString("password", intent.getStringExtra("password"));
//        bundle.putInt("active", intent.getIntExtra("active", 0));
//        bundle.putInt("type", intent.getIntExtra("type", 0));
//        Fragment frag = new HomeFragment();
//        frag.setArguments(bundle);
//        tx.replace(R.id.frame, frag);
//        tx.commit();
        setNavigationDrawer();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dLayout.openDrawer(Gravity.START);
            }
        });

        //Toast.makeText(getApplicationContext(), intent.getIntExtra("id",0) + " " +  intent.getStringExtra("email"),Toast.LENGTH_LONG).show();

    }

    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View


        View hView = navView.getHeaderView(0);
        final TextView nav_user = (TextView) hView.findViewById(R.id.nav_header_company_name);
        final ImageView img = (ImageView) hView.findViewById(R.id.nav_header_logo);
        int num = getIntent().getIntExtra("id", 0);
        if (num == 0)
            num = prf.getInt("id", 0);
        Api.getClient().getProfile(num).enqueue(new Callback<GetProfilePojo>() {
            @Override
            public void onResponse(Call<GetProfilePojo> call, Response<GetProfilePojo> response) {
                GetProfilePojo pojo = response.body();
                //Log.d("TestAPI", pojo.getCompany_logo_image());
                img.setImageBitmap(decodeBase64(pojo.getCompany_logo_image()));
                nav_user.setText(pojo.getCompany_name());
            }

            @Override
            public void onFailure(Call<GetProfilePojo> call, Throwable t) {

            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
// check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.Home) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putString("", "From Activity");
                    bundle.putInt("id", intent.getIntExtra("id", 0));
                    bundle.putString("email", intent.getStringExtra("email"));
                    bundle.putString("password", intent.getStringExtra("password"));
                    bundle.putInt("active", intent.getIntExtra("active", 0));
                    bundle.putInt("type", intent.getIntExtra("type", 0));
                    frag = new HomeFragment();
                    frag.setArguments(bundle);

                } else if (itemId == R.id.offer) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", intent.getIntExtra("id", 0));
                    frag = new OfferFragment();
                    frag.setArguments(bundle);

                } else if (itemId == R.id.Profile) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ss", "From Activity");
                    bundle.putInt("id", intent.getIntExtra("id", 0));
                    bundle.putString("email", intent.getStringExtra("email"));
                    bundle.putString("password", intent.getStringExtra("password"));
                    bundle.putInt("active", intent.getIntExtra("active", 0));
                    bundle.putInt("type", intent.getIntExtra("type", 0));
                    frag = new ProfileFragment();
                    frag.setArguments(bundle);
                } else if (itemId == R.id.SignOut) {

                    SharedPreferences remPreference = getSharedPreferences("remmber", MODE_PRIVATE);
                    SharedPreferences.Editor editor = remPreference.edit();
                    editor.putBoolean("remm", false);
                    editor.commit();
                    Api.getClient().inActiveLogin(getIntent().getIntExtra("id", 0)).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("inactive", "response");
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("inactive", "fail");
                        }
                    });
                    Intent intent = new Intent(getBaseContext(), login.class);
                    startActivity(intent);
//                    signOutGoogle();
                    //Toast.makeText(getBaseContext(), "signout", Toast.LENGTH_LONG).show();

                } else if (itemId == R.id.add_product) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", intent.getIntExtra("id", 0));
                    frag = new AddProductFragment();
                    frag.setArguments(bundle);

                }
//                else if (itemId == R.id.add_product){
//                    frag = new AddProductFragment();
//                }
// display a toast message with menu item's title
                //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
                    transaction.commit(); // commit the changes
                    dLayout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        Api.getClient().inActiveLogin(getIntent().getIntExtra("id", 0)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("inactived", "response");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("inactive", "fail");
            }
        });
        super.onDestroy();

    }

}
