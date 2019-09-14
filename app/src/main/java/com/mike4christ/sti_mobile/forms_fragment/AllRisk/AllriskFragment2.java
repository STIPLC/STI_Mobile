package com.mike4christ.sti_mobile.forms_fragment.AllRisk;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.snackbar.Snackbar;

import com.google.android.material.textfield.TextInputLayout;
import com.mike4christ.sti_mobile.BuildConfig;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.ServiceGenerator;
import com.mike4christ.sti_mobile.Model.Vehicle.Quote.PostVehicleData;
import com.mike4christ.sti_mobile.Model.Vehicle.Quote.QouteHead;
import com.mike4christ.sti_mobile.NetworkConnection;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.forms_fragment.MotorInsurance.MotorInsureFragment3;
import com.mike4christ.sti_mobile.retrofit_interface.ApiInterface;
import com.shuhart.stepview.StepView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllriskFragment2 extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.qb_form_layout2)
    FrameLayout mQbFormLayout2;
    @BindView(R.id.step_view)
    StepView mStepView;
    @BindView(R.id.item_type_spinner_a2)
    Spinner mItemTypeSpinnerA2;
    @BindView(R.id.inputLayoutItemDescriptn_a2)
    TextInputLayout mInputLayoutItemDescriptnA2;
    @BindView(R.id.item_desc_a2)
    EditText mItemDescA2;
    @BindView(R.id.inputLayoutStartDate_a2)
    TextInputLayout mInputLayoutStartDateA2;
    @BindView(R.id.start_date_a2)
    EditText mStartDateA2;
    @BindView(R.id.inputLayoutSerialNo_a2)
    TextInputLayout mInputLayoutSerialNoA2;
    @BindView(R.id.serial_num_a2)
    EditText mSerialNumA2;
    @BindView(R.id.inputLayoutItemValue_a2)
    TextInputLayout mInputLayoutItemValueA2;

    @BindView(R.id.item_imei_a2)
    EditText mImeiA2;
    @BindView(R.id.inputLayoutItemImei_a2)
    TextInputLayout mInputLayoutItemImeiA2;

    @BindView(R.id.item_value_a2)
    EditText mItemValueA2;
    @BindView(R.id.upload_receipt_btn2_a2)
    Button mUploadReceiptBtn2A2;
    @BindView(R.id.btn_layout2_a2)
    LinearLayout mBtnLayout2A2;
    @BindView(R.id.v_back_btn2_a2)
    Button mVBackBtn2A2;
    @BindView(R.id.v_next_btn2_a2)
    Button mVNextBtn2A2;
    @BindView(R.id.progressbar)
    AVLoadingIndicatorView mProgressbar;


    private int currentStep = 1;

 

    String itemTypeString,startDateStrg;
    DatePickerDialog datePickerDialog1;
    UserPreferences userPreferences;

    int PICK_IMAGE_RECEIPT = 1;
    int CAM_IMAGE_RECEIPT = 2;
    private String cameraFilePath;
    NetworkConnection networkConnection=new NetworkConnection();

    Uri receipt_info_img_uri;
    String receipt_img_url;

    public AllriskFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static AllriskFragment2 newInstance(String param1, String param2) {
        AllriskFragment2 fragment = new AllriskFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_allrisk2, container, false);
        ButterKnife.bind(this,view);
        //        mStepView next registration step
        

        mStepView.go(currentStep, true);
        userPreferences = new UserPreferences(getContext());

        init();

        itemtypeSpinner();


        setViewActions();
        showDatePicker();

        return  view;
    }


    private  void init(){
        UserPreferences userPreferences = new UserPreferences(getContext());

        //Temporal save and go to next Operation


        mItemDescA2.setText(userPreferences.getAllRiskItemDesc());

        mStartDateA2.setText(userPreferences.getAllRiskStartDate());

        mSerialNumA2.setText(userPreferences.getAllRiskSerialNo());

        mItemValueA2.setText(userPreferences.getAllRiskItemValue());

        mUploadReceiptBtn2A2.setOnClickListener(this);


    }


    private void itemtypeSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.select_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mItemTypeSpinnerA2.setAdapter(staticAdapter);

        mItemTypeSpinnerA2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String stringText = (String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mItemTypeSpinnerA2.getItemAtPosition(0);



            }
        });

    }


    //seting onclicks listeners
    private void setViewActions() {

        mVNextBtn2A2.setOnClickListener(this);
        mVBackBtn2A2.setOnClickListener(this);
        mStartDateA2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v_next_btn2_a2:
//                validate user input
                validateUserInputs();
                break;

            case R.id.start_date_a2:

                datePickerDialog1.show();
                break;

            case R.id.v_back_btn2_a2:
                if (currentStep > 0) {
                    currentStep--;
                }
                mStepView.done(false);
                mStepView.go(currentStep, true);

                Fragment allriskFragment1 = new AllriskFragment1();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_allrisk_form_container, allriskFragment1);
                ft.commit();

                break;

            case R.id.upload_receipt_btn2_a2:
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Mode of Entry");
// add a list
                String[] entry = {"Camera", "Gallery"};
                builder.setItems(entry, (dialog, option) -> {
                    switch (option) {
                        case 0:
                            // direct entry
                            chooseIdImage_camera();
                            dialog.dismiss();
                            break;

                        case 1: // export

                            chooseImageFile();
                            dialog.dismiss();

                            break;

                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                mUploadReceiptBtn2A2.setBackgroundColor(getResources().getColor(R.color.colorAccentEnds));

                break;
        }
    }


    private void chooseImageFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_RECEIPT);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }



    private void chooseIdImage_camera() {

        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", createImageFile()));
            startActivityForResult(intent, CAM_IMAGE_RECEIPT);
        } catch (IOException ex) {
            ex.printStackTrace();
            showMessage("Invalid Entry");
            Log.i("Invalid_Cam_Entry",ex.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            showMessage("No image is selected, try again");
            return;
        }


        showMessage("Uploading...");
        if (networkConnection.isNetworkConnected(getContext())) {
            Random random=new Random();
            String rand= String.valueOf(random.nextInt());
            if (requestCode == 1) {
                receipt_info_img_uri = data.getData();

                try {
                    if (receipt_info_img_uri != null) {
                        String name = mSerialNumA2.getText().toString()+rand;
                        if (name.equals("")) {
                            showMessage("Enter your Serial Number first");

                        } else {

                            String imageId = MediaManager.get().upload(Uri.parse(receipt_info_img_uri.toString()))
                                    .option("public_id", "user_registration/profile_photos/user_passport" + name)
                                    .unsigned("xbiscrhh").callback(new UploadCallback() {
                                        @Override
                                        public void onStart(String requestId) {
                                            // your code here
                                            mVNextBtn2A2.setVisibility(View.GONE);
                                            mProgressbar.setVisibility(View.VISIBLE);

                                        }

                                        @Override
                                        public void onProgress(String requestId, long bytes, long totalBytes) {
                                            // example code starts here
                                            Double progress = (double) bytes / totalBytes;
                                            // post progress to app UI (e.g. progress bar, notification)
                                            // example code ends here
                                            mProgressbar.setVisibility(View.VISIBLE);
                                            if(!networkConnection.isNetworkConnected(getContext())){
                                                mProgressbar.setVisibility(View.GONE);
                                                mVNextBtn2A2.setVisibility(View.VISIBLE);
                                                showMessage("Internet Connection Failed");
                                            }

                                        }

                                        @Override
                                        public void onSuccess(String requestId, Map resultData) {
                                            // your code here

                                            showMessage("Image Uploaded Successfully");
                                            Log.i("ImageRequestId ", requestId);
                                            Log.i("ImageUrl ", String.valueOf(resultData.get("url")));
                                            mProgressbar.setVisibility(View.GONE);
                                            mVNextBtn2A2.setVisibility(View.VISIBLE);
                                            receipt_img_url = String.valueOf(resultData.get("url"));


                                        }

                                        @Override
                                        public void onError(String requestId, ErrorInfo error) {
                                            // your code here
                                            showMessage("Error: " + error.toString());
                                            Log.i("Error: ", error.toString());

                                            mVNextBtn2A2.setVisibility(View.VISIBLE);
                                            mProgressbar.setVisibility(View.GONE);
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

            }else if(requestCode==2){
                receipt_info_img_uri = Uri.parse(cameraFilePath);

                try {
                    if (receipt_info_img_uri != null) {
                        String name = mSerialNumA2.getText().toString()+rand;
                        if (name.equals("")) {
                            showMessage("Enter your email address first");

                        } else {

                            String imageId = MediaManager.get().upload(receipt_info_img_uri)
                                    .option("public_id", "user_registration/profile_photos/user_passport" + name)
                                    .unsigned("xbiscrhh").callback(new UploadCallback() {
                                        @Override
                                        public void onStart(String requestId) {
                                            // your code here
                                            mVNextBtn2A2.setVisibility(View.GONE);
                                            mProgressbar.setVisibility(View.VISIBLE);

                                        }

                                        @Override
                                        public void onProgress(String requestId, long bytes, long totalBytes) {
                                            // example code starts here
                                            Double progress = (double) bytes / totalBytes;
                                            // post progress to app UI (e.g. progress bar, notification)
                                            // example code ends here
                                            mProgressbar.setVisibility(View.VISIBLE);
                                            if(!networkConnection.isNetworkConnected(getContext())){
                                                mProgressbar.setVisibility(View.GONE);
                                                mVNextBtn2A2.setVisibility(View.VISIBLE);
                                                showMessage("Internet Connection Failed");
                                            }

                                        }

                                        @Override
                                        public void onSuccess(String requestId, Map resultData) {
                                            // your code here

                                            showMessage("Image Uploaded Successfully");
                                            Log.i("ImageRequestId ", requestId);
                                            Log.i("ImageUrl ", String.valueOf(resultData.get("url")));
                                            mProgressbar.setVisibility(View.GONE);
                                            mVNextBtn2A2.setVisibility(View.VISIBLE);
                                            receipt_img_url = String.valueOf(resultData.get("url"));


                                        }

                                        @Override
                                        public void onError(String requestId, ErrorInfo error) {
                                            // your code here
                                            showMessage("Error: " + error.toString());
                                            Log.i("Error: ", error.toString());

                                            mVNextBtn2A2.setVisibility(View.VISIBLE);
                                            mProgressbar.setVisibility(View.GONE);
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



    private void validateUserInputs() {


        boolean isValid = true;

        if (mItemDescA2.getText().toString().isEmpty()) {
            mInputLayoutItemDescriptnA2.setError("Your Item Description is required!");

            isValid = false;
        } else if (mStartDateA2.getText().toString().isEmpty()) {
            mInputLayoutStartDateA2.setError("Start Date is required!");

            isValid = false;
        } else if (mSerialNumA2.getText().toString().isEmpty()) {
            mInputLayoutSerialNoA2.setError("Serial Number is required!");
            isValid = false;
        } else if (mItemValueA2.getText().toString().isEmpty()) {
            mInputLayoutItemValueA2.setError("Chasis Number is required!");

            isValid = false;
        } else {
            mInputLayoutItemValueA2.setErrorEnabled(false);
            mInputLayoutSerialNoA2.setErrorEnabled(false);
            mInputLayoutStartDateA2.setErrorEnabled(false);
            mInputLayoutItemDescriptnA2.setErrorEnabled(false);

        }
        itemTypeString = mItemTypeSpinnerA2.getSelectedItem().toString();
        if (itemTypeString.equals("Select Item")) {
            showMessage("Select Item");
            isValid = false;
        }


        if (isValid) {
//            send inputs to next next page
//            Goto to the next Registration step
            initFragment();
        }




    }

    private void initFragment() {
        mBtnLayout2A2.setVisibility(View.GONE);
        mProgressbar.setVisibility(View.VISIBLE);

        try {


            //Temporal save and go to next Operation

            userPreferences.setAllRiskItemDesc(mItemDescA2.getText().toString());
            userPreferences.setAllRiskStartDate(mStartDateA2.getText().toString());
            userPreferences.setAllRiskItemType(itemTypeString);
            userPreferences.setAllRiskSerialNo(mSerialNumA2.getText().toString());
            userPreferences.setAllRiskItemValue(mItemValueA2.getText().toString());
            userPreferences.setAllRiskItemImei(mImeiA2.getText().toString());
            userPreferences.setAllRiskItemReceipt(receipt_img_url);
            sendAllRiskData();

        }catch (Exception e){
            Log.i("Form Error",e.getMessage());
            mProgressbar.setVisibility(View.GONE);
            mVNextBtn2A2.setVisibility(View.VISIBLE);
            showMessage("Error: " + e.getMessage());
        }
    }

    private void sendAllRiskData(){


        //get client and call object for request
        ApiInterface client = ServiceGenerator.createService(ApiInterface.class);


        Call<QouteHead> call=client.allrisk_quote("Token "+userPreferences.getUserToken(),mItemValueA2.getText().toString());

        call.enqueue(new Callback<QouteHead>() {
            @Override
            public void onResponse(Call<QouteHead> call, Response<QouteHead> response) {
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
                            showMessage("Failed to Fetch Quote"+e.getMessage());
                            mBtnLayout2A2.setVisibility(View.VISIBLE);
                            mProgressbar.setVisibility(View.GONE);

                        }
                        mBtnLayout2A2.setVisibility(View.VISIBLE);
                        mProgressbar.setVisibility(View.GONE);
                        return;
                    }

                    String quote_price=response.body().getData().getPrice();
                    Log.i("quote_price",quote_price);
                    showMessage("Successfully Fetched Quote");
                    mBtnLayout2A2.setVisibility(View.VISIBLE);
                    mProgressbar.setVisibility(View.GONE);


                    // Fragment quoteBuyFragment3 = new MotorInsureFragment3();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_allrisk_form_container, AllriskFragment3.newInstance(userPreferences.getAllRiskItemType(),quote_price), AllriskFragment3.class.getSimpleName());
                    ft.commit();
                }catch (Exception e){
                    Log.i("policyResponse", e.getMessage());
                    mBtnLayout2A2.setVisibility(View.VISIBLE);
                    mProgressbar.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(Call<QouteHead> call, Throwable t) {
                showMessage("Submission Failed "+t.getMessage());
                Log.i("GEtError",t.getMessage());
                mBtnLayout2A2.setVisibility(View.VISIBLE);
                mProgressbar.setVisibility(View.GONE);
            }
        });

    }



    private void showMessage(String s) {
        Snackbar.make(mQbFormLayout2, s, Snackbar.LENGTH_LONG).show();
    }



    private void showDatePicker() {
        //Get current date
        Calendar calendar = Calendar.getInstance();

        //Create datePickerDialog with initial date which is current and decide what happens when a date is selected.
        datePickerDialog1 = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //When a date is selected, it comes here.
                //Change birthdayEdittext's text and dismiss dialog.
                if(year<calendar.get(Calendar.YEAR)){

                    showMessage("Invalid Start Date");
                    Log.i("Calendar",year+" "+calendar.get(Calendar.YEAR));
                    return;
                }
                int monthofYear=monthOfYear+1;
                startDateStrg = dayOfMonth + "-" + monthofYear + "-" + year;
                mStartDateA2.setText(startDateStrg);
                datePickerDialog1.dismiss();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

}
