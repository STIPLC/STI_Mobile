package com.sti.sti_mobile;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.sti.sti_mobile.Model.Auth.RegisterObj;
import com.sti.sti_mobile.Model.Auth.User;
import com.sti.sti_mobile.Model.Auth.UserDataHead;
import com.sti.sti_mobile.Model.Errors.APIError;
import com.sti.sti_mobile.Model.Errors.ErrorUtils;
import com.sti.sti_mobile.Model.ServiceGenerator;
import com.sti.sti_mobile.retrofit_interface.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    /** ButterKnife Code **/
    @BindView(R.id.layout_signUp)
    CoordinatorLayout mLayoutSignUp;
    @BindView(R.id.inputLayoutFirstName)
    TextInputLayout mInputLayoutFirstName;
    @BindView(R.id.firstname_editxt)
    EditText mFirstnameEditxt;
    @BindView(R.id.inputLayoutLastName)
    TextInputLayout mInputLayoutLastName;
    @BindView(R.id.lastname_editxt)
    EditText mLastnameEditxt;
    @BindView(R.id.inputLayoutPhoneNum)
    TextInputLayout mInputLayoutPhoneNum;
    @BindView(R.id.phonenum_editxt)
    EditText mPhonenumEditxt;
    @BindView(R.id.inputLayoutEmail)
    TextInputLayout mInputLayoutEmail;
    @BindView(R.id.email_editxt)
    EditText mEmailEditxt;
    @BindView(R.id.inputLayoutPassword)
    TextInputLayout mInputLayoutPassword;
    @BindView(R.id.password_editxt)
    EditText mPasswordEditxt;
    @BindView(R.id.signup_btn)
    Button mSignupBtn;
    @BindView(R.id.avi1)
    AVLoadingIndicatorView mAvi1;
    @BindView(R.id.login)
    TextView mLogin;
    /** ButterKnife Code **/

    NetworkConnection networkConnection=new NetworkConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        //Underline CLick to Login
        mLogin.setPaintFlags(mLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mLogin.setText("Click to Login");

        mSignupBtn.setOnClickListener(this);
        mLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.signup_btn:
                ValidateForm();
                break;
            case R.id.login:
                startActivity(new Intent(getApplicationContext(),SignIn.class));
                finish();
                break;

        }

    }


    private void ValidateForm() {

        if (networkConnection.isNetworkConnected(this)) {
            boolean isValid = true;
            if (mFirstnameEditxt.getText().toString().isEmpty()) {
                mInputLayoutFirstName.setError("First Name is required");
                isValid = false;
            } else {
                mInputLayoutFirstName.setErrorEnabled(false);
            }
            if (mLastnameEditxt.getText().toString().isEmpty()) {
                mInputLayoutLastName.setError("Last Name is required");
                isValid = false;
            } else {
                mInputLayoutLastName.setErrorEnabled(false);
            }

            if (mEmailEditxt.getText().toString().isEmpty()) {
                mInputLayoutEmail.setError("Email is required!");
                isValid = false;
            } else if (!isValidEmailAddress(mEmailEditxt.getText().toString())&&mInputLayoutEmail.isClickable()) {
                mInputLayoutEmail.setError("Valid Email is required!");
                isValid = false;
            } else {
                mInputLayoutEmail.setErrorEnabled(false);
            }
            if (mPasswordEditxt.getText().toString().isEmpty()&&mInputLayoutPassword.isClickable()) {
                mInputLayoutPassword.setError("Password is required!");
                isValid = false;
            } else if (mPasswordEditxt.getText().toString().trim().length()<6 && mInputLayoutPassword.isClickable()) {
                mInputLayoutPassword.setError("Your Password must not less than 6 character");
                isValid = false;
            } else {
                mInputLayoutPassword.setErrorEnabled(false);
            }
            if (mPhonenumEditxt.getText().toString().isEmpty()) {
                mInputLayoutPhoneNum.setError("Phone number is required");
                isValid = false;
            } else if (mPhonenumEditxt.getText().toString().trim().length() < 11&&mInputLayoutPhoneNum.isClickable()) {
                mInputLayoutPhoneNum.setError("Your Phone number must be 11 in length");
                isValid = false;
            } else {
                mInputLayoutPhoneNum.setErrorEnabled(false);
            }
           
            if (isValid) {

                //Post Request to Api

                sendData();


            }

            //
            return;
        }
        showMessage("No Internet connection discovered!");
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        if (null != email) {
            String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                result = false;
            }
        }

        return result;
    }

    private void sendData(){
        mSignupBtn.setVisibility(View.GONE);
        mAvi1.setVisibility(View.VISIBLE);

        User dataPart = new User(mFirstnameEditxt.getText().toString(), mLastnameEditxt.getText().toString().replace(" ", "-"),
                mEmailEditxt.getText().toString(),mPasswordEditxt.getText().toString(),mPhonenumEditxt.getText().toString());

        RegisterObj regPostData=new RegisterObj(dataPart);

        sentNetworkRequest(regPostData);

    }

    private  void sentNetworkRequest(RegisterObj regPostData){
        //To create retrofit instance

        HashMap hashMap= new HashMap();
        hashMap.put("Content-Type","application/json;charset=UTF-8");

        //get client and call object for request
        ApiInterface client = ServiceGenerator.createService(ApiInterface.class);

        Call<UserDataHead> call=client.register(regPostData,hashMap);

        call.enqueue(new Callback<UserDataHead>() {
            @Override
            public void onResponse(Call<UserDataHead> call, Response<UserDataHead> response) {

                if(response.code()==400){
                    showMessage("Check your internet connection");
                    mSignupBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                    return;
                }else if(response.code()==429){
                    showMessage("Too many requests on database");
                    mSignupBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                    return;
                }else if(response.code()==500){
                    showMessage("Server Error");
                    mSignupBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                    return;
                }else if(response.code()==401){
                    showMessage("Unauthorized access, please try login again");
                    mSignupBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                    return;
                }


                try {
                    if (!response.isSuccessful()) {

                        try{
                            APIError apiError= ErrorUtils.parseError(response);

                            showMessage("Invalid Entry: "+apiError.getErrors());
                            Log.i("Invalid EntryK",apiError.getErrors().toString());
                            Log.i("Invalid Entry",response.errorBody().toString());
                            mSignupBtn.setVisibility(View.VISIBLE);
                            mAvi1.setVisibility(View.GONE);

                        }catch (Exception e){
                            Log.i("InvalidEntry",e.getMessage());
                            showMessage("Failed to Register"+e.getMessage());
                            mSignupBtn.setVisibility(View.VISIBLE);
                            mAvi1.setVisibility(View.GONE);

                        }
                        mSignupBtn.setVisibility(View.VISIBLE);
                        mAvi1.setVisibility(View.GONE);
                        return;
                    }
                    String user_email = response.body().getUser().getEmail();

                    Log.i("response", user_email);

                    showMessage("ProfileImageUser ID: " + user_email);
                    Log.i("ProfileImageUser ID", response.body().getUser().toString());

                    mSignupBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                    if (user_email != null) {
                        Intent intent = new Intent(SignUp.this, SignIn.class);
                        intent.putExtra(Constant.USER_EMAIL, user_email);
                        startActivity(intent);
                        SignUp.this.finish();
                    } else {
                        showMessage("Error: " + response.body());
                    }
                }catch (Exception e){
                    showMessage("Registration Error: " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<UserDataHead> call, Throwable t) {
                showMessage("Registration Failed "+t.getMessage());
                Log.i("GEtError",t.getMessage());
            }
        });



    }

    
    private void showMessage(String s) {
        Snackbar.make(mLayoutSignUp, s, Snackbar.LENGTH_LONG).show();
    }



}
