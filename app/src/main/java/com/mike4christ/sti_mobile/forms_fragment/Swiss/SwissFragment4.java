package com.mike4christ.sti_mobile.forms_fragment.Swiss;

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
import com.mike4christ.sti_mobile.Model.AllRisk.AllRiskPost.AllRiskPostHead;
import com.mike4christ.sti_mobile.Model.AllRisk.FormSuccessDetail.BuyQuoteFormGetHead_AllRisk;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.ServiceGenerator;
import com.mike4christ.sti_mobile.Model.Swiss.AdditionInsured;
import com.mike4christ.sti_mobile.Model.Swiss.FormSuccessDetail.BuyQuoteFormGetHead_Swiss;
import com.mike4christ.sti_mobile.Model.Swiss.FormSuccessDetail.Policy;
import com.mike4christ.sti_mobile.Model.Swiss.Personal_Detail_swiss;
import com.mike4christ.sti_mobile.Model.Swiss.SwissInsured;
import com.mike4christ.sti_mobile.Model.Swiss.SwissPost.AdditionalInsuredPost;
import com.mike4christ.sti_mobile.Model.Swiss.SwissPost.Swiss;
import com.mike4christ.sti_mobile.Model.Swiss.SwissPost.SwissPersona;
import com.mike4christ.sti_mobile.Model.Swiss.SwissPost.SwissPostHead;
import com.mike4christ.sti_mobile.NetworkConnection;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.activity.PolicyPaymentActivity;
import com.mike4christ.sti_mobile.adapter.AddInsuredListAdapter;
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

