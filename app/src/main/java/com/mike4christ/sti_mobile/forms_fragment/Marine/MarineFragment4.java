package com.mike4christ.sti_mobile.forms_fragment.Marine;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.Marine.CargoDetail;
import com.mike4christ.sti_mobile.Model.Marine.FormSuccessDetail.BuyQuoteFormGetHead_Marine;
import com.mike4christ.sti_mobile.Model.Marine.FormSuccessDetail.Policy;
import com.mike4christ.sti_mobile.Model.Marine.FormSuccessDetail.Transaction;
import com.mike4christ.sti_mobile.Model.Marine.MarinePolicy;
import com.mike4christ.sti_mobile.Model.Marine.MarinePost.Cargo;
import com.mike4christ.sti_mobile.Model.Marine.MarinePost.MarinePersona;
import com.mike4christ.sti_mobile.Model.Marine.MarinePost.MarinePostHead;
import com.mike4christ.sti_mobile.Model.Marine.Personal_Detail_marine;

import com.mike4christ.sti_mobile.Model.ServiceGenerator;
import com.mike4christ.sti_mobile.Model.Vehicle.FormSuccessDetail.BuyQuoteFormGetHead;
import com.mike4christ.sti_mobile.Model.Vehicle.VehicleDetails;
import com.mike4christ.sti_mobile.Model.Vehicle.VehiclePictures;
import com.mike4christ.sti_mobile.Model.Vehicle.VehiclePost.Persona;
import com.mike4christ.sti_mobile.Model.Vehicle.VehiclePost.Vehicle;
import com.mike4christ.sti_mobile.Model.Vehicle.VehiclePost.VehiclePostHead;
import com.mike4christ.sti_mobile.NetworkConnection;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.activity.PolicyPaymentActivity;
import com.mike4christ.sti_mobile.adapter.CargoListAdapter;
import com.mike4christ.sti_mobile.retrofit_interface.ApiInterface;
import com.shuhart.stepview.StepView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MarineFragment4 extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match

    private static final String PRIMARY_KEY = "primaryKey";
    private static final String FOREIGN_KEY = "foreignKey";




    // TODO: Rename and change types of parameters
    private String primaryKey,foreignKey;

    @BindView(R.id.qb_form_layout3)
    FrameLayout mQbFormLayout3;
    @BindView(R.id.step_view)
    StepView mStepView;
    @BindView(R.id.personal_info_txt_m4)
    TextView mPersonalInfoTxtM4;
    @BindView(R.id.fabShowCargoes)
    FloatingActionButton mFabShowCargoes;
    @BindView(R.id.inputLayoutPin_m4)
    TextInputLayout mInputLayoutPinM4;
    @BindView(R.id.pin_txt_m4)
    EditText mPinTxtM4;
    @BindView(R.id.modeOfPayment_spinner_m4)
    Spinner mModeOfPaymentSpinnerM4;
    @BindView(R.id.btn_layout4_m4)
    LinearLayout mBtnLayout4M4;
    @BindView(R.id.v_back_btn4_m4)
    Button mVBackBtn4M4;
    @BindView(R.id.v_next_btn4_m4)
    Button mVNextBtn4M4;
    @BindView(R.id.progressbar4_m4)
    AVLoadingIndicatorView mProgressbar4M4;


    private  int currentStep=3;
    Realm realm;
    CargoListAdapter cargoListAdapter;

    String modeofPaymentString;
    UserPreferences userPreferences;
    MarinePolicy marinePolicy;
    RealmList<Personal_Detail_marine> personal_detail_marines;
    //Cargo return policy
    List<Policy> policy;

    String total_quoteprice;

    List<Cargo> cargoList_post=new ArrayList<Cargo>();
    List<String> cargoPictureList_post=new ArrayList<>();


    NetworkConnection networkConnection=new NetworkConnection();
    String policy_num="";
    String total_price="";




    public MarineFragment4() {
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
    public static MarineFragment4 newInstance(String param1) {
        MarineFragment4 fragment = new MarineFragment4();
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
        View view=inflater.inflate(R.layout.fragment_marine4, container, false);
        ButterKnife.bind(this,view);
        //  mStepView next registration step
        mStepView.go(currentStep, true);
        userPreferences=new UserPreferences(getContext());
        realm= Realm.getDefaultInstance();


        init();
        modeofPaymentSpinner();
        setViewActions();

        return  view;
    }

    private void modeofPaymentSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.mode_of_payment,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mModeOfPaymentSpinnerM4.setAdapter(staticAdapter);

        mModeOfPaymentSpinnerM4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String modeofPaymentTypeString = (String) parent.getItemAtPosition(position);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mModeOfPaymentSpinnerM4.getItemAtPosition(0);
            }
        });

    }
    private void init(){
        

        //retrieve data for personal detail first
        MarinePolicy marinePolicy;

        marinePolicy=realm.where(MarinePolicy.class).equalTo("id",primaryKey).findFirst();
        total_quoteprice=marinePolicy.getQuote_price();
        personal_detail_marines=marinePolicy.getPersonal_detail_marines();




        if(userPreferences.getMotorPtype().equals("Corporate")){
            String corperate="Company Name: "+personal_detail_marines.get(0).getCompany_name()+"\n"+"TIN Number: "+personal_detail_marines.get(0).getTin_number()+"\n"+
                   "\n"+"Phone Number: "+personal_detail_marines.get(0).getPhone()+"\n"+
                    "Office Address: "+personal_detail_marines.get(0).getOffice_address()+"\n"+"Contact Person: "+personal_detail_marines.get(0).getContact_person()+"\n"+
                    "Phone Number: "+personal_detail_marines.get(0).getPhone()+"\n"+"Email Address: "+personal_detail_marines.get(0).getEmail()+"\n"+
                    "Total Premium: "+total_quoteprice;
            mPersonalInfoTxtM4.setText(corperate);

            Log.i("Personaal",personal_detail_marines.get(0).getCompany_name());

        }else if (userPreferences.getMotorPtype().equals("Individual")){
            String individual="First Name: "+personal_detail_marines.get(0).getFirst_name()+"\n"+
                    "Last Name: "+personal_detail_marines.get(0).getLast_name()+"\n"+"Phone Number: "+personal_detail_marines.get(0).getPhone()+"\n"+
                    "Gender: "+personal_detail_marines.get(0).getResident_address()+"\n"+"Mailing Address: "+personal_detail_marines.get(0).getMailing_address()+"\n"+
                    "Total Premium: "+total_quoteprice;
            mPersonalInfoTxtM4.setText(individual);
            Log.i("Personaal",personal_detail_marines.get(0).getFirst_name());

        }




    }



    //seting onclicks listeners
    private void setViewActions() {

        mVNextBtn4M4.setOnClickListener(this);
        mVBackBtn4M4.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v_next_btn4_m4:
//                send quote to payment after validate pin
                ValidateForm();
               
                break;

            case R.id.v_back_btn4_m4:

                mStepView.go(1, true);
                asyncMarinePolicy(primaryKey);
                userPreferences.setTempMarineQuotePrice("0.0");
                Fragment marineFragment2 = new MarineFragment2();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_marine_form_container, marineFragment2);
                ft.commit();

                break;
        }
    }


    private void ValidateForm() {

        if (networkConnection.isNetworkConnected(getContext())) {
            boolean isValid = true;

            if (mPinTxtM4.getText().toString().isEmpty()) {
                mInputLayoutPinM4.setError("Pin is required!");
                isValid = false;
            } else {
                mInputLayoutPinM4.setErrorEnabled(false);
            }
            //Prefix Spinner
            modeofPaymentString = mModeOfPaymentSpinnerM4.getSelectedItem().toString();
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

    @OnClick(R.id.fabShowCargoes)
    public void showBottomVehicleList() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_vehicles, null);
        final TextView textView = (TextView) view.findViewById(R.id.detail);
        final RecyclerView recycler_vehicles = (RecyclerView) view.findViewById(R.id.recycler_detail);
        final ImageView close = (ImageView) view.findViewById(R.id.close);
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mStepView.go(5, true);
                dialog.dismiss();
            }
        });

        //retrieve data
        final RealmResults<CargoDetail> results;
        //String title;
        results=realm.where(CargoDetail.class).findAll();

        if(results==null){
            showMessage("Result is null");
        }

        //Bind
        cargoListAdapter=new CargoListAdapter(results,getActivity().getBaseContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getBaseContext(),RecyclerView.VERTICAL,false);
        recycler_vehicles.setLayoutManager(linearLayoutManager);
        recycler_vehicles.setAdapter(cargoListAdapter);


        dialog.setContentView(view);
        dialog.show();
    }

    private void mSubmit() {

        mBtnLayout4M4.setVisibility(View.GONE);
        mProgressbar4M4.setVisibility(View.VISIBLE);


        //retrieve data
        final RealmResults<CargoDetail> results;

        //String title;
        results=realm.where(CargoDetail.class).findAll();
        

        Log.i("cargos_result",results.toString());



        if(userPreferences.getMotorPtype().equals("Corporate")){

            MarinePersona marinePersona=new MarinePersona("null","null",personal_detail_marines.get(0).getEmail(),"null",personal_detail_marines.get(0).getPhone(),"null",
                    "null","null","null","null","null","null","null","null",
                    "null","2",personal_detail_marines.get(0).getCompany_name(),"null",personal_detail_marines.get(0).getTin_number(),
                    personal_detail_marines.get(0).getOffice_address(),personal_detail_marines.get(0).getContact_person());
            

            for(int i=0;i<results.size();i++) {
                cargoPictureList_post.add("null");
                CargoDetail cargoDetail=results.get(i);
                Cargo cargo=new Cargo("12 Months","Marine",cargoDetail.getDesc_goods(),
                        cargoDetail.getPfi_number(),cargoDetail.getPfi_date(),cargoDetail.getQuantity(),
                        cargoDetail.getValue(),cargoDetail.getConversion_rate(),cargoDetail.getLoading_port(),cargoDetail.getDischarge_port(),
                        cargoDetail.getConveyance_mode(),"null",cargoPictureList_post);

                cargoList_post.add(cargo);

                Log.i("cargoDetailLoop",cargoDetail.getPfi_number());

            }

            MarinePostHead marinePostHead=new MarinePostHead(marinePersona,cargoList_post,total_quoteprice,
                    mPinTxtM4.getText().toString(),modeofPaymentString,userPreferences.getUserId());

            sendPolicy(marinePostHead);


        }else if (userPreferences.getMotorPtype().equals("Individual")){

            MarinePersona marinePersona=new MarinePersona(personal_detail_marines.get(0).getFirst_name(),personal_detail_marines.get(0).getLast_name(),personal_detail_marines.get(0).getEmail(),personal_detail_marines.get(0).getGender(),personal_detail_marines.get(0).getPhone(),personal_detail_marines.get(0).getResident_address(),
                    "null","null","null","null","null","null","null","null",
                    "null","1","null",personal_detail_marines.get(0).getMailing_address(),"null",
                    "null","null");

            for(int i=0;i<results.size();i++) {
                cargoPictureList_post.add("null");
                CargoDetail cargoDetail=results.get(i);
                Cargo cargo=new Cargo("12 Months","null",cargoDetail.getDesc_goods(),
                        cargoDetail.getPfi_number(),cargoDetail.getPfi_date(),cargoDetail.getQuantity(),
                        cargoDetail.getValue(),cargoDetail.getConversion_rate(),cargoDetail.getLoading_port(),cargoDetail.getDischarge_port(),
                        cargoDetail.getConveyance_mode(),"null",cargoPictureList_post);

                cargoList_post.add(cargo);

                Log.i("cargoDetailLoop",cargoDetail.getPfi_number());

            }
            MarinePostHead marinePostHead=new MarinePostHead(marinePersona,cargoList_post,total_quoteprice,
                    mPinTxtM4.getText().toString(),modeofPaymentString,userPreferences.getUserId());

            sendPolicy(marinePostHead);


        }






        // asyncVehicleList(primaryKey);



    }

    private void sendPolicy( MarinePostHead marinePostHead){


        //get client and call object for request
        ApiInterface client = ServiceGenerator.createService(ApiInterface.class);
        Log.i("TokenP", userPreferences.getUserToken());

        Call<BuyQuoteFormGetHead_Marine> call=client.marine_policy("Token "+userPreferences.getUserToken(),marinePostHead);

        call.enqueue(new Callback<BuyQuoteFormGetHead_Marine>() {
            @Override
            public void onResponse(Call<BuyQuoteFormGetHead_Marine> call, Response<BuyQuoteFormGetHead_Marine> response) {
                Log.i("ResponseCode", String.valueOf(response.code()));
                if(response.code()==400){
                    showMessage("Check your internet connection");
                    mBtnLayout4M4.setVisibility(View.VISIBLE);
                    mProgressbar4M4.setVisibility(View.GONE);
                    return;
                }else if(response.code()==429){
                    showMessage("Too many requests on database");
                    mBtnLayout4M4.setVisibility(View.VISIBLE);
                    mProgressbar4M4.setVisibility(View.GONE);
                    return;
                }else if(response.code()==500){
                    showMessage("Server Error");
                    mBtnLayout4M4.setVisibility(View.VISIBLE);
                    mProgressbar4M4.setVisibility(View.GONE);
                    return;
                }else if(response.code()==401){
                    showMessage("Unauthorized access, please try login again");
                    mBtnLayout4M4.setVisibility(View.VISIBLE);
                    mProgressbar4M4.setVisibility(View.GONE);
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
                            mBtnLayout4M4.setVisibility(View.VISIBLE);
                            mProgressbar4M4.setVisibility(View.GONE);

                        }
                        mBtnLayout4M4.setVisibility(View.VISIBLE);
                        mProgressbar4M4.setVisibility(View.GONE);
                        return;
                    }

                    policy=response.body().getData().getPolicy();
                    for(int i=0;i<policy.size();i++){
                        policy_num=policy_num.concat("\n"+policy.get(i).getPolicyNumber());
                    }

                    total_price= String.valueOf(response.body().getData().getUnitPrice());

                    String ref= response.body().getData().getTransactions().get(0).getReference();

                    Log.i("policyNum", policy_num);
                    Log.i("totalPrice", total_price);

                    showMessage("Submit Successful, Proceed to Payment");
                    userPreferences.setTempMarineQuotePrice("0.0");

                    mBtnLayout4M4.setVisibility(View.VISIBLE);
                    mProgressbar4M4.setVisibility(View.GONE);
                    if (total_price != null) {
                        asyncMarinePolicy(primaryKey);
                        Intent intent = new Intent(getContext(), PolicyPaymentActivity.class);
                        intent.putExtra(Constant.TOTAL_PRICE, total_price);
                        intent.putExtra(Constant.POLICY_NUM, policy_num);
                        intent.putExtra(Constant.POLICY_TYPE, "marine");
                        intent.putExtra(Constant.REF, ref);
                        startActivity(intent);
                        getActivity().finish();

                    } else {
                        showMessage("Error: " + response.body());
                        asyncMarinePolicy(primaryKey);
                        userPreferences.setTempMarineQuotePrice("0.0");
                        Fragment marineFragment2 = new MarineFragment2();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_marine_form_container, marineFragment2);
                        ft.commit();
                    }
                }catch (Exception e){
                    showMessage("Submission Error: " + e.getMessage());
                    Log.i("policyResponse", e.getMessage());
                    mBtnLayout4M4.setVisibility(View.VISIBLE);
                    mProgressbar4M4.setVisibility(View.GONE);

                    asyncMarinePolicy(primaryKey);
                    userPreferences.setTempMarineQuotePrice("0.0");
                    Fragment marineFragment2 = new MarineFragment2();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_marine_form_container, marineFragment2);
                    ft.commit();
                }

            }
            @Override
            public void onFailure(Call<BuyQuoteFormGetHead_Marine> call, Throwable t) {
                showMessage("Submission Failed "+t.getMessage());
                Log.i("GEtError",t.getMessage());
                mBtnLayout4M4.setVisibility(View.VISIBLE);
                mProgressbar4M4.setVisibility(View.GONE);
            }
        });

    }

    //To Delete vehicle
    private void asyncMarinePolicy(final String id){
        AsyncTask<Void, Void, Void> remoteItem =new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                realm=Realm.getDefaultInstance();

                MarinePolicy marinePolicy=realm.where(MarinePolicy.class).equalTo("id",id).findFirst();
                if(marinePolicy !=null){
                    realm.beginTransaction();
                    marinePolicy.deleteFromRealm();
                    realm.commitTransaction();
                }

                RealmResults<CargoDetail> cargoDetails=realm.where(CargoDetail.class).findAll();
                if(cargoDetails!=null){
                    realm.beginTransaction();
                    cargoDetails.deleteAllFromRealm();
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


    private void showMessage(String s) {
        Snackbar.make(mQbFormLayout3, s, Snackbar.LENGTH_SHORT).show();
    }


}
