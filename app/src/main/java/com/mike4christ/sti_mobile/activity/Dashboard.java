package com.mike4christ.sti_mobile.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;


import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.mike4christ.sti_mobile.Forms.Claim;
import com.mike4christ.sti_mobile.MainActivity;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.SignUp;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.forms_fragment.Claim.SubFragment_Claim;
import com.mike4christ.sti_mobile.forms_fragment.Claim.Track_Claim;
import com.mike4christ.sti_mobile.forms_fragment.Pin.PinFragment;
import com.mike4christ.sti_mobile.fragment.DashboardFragment;
import com.mike4christ.sti_mobile.fragment.EmailUsFragment;
import com.mike4christ.sti_mobile.fragment.MyClaimFragment;
import com.mike4christ.sti_mobile.fragment.MyPoliciesFragment;
import com.mike4christ.sti_mobile.fragment.ProfileFragment;
import com.mike4christ.sti_mobile.fragment.QuoteBuyFragment;
import com.mike4christ.sti_mobile.fragment.TransactionHistoryFragment;
import com.mike4christ.sti_mobile.fragment.ValidatePolicyFragment;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/*
 * Author: Michael Boluwaji
 *  STI  Mobile App
 *
 *
 *
 *
 * */

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

/** ButterKnife Code **/
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.content_dash_layout)
    LinearLayout mContentDashLayout;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.dashboard_btn_lay)
    LinearLayout mDashboardBtnLay;
    @BindView(R.id.find_us_btn_lay)
    LinearLayout mFindUsBtnLay;
    @BindView(R.id.policy_btn_lay)
    LinearLayout mPolicyBtnLay;
    @BindView(R.id.claim_btn_lay)
    LinearLayout mClaimBtnLay;
  /*  @BindView(R.id.profile_icon)
    CircleImageView mProfileIcon;*/
    /** ButterKnife Code **/
    CircleImageView mProfileIcon;
    TextView nav_lastname;

    UserPreferences userPreferences;

    Fragment fragment;
    String personal_img_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);


        userPreferences=new UserPreferences(this);
       // customizeToolbar(mToolbar);
        setClick();
        personal_img_url=userPreferences.getProfileImg();

        fragment = new DashboardFragment();
        showFragment(fragment);




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
       /* TextView userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        userName.setText(new UserPreference().getUser(MainActivity.this).userName);*/

        mProfileIcon=navigationView.getHeaderView(0).findViewById(R.id.profile_icon);
        nav_lastname=navigationView.getHeaderView(0).findViewById(R.id.nav_lastname);
        nav_lastname.setText(userPreferences.getLastName());



        if(personal_img_url==null) {
            Glide.with(this).load(userPreferences.getProfileImg()).apply(new RequestOptions().fitCenter().circleCrop()).into(mProfileIcon);
        }else{
            Glide.with(this).load(personal_img_url).apply(new RequestOptions().fitCenter().circleCrop()).into(mProfileIcon);

        }

        mProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ProfileFragment();
                showFragment(fragment);
            }
        });

        checkPremission();

    }
    private  void setClick(){
        //Onclick Method initiated
        mDashboardBtnLay.setOnClickListener(this);
        mFindUsBtnLay.setOnClickListener(this);
        mPolicyBtnLay.setOnClickListener(this);
        mClaimBtnLay.setOnClickListener(this);
    }



    //Method to set fragment immediately Onclick
    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }


    //Overriding Back Press button to close an already open Navigation
    //View
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {
           // startActivity(new Intent(Dashboard.this,ProfileActivity.class));
            return true;
        }else if (id == R.id.action_about) {
            // startActivity(new Intent(Dashboard.this,ProfileActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dashboard) {
            //Shopping Fragment Onclick
            fragment = new DashboardFragment();
            showFragment(fragment);

        } else if (id == R.id.quote_buy) {

            fragment = new QuoteBuyFragment();
            showFragment(fragment);

        } else if (id == R.id.my_policy) {

            fragment = new MyPoliciesFragment();
            showFragment(fragment);

        } else if (id == R.id.find_us) {

            startActivity(new Intent(Dashboard.this, FindUs.class));


        } else if (id == R.id.transaction_history) {

            fragment = new TransactionHistoryFragment();
            showFragment(fragment);

        } else if (id == R.id.make_claim) {

            startActivity(new Intent(Dashboard.this, Claim.class));


        } else if (id == R.id.my_claim) {

            fragment = new MyClaimFragment();
            showFragment(fragment);


        }else if (id == R.id.track_claim) {
            fragment = new Track_Claim();
            showFragment(fragment);
        }
        else if (id == R.id.val_motor_insured) {
            fragment = new ValidatePolicyFragment();
            showFragment(fragment);

        }else if (id == R.id.email_us) {
            fragment = new EmailUsFragment();
            showFragment(fragment);
        }
        else if (id == R.id.nav_account) {

            fragment = new ProfileFragment();
            showFragment(fragment);

        }else if (id == R.id.set_change_pin) {

            fragment = new PinFragment();
            showFragment(fragment);

        }
        else if (id == R.id.log_out) {
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dashboard_btn_lay:
                fragment = new DashboardFragment();
                showFragment(fragment);
                break;
            case R.id.find_us_btn_lay:
                startActivity(new Intent(Dashboard.this, FindUs.class));
                break;

            case R.id.policy_btn_lay:
                fragment = new MyPoliciesFragment();
                showFragment(fragment);

                break;

            case R.id.claim_btn_lay:

               startActivity(new Intent(Dashboard.this, Claim.class));
                break;

        }
    }


    private static final int REQUEST_RUNTIME_PERMISSION = 1;

    public void checkPremission() {
        //select which permission you want
        final String permission = Manifest.permission.CAMERA;
        //final String permission = Manifest.permission.Storage;
        // if in fragment use getActivity()
        if (ContextCompat.checkSelfPermission(Dashboard.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Dashboard.this, permission)) {

            } else {
                ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_RUNTIME_PERMISSION);
            }
        } else {
            // you have permission go ahead
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RUNTIME_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    // you have permission go ahead
                }else{
                    // you dont have permission show toast

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



}
