package com.mike4christ.sti_mobile.forms_fragment.MotorInsurance;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.Model.Auth.RegisterObj;
import com.mike4christ.sti_mobile.Model.Auth.UserDataHead;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.ServiceGenerator;
import com.mike4christ.sti_mobile.Model.Vehicle.Personal_detail;
import com.mike4christ.sti_mobile.Model.Vehicle.VehicleDetails;
import com.mike4christ.sti_mobile.Model.Vehicle.VehiclePictures;
import com.mike4christ.sti_mobile.Model.Vehicle.VehiclePolicy;
import com.mike4christ.sti_mobile.Model.Vehicle.VehiclePost.Persona;
import com.mike4christ.sti_mobile.Model.Vehicle.VehiclePost.Vehicle;
import com.mike4christ.sti_mobile.Model.Vehicle.VehiclePost.VehiclePostHead;
import com.mike4christ.sti_mobile.NetworkConnection;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.SignIn;
import com.mike4christ.sti_mobile.SignUp;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.adapter.VehiclesListAdapter;
import com.mike4christ.sti_mobile.retrofit_interface.ApiInterface;
import com.shuhart.stepview.StepView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mike4christ.sti_mobile.SignUp.isValidEmailAddress;

class MotorInsureFragment5 extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match

    private static final String PRIMARY_KEY = "primaryKey";
    private static final String FOREIGN_KEY = "foreignKey";


    // TODO: Rename and change types of parameters
    private String primaryKey,foreignKey;


    @BindView(R.id.step_view)
    StepView stepView;

    @BindView(R.id.v_next_btn2)
    Button v_next_btn;

    @BindView(R.id.v_back_btn2)
    Button v_back_btn;

    @BindView(R.id.personal_info)
    TextView personal_info;

    @BindView(R.id.inputLayoutPin_m5)
    TextInputLayout inputLayoutPin_m5;

    @BindView(R.id.pin_txt_m5)
    EditText pin_txt_m5;

    @BindView(R.id.modeOfPayment_spinner_m5)
    Spinner modeOfPayment_spinner_m5;

    @BindView(R.id.qb_form_layout3)
    FrameLayout qb_form_layout4;

    @BindView(R.id.btn_layout3)
    LinearLayout btn_layout3;

    @BindView(R.id.progressbar)
    AVLoadingIndicatorView progressbar;


    private  int currentStep=4;
    Realm realm;
    VehiclesListAdapter vehiclesListAdapter;
    String modeofPaymentString;
    UserPreferences userPreferences;
    VehiclePolicy vehiclePolicy;
    RealmList<Personal_detail> personal_details;

    String total_quoteprice;

    List<Vehicle> vehiclesList_post=new ArrayList<Vehicle>();

    List<String> vehiclePictureList_post=new ArrayList<>();

    NetworkConnection networkConnection=new NetworkConnection();




    public MotorInsureFragment5() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment Fragment_Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static MotorInsureFragment5 newInstance(String param1) {
        MotorInsureFragment5 fragment = new MotorInsureFragment5();
        Bundle args = new Bundle();
        args.putString(PRIMARY_KEY,param1);
        //args.putString(FOREIGN_KEY,param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            primaryKey = getArguments().getString(PRIMARY_KEY);
            //foreignKey = getArguments().getString(FOREIGN_KEY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_motor_insured5, container, false);
        ButterKnife.bind(this,view);
        //  stepView next registration step
        stepView.go(currentStep, true);
        realm= Realm.getDefaultInstance();


        init();
        modeofPaymentSpinner();
        setViewActions();

        return  view;
    }
