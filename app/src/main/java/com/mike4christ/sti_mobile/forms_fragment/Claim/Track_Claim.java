package com.mike4christ.sti_mobile.forms_fragment.Claim;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.mike4christ.sti_mobile.BuildConfig;
import com.mike4christ.sti_mobile.Model.Claim.ClaimPost;
import com.mike4christ.sti_mobile.Model.Claim.TrackClaim.GetClaimStatus;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.ServiceGenerator;
import com.mike4christ.sti_mobile.NetworkConnection;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.retrofit_interface.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Track_Claim extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /** ButterKnife Code **/
    @BindView(R.id.track_claim_layout1)
    FrameLayout mTrackClaimLayout1;
    @BindView(R.id.polynum_type_spinner)
    Spinner mPolynumTypeSpinner;
    @BindView(R.id.search)
    Button mSearch;
    @BindView(R.id.progressbar)
   AVLoadingIndicatorView mProgressbar;
    /** ButterKnife Code **/

     /*"policy_id": "MOT/CV/06/19/SA/00297",
             "type": "vehicle",
             "description": "ghh\n",
             "cost_estimate": "2000.00",
             "loss_estimate": "20000000.00",
             "created_at": "2019-09-06 10:37:29",
             "updated_at": "2019-09-06 10:37:29",
             "status": "PENDING",
             "reference": "MOT/CV/06/19/SA/00297/2019/C00001",
             "date_loss": null,
             "sti_estimate": null*/



    String policyTypeString,policy_id,type,cost_estimate,loss_estimate,status,reference_code;

    NetworkConnection networkConnection=new NetworkConnection();

    UserPreferences userPreferences;


    public Track_Claim() {
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
    public static Track_Claim newInstance(String param1, String param2) {
        Track_Claim fragment = new Track_Claim();
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
        View view=inflater.inflate(R.layout.fragment_track_claim, container, false);
        ButterKnife.bind(this,view);
        userPreferences=new UserPreferences(getContext());


        ArrayList<String> policies = new ArrayList<String>(
                Arrays.asList("Claim Number","MOT/8767/CF", "MOV/5688/hsfgs", "TL/576223/hdgd77"));

        polyNumTypeSpinner(policies);

        setViewActions();

        return  view;
    }




    private void polyNumTypeSpinner(ArrayList<String> arrayList) {
        // Create an ArrayAdapter using the string array and a default spinner

        mPolynumTypeSpinner
                .setAdapter(new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayList));

        mPolynumTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String VehicleTypeString = (String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPolynumTypeSpinner.getItemAtPosition(0);
            }
        });

    }

    //seting onclicks listeners
    private void setViewActions() {

        mSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.search:
//                validate user input

                validateUserInputs();
                break;
        }
    }



    private void validateUserInputs() {

        boolean isValid = true;

            //Policy type Spinner
            policyTypeString = mPolynumTypeSpinner.getSelectedItem().toString();
            if (policyTypeString.equals("Policy Number")) {
                showMessage("Select your Policy Number");
                isValid = false;
            }


            if (isValid) {
//            send inputs to next next page
//            Goto to the next Registration step
                initFragment();
            }





    }



    private void initFragment() {
        mSearch.setVisibility(View.GONE);
        mProgressbar.setVisibility(View.VISIBLE);


        sendClaimData();




    }

    private void sendClaimData(){


        //get client and call object for request
        ApiInterface client = ServiceGenerator.createService(ApiInterface.class);

        Call<GetClaimStatus> call=client.track_claim("Token "+userPreferences.getUserToken(),policyTypeString);

        call.enqueue(new Callback<GetClaimStatus>() {
            @Override
            public void onResponse(Call<GetClaimStatus> call, Response<GetClaimStatus> response) {
                Log.i("ResponseCode", String.valueOf(response.code()));

                if(response.code()==406){
                    showMessage("Error! Wrong Policy Number provided!");
                    mSearch.setVisibility(View.VISIBLE);
                    mProgressbar.setVisibility(View.GONE);
                    return;
                }


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
                            mSearch.setVisibility(View.VISIBLE);
                            mProgressbar.setVisibility(View.GONE);

                        }
                        mSearch.setVisibility(View.VISIBLE);
                        mProgressbar.setVisibility(View.GONE);
                        return;
                    }

                    String status=response.body().getData().getClaim().getStatus();
                    showMessage("Claim Successfully Submitted");
                    Log.i("claim_response",claim_response);


                }catch (Exception e){
                    showMessage("Submission Error: " + e.getMessage());
                    Log.i("policyResponse", e.getMessage());
                    mSearch.setVisibility(View.VISIBLE);
                    mProgressbar.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(Call<GetClaimStatus> call, Throwable t) {
                showMessage("Submission Failed "+t.getMessage());
                mSearch.setVisibility(View.VISIBLE);
                mProgressbar.setVisibility(View.GONE);
                Log.i("GEtError",t.getMessage());
            }
        });




    }


    private void showMessage(String s) {
        Snackbar.make(mTrackClaimLayout1, s, Snackbar.LENGTH_LONG).show();
    }


}
