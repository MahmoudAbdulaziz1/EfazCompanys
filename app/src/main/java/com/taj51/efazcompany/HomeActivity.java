package com.taj51.efazcompany;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout dLayout;
;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.home_toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar4
        toolbar.setTitle(getResources().getString(R.string.app_name)); // setting a title for this Toolbar
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

//
//        View hView =  navView.getHeaderView(0);
//        TextView nav_user = (TextView)hView.findViewById(R.id.nav_text);
//        nav_user.setText(name);
//        ImageView img = (ImageView)hView.findViewById(R.id.nav_image);
//            Picasso.with(getBaseContext()).load(imgStr).placeholder(R.drawable.clean1).centerCrop().fit()
//                    .into(img);
//        img.setImageBitmap(decodeBase64(imgStr));

        //img.setImageBitmap(decodeBase64(imgStr));
// implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
// check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.Home) {
                    frag = new HomeFragment();
                } else if (itemId == R.id.Profile) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putString("", "From Activity");
                    bundle.putInt("id", intent.getIntExtra("id",0));
                    bundle.putString("email", intent.getStringExtra("email"));
                    bundle.putString("password", intent.getStringExtra("password"));
                    bundle.putInt("active",intent.getIntExtra("active",0));
                    bundle.putInt("type",intent.getIntExtra("type", 0));
                    frag = new ProfileFragment();
                    frag.setArguments(bundle);
                } else if (itemId == R.id.SignOut) {
//                    signOutGoogle();
                    Toast.makeText(getBaseContext(), "signout", Toast.LENGTH_LONG).show();

                }else if (itemId == R.id.add_product){
                    frag = new AddProductFragment();
                }
//                else if (itemId == R.id.add_product){
//                    frag = new AddProductFragment();
//                }
// display a toast message with menu item's title
                Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
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

}
