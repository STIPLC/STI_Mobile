package com.mike4christ.sti_mobile.activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.MainActivity;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.ServiceGenerator;
import com.mike4christ.sti_mobile.Model.Swiss.QouteHeadSwiss;
import com.mike4christ.sti_mobile.NetworkConnection;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.forms_fragment.Swiss.SwissFragment3;
import com.mike4christ.sti_mobile.retrofit_interface.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyPaymentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolBar;
    
    @BindView(R.id.inputLayoutCardNo)
    TextInputLayout inputLayoutCardNo;
    @BindView(R.id.inputLayoutCVV)
    TextInputLayout inputLayoutCVV;


    @BindView(R.id.edit_card_number)
    EditText cardNumberE;
    @BindView(R.id.mm_spinner)
    Spinner mmSpinner;
    @BindView(R.id.yy_spinner)
    Spinner yySpinner;
    @BindView(R.id.cvv)
    EditText cvvE;

    @BindView(R.id.pay_button)
    Button payButton;
    @BindView(R.id.progress)
    AVLoadingIndicatorView mProgress;


    private Charge charge;
    ProgressDialog dialog;


    private UserPreferences userPreferences;
    NetworkConnection networkConnection=new NetworkConnection();

    @BindView(R.id.amount)
    TextView mAmount;
    @BindView(R.id.policy_num)
    TextView mPolicyNum;

    String amount="",poly_num="",ref="";
    String provider_ref="",poliy_type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_payment);
        ButterKnife.bind(this);
        userPreferences=new UserPreferences(this);
        applyToolbarChildren("Payment");

        

        Intent intent=getIntent();
        amount=intent.getStringExtra(Constant.TOTAL_PRICE);
        poly_num=intent.getStringExtra(Constant.POLICY_NUM);
        ref=intent.getStringExtra(Constant.REF);
        poliy_type=intent.getStringExtra(Constant.POLICY_TYPE);

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("en", "US"));
        nf.setMaximumFractionDigits(2);
        DecimalFormat df = (DecimalFormat) nf;
        String v_price = df.format(Double.valueOf(amount));


        String amt = getString(R.string.naira_currency) + " " + v_price;
        poly_num=getString(R.string.policy_no)+": "+poly_num;
        mAmount.setText(amt);
        mPolicyNum.setText(poly_num);

        mm();
        yy();

        if(ref!=null || !ref.equals("")) {
            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (networkConnection.isNetworkConnected(getBaseContext())) {
                        boolean isValid = true;

                        String expiryMonth = mmSpinner.getSelectedItem().toString();
                        String expiryYear = yySpinner.getSelectedItem().toString();

                        if (cardNumberE.getText().toString().isEmpty()) {
                            inputLayoutCardNo.setError("Card Number is required!");
                            isValid = false;
                        } else if (cardNumberE.getText().toString().length() < 15) {
                            inputLayoutCardNo.setError("Card Number must be 16 upward!");
                            isValid = false;
                        } else {
                            inputLayoutCardNo.setErrorEnabled(false);
                        }

                        if (cvvE.getText().toString().isEmpty()) {
                            inputLayoutCVV.setError("CVV is required!");
                            isValid = false;
                        } else {
                            inputLayoutCVV.setErrorEnabled(false);
                        }

                        if (expiryMonth.equals("MM") || expiryYear.equals("YY")) {
                            isValid = false;
                            showShortMsg("Kindly choose, the Month & Year of expiry");
                        }


                        if (isValid) {
                            payButton.setEnabled(false);
//                        payButton.setBackgroundColor(Color.RED);
                            charge = new Charge();
                            dialogMessage("Performing transaction... please wait!");

                            String cardNumber = cardNumberE.getText().toString();
                            String cvv = cvvE.getText().toString();// cvv of the test card
//                        String cardNumber = "5060666666666666666";
                            int expiryMonthInt = Integer.parseInt(expiryMonth); //any month in the future
                            int expiryYearInt = Integer.parseInt(expiryYear); // any year in the future.
//                        String cvv = "123";  // cvv of the test card


                            Card card = new Card(cardNumber, expiryMonthInt, expiryYearInt, cvv);
                            if (card.isValid()) {
                                charge.setCard(card);
                                int cost = (int) Math.round(Double.parseDouble(amount));

                                charge.setAmount(cost * 100);
                                charge.setEmail(userPreferences.getEmail());
                                charge.setReference(ref);


                                PaystackSdk.chargeCard(PolicyPaymentActivity.this, charge, new Paystack.TransactionCallback() {
                                    @Override
                                    public void onSuccess(Transaction transaction) {

                                        payButton.setEnabled(false);
                                        // This is called only after transaction is deemed successful.
                                        // Retrieve the transaction, and send its reference to your server
                                        // for verification.
                                        provider_ref=transaction.getReference();
                                        Log.i("provider_ref", provider_ref);
                                        showShortMsg("Transaction Successfully, Please hold on");

                                        payButton.setVisibility(View.GONE);
                                        mProgress.setVisibility(View.VISIBLE);

                                        //get client and call object for request
                                        ApiInterface client = ServiceGenerator.createService(ApiInterface.class);


                                        Call<ResponseBody> call=client.update_payment("Token "+userPreferences.getUserToken(),ref,provider_ref,"Success",poliy_type);

                                        call.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                Log.i("ResponseCode", String.valueOf(response.code()));

                                                if(response.code()==400){
                                                    showShortMsg("Check your internet connection");
                                                    payButton.setVisibility(View.VISIBLE);
                                                    mProgress.setVisibility(View.GONE);
                                                    return;
                                                }else if(response.code()==429){
                                                    showShortMsg("Too many requests on database");
                                                    payButton.setVisibility(View.VISIBLE);
                                                    mProgress.setVisibility(View.GONE);
                                                    return;
                                                }else if(response.code()==500){
                                                    showShortMsg("Server Error");
                                                    payButton.setVisibility(View.VISIBLE);
                                                    mProgress.setVisibility(View.GONE);
                                                    return;
                                                }else if(response.code()==401){
                                                    showShortMsg("Unauthorized access, please try login again");
                                                    payButton.setVisibility(View.VISIBLE);
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
                                                            showShortMsg("Failed to Update"+e.getMessage());
                                                            payButton.setVisibility(View.VISIBLE);
                                                            mProgress.setVisibility(View.GONE);

                                                        }
                                                        payButton.setVisibility(View.VISIBLE);
                                                        mProgress.setVisibility(View.GONE);
                                                        return;
                                                    }

                                                    String quote_price=response.body().string();
                                                    payButton.setVisibility(View.VISIBLE);
                                                    mProgress.setVisibility(View.GONE);

                                                    gotoDashboard();

                                                }catch (Exception e){
                                                    Log.i("PaymentUpdateError", e.getMessage());
                                                    payButton.setVisibility(View.VISIBLE);
                                                    mProgress.setVisibility(View.GONE);
                                                }

                                            }
                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                showShortMsg("Update Failed "+t.getMessage());
                                                Log.i("GEtError",t.getMessage());
                                                payButton.setVisibility(View.VISIBLE);
                                                mProgress.setVisibility(View.GONE);
                                            }
                                        });


                                         }

                                    @Override
                                    public void beforeValidate(Transaction transaction) {
                                        payButton.setEnabled(false);
//                                    payButton.setBackgroundColor(Color.RED);
                                        dialogMessage("Validating...please wait!");
                                        // This is called only before requesting OTP.
                                        // Save reference so you may send to server. If
                                        // error occurs with OTP, you should still verify on server.

                                        showShortMsg("loading...");
                                    }

                                    @Override
                                    public void onError(Throwable error, Transaction transaction) {
                                        payButton.setEnabled(true);
                                        dismissDialog();
                                        //handle error here
                                        showShortMsg("Failed: " + error.getMessage());
                                    }

                                });
                                // charge card
                                try {
                                    charge.putCustomField("Charged From", "Android SDK");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                payButton.setEnabled(true);
                                showShortMsg("Invalid card!");
                                dismissDialog();
                            }

                        }
                    } else {
                        showShortMsg("Kindly, connect to the internet!");
                    }


                }
            });
        }else{
            showShortMsg("Payment Reference is Null");
        }

    }

    private void applyToolbarChildren(String title) {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
        //setting Elevation for > API 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolBar.setElevation(10f);
        }

    }



    private void mm(){
        // MM Spinner
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> mmAdapter = ArrayAdapter
                .createFromResource(this, R.array.mm_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        mmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mmSpinner.setAdapter(mmAdapter);

        mmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //String stringTextLevel = (String) parent.getItemAtPosition(position);
                //electedView = view;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void yy(){
// YY Spinner
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> yyAdapter = ArrayAdapter
                .createFromResource(this, R.array.yy_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        yyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        yySpinner.setAdapter(yyAdapter);

        yySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //String stringTextLevel = (String) parent.getItemAtPosition(position);
                //electedView = view;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    private void showShortMsg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void dialogMessage(String s) {
        dialog = new ProgressDialog(PolicyPaymentActivity.this);
        dialog.setMessage("Performing transaction... please wait");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void dismissDialog() {
        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    private void gotoDashboard() {
        startActivity(new Intent(PolicyPaymentActivity.this, Dashboard.class));
        this.finish();
        Toast.makeText(getApplicationContext(), "Transaction Successful, Check Your Policy", Toast.LENGTH_LONG).show();


    }


    @Override
    public void onPause() {
        super.onPause();

        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PolicyPaymentActivity.this, Dashboard.class));
        this.finish();
        super.onBackPressed();
    }
}
