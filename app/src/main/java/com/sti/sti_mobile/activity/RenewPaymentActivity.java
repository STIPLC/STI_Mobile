package com.sti.sti_mobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.sti.sti_mobile.Model.Errors.APIError;
import com.sti.sti_mobile.Model.Errors.ErrorUtils;
import com.sti.sti_mobile.Model.ServiceGenerator;
import com.sti.sti_mobile.NetworkConnection;
import com.sti.sti_mobile.R;
import com.sti.sti_mobile.UserPreferences;
import com.sti.sti_mobile.retrofit_interface.ApiInterface;

import org.json.JSONException;

import java.util.Calendar;

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

public class RenewPaymentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolBar;

    @BindView(R.id.inputLayoutCardNo)
    TextInputLayout inputLayoutCardNo;
    @BindView(R.id.inputLayoutCVV)
    TextInputLayout inputLayoutCVV;

    @BindView(R.id.inputLayoutAmount)
    TextInputLayout inputLayoutAmount;
    @BindView(R.id.inputLayoutPolicyNum)
    TextInputLayout inputLayoutPolicyNum;

    
    @BindView(R.id.edit_card_number)
    EditText cardNumberE;
    @BindView(R.id.policy_num)
    EditText policy_num;
    @BindView(R.id.edit_amount)
    EditText edit_amount;
    @BindView(R.id.mm_spinner)
    Spinner mmSpinner;
    @BindView(R.id.yy_spinner)
    Spinner yySpinner;
    @BindView(R.id.cvv)
    EditText cvvE;

    @BindView(R.id.pay_button)
    Button payButton;

    private Charge charge;
    ProgressDialog dialog;
    int amt;



    private UserPreferences userPreferences;
    NetworkConnection networkConnection=new NetworkConnection();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_payment);
        ButterKnife.bind(this);
        userPreferences=new UserPreferences(this);
        applyToolbarChildren("Renew Vehicle Policy");

        mm();
        yy();

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
                    }else if (edit_amount.getText().toString().isEmpty()) {
                        inputLayoutAmount.setError("Card Number is required!");
                        isValid = false;
                    }else if (policy_num.getText().toString().isEmpty()) {
                        inputLayoutPolicyNum.setError("Policy Number is required!");
                        isValid = false;
                    } else {
                        inputLayoutCardNo.setErrorEnabled(false);
                        inputLayoutAmount.setErrorEnabled(false);
                        inputLayoutPolicyNum.setErrorEnabled(false);
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

                        try {
                           amt = Integer.parseInt(edit_amount.getText().toString());
                        }catch (Exception e){
                            showShortMsg("Invalid Amount Entered");
                        }

                        Card card = new Card(cardNumber, expiryMonthInt, expiryYearInt, cvv);
                        if (card.isValid()) {
                            charge.setCard(card);
                            charge.setAmount(amt*100);
                            charge.setEmail(userPreferences.getEmail());
                            charge.setReference("Ref: " + Calendar.getInstance().getTimeInMillis() + "_" +userPreferences.getFirstName() + " " + userPreferences.getLastName() + "/" + userPreferences.getPhoneNUM());


                            PaystackSdk.chargeCard(RenewPaymentActivity.this, charge, new Paystack.TransactionCallback() {
                                @Override
                                public void onSuccess(Transaction transaction) {
                                    dismissDialog();
                                    payButton.setEnabled(false);
                                    // This is called only after transaction is deemed successful.
                                    // Retrieve the transaction, and send its reference to your server
                                    // for verification.
                                    sendRenewData();
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
                        }

                    }
                } else {
                    showShortMsg("Kindly, connect to the internet!");
                }


            }
        });


    }

    private void sendRenewData(){


        //get client and call object for request
        ApiInterface client = ServiceGenerator.createService(ApiInterface.class);

        Call<ResponseBody> call=client.renew_vehicle_policy("Token "+userPreferences.getUserToken(),policy_num.getText().toString(),edit_amount.getText().toString(),"paystack");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("ResponseCode", String.valueOf(response.code()));

                if(response.code()!=200){
                    showShortMsg("Error! Could not submit request!");
                    payButton.setEnabled(true);
                    return;
                }


                try {
                    if (!response.isSuccessful()) {

                        try{
                            APIError apiError= ErrorUtils.parseError(response);

                            Log.i("Invalid EntryK",apiError.getErrors().toString());
                            Log.i("Invalid Entry",response.errorBody().toString());

                        }catch (Exception e){
                            Log.i("InvalidEntry",e.getMessage());
                            Log.i("ResponseError",response.errorBody().string());
                            payButton.setEnabled(true);

                        }
                        payButton.setEnabled(true);
                        return;
                    }

                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    Toast.makeText(getApplicationContext(), "Transaction Successful, Please Hold on for Update!", Toast.LENGTH_LONG).show();


                }catch (Exception e){
                    Log.i("policyResponse", e.getMessage());
                    payButton.setEnabled(true);
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                payButton.setEnabled(true);
                Log.i("GEtError",t.getMessage());
            }
        });


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
        dialog = new ProgressDialog(RenewPaymentActivity.this);
        dialog.setMessage("Performing transaction... please wait");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void dismissDialog() {
        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
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
        finish();
        return super.onOptionsItemSelected(item);
    }
}
