package com.mike4christ.sti_mobile.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.RenewPolicyGet;
import com.mike4christ.sti_mobile.Model.ServiceGenerator;
import com.mike4christ.sti_mobile.NetworkConnection;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.retrofit_interface.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyVehicleDetail extends AppCompatActivity {

    /** ButterKnife Code **/
    @BindView(R.id.toolbar)
    androidx.appcompat.widget.Toolbar mToolbar;
    @BindView(R.id.status_bg)
    ImageView mStatusBg;
    @BindView(R.id.status)
    TextView mStatus;
    @BindView(R.id.payment_status)
    TextView mPaymentStatus;
    @BindView(R.id.policy_num)
    TextView mPolicyNum;
    @BindView(R.id.policy_type)
    TextView mPolicyType;
    @BindView(R.id.cover_type)
    TextView mCoverType;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.start_date)
    TextView mStartDate;
    @BindView(R.id.end_date)
    TextView mEndDate;
    @BindView(R.id.vehicle_make)
    TextView mVehicleMake;
    @BindView(R.id.body_type)
    TextView mBodyType;
    @BindView(R.id.reg_num)
    TextView mRegNum;
    @BindView(R.id.chasis_num)
    TextView mChasisNum;
    @BindView(R.id.eng_num)
    TextView mEngNum;
    @BindView(R.id.payment_ref)
    TextView mPaymentRef;
    @BindView(R.id.vehicle_value)
    TextView mVehicleValue;
    @BindView(R.id.vehicle_year)
    TextView mVehicleYear;
    @BindView(R.id.renew_btn)
    TextView mRenew;
    @BindView(R.id.progress)
    AVLoadingIndicatorView mProgress;
    /** ButterKnife Code **/

    Animation slide_front_left, blink;

    private UserPreferences userPreferences;
    NetworkConnection networkConnection=new NetworkConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);
        ButterKnife.bind(this);
        userPreferences=new UserPreferences(this);
        applyToolbarChildren("My Policy Detail");


        Intent intent=getIntent();
        String body_type=intent.getStringExtra("body_type");
        String chasis_num=intent.getStringExtra("chasis_num");
        String engine_num=intent.getStringExtra("engine_num");
        String policy_type=intent.getStringExtra("policy_type");
        String make=intent.getStringExtra("make");
        String start_date=intent.getStringExtra("start_date");
        String end_date=intent.getStringExtra("end_date");
        String price=intent.getStringExtra("price");
        String payment_ref=intent.getStringExtra("payment_ref");
        String payment_status=intent.getStringExtra("payment_status");
        String status=intent.getStringExtra("status");
        String cover_type=intent.getStringExtra("cover_type");

        String reg_num=intent.getStringExtra("reg_num");
        String value=intent.getStringExtra("value");
        String policy_num=intent.getStringExtra("policy_num");
        String year=intent.getStringExtra("year");




        mBodyType.setText(body_type);
        mStatus.setText(status);
        mChasisNum.setText(chasis_num);
        mPolicyNum.setText(policy_num);
        mEngNum.setText(engine_num);
        mVehicleMake.setText(make);
        mPolicyType.setText(policy_type);
        mStartDate.setText(start_date);
        mEndDate.setText(end_date);
        mPrice.setText(price);
        mPaymentRef.setText(payment_ref);
        mPaymentStatus.setText(payment_status);
        mCoverType.setText(cover_type);
        mRegNum.setText(reg_num);
        mVehicleValue.setText(value);
        mVehicleYear.setText(year);


        slide_front_left = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_from_left);

        blink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);

        //start animation
        mStatusBg.startAnimation(slide_front_left);

        mStatus.startAnimation(blink);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date strDate = null;
        try {
            strDate = sdf.parse(end_date);
            //if current date is greater then make renew visible

            if (System.currentTimeMillis() > strDate.getTime()) {
                mRenew.setVisibility(View.VISIBLE);

                mRenew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(networkConnection.isNetworkConnected(getBaseContext())){
                        mRenew.setVisibility(View.GONE);
                        mProgress.setVisibility(View.VISIBLE);


                        //get client and call object for request
                        ApiInterface client = ServiceGenerator.createService(ApiInterface.class);


                        Call<RenewPolicyGet> call=client.renew_policy("Token "+userPreferences.getUserToken(),policy_num,price,"paystack");

                        call.enqueue(new Callback<RenewPolicyGet>() {
                            @Override
                            public void onResponse(Call<RenewPolicyGet> call, Response<RenewPolicyGet> response) {
                                Log.i("ResponseCode", String.valueOf(response.code()));

                                if(response.code()==400){
                                    showShortMsg("Check your internet connection");
                                    mRenew.setVisibility(View.VISIBLE);
                                    mProgress.setVisibility(View.GONE);
                                    return;
                                }else if(response.code()==429){
                                    showShortMsg("Too many requests on database");
                                    mRenew.setVisibility(View.VISIBLE);
                                    mProgress.setVisibility(View.GONE);
                                    return;
                                }else if(response.code()==500){
                                    showShortMsg("Server Error");
                                    mRenew.setVisibility(View.VISIBLE);
                                    mProgress.setVisibility(View.GONE);
                                    return;
                                }else if(response.code()==401){
                                    showShortMsg("Unauthorized access, please try login again");
                                    mRenew.setVisibility(View.VISIBLE);
                                    mProgress.setVisibility(View.GONE);
                                    return;
                                }
                                try {
                                    if (!response.isSuccessful()) {

                                        try{
                                            APIError apiError= ErrorUtils.parseError(response);

                                            showShortMsg("Invalid Entry: "+apiError.getErrors());
                                            Log.i("Invalid EntryK",apiError.getErrors().toString());
                                            Log.i("Invalid Entry",response.errorBody().toString());

                                        }catch (Exception e){
                                            Log.i("InvalidEntry",e.getMessage());
                                            Log.i("ResponseError",response.errorBody().string());
                                            showShortMsg("Failed to Renew"+e.getMessage());
                                            mRenew.setVisibility(View.VISIBLE);
                                            mProgress.setVisibility(View.GONE);

                                        }
                                        mRenew.setVisibility(View.VISIBLE);
                                        mProgress.setVisibility(View.GONE);
                                        return;
                                    }


                                    String amount=response.body().getAmount();
                                    String policyNumber=response.body().getPolicyNumber();
                                    String reference=response.body().getReference();


                                    Intent i = new Intent(MyVehicleDetail.this, PolicyPaymentActivity.class);
                                    i.putExtra(Constant.POLICY_NUM, policyNumber);
                                    i.putExtra(Constant.TOTAL_PRICE, amount);
                                    i.putExtra(Constant.POLICY_TYPE, "vehicle");
                                    i.putExtra(Constant.REF, reference);
                                    startActivity(i);

                                    mRenew.setVisibility(View.VISIBLE);
                                    mProgress.setVisibility(View.GONE);

                                }catch (Exception e){
                                    Log.i("PolicyRenewError", e.getMessage());
                                    mRenew.setVisibility(View.VISIBLE);
                                    mProgress.setVisibility(View.GONE);
                                }

                            }
                            @Override
                            public void onFailure(Call<RenewPolicyGet> call, Throwable t) {
                                showShortMsg("Renewed Failed "+t.getMessage());
                                Log.i("GetError",t.getMessage());
                                mRenew.setVisibility(View.VISIBLE);
                                mProgress.setVisibility(View.GONE);
                            }
                        });

                        }else{
                            showShortMsg("No Internet Connection");
                        }


                    }
                });



            }


        } catch (ParseException e) {
            e.printStackTrace();
            showShortMsg("Error: "+e.getMessage());
        }

    }

    private void applyToolbarChildren(String title) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
        //setting Elevation for > API 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(10f);
        }

    }




    private void showShortMsg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
