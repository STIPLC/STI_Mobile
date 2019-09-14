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

import com.google.android.material.navigation.NavigationView;
import com.mike4christ.sti_mobile.Forms.Claim;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.SignUp;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.forms_fragment.Claim.SubFragment_Claim;
import com.mike4christ.sti_mobile.fragment.DashboardFragment;
import com.mike4christ.sti_mobile.fragment.ProfileFragment;
import com.mike4christ.sti_mobile.fragment.QuoteBuyFragment;
import com.mike4christ.sti_mobile.fragment.TransactionHistoryFragment;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    @BindView(R.id.payment_btn_lay)
    LinearLayout mPaymentBtnLay;
    @BindView(R.id.claim_btn_lay)
    LinearLayout mClaimBtnLay;
    /** ButterKnife Code **/

    UserPreferences userPreferences;

    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        userPreferences=new UserPreferences(this);
       // customizeToolbar(mToolbar);
        setClick();
        fragment = new DashboardFragment();
        showFragment(fragment);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        checkPremission();

    }
    private  void setClick(){
        //Onclick Method initiated
        mDashboardBtnLay.setOnClickListener(this);
        mFindUsBtnLay.setOnClickListener(this);
        mPaymentBtnLay.setOnClickListener(this);
        mClaimBtnLay.setOnClickListener(this);
    }


    //customizing the toolbar to fit middle of appbar
    public void customizeToolbar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        //setting Elevation for > API 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10f);
        }
        // Save current title and subtitle
        final CharSequence originalTitle = toolbar.getTitle();

        // Temporarily modify title and subtitle to help detecting each
        toolbar.setTitle("storex");

        for(int i = 0; i < toolbar.getChildCount(); i++){
            View view = toolbar.getChildAt(i);

            if(view instanceof TextView){
                TextView textView = (TextView) view;


                if(textView.getText().equals("storex")){
                    // Customize title's TextView
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    textView.setLayoutParams(params);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));


                }
            }
        }

        // Restore title and subtitle
        toolbar.setTitle(originalTitle);
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

        } else if (id == R.id.manage_policy) {

            /*fragment = new ManagePolicyFragment();
            showFragment(fragment);*/

        } else if (id == R.id.find_us) {

            startActivity(new Intent(Dashboard.this, FindUs.class));


        } else if (id == R.id.transaction_history) {

            fragment = new TransactionHistoryFragment();
            showFragment(fragment);

        } else if (id == R.id.make_claim) {

            startActivity(new Intent(Dashboard.this, Claim.class));


        } else if (id == R.id.track_claim) {

        }
        else if (id == R.id.val_motor_insured) {

        }
        else if (id == R.id.email_us) {

        }
        else if (id == R.id.nav_account) {

            fragment = new ProfileFragment();
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

            case R.id.payment_btn_lay:

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