//Mode of Payment

    private void modeofPaymentSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.mode_of_payment,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        modeOfPayment_spinner_m5.setAdapter(staticAdapter);

        modeOfPayment_spinner_m5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String modeofPaymentTypeString = (String) parent.getItemAtPosition(position);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                modeOfPayment_spinner_m5.getItemAtPosition(0);
            }
        });

    }

    private void init(){

        userPreferences=new UserPreferences(getContext());

        //retrieve data for personal detail first


        vehiclePolicy=realm.where(VehiclePolicy.class).equalTo("id",primaryKey).findFirst();
        total_quoteprice=vehiclePolicy.getQuote_price();
        personal_details=vehiclePolicy.getPersonal_info();




        if(userPreferences.getMotorPtype().equals("Corporate")){

            String corperate="Comapany Name: "+personal_details.get(0).getCompany_name()+"\n"+"TIN Number: "+personal_details.get(0).getTin_number()+"\n"+
                    "\n"+"Phone Number: "+personal_details.get(0).getPhone()+"\n"+
                    "Office Address: "+personal_details.get(0).getOffice_address()+"\n"+"Contact Person: "+personal_details.get(0).getContact_person()+"\n"+
                    "\n"+"Email Address: "+personal_details.get(0).getEmail()+"\n"+
                    "Total Premium: "+total_quoteprice;
            personal_info.setText(corperate);

        }else if (userPreferences.getMotorPtype().equals("Individual")){
            String individual="Prefix: "+personal_details.get(0).getPrefix()+"\n"+"First Name: "+personal_details.get(0).getFirst_name()+"\n"+
                    "Last Name: "+personal_details.get(0).getLast_name()+"\n"+"Phone Number: "+personal_details.get(0).getPhone()+"\n"+
                    "Gender: "+personal_details.get(0).getResident_address()+"\n"+"Mailing Address: "+personal_details.get(0).getMailing_address()+"\n"+
                    "Total Premium: "+total_quoteprice;
            personal_info.setText(individual);

        }




    }



    //seting onclicks listeners
    private void setViewActions() {

        v_next_btn.setOnClickListener(this);
        v_back_btn.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v_next_btn2:
//                send quote to client and sti
                ValidateForm();
                break;

            case R.id.v_back_btn2:

                stepView.go(1, true);

                Fragment quoteBuyFragment2 = new MotorInsureFragment2();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_motor_form_container, quoteBuyFragment2);
                ft.commit();

                break;
        }
    }


    @OnClick(R.id.fabShowVehicles)
    public void showBottomVehicleList() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_vehicles, null);
        final TextView textView = (TextView) view.findViewById(R.id.detail);
        final RecyclerView recycler_vehicles = (RecyclerView) view.findViewById(R.id.recycler_detail);
        final ImageView close = (ImageView) view.findViewById(R.id.close);
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stepView.go(5, true);
                dialog.dismiss();
            }
        });

        //retrieve data
        final RealmResults<VehicleDetails> results;
        //String title;
        results=realm.where(VehicleDetails.class).findAll();

        //VehiclePictures
        //retrieve data
        final RealmResults<VehiclePictures> picture_results;
        //String title;
        picture_results=realm.where(VehiclePictures.class).findAll();


        Log.i("VPictures",picture_results.toString());
        Log.i("VPicturesSize", String.valueOf(picture_results.size()));

        if(results==null){
            showMessage("REsult is null");
        }

        //Bind
        vehiclesListAdapter=new VehiclesListAdapter(results,getActivity().getBaseContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getBaseContext(),RecyclerView.VERTICAL,false);
        recycler_vehicles.setLayoutManager(linearLayoutManager);
        recycler_vehicles.setAdapter(vehiclesListAdapter);



        dialog.setContentView(view);
        dialog.show();
    }

    private void ValidateForm() {

        if (networkConnection.isNetworkConnected(getContext())) {
            boolean isValid = true;

            if (pin_txt_m5.getText().toString().isEmpty()) {
                inputLayoutPin_m5.setError("Pin is required!");
                isValid = false;
            } else {
                inputLayoutPin_m5.setErrorEnabled(false);
            }
            //Prefix Spinner
            modeofPaymentString = modeOfPayment_spinner_m5.getSelectedItem().toString();
            if (modeofPaymentString.equals("Mode of Payment")) {
                showMessage("Select your mode of payment");
                isValid = false;
            }

            if (isValid) {

                //Post Request to Api

                mSubmit();
            }

            return;
        }
        showMessage("No Internet connection discovered!");
    }

    private void mSubmit() {

        btn_layout3.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);

        //retrieve data
        final RealmResults<VehicleDetails> results;

        //String title;
        results=realm.where(VehicleDetails.class).findAll();

        //VehiclePictures
        //retrieve data
        final RealmResults<VehiclePictures> picture_results;
        //String title;
        picture_results=realm.where(VehiclePictures.class).findAll();

        Log.i("pix_result",results.toString());



        if(userPreferences.getMotorPtype().equals("Corporate")){

            Persona persona=new Persona("null","null",personal_details.get(0).getEmail(),"null",personal_details.get(0).getPhone(),"null",
                    "null","null",personal_details.get(0).getPicture(),"null","null","null","null",personal_details.get(0).getCompany_name(),
                    personal_details.get(0).getOffice_address(),"2",personal_details.get(0).getCompany_name(),"null",personal_details.get(0).getTin_number(),
                    personal_details.get(0).getOffice_address(),personal_details.get(0).getContact_person());

            for(int i=0;i<results.size();i++) {

                VehiclePictures vehiclePictures= results.get(i).getVehiclePictures();

                vehiclePictureList_post.add(vehiclePictures.getFront_view());
                vehiclePictureList_post.add(vehiclePictures.getBack_view());
                vehiclePictureList_post.add(vehiclePictures.getLeft_view());
                vehiclePictureList_post.add(vehiclePictures.getRight_view());

            }


            for(int i=0;i<results.size();i++) {
                VehicleDetails vehicleDetails=results.get(i);
                Vehicle vehicle=new Vehicle(vehicleDetails.getPeriod(),vehicleDetails.getPolicy_type(),vehicleDetails.getEnhanced_third_party(),
                        vehicleDetails.getPrivate_policy(),vehicleDetails.getMotor_cycle_policy(),vehicleDetails.getVehicle_make(),
                        vehicleDetails.getBody_type(),vehicleDetails.getYear(),"null",vehicleDetails.getRegistration_number(),
                        vehicleDetails.getChasis_number(),vehicleDetails.getEngine_number(),vehicleDetails.getVehicle_value(),vehiclePictureList_post);
                vehiclesList_post.add(vehicle);

                Log.i("policyLoop",vehicleDetails.getPolicy_type());


            }

            VehiclePostHead vehiclePostHead=new VehiclePostHead(persona,vehiclesList_post,total_quoteprice,
                    pin_txt_m5.getText().toString(),modeofPaymentString,userPreferences.getUserId());

            Log.i("policyPostPix1",vehiclePictureList_post.toString());
            Log.i("policyPostVec1",vehiclesList_post.toString());

            sendPolicy(vehiclePostHead);


        }else if (userPreferences.getMotorPtype().equals("Individual")){

            Persona persona=new Persona(personal_details.get(0).getFirst_name(),personal_details.get(0).getLast_name(),personal_details.get(0).getEmail(),personal_details.get(0).getGender(),personal_details.get(0).getPhone(),personal_details.get(0).getResident_address(),
                    "null","null",personal_details.get(0).getPicture(),"null","null","null","null","null",
                    "null","1","null",personal_details.get(0).getMailing_address(),"null",
                    "null","null"
                    );

            for(int i=0;i<picture_results.size();i++) {

                VehiclePictures vehiclePictures=picture_results.get(i);
                vehiclePictureList_post.add(vehiclePictures.getFront_view());
                vehiclePictureList_post.add(vehiclePictures.getBack_view());
                vehiclePictureList_post.add(vehiclePictures.getLeft_view());
                vehiclePictureList_post.add(vehiclePictures.getRight_view());

                Log.i("mypix",vehiclePictures.getRight_view());

            }


            for(int i=0;i<results.size();i++) {
                VehicleDetails vehicleDetails=results.get(i);
                vehiclesList_post.add(new Vehicle(vehicleDetails.getPeriod(),vehicleDetails.getPolicy_type(),vehicleDetails.getEnhanced_third_party(),
                        vehicleDetails.getPrivate_policy(),vehicleDetails.getMotor_cycle_policy(),vehicleDetails.getVehicle_make(),
                        vehicleDetails.getBody_type(),vehicleDetails.getYear(),"null",vehicleDetails.getRegistration_number(),
                        vehicleDetails.getChasis_number(),vehicleDetails.getEngine_number(),vehicleDetails.getVehicle_value(),vehiclePictureList_post));


            }

           /* VehiclePostHead vehiclePostHead=new VehiclePostHead(persona,vehiclesList_post,total_quoteprice,
                    pin_txt_m5.getText().toString(),modeofPaymentString,userPreferences.getUserId());*/

               // Log.i("policyPost2",vehiclePostHead.toString());
            //sendPolicy(vehiclePostHead);


        }


        asyncVehiclePolicy(primaryKey);
        showMessage("Proceeding to Payment");

    }


    private void sendPolicy(VehiclePostHead vehiclehead){


        //get client and call object for request
        ApiInterface client = ServiceGenerator.createService(ApiInterface.class);
        Log.i("TokenP", userPreferences.getUserToken());

        Call<ResponseBody> call=client.vehicle_policy("Token "+userPreferences.getUserToken(),vehiclehead);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                            btn_layout3.setVisibility(View.VISIBLE);
                            progressbar.setVisibility(View.GONE);

                        }
                        btn_layout3.setVisibility(View.VISIBLE);
                        progressbar.setVisibility(View.GONE);
                        return;
                    }
                    
                    String p_response=response.body().string();
                    Log.i("policyResponse", p_response);

                    showMessage("Submit Successful, Proceed to Payment");

                    btn_layout3.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                    /*if (user_email != null) {
                        Intent intent = new Intent(SignUp.this, SignIn.class);
                        intent.putExtra(Constant.USER_EMAIL, user_email);
                        startActivity(intent);
                        SignUp.this.finish();

                    } else {
                        showMessage("Error: " + response.body());
                    }*/
                }catch (Exception e){
                    showMessage("Submission Error: " + e.getMessage());
                    Log.i("policyResponse", e.getMessage());
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showMessage("Submission Failed "+t.getMessage());
                Log.i("GEtError",t.getMessage());
            }
        });




    }

    //To Delete vehicle
    private void asyncVehiclePolicy(final String id){
        AsyncTask<Void, Void, Void> remoteItem =new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                realm=Realm.getDefaultInstance();

                VehiclePolicy vehiclePolicy=realm.where(VehiclePolicy.class).equalTo("id",id).findFirst();
                if(vehiclePolicy !=null){
                    realm.beginTransaction();
                    vehiclePolicy.deleteFromRealm();
                    realm.commitTransaction();
                }

                RealmResults<VehicleDetails> vehicleDetails=realm.where(VehicleDetails.class).findAll();
                if(vehicleDetails!=null){
                    realm.beginTransaction();
                    vehicleDetails.deleteAllFromRealm();
                    realm.commitTransaction();
                }else {
                    showMessage("Error in deletion");
                }
                RealmResults<VehiclePictures> vehiclePictures=realm.where(VehiclePictures.class).findAll();
                if(vehiclePictures!=null){
                    realm.beginTransaction();
                    vehiclePictures.deleteAllFromRealm();
                    realm.commitTransaction();
                }else {
                    showMessage("Error in deletion");
                }
                realm.close();
                return null;
            }
        };
        remoteItem.execute();
    }

    /*//To Delete vehicle
    private void asyncVehicleList(final String id){
        AsyncTask<Void,Void,Void> remoteItem =new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                realm=Realm.getDefaultInstance();

                VehiclePolicy expenses=realm.where(VehiclePolicy.class).equalTo("id",id).findFirst();
                if(expenses !=null){
                    realm.beginTransaction();
                    expenses.deleteFromRealm();
                    realm.commitTransaction();
                }
                realm.close();
                return null;
            }
        };
        remoteItem.execute();
    }
*/





    private void showMessage(String s) {
        Snackbar.make(qb_form_layout4, s, Snackbar.LENGTH_SHORT).show();
    }


}
