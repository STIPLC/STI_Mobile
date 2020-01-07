package com.mike4christ.sti_mobile.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.ProfileUpdate.ProfileImage.ProfileGetHead;
import com.mike4christ.sti_mobile.Model.ProfileUpdate.ProfileImage.ProfileImagePostHead;
import com.mike4christ.sti_mobile.Model.ProfileUpdate.ProfileImage.ProfileImageUser;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {


    @BindView(R.id.profile_lay)
    LinearLayout mProfileLay;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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
    /* @BindView(R.id.pin_profile_txt)
     TextView mPinProfileTxt;
     @BindView(R.id.bank)
     TextView mBank;
     @BindView(R.id.account_name)
     TextView mAccountName;
     @BindView(R.id.account_number)
     TextView mAccountNumber;*/
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
    @BindView(R.id.update_btn)
    Button mUpdateBtn;
    @BindView(R.id.avi1)
    AVLoadingIndicatorView mAvi1;


    String password,firstname,phone_num,lastname,username;

    int PICK_IMAGE_PASSPORT = 1;
    NetworkConnection networkConnection=new NetworkConnection();

    Uri profile_photo_img_uri;
    String personal_img_url;
    UserPreferences userPreferences;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        userPreferences = new UserPreferences(this);
        applyToolbarChildren("My Account");
        getUserProfile();

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
        if (resultCode == 0) {
            showMessage("No image is selected, try again");
            return;
        }


        if (networkConnection.isNetworkConnected(this)) {
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
                            mProfilePhoto.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), profile_photo_img_uri));


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


                                            Log.i("ImageRequestId ", requestId);
                                            Log.i("ImageUrl ", String.valueOf(resultData.get("url")));

                                            personal_img_url = String.valueOf(resultData.get("url"));
                                            updateImage(personal_img_url);

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

    private void updateImage(String url) {


        ProfileImageUser profileImageUser = new ProfileImageUser(url);
        ProfileImagePostHead profileImagePostHead = new ProfileImagePostHead(profileImageUser);

        //get client and call object for request
        ApiInterface client = ServiceGenerator.createService(ApiInterface.class);

        Call<ProfileGetHead> call = client.change_profile_image("Token " + userPreferences.getUserToken(), profileImagePostHead);

        call.enqueue(new Callback<ProfileGetHead>() {
            @Override
            public void onResponse(Call<ProfileGetHead> call, Response<ProfileGetHead> response) {
                Log.i("ResponseCode", String.valueOf(response.code()));


                if (response.code() == 400) {
                    showMessage("Check your internet connection");
                    mAvi1.setVisibility(View.GONE);
                    mUpdateBtn.setVisibility(View.VISIBLE);
                    return;
                } else if (response.code() == 429) {
                    showMessage("Too many requests on database");
                    mAvi1.setVisibility(View.GONE);
                    mUpdateBtn.setVisibility(View.VISIBLE);
                    return;
                } else if (response.code() == 500) {
                    showMessage("Server Error");
                    mAvi1.setVisibility(View.GONE);
                    mUpdateBtn.setVisibility(View.VISIBLE);
                    return;
                } else if (response.code() == 401) {
                    showMessage("Unauthorized access, please try login again");
                    mAvi1.setVisibility(View.GONE);
                    mUpdateBtn.setVisibility(View.VISIBLE);
                    return;
                }


                try {
                    if (!response.isSuccessful()) {

                        try {
                            APIError apiError = ErrorUtils.parseError(response);

                            showMessage("Invalid Entry: " + apiError.getErrors());
                            Log.i("Invalid EntryK", apiError.getErrors().toString());
                            Log.i("Invalid Entry", response.errorBody().toString());
                            mAvi1.setVisibility(View.GONE);
                            mUpdateBtn.setVisibility(View.VISIBLE);

                        } catch (Exception e) {
                            Log.i("InvalidEntry", e.getMessage());
                            Log.i("ResponseError", response.errorBody().string());
                            showMessage("Failed to Register" + e.getMessage());
                            mAvi1.setVisibility(View.GONE);
                            mUpdateBtn.setVisibility(View.VISIBLE);

                        }
                        mAvi1.setVisibility(View.GONE);
                        mUpdateBtn.setVisibility(View.VISIBLE);
                        return;
                    }

                    personal_img_url = response.body().getUser().getImage();
                    userPreferences.setProfileImg(personal_img_url);

                   /* if(personal_img_url==null) {
                        Glide.with(this).load(userPreferences.getProfileImg()).apply(new RequestOptions().fitCenter().circleCrop()).into(mProfilePhoto);
                    }else{
                        Glide.with(this).load(personal_img_url).apply(new RequestOptions().fitCenter().circleCrop()).into(mProfilePhoto);

                    }*/

                    showMessage("Image Uploaded Successfully");
                    mAvi1.setVisibility(View.GONE);
                    mUpdateBtn.setVisibility(View.VISIBLE);


                } catch (Exception e) {
                    showMessage("Upload Error: " + e.getMessage());
                    Log.i("Upload", e.getMessage());
                    mUpdateBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ProfileGetHead> call, Throwable t) {
                showMessage("Upload Failed, Try Again: " + t.getMessage());
                mUpdateBtn.setVisibility(View.VISIBLE);
                mAvi1.setVisibility(View.GONE);
                Log.i("GetError", t.getMessage());
            }
        });


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
            }
        });
    }

    private void validateUserInputs() {

        boolean isValid = true;

        if (mFirstnameEditxt.getText().toString().trim().isEmpty()) {
            mInputLayoutFirstnameP.setError("First Name is Required !");
            isValid = false;
        }else if(mLastnameEditxt.getText().toString().trim().isEmpty()){
            mInputLayoutLastnameP.setError("Last Name is Required !");
            isValid = false;
        }else if (mPhoneNumEditxt.getText().toString().trim().isEmpty()) {
            mInputLayoutPhoneNumP.setError("Phone number is Required!");
            isValid = false;
        } else if (mPhoneNumEditxt.getText().toString().trim().length() != 11) {
            mInputLayoutPhoneNumP.setError("Your Phone number must be 11 in length");
            isValid = false;
        }else if (mUsernameEditxt.getText().toString().trim().isEmpty()) {
            mInputLayoutUsername.setError("Invalid Entry !");
            isValid = false;
        }else {
            mInputLayoutFirstnameP.setErrorEnabled(false);
            mInputLayoutLastnameP.setErrorEnabled(false);
            mInputLayoutPhoneNumP.setErrorEnabled(false);
            mInputLayoutUsername.setErrorEnabled(false);

        }

        if (isValid) {
            if (networkConnection.isNetworkConnected(this)) {
                initFragment();
            }else {
                showMessage("No Internet Connection");
            }
        }





    }

    private void initFragment(){
        mUpdateBtn.setVisibility(View.GONE);
        mAvi1.setVisibility(View.VISIBLE);

        User user = new User(userPreferences.getEmail(), mPhoneNumEditxt.getText().toString(),
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

                            showMessage("Failed Entry: " + apiError.getErrors());
                            Log.i("Invalid EntryK",apiError.getErrors().toString());
                            Log.i("Invalid Entry",response.errorBody().toString());
                            mAvi1.setVisibility(View.GONE);
                            mUpdateBtn.setVisibility(View.VISIBLE);

                        }catch (Exception e){
                            Log.i("InvalidEntry",e.getMessage());
                            Log.i("ResponseError",response.errorBody().string());
                            showMessage("Failed to Update" + e.getMessage());
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

                    mProfilePhoto.setClickable(false);
                    mEditLayout.setVisibility(View.GONE);

                    mProfileDataLayout.setVisibility(View.VISIBLE);
                    mEditProf.setVisibility(View.VISIBLE);



                }catch (Exception e){
                    showMessage("Submission Error: " + e.getMessage());
                    Log.i("Submission", e.getMessage());
                    mUpdateBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(Call<UserGetUpdateHead> call, Throwable t) {
                showMessage("Update Failed " + t.getMessage());
                mUpdateBtn.setVisibility(View.VISIBLE);
                mAvi1.setVisibility(View.GONE);
                Log.i("GEtError",t.getMessage());
            }
        });

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


    private void showMessage(String s) {
        Snackbar.make(mProfileLay, s, Snackbar.LENGTH_SHORT).show();
    }

    private void getUserProfile() {
        //Getting profile from Pref
        mFirstname.setText("FirstName: "+userPreferences.getFirstName());
        mLastname.setText("LastName: "+userPreferences.getLastName());

        if(userPreferences.getUsername()==null){
            mUsernameTxt.setText("");
        }else{
            mUsernameTxt.setText(userPreferences.getUsername());
        }
        mEmail.setText("Email: "+userPreferences.getEmail());
        mPhoneNum.setText("Phone No: "+userPreferences.getPhoneNUM());


       /* mPinProfileTxt.setText("Pin: "+userPreferences.getPin());
        mBank.setText("Bank Name: "+userPreferences.getBank());
        mAccountName.setText("Acct Name: "+userPreferences.getAccountName());
        mAccountNumber.setText("Acct No: "+userPreferences.getAccountNumber());*/

        mProgressBarProfile.setVisibility(View.VISIBLE);
        if(personal_img_url==null) {
            Glide.with(this).load(userPreferences.getProfileImg()).apply(new RequestOptions().fitCenter().circleCrop()).into(mProfilePhoto);
        }else{
            Glide.with(this).load(personal_img_url).apply(new RequestOptions().fitCenter().circleCrop()).into(mProfilePhoto);

        }
        mProgressBarProfile.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

}



