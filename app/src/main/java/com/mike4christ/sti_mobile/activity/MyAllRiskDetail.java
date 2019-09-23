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
import com.mike4christ.sti_mobile.MainActivity;
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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAllRiskDetail extends AppCompatActivity {

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
    @BindView(R.id.item)
    TextView mItem;
    @BindView(R.id.value)
    TextView mValue;
    @BindView(R.id.period)
    TextView mPeriod;
    @BindView(R.id.start_date)
    TextView mStartDate;
    @BindView(R.id.end_date)
    TextView mEndDate;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.imei_num)
    TextView mImeiNum;
    @BindView(R.id.payment_ref)
    TextView mPaymentRef;
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
        setContentView(R.layout.activity_allrisk_detail);
        ButterKnife.bind(this);
        userPreferences=new UserPreferences(this);
        applyToolbarChildren("My Policy Detail");



        Intent intent=getIntent();

        String Item=intent.getStringExtra("Item");
        String value=intent.getStringExtra("value");
        String period=intent.getStringExtra("period");
        String policynum=intent.getStringExtra("policynum");
        String policy_type=intent.getStringExtra("policy_type");
        String start_date=intent.getStringExtra("start_date");
        String end_date=intent.getStringExtra("end_date");
        String policy_price=intent.getStringExtra("policy_price");
        String payment_status=intent.getStringExtra("payment_status");
        String status=intent.getStringExtra("status");
        String payment_reference=intent.getStringExtra("payment_reference");
        String imei=intent.getStringExtra("imei");



        mItem.setText(Item);
        mStatus.setText(status);
        mValue.setText(value);
        mPolicyNum.setText(policynum);
        mPeriod.setText(period);
        mPolicyType.setText(policy_type);
        mStartDate.setText(start_date);
        mEndDate.setText(end_date);
        mPrice.setText(policy_price);
        mPaymentStatus.setText(payment_status);
        mPaymentRef.setText(payment_reference);
        mImeiNum.setText(imei);


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

                        if (networkConnection.isNetworkConnected(getBaseContext())) {

                            mRenew.setVisibility(View.GONE);
                            mProgress.setVisibility(View.VISIBLE);

                            //get client and call object for request
                            ApiInterface client = ServiceGenerator.createService(ApiInterface.class);


                            Call<RenewPolicyGet> call = client.renew_policy("Token " + userPreferences.getUserToken(), policynum, policy_price, "paystack");

                            call.enqueue(new Callback<RenewPolicyGet>() {
                                @Override
                                public void onResponse(Call<RenewPolicyGet> call, Response<RenewPolicyGet> response) {
                                    Log.i("ResponseCode", String.valueOf(response.code()));

                                    if (response.code() == 400) {
                                        showShortMsg("Check your internet connection");
                                        mRenew.setVisibility(View.VISIBLE);
                                        mProgress.setVisibility(View.GONE);
                                        return;
                                    } else if (response.code() == 429) {
                                        showShortMsg("Too many requests on database");
                                        mRenew.setVisibility(View.VISIBLE);
                                        mProgress.setVisibility(View.GONE);
                                        return;
                                    } else if (response.code() == 500) {
                                        showShortMsg("Server Error");
                                        mRenew.setVisibility(View.VISIBLE);
                                        mProgress.setVisibility(View.GONE);
                                        return;
                                    } else if (response.code() == 401) {
                                        showShortMsg("Unauthorized access, please try login again");
                                        mRenew.setVisibility(View.VISIBLE);
                                        mProgress.setVisibility(View.GONE);
                                        return;
                                    }
                                    try {
                                        if (!response.isSuccessful()) {

                                            try {
                                                APIError apiError = ErrorUtils.parseError(response);

                                                showShortMsg("Invalid Entry: " + apiError.getErrors());
                                                Log.i("Invalid EntryK", apiError.getErrors().toString());
                                                Log.i("Invalid Entry", response.errorBody().toString());

                                            } catch (Exception e) {
                                                Log.i("InvalidEntry", e.getMessage());
                                                Log.i("ResponseError", response.errorBody().string());
                                                showShortMsg("Failed to Renew" + e.getMessage());
                                                mRenew.setVisibility(View.VISIBLE);
                                                mProgress.setVisibility(View.GONE);

                                            }
                                            mRenew.setVisibility(View.VISIBLE);
                                            mProgress.setVisibility(View.GONE);
                                            return;
                                        }


                                        String amount = response.body().getAmount();
                                        String policyNumber = response.body().getPolicyNumber();
                                        String reference = response.body().getReference();


                                        Intent i = new Intent(MyAllRiskDetail.this, PolicyPaymentActivity.class);
                                        i.putExtra(Constant.POLICY_NUM, policyNumber);
                                        i.putExtra(Constant.TOTAL_PRICE, amount);
                                        i.putExtra(Constant.POLICY_TYPE, "all_risk");
                                        i.putExtra(Constant.REF, reference);
                                        startActivity(i);

                                        mRenew.setVisibility(View.VISIBLE);
                                        mProgress.setVisibility(View.GONE);

                                    } catch (Exception e) {
                                        Log.i("PolicyRenewError", e.getMessage());
                                        mRenew.setVisibility(View.VISIBLE);
                                        mProgress.setVisibility(View.GONE);
                                    }

                                }

                                @Override
                                public void onFailure(Call<RenewPolicyGet> call, Throwable t) {
                                    showShortMsg("Renewed Failed " + t.getMessage());
                                    Log.i("GetError", t.getMessage());
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

    private void renewPolicy(){


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
