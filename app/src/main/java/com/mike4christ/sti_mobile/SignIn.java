package com.mike4christ.sti_mobile;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.mike4christ.sti_mobile.Model.Auth.LoginModel.UserGetObj;
import com.mike4christ.sti_mobile.Model.Auth.LoginModel.UserPostData;
import com.mike4christ.sti_mobile.Model.Auth.LoginModel.UserPostObj;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.ServiceGenerator;
import com.mike4christ.sti_mobile.activity.Dashboard;
import com.mike4christ.sti_mobile.retrofit_interface.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mike4christ.sti_mobile.SignUp.isValidEmailAddress;


public class SignIn extends AppCompatActivity implements View.OnClickListener{

    /** ButterKnife Code **/
    @BindView(R.id.layout_signIn)
    FrameLayout mLayoutSignIn;
    @BindView(R.id.inputLayoutEmail)
    TextInputLayout mInputLayoutEmail;
    @BindView(R.id.email_editxt)
    EditText mEmailEditxt;
    @BindView(R.id.inputLayoutPassword)
    TextInputLayout mInputLayoutPassword;
    @BindView(R.id.password_editxt)
    EditText mPasswordEditxt;
    @BindView(R.id.signin_btn)
    Button mSigninBtn;
    @BindView(R.id.avi1)
    AVLoadingIndicatorView mAvi1;
    @BindView(R.id.register)
    TextView mRegister;
    /** ButterKnife Code **/

    UserPreferences userPreferences;


    String user_email="";
    NetworkConnection networkConnection=new NetworkConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        mRegister.setPaintFlags(mRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mRegister.setText("Click to Register");
        userPreferences = new UserPreferences(this);

        Intent intent=getIntent();
        user_email=intent.getStringExtra(Constant.USER_EMAIL);
        setUp();

        mSigninBtn.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    private void setUp(){
        mEmailEditxt.setText(user_email);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.signin_btn:
                ValidateForm();

                break;

            case R.id.register:
                startActivity(new Intent(SignIn.this,SignUp.class));
                finish();
                break;

        }

    }

    private void ValidateForm() {

        if (networkConnection.isNetworkConnected(this)) {
            boolean isValid = true;

            if (mEmailEditxt.getText().toString().isEmpty()) {
                mInputLayoutEmail.setError("Email is required!");
                isValid = false;
            } else if (!isValidEmailAddress(mEmailEditxt.getText().toString())) {
                mInputLayoutEmail.setError("Valid Email is required!");
                isValid = false;
            } else {
                mInputLayoutEmail.setErrorEnabled(false);
            }
            if (mPasswordEditxt.getText().toString().isEmpty()) {
                mInputLayoutPassword.setError("Password is required!");
                isValid = false;
            } else if (mPasswordEditxt.getText().toString().trim().length()<6 && mInputLayoutPassword.isClickable()) {
                mInputLayoutPassword.setError("Your Password must not less than 6 character");
                isValid = false;
            } else {
                mInputLayoutPassword.setErrorEnabled(false);
            }

            if (isValid) {

                //Post Request to Api

                sendData();
              }

          return;
        }
        showMessage("No Internet connection discovered!");
    }


    private void sendData(){
        mSigninBtn.setVisibility(View.GONE);
        mAvi1.setVisibility(View.VISIBLE);

        UserPostData userPostData=new UserPostData(mEmailEditxt.getText().toString(),
                mPasswordEditxt.getText().toString()

        );
        Log.i("UserObj",userPostData.toString());
        UserPostObj userPostObj=new UserPostObj(userPostData);
        Log.i("UserHead",userPostObj.toString());

        sentNetworkRequest(userPostObj);
    }

