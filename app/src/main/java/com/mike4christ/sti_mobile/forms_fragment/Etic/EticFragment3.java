package com.mike4christ.sti_mobile.forms_fragment.Etic;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.snackbar.Snackbar;
import com.mike4christ.sti_mobile.Model.Etic.EticPolicy;
import com.mike4christ.sti_mobile.Model.Etic.Personal_Detail_etic;
import com.mike4christ.sti_mobile.Model.Etic.Travel_Info;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;


public class EticFragment3 extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PREMIUM_TXT = "premium_txt";
    private static final String PREMIUM_AMOUNT = "amount";

    // TODO: Rename and change types of parameters
    private String premiumText,p_amount;

    @BindView(R.id.qb_form_layout3)
    FrameLayout mQbFormLayout3;
    @BindView(R.id.step_view)
    com.shuhart.stepview.StepView mStepView;
    @BindView(R.id.transport_txt_e3)
    TextView mTransportTxtE3;
    @BindView(R.id.amount_e3)
    TextView mAmountE3;
    @BindView(R.id.btn_layout3_e3)
    LinearLayout mBtnLayout3E3;
    @BindView(R.id.v_back_btn3_e3)
    Button mVBackBtn3E3;
    @BindView(R.id.v_next_btn3_e3)
    Button mVNextBtn3E3;
    @BindView(R.id.progressbar3_e3)
   AVLoadingIndicatorView mProgressbar3E3;



    private  int currentStep=2;

    Realm realm;

    EticPolicy id=new EticPolicy();
    String primaryKey=id.getId();
    UserPreferences userPreferences;

    public EticFragment3() {
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
    public static EticFragment3 newInstance(String param1, String param2) {
        EticFragment3 fragment = new EticFragment3();
        Bundle args = new Bundle();
        args.putString(PREMIUM_TXT, param1);
        args.putString(PREMIUM_AMOUNT, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            premiumText = getArguments().getString(PREMIUM_TXT);
            UserPreferences userPreferences=new UserPreferences(getContext());

            double oldquote=Double.valueOf(userPreferences.getTempEticQuotePrice());
            p_amount=getArguments().getString(PREMIUM_AMOUNT);

            double newquote= Double.valueOf(getArguments().getString(PREMIUM_AMOUNT));
            double total_quote=oldquote+newquote;
            userPreferences.setTempEticQuotePrice(String.valueOf(total_quote));
            Log.i("PrimaryVariable",primaryKey);




        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_etic3, container, false);
        ButterKnife.bind(this,view);
        //        mStepView next registration step
        mStepView.go(currentStep, true);
        userPreferences = new UserPreferences(getContext());

        //instancial Realm db
        realm=Realm.getDefaultInstance();

        mTransportTxtE3.setText(premiumText);
        if(p_amount==null){
            p_amount="000";
            String format = p_amount + ".00";
            mAmountE3.setText(format);
        }else {
            String format = p_amount;
            mAmountE3.setText(format);
        }




        setViewActions();

        return  view;
    }



    //seting onclicks listeners
    private void setViewActions() {

        mVNextBtn3E3.setOnClickListener(this);
        mVBackBtn3E3.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v_next_btn3_e3:
//                send quote to client and sti
                mailClientAndSti();
                break;

            case R.id.v_back_btn3_e3:
                if (currentStep > 0) {
                    currentStep--;
                }
                mStepView.done(false);
                mStepView.go(currentStep, true);
                userPreferences.setTempEticQuotePrice("0.0");
                Fragment eticFragment2 = new EticFragment2();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_etic_form_container, eticFragment2);
                ft.commit();

                break;
        }
    }

    private void mailClientAndSti() {
        UserPreferences userPreferences = new UserPreferences(getContext());

        mBtnLayout3E3.setVisibility(View.GONE);
        mProgressbar3E3.setVisibility(View.VISIBLE);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Personal_Detail_etic personal_detail_etic=new Personal_Detail_etic();

                personal_detail_etic.setPrefix(userPreferences.getEticIPrefix());
                personal_detail_etic.setFirst_name(userPreferences.getEticIFirstName());
                personal_detail_etic.setLast_name(userPreferences.getEticILastName());
                personal_detail_etic.setGender(userPreferences.getEticIGender());
                personal_detail_etic.setPhone(userPreferences.getEticIPhoneNum());
                personal_detail_etic.setResident_address(userPreferences.getEticIResAdrr());
                personal_detail_etic.setNext_of_kin(userPreferences.getEticINextKin());
                personal_detail_etic.setMailing_addr(userPreferences.getEticIMailingAddr());
                personal_detail_etic.setEmail(userPreferences.getEticIEmail());


                personal_detail_etic.setBusiness(userPreferences.getEticIBusiness());
                personal_detail_etic.setNationality(userPreferences.getEticINationality());
                personal_detail_etic.setEmployer_name(userPreferences.getEticIEmployerName());
                personal_detail_etic.setEmployer_addr(userPreferences.getEticIEmployerAddr());
                personal_detail_etic.setState(userPreferences.getEticIState());
                personal_detail_etic.setIntnded_start_dateCover(userPreferences.getEticIIntendStartDate());
                personal_detail_etic.setEnd_dateCover(userPreferences.getEticIEndDate());


                personal_detail_etic.setPicture(userPreferences.getEticIPersonalImage());
                
                //Additional Insured List
                Travel_Info travel_info=new Travel_Info();
                travel_info.setTrip_duration(userPreferences.getEticITripDuration());
                travel_info.setStart_date(userPreferences.getEticStartDate());
                travel_info.setTravel_mode(userPreferences.getEticITravelMode());
                travel_info.setDisability(userPreferences.getEticIDisability());
                travel_info.setDisability_details(userPreferences.getEticIDisabilityDetail());
                travel_info.setPlace_departure(userPreferences.getEticIDeparturePlc());
                travel_info.setPlace_arrival(userPreferences.getEticIArrivalPlc());
                travel_info.setAddress_country_of_visit(userPreferences.getEticICountryOfVisit());
                
                RealmList<Travel_Info> travel_infoList=new RealmList<>();
                travel_infoList.add(travel_info);

                personal_detail_etic.setTravel_infos(travel_infoList);


                final Personal_Detail_etic personal_detail_etic1=realm.copyToRealm(personal_detail_etic);

                EticPolicy eticPolicy=realm.createObject(EticPolicy.class,primaryKey);
                eticPolicy.setAgent_id(userPreferences.getUserId());
                eticPolicy.setQuote_price(String.valueOf(userPreferences.getTempEticQuotePrice()));
                eticPolicy.setPayment_source("paystack");
                eticPolicy.setPin("0000");

                Log.i("Primary1",primaryKey);

                eticPolicy.getPersonal_detail_etic().add(personal_detail_etic1);



            }
        });



        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_etic_form_container, EticFragment4.newInstance(primaryKey),EticFragment4.class.getSimpleName());
        ft.commit();



    }




    private void showMessage(String s) {
        Snackbar.make(mQbFormLayout3, s, Snackbar.LENGTH_SHORT).show();
    }


}