class SwissFragment4 extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match

    private static final String PRIMARY_KEY = "primaryKey";
    private static final String FOREIGN_KEY = "foreignKey";


    // TODO: Rename and change types of parameters
    private String primaryKey,foreignKey;



    @BindView(R.id.qb_form_layout4)
    FrameLayout mQbFormLayout4;
    @BindView(R.id.step_view)
    StepView mStepView;
    @BindView(R.id.personal_info_s4)
    TextView mPersonalInfoS3;
    @BindView(R.id.fabShowAddInsure_s4)
    FloatingActionButton mFabShowAddInsureS3;
    @BindView(R.id.btn_layout4_s4)
    LinearLayout mBtnLayout4S4;
    @BindView(R.id.v_back_btn4_s4)
    Button mVBackBtn4S4;
    @BindView(R.id.v_next_btn4_s4)
    Button mVNextBtn4S4;
    @BindView(R.id.progressbar4_s4)
    AVLoadingIndicatorView mProgressbar4S4;

    @BindView(R.id.inputLayoutPin_s4)
    TextInputLayout inputLayoutPin_s4;

    @BindView(R.id.pin_txt_s4)
    EditText pin_txt_s4;

    @BindView(R.id.modeOfPayment_spinner_s4)
    Spinner modeOfPayment_spinner_s4;



    private  int currentStep=3;
    Realm realm;
    AddInsuredListAdapter addInsureListAdapter;

    String modeofPaymentString;
    UserPreferences userPreferences;
    SwissInsured swissInsured;
    RealmList<Personal_Detail_swiss> personal_detail_swisses;
    //Swiss return policy
    List<Policy> policy;

    String total_quoteprice;

    List<AdditionalInsuredPost> additionaList_post=new ArrayList<AdditionalInsuredPost>();

    NetworkConnection networkConnection=new NetworkConnection();
    Swiss swiss;
    String policy_num="";
    String total_price="";




    public SwissFragment4() {
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
    public static SwissFragment4 newInstance(String param1) {
        SwissFragment4 fragment = new SwissFragment4();
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
        View view=inflater.inflate(R.layout.fragment_swiss4, container, false);
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
        modeOfPayment_spinner_s4.setAdapter(staticAdapter);

        modeOfPayment_spinner_s4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String modeofPaymentTypeString = (String) parent.getItemAtPosition(position);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                modeOfPayment_spinner_s4.getItemAtPosition(0);
            }
        });

    }
    private void init(){

        UserPreferences userPreferences=new UserPreferences(getContext());

        //retrieve data for personal detail first
        SwissInsured swissInsured;

        swissInsured=realm.where(SwissInsured.class).equalTo("id",primaryKey).findFirst();
        total_quoteprice=swissInsured.getQuote_price();
        personal_detail_swisses=swissInsured.getPersonal_detail_swisses();


            String insurer="Prefix: "+personal_detail_swisses.get(0).getPrefix()+"\n"+"First Name: "+personal_detail_swisses.get(0).getFirst_name()+"\n"+
                    "Last Name: "+personal_detail_swisses.get(0).getLast_name()+"\n"+"Phone Number: "+personal_detail_swisses.get(0).getPhone()+"\n"+
                    "Gender: "+personal_detail_swisses.get(0).getResident_address()+"\n"+ "Total Premium: "+total_quoteprice;
            mPersonalInfoS3.setText(insurer);

        




    }



    //seting onclicks listeners
    private void setViewActions() {

        mVNextBtn4S4.setOnClickListener(this);
        mVBackBtn4S4.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v_next_btn4_s4:
//                send quote to client and sti
                ValidateForm();
                break;

            case R.id.v_back_btn4_s4:

                mStepView.go(1, true);

                Fragment swissFragment2 = new SwissFragment2();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_swiss_form_container, swissFragment2);
                ft.commit();

                break;
        }
    }




    @OnClick(R.id.fabShowAddInsure_s4)
    public void showBottomAddInsureList() {
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
        final RealmResults<AdditionInsured> results;
        //String title;
        results=realm.where(AdditionInsured.class).findAll();

        if(results==null){
            showMessage("Result is null");
        }

        //Bind
        addInsureListAdapter=new AddInsuredListAdapter(results,getActivity().getBaseContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getBaseContext(),RecyclerView.VERTICAL,false);
        recycler_vehicles.setLayoutManager(linearLayoutManager);
        recycler_vehicles.setAdapter(addInsureListAdapter);

        /*RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recycler_note.setLayoutManager(mLayoutManager);
        recycler_note.setItemAnimator(new DefaultItemAnimator());
        recycler_note.setAdapter(adapter);*/


        dialog.setContentView(view);
        dialog.show();
    }

    private void ValidateForm() {

        if (networkConnection.isNetworkConnected(getContext())) {
            boolean isValid = true;

            if (pin_txt_s4.getText().toString().isEmpty()) {
                inputLayoutPin_s4.setError("Pin is required!");
                isValid = false;
            } else {
                inputLayoutPin_s4.setErrorEnabled(false);
            }
            //Prefix Spinner
            modeofPaymentString = modeOfPayment_spinner_s4.getSelectedItem().toString();
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

        mBtnLayout4S4.setVisibility(View.GONE);
        mProgressbar4S4.setVisibility(View.VISIBLE);

//retrieve data
        final RealmResults<AdditionInsured> results;

        //String title;
        results=realm.where(AdditionInsured.class).findAll();

        for(int i=0;i<results.size();i++) {
            AdditionInsured additionInsured=results.get(i);
            AdditionalInsuredPost additionalInsuredPost=new AdditionalInsuredPost(additionInsured.getFirst_name(),additionInsured.getLast_name(),
                    additionInsured.getEmail(),additionInsured.getGender(),additionInsured.getPhone(),additionInsured.getDate_of_birth(),
                    additionInsured.getMarital_status(),"null",additionInsured.getDisability());
            additionaList_post.add(additionalInsuredPost);

            Log.i("policyItemLoop",additionInsured.getMarital_status());

        }

            SwissPersona swissPersona =new SwissPersona(personal_detail_swisses.get(0).getFirst_name(),personal_detail_swisses.get(0).getLast_name(),personal_detail_swisses.get(0).getEmail(),personal_detail_swisses.get(0).getGender(),personal_detail_swisses.get(0).getDate_of_birth()
                    ,"null",personal_detail_swisses.get(0).getPhone(),personal_detail_swisses.get(0).getResident_address(),
                    personal_detail_swisses.get(0).getMarital_status(),"",personal_detail_swisses.get(0).getNext_of_kin(),personal_detail_swisses.get(0).getNext_of_kin_phone(),personal_detail_swisses.get(0).getNext_of_kin_address()
                    ,personal_detail_swisses.get(0).getDisability(),additionaList_post);

            swiss=new Swiss("12 month");

            SwissPostHead swissPostHead=new SwissPostHead(swissPersona,swiss,total_quoteprice, pin_txt_s4.getText().toString()
                   ,modeofPaymentString,userPreferences.getUserId());


            sendPolicy(swissPostHead);


        asyncSwissInsured(primaryKey);

    }

    private void sendPolicy(SwissPostHead swissPostHead){


        //get client and call object for request
        ApiInterface client = ServiceGenerator.createService(ApiInterface.class);

        Call<BuyQuoteFormGetHead_Swiss> call=client.swiss_policy("Token "+userPreferences.getUserToken(),swissPostHead);

        call.enqueue(new Callback<BuyQuoteFormGetHead_Swiss>() {
            @Override
            public void onResponse(Call<BuyQuoteFormGetHead_Swiss> call, Response<BuyQuoteFormGetHead_Swiss> response) {
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
                            mBtnLayout4S4.setVisibility(View.VISIBLE);
                            mProgressbar4S4.setVisibility(View.GONE);

                        }
                        mBtnLayout4S4.setVisibility(View.VISIBLE);
                        mProgressbar4S4.setVisibility(View.GONE);
                        return;
                    }

                    policy=response.body().getData().getPolicy();
                    for(int i=0;i<policy.size();i++){
                        policy_num=policy_num.concat("\n"+policy.get(i).getPolicyNumber());
                    }

                    total_price= String.valueOf(response.body().getData().getTotalPrice());

                    Log.i("policyNum", policy_num);
                    Log.i("totalPrice", total_price);

                    showMessage("Submit Successful, Proceed to Payment");
                    userPreferences.setTempAllRiskQuotePrice(0);

                    mBtnLayout4S4.setVisibility(View.VISIBLE);
                    mProgressbar4S4.setVisibility(View.GONE);
                    if (total_price != null) {

                        Intent intent = new Intent(getContext(), PolicyPaymentActivity.class);
                        intent.putExtra(Constant.TOTAL_PRICE, total_price);
                        intent.putExtra(Constant.POLICY_NUM, policy_num);
                        startActivity(intent);
                        getActivity().finish();

                    } else {
                        showMessage("Error: " + response.body());
                        mBtnLayout4S4.setVisibility(View.VISIBLE);
                        mProgressbar4S4.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    showMessage("Submission Error: " + e.getMessage());
                    Log.i("policyResponse", e.getMessage());
                    mBtnLayout4S4.setVisibility(View.VISIBLE);
                    mProgressbar4S4.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(Call<BuyQuoteFormGetHead_Swiss> call, Throwable t) {
                showMessage("Submission Failed "+t.getMessage());
                Log.i("GEtError",t.getMessage());
                mBtnLayout4S4.setVisibility(View.VISIBLE);
                mProgressbar4S4.setVisibility(View.GONE);
            }
        });




    }


    //To Delete vehicle
    private void asyncSwissInsured(final String id){
        AsyncTask<Void, Void, Void> remoteItem =new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                realm=Realm.getDefaultInstance();

                SwissInsured swissPolicy=realm.where(SwissInsured.class).equalTo("id",id).findFirst();
                if(swissPolicy !=null){
                    realm.beginTransaction();
                    swissPolicy.deleteFromRealm();
                    realm.commitTransaction();
                }

                RealmResults<AdditionInsured> addInsureDetails=realm.where(AdditionInsured.class).findAll();
                if(addInsureDetails!=null){
                    realm.beginTransaction();
                    addInsureDetails.deleteAllFromRealm();
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

                SwissInsured expenses=realm.where(SwissInsured.class).equalTo("id",id).findFirst();
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
        Snackbar.make(mQbFormLayout4, s, Snackbar.LENGTH_SHORT).show();
    }

}
