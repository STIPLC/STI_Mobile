package com.mike4christ.sti_mobile.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.Pin.setPin;
import com.mike4christ.sti_mobile.Model.ProfileUpdate.User;
import com.mike4christ.sti_mobile.Model.ProfileUpdate.UserEditHead;
import com.mike4christ.sti_mobile.Model.ProfileUpdate.UserGetUpdateHead;
import com.mike4christ.sti_mobile.Model.ServiceGenerator;
import com.mike4christ.sti_mobile.NetworkConnection;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.retrofit_interface.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public  class ProfileFragment extends Fragment {


    @BindView(R.id.profile_lay)
    LinearLayout mProfileLay;
    @BindView(R.id.relative_layout_photo)
    RelativeLayout mRelativeLayoutPhoto;
    @BindView(R.id.profile_photo)
    CircleImageView mProfilePhoto;
    @BindView(R.id.edit_prof)
    ImageView mEditProf;
    @BindView(R.id.username_txt)
    TextView mUsernameTxt;
    @BindView(R.id.progressBar_profile)
    ProgressBar mProgressBarProfile;
    @BindView(R.id.profile_data_layout)
    ScrollView mProfileDataLayout;
    @BindView(R.id.firstname)
    TextView mFirstname;
    @BindView(R.id.lastname)
    TextView mLastname;
    @BindView(R.id.email)
    TextView mEmail;
    @BindView(R.id.phone_num)
    TextView mPhoneNum;
    @BindView(R.id.pin_profile_txt)
    TextView mPinProfileTxt;
    @BindView(R.id.bank)
    TextView mBank;
    @BindView(R.id.account_name)
    TextView mAccountName;
    @BindView(R.id.account_number)
    TextView mAccountNumber;
    @BindView(R.id.edit_layout)
    ScrollView mEditLayout;
    @BindView(R.id.inputLayoutFirstnameP)
    TextInputLayout mInputLayoutFirstnameP;
    @BindView(R.id.firstname_editxt)
    EditText mFirstnameEditxt;
    @BindView(R.id.inputLayoutLastnameP)
    TextInputLayout mInputLayoutLastnameP;
    @BindView(R.id.lastname_editxt)
    EditText mLastnameEditxt;
    @BindView(R.id.inputLayoutUsername)
    TextInputLayout mInputLayoutUsername;
    @BindView(R.id.username_editxt)
    EditText mUsernameEditxt;
    @BindView(R.id.inputLayoutPhone_NumP)
    TextInputLayout mInputLayoutPhoneNumP;
    @BindView(R.id.phone_num_editxt)
    EditText mPhoneNumEditxt;
    @BindView(R.id.inputLayoutPassword)
    TextInputLayout inputLayoutPassword;
    @BindView(R.id.password_editxt)
    EditText password_editxt;
    @BindView(R.id.update_btn)
    Button mUpdateBtn;
    @BindView(R.id.avi1)
    AVLoadingIndicatorView mAvi1;

    private Uri imageUri;
    String password,firstname,phone_num,lastname,username;

    int PICK_IMAGE_PASSPORT = 1;
    NetworkConnection networkConnection=new NetworkConnection();

    Uri profile_photo_img_uri;
    String personal_img_url;
    UserPreferences userPreferences;


    public ProfileFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, fragmentView);
        userPreferences=new UserPreferences(getContext());
        getUserProfile();
        return fragmentView;
    }



    @OnClick(R.id.edit_prof)
    public void showEditProfile() {

        editProfile();

    }


    private void chooseImageFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_PASSPORT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0 || data == null || data.getData() == null) {
            showMessage("No image is selected, try again");
            return;
        }




        showMessage(String.valueOf(requestCode));
        if (networkConnection.isNetworkConnected(getContext())) {
            Random random=new Random();
            String rand= String.valueOf(random.nextInt());
            if (requestCode == 1) {
                profile_photo_img_uri = data.getData();

                try {
                    if (profile_photo_img_uri != null) {
                        String name = "profile_photo"+rand;
                        if (name.equals("")) {
                            showMessage("Try again");

                        } else {
                            mProfilePhoto.setImageBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), profile_photo_img_uri));


                            String imageId = MediaManager.get().upload(Uri.parse(profile_photo_img_uri.toString()))
                                    .option("public_id", "user_registration/profile_photos/user_passport" + name)
                                    .unsigned("xbiscrhh").callback(new UploadCallback() {
                                        @Override
                                        public void onStart(String requestId) {
                                            // your code here
                                            mUpdateBtn.setVisibility(View.GONE);
                                            mAvi1.setVisibility(View.VISIBLE);

                                        }

                                        @Override
                                        public void onProgress(String requestId, long bytes, long totalBytes) {
                                            // example code starts here
                                            Double progress = (double) bytes / totalBytes;
                                            // post progress to app UI (e.g. progress bar, notification)
                                            // example code ends here
                                            mAvi1.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onSuccess(String requestId, Map resultData) {
                                            // your code here

                                            showMessage("Image Uploaded Successfully");
                                            Log.i("ImageRequestId ", requestId);
                                            Log.i("ImageUrl ", String.valueOf(resultData.get("url")));
                                            mAvi1.setVisibility(View.GONE);
                                            mUpdateBtn.setVisibility(View.VISIBLE);
                                            personal_img_url = String.valueOf(resultData.get("url"));


                                        }

                                        @Override
                                        public void onError(String requestId, ErrorInfo error) {
                                            // your code here
                                            showMessage("Error: " + error.toString());
                                            Log.i("Error: ", error.toString());

                                            mUpdateBtn.setVisibility(View.VISIBLE);
                                            mAvi1.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onReschedule(String requestId, ErrorInfo error) {
                                            // your code here
                                        }
                                    })
                                    .dispatch();

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("Please Check your Image");

                }

            }
            return;
        }
        showMessage("No Internet connection discovered!");
    }

    private void editProfile() {
        mProfileDataLayout.setVisibility(View.GONE);
        mEditProf.setVisibility(View.GONE);
        mProfilePhoto.setClickable(true);
        mEditLayout.setVisibility(View.VISIBLE);


            mPhoneNumEditxt.setText(userPreferences.getPhoneNUM());
            mUsernameEditxt.setText(userPreferences.getUsername());
            mFirstnameEditxt.setText(userPreferences.getFirstName());
            mLastnameEditxt.setText(userPreferences.getLastName());


        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFile();
            }
        });



        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request for Post Request for Profile Update
                validateUserInputs();

                mProfilePhoto.setClickable(false);
                mEditLayout.setVisibility(View.GONE);

                mProfileDataLayout.setVisibility(View.VISIBLE);
                mEditProf.setVisibility(View.VISIBLE);

            }
        });
    }

    private void validateUserInputs() {

        boolean isValid = true;

        if (mFirstnameEditxt.getText().toString().trim().isEmpty()) {
            mInputLayoutFirstnameP.setError("First Name is Required !");
            isValid = false;
        }else if(mLastnameEditxt.getText().toString().trim().isEmpty()){
            mInputLayoutLastnameP.setError("First Name is Required !");
            isValid = false;
        }else if (mPhoneNumEditxt.getText().toString().trim().isEmpty()) {
            mInputLayoutPhoneNumP.setError("Password is Required!");
            isValid = false;
        }else if (mPhoneNumEditxt.getText().toString().trim().length() < 11||mPhoneNumEditxt.getText().toString().trim().length() >11) {
            mInputLayoutPhoneNumP.setError("Your Phone number must be 11 in length");
            isValid = false;
        }else if (password_editxt.getText().toString().trim().length() < 11||mPhoneNumEditxt.getText().toString().trim().length() >11) {
            mInputLayoutPhoneNumP.setError("Your Phone number must be 11 in length");
            isValid = false;
        }else if (password_editxt.getText().toString().isEmpty()) {
            inputLayoutPassword.setError("Password is required!");
            isValid = false;
        } else if (password_editxt.getText().toString().trim().length()<6 ) {
            inputLayoutPassword.setError("Your Password must not less than 6 character");
            isValid = false;
        }else if (mUsernameEditxt.getText().toString().trim().isEmpty()) {
            mInputLayoutUsername.setError("Invalid Entry !");
            isValid = false;
        }else {
            mInputLayoutFirstnameP.setErrorEnabled(false);
            mInputLayoutLastnameP.setErrorEnabled(false);
            mInputLayoutPhoneNumP.setErrorEnabled(false);
            mInputLayoutUsername.setErrorEnabled(false);
            inputLayoutPassword.setErrorEnabled(false);
        }

        if (isValid) {
            if(networkConnection.isNetworkConnected(getContext())) {
                initFragment();
            }else {
                showMessage("No Internet Connection");
            }
        }





    }

    private void initFragment(){
        mUpdateBtn.setVisibility(View.GONE);
        mAvi1.setVisibility(View.VISIBLE);

        User user=new User(userPreferences.getEmail(),mPhoneNumEditxt.getText().toString(),password_editxt.getText().toString(),
                mUsernameEditxt.getText().toString(),mFirstnameEditxt.getText().toString(),mLastnameEditxt.getText().toString());
        UserEditHead userEditHead=new UserEditHead(user);
        updateUserDate(userEditHead);


    }


    private void updateUserDate(UserEditHead userEditHead){


        //get client and call object for request
        ApiInterface client = ServiceGenerator.createService(ApiInterface.class);

        Call<UserGetUpdateHead> call=client.update_profile("Token "+userPreferences.getUserToken(),userEditHead);

        call.enqueue(new Callback<UserGetUpdateHead>() {
            @Override
            public void onResponse(Call<UserGetUpdateHead> call, Response<UserGetUpdateHead> response) {
                Log.i("ResponseCode", String.valueOf(response.code()));

                try {
                    if (!response.isSuccessful()) {

                        try{
                            APIError apiError= ErrorUtils.parseError(response);

                            showMessage("Invalid Entry: "+apiError.getErrors());
                            Log.i("Invalid EntryK",apiError.getErrors().toString());
                            Log.i("Invalid Entry",response.errorBody().toString());

                        }catch (Exception e){
                            Log.i("InvalidEntry",e.getMessage());
                            Log.i("ResponseError",response.errorBody().string());
                            showMessage("Failed to Register"+e.getMessage());
                            mUpdateBtn.setVisibility(View.VISIBLE);
                            mAvi1.setVisibility(View.GONE);

                        }
                        mUpdateBtn.setVisibility(View.VISIBLE);
                        mAvi1.setVisibility(View.GONE);
                        return;
                    }

                    firstname=response.body().getUser().getFirstName();
                    lastname=response.body().getUser().getLastName();
                    username=response.body().getUser().getUsername();
                    phone_num=response.body().getUser().getPhone();

                    userPreferences.setFirstName(firstname);
                    userPreferences.setLastName(lastname);
                    userPreferences.setUsername(username);
                    userPreferences.setPhoneNUM(phone_num);

                    mUpdateBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);

                    showMessage("Profile Updated Successfully");
                    getUserProfile();



                }catch (Exception e){
                    showMessage("Submission Error: " + e.getMessage());
                    Log.i("Submission", e.getMessage());
                    mUpdateBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(Call<UserGetUpdateHead> call, Throwable t) {
                showMessage("Submission Failed "+t.getMessage());
                mUpdateBtn.setVisibility(View.VISIBLE);
                mAvi1.setVisibility(View.GONE);
                Log.i("GEtError",t.getMessage());
            }
        });

    }



    private void showMessage(String s) {
        Snackbar.make(mProfileLay, s, Snackbar.LENGTH_SHORT).show();
    }

    private void getUserProfile() {
        //Getting profile from Pref
        mFirstname.setText("FirstName: "+userPreferences.getFirstName());
        mLastname.setText("LastName: "+userPreferences.getLastName());
        if(userPreferences.getUsername()=="null"){
            mUsernameTxt.setText("");
        }else{
            mUsernameTxt.setText(userPreferences.getUsername());
        }
        mEmail.setText("Email: "+userPreferences.getEmail());
        mPhoneNum.setText("Phone No: "+userPreferences.getPhoneNUM());
        mPinProfileTxt.setText("Pin: "+userPreferences.getPin());
        mBank.setText("Bank Name: "+userPreferences.getBank());
        mAccountName.setText("Acct Name: "+userPreferences.getAccountName());
        mAccountNumber.setText("Acct No: "+userPreferences.getAccountNumber());

        mProgressBarProfile.setVisibility(View.VISIBLE);
        if(personal_img_url==null) {
            Glide.with(getContext()).load(userPreferences.getProfileImg()).apply(new RequestOptions().fitCenter().circleCrop()).into(mProfilePhoto);
        }else{
            Glide.with(getContext()).load(personal_img_url).apply(new RequestOptions().fitCenter().circleCrop()).into(mProfilePhoto);

        }
        mProgressBarProfile.setVisibility(View.GONE);
    }



}
