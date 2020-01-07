package com.mike4christ.sti_mobile.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.mike4christ.sti_mobile.Forms.Claim;
import com.mike4christ.sti_mobile.Model.Auth.ChangePassPost;
import com.mike4christ.sti_mobile.Model.Auth.UserPassword;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.ServiceGenerator;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.SignIn;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.forms_fragment.Claim.Track_Claim;
import com.mike4christ.sti_mobile.fragment.DashboardFragment;
import com.mike4christ.sti_mobile.fragment.EmailUsFragment;
import com.mike4christ.sti_mobile.fragment.MyClaimFragment;
import com.mike4christ.sti_mobile.fragment.MyPoliciesFragment;
import com.mike4christ.sti_mobile.fragment.QuoteBuyFragment;
import com.mike4christ.sti_mobile.fragment.TransactionHistoryFragment;
import com.mike4christ.sti_mobile.fragment.ValidatePolicyFragment;
import com.mike4christ.sti_mobile.retrofit_interface.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

        personal_img_url = userPreferences.getProfileImg();

        if(personal_img_url==null) {
            Glide.with(this).load(userPreferences.getProfileImg()).apply(new RequestOptions().fitCenter().circleCrop()).into(mProfileIcon);
        }else{
            Glide.with(this).load(userPreferences.getProfileImg()).apply(new RequestOptions().fitCenter().circleCrop()).into(mProfileIcon);
        }

        mProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ProfileActivity.class));
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
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

            startActivity(new Intent(Dashboard.this, ProfileActivity.class));

        } else if (id == R.id.change_pass) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            changePassword();

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

    private void changePassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Change Password");
        builder.setIcon(R.drawable.ic_vpn_key_black_24dp);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_pass, null);
        builder.setView(dialogView);
        EditText oldPassword = dialogView.findViewById(R.id.oldpass);
        EditText newPassword = dialogView.findViewById(R.id.newpass);
        AVLoadingIndicatorView progressBar = dialogView.findViewById(R.id.progressbar);

        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (oldPassword.getText().toString().isEmpty() || oldPassword.getText().toString().trim().length() < 6) {
                    showMessage("Invalid Password, ensure at least 6 characters");
                    return;
                } else if (newPassword.getText().toString().isEmpty() || newPassword.getText().toString().trim().length() < 6) {
                    showMessage("Invalid Password, ensure at least 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                UserPassword userPassword = new UserPassword(oldPassword.getText().toString().trim(), newPassword.getText().toString().trim());

                ChangePassPost changePassPost = new ChangePassPost(userPassword);

                ApiInterface client = ServiceGenerator.createService(ApiInterface.class);
                Call<ResponseBody> call = client.change_password("Token " + userPreferences.getUserToken(), changePassPost);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            if (response.code() == 400) {
                                showMessage("Check your internet connection");
                                progressBar.setVisibility(View.GONE);
                                return;
                            } else if (response.code() == 429) {
                                showMessage("Too many requests on database");
                                progressBar.setVisibility(View.GONE);
                                return;
                            } else if (response.code() == 500) {
                                showMessage("Server Error");
                                progressBar.setVisibility(View.GONE);
                                return;
                            } else if (response.code() == 401) {
                                showMessage("Unauthorized access, please try login again");
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                            try {
                                APIError apiError = ErrorUtils.parseError(response);

                                showMessage("Failed Entry: " + apiError.getErrors());
                                Log.i("Invalid EntryK", apiError.getErrors().toString());
                                Log.i("Invalid Entry", response.errorBody().toString());
                                progressBar.setVisibility(View.GONE);

                            } catch (Exception e) {
                                Log.i("InvalidEntry", e.getMessage());
                                showMessage("Failed Entry");

                                progressBar.setVisibility(View.GONE);

                            }

                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                        progressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                        showMessage("Password Changed Successfully, Re-Login! ");
                        startActivity(new Intent(Dashboard.this, SignIn.class));
                        finish();

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showMessage("Change Password Failed: " + t.getMessage());
                        Log.i("GEtError", t.getMessage());
                        //progressBar.setVisibility(View.GONE);
                    }
                });


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    private void showMessage(String s) {
        Snackbar.make(mContentDashLayout, s, Snackbar.LENGTH_LONG).show();
    }



}