    private  void sentNetworkRequest(UserPostObj userPostObj){
        try {
            //To create retrofit instance
            HashMap hashMap = new HashMap();
            hashMap.put("Content-Type", "application/json;charset=UTF-8");

            //get client and call object for request
            ApiInterface client = ServiceGenerator.createService(ApiInterface.class);

            Call<UserGetObj> call = client.login(userPostObj);

            call.enqueue(new Callback<UserGetObj>() {
                @Override
                public void onResponse(Call<UserGetObj> call, Response<UserGetObj> response) {
                    try {
                        if (!response.isSuccessful()) {

                            try {
                                APIError apiError = ErrorUtils.parseError(response);

                                showMessage("Invalid Entry: " + apiError.getErrors());
                                Log.i("Invalid EntryK", apiError.getErrors().toString());
                                Log.i("Invalid Entry", response.errorBody().toString());

                            } catch (Exception e) {
                                Log.i("InvalidEntry", e.getMessage());
                                showMessage("Invalid Entry");

                            }
                            mSigninBtn.setVisibility(View.VISIBLE);
                            mAvi1.setVisibility(View.GONE);
                            return;
                        }

                        String user_email = response.body().getUser().getEmail();
                        String username = String.valueOf(response.body().getUser().getUsername());
                        String bio = String.valueOf(response.body().getUser().getBio());
                        String prof_img = response.body().getUser().getImage();
                        String phone_no = response.body().getUser().getPhone();
                        String pin = String.valueOf(response.body().getUser().getPin());
                        String firstname = response.body().getUser().getFirstName();
                        String lastname = response.body().getUser().getLastName();
                        String wallet_balance = response.body().getWalletBalance();
                        String bank = String.valueOf(response.body().getPayoutAccount().getBank());
                        String acct_name = String.valueOf(response.body().getPayoutAccount().getAccountName());
                        String acct_no = String.valueOf(response.body().getPayoutAccount().getAccountNumber());
                        String user_id = String.valueOf(response.body().getUser().getId());
                        String user_token = response.body().getUser().getToken();

                        Log.i("", response.body().getUser().getFirstName());
                        Log.i("Email", user_email);
                        Log.i("Wallet", response.body().getWalletBalance());
                        Log.i("Image", response.body().getUser().getImage());
                        Log.i("Token", response.body().getUser().getToken());
                        try {
                            userPreferences.setEmail(user_email);
                            userPreferences.setUsername(username);
                            userPreferences.setProfileImg(prof_img);
                            userPreferences.setPhoneNUM(phone_no);
                            userPreferences.setPin(pin);
                            userPreferences.setFirstName(firstname);
                            userPreferences.setLastName(lastname);
                            userPreferences.setWalletBalance(wallet_balance);
                            userPreferences.setBank(bank);
                            userPreferences.setAccountName(acct_name);
                            userPreferences.setAccountNumber(acct_no);
                            userPreferences.setUserId(user_id);
                            userPreferences.setUserToken(user_token);
                        }catch (Exception e){
                            Log.i("PrefError", e.getMessage());
                            mSigninBtn.setVisibility(View.VISIBLE);
                            mAvi1.setVisibility(View.GONE);
                        }

                        mSigninBtn.setVisibility(View.VISIBLE);
                        mAvi1.setVisibility(View.GONE);
                        if (user_email != null) {
                            Intent intent = new Intent(SignIn.this, Dashboard.class);
                            intent.putExtra(Constant.USER_FIRSTNAME, firstname);
                            intent.putExtra(Constant.USER_LASTNAME, lastname);
                            startActivity(intent);
                            finish();
                        } else {
                            showMessage("Error: " + response.body());
                        }
                    } catch (Exception e) {
                        showMessage("Login Failed: " + e.getMessage());
                        mSigninBtn.setVisibility(View.VISIBLE);
                        mAvi1.setVisibility(View.GONE);
                        Log.i("Login Failed", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UserGetObj> call, Throwable t) {
                    showMessage("Login Failed " + t.getMessage());
                    Log.i("GEtError", t.getMessage());
                    mSigninBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            Log.i("GenError", e.getMessage());
        }


    }

    private void showMessage(String s) {
        Snackbar.make(mLayoutSignIn, s, Snackbar.LENGTH_LONG).show();
    }
}
