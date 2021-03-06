package com.sti.sti_mobile.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.sti.sti_mobile.Model.Errors.APIError;
import com.sti.sti_mobile.Model.Errors.ErrorUtils;
import com.sti.sti_mobile.Model.MyPolicies.AllRisk;
import com.sti.sti_mobile.Model.MyPolicies.Marine;
import com.sti.sti_mobile.Model.MyPolicies.PolicyHead;
import com.sti.sti_mobile.Model.MyPolicies.Swis;
import com.sti.sti_mobile.Model.MyPolicies.Travel;
import com.sti.sti_mobile.Model.MyPolicies.Vehicle;
import com.sti.sti_mobile.Model.ServiceGenerator;
import com.sti.sti_mobile.Model.TransactionHistroy.History;
import com.sti.sti_mobile.Model.TransactionHistroy.TransactionHead;
import com.sti.sti_mobile.R;
import com.sti.sti_mobile.SignIn;
import com.sti.sti_mobile.UserPreferences;
import com.sti.sti_mobile.activity.NewsDetail;
import com.sti.sti_mobile.retrofit_interface.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener, View.OnClickListener {

    /** ButterKnife Code **/
    @BindView(R.id.dashboard_layout)
    FrameLayout mDashboardLayout;
    @BindView(R.id.transaction_notify)
    MaterialCardView mTransactnNotify;
    @BindView(R.id.transactn_notify_btn)
    Button mTrasactnNotifyBtn;
    @BindView(R.id.transact_note)
    TextView mTrasactnNote;
    @BindView(R.id.user_policy_btn)
    MaterialCardView mUserPolicyBtn;
    @BindView(R.id.firstname_txt)
    TextView mFirstnameTxt;
    @BindView(R.id.lastname_txt)
    TextView mLastnameTxt;
    @BindView(R.id.user_policy_spinner)
    Spinner mUserPolicySpinner;
    @BindView(R.id.slider)
    SliderLayout mSlider;
    @BindView(R.id.product_tag_btn)
    Button mProductTagBtn;
    @BindView(R.id.news_update_btn)
    MaterialCardView mNewsUpdateBtn;
    @BindView(R.id.read_more)
    TextView mReadMore;
    @BindView(R.id.policy_status)
    LinearLayout mPolicyStatus;
    @BindView(R.id.policy_type)
    TextView mPolicyType;
    @BindView(R.id.expire_date)
    TextView mExpireDate;
    @BindView(R.id.refresh_policy)
    ImageView refresh_policy;
    /** ButterKnife Code **/
    String policyNumString;
    String checkIncomplete;

    Fragment fragment;
    UserPreferences userPreferences;

    //The variables for VehincleData
    List<Vehicle> vehicleList;
    List<Swis> swisList;
    List<Marine> marineList;
    List<Travel> travelList;
    List<AllRisk> allRiskList;
    List<History> policy_item;
    ArrayList<String> policySpinnerList=new ArrayList<>();
    
    
    ArrayList<String> vehicleTypeString=new ArrayList<>();
    ArrayList<String> swissTypeString=new ArrayList<>();
    ArrayList<String> marineTypeString=new ArrayList<>();
    ArrayList<String> travelTypeString=new ArrayList<>();
    ArrayList<String> allriskTypeString=new ArrayList<>();
    String expire_date;
    String policy_type;


    ApiInterface client= ServiceGenerator.createService(ApiInterface.class);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this,view);
        userPreferences=new UserPreferences(getContext());
        policySpinnerList.add("Select Policy");
        policySpinnerList.add("Motor");
        policySpinnerList.add("Swiss-F");
        policySpinnerList.add("Marine");
        policySpinnerList.add("Travel");
        policySpinnerList.add("All Risk");



        mReadMore.setPaintFlags(mReadMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mReadMore.setText("Read more");

        setAction();
        setUp();
        getPolicies();
        getHistory();
        userPolicy();
        SLide();

        mTrasactnNotifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new TransactionHistoryFragment();
                showFragment(fragment);
            }
        });

        String news_info = getResources().getString(R.string.news_info);
        //String news_img=getResources().getString(R.string.news_img);
        String news_title = getResources().getString(R.string.news_title);
        String news_datetime = getResources().getString(R.string.news_datetime);

        mReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewsDetail.class);
                intent.putExtra("news_info", news_info);
                //intent.putExtra("news_img",news_img);
                intent.putExtra("news_title", news_title);
                intent.putExtra("news_datetime", news_datetime);
                startActivity(intent);
            }
        });


        return view;
    }

    private void setUp(){
        mFirstnameTxt.setText(userPreferences.getFirstName());
        mLastnameTxt.setText(userPreferences.getLastName());
    }

    private void setAction(){
        mProductTagBtn.setOnClickListener(this);
        mNewsUpdateBtn.setOnClickListener(this);
        refresh_policy.setOnClickListener(this);

    }

    private void getPolicies(){


        //get client and call object for request

        Call<PolicyHead> call=client.get_paid_policies("Token "+userPreferences.getUserToken());
        call.enqueue(new Callback<PolicyHead>() {
            @Override
            public void onResponse(Call<PolicyHead> call, Response<PolicyHead> response) {

                if(!response.isSuccessful()){
                    try {
                        APIError apiError = ErrorUtils.parseError(response);

                        showMessage("Fetch Failed: " + apiError.getErrors());
                        Log.i("Invalid Fetch", String.valueOf(apiError.getErrors()));
                        timeout_alert();
                        //Log.i("Invalid Entry", response.errorBody().toString());

                    } catch (Exception e) {
                        Log.i("Fetch Failed", e.getMessage());
                        showMessage("Fetch Failed");
                        timeout_alert();

                    }

                    return;
                }

                vehicleList=response.body().getData().getPolicies().getVehicle();
                swisList=response.body().getData().getPolicies().getSwiss();
                travelList=response.body().getData().getPolicies().getTravel();
                allRiskList=response.body().getData().getPolicies().getAllRisk();
                marineList=response.body().getData().getPolicies().getMarine();

                int count_vehicle=vehicleList.size();
                int count_swis=swisList.size();
                int count_travel=travelList.size();
                int count_allrisk=allRiskList.size();
                int count_marine=marineList.size();

                //policySpinnerList.add("Select Policy Number");
                if(count_vehicle!=0){
                    for(int i = 0; i<vehicleList.size(); i++){
                        // policySpinnerList.add(vehicleList.get(i).getPolicyNumber());
                        vehicleTypeString.add(vehicleList.get(i).getPolicyNumber());
                    }
                } else {
                    vehicleTypeString.add("No Vehicle Policy");
                }
                if(count_swis!=0) {
                    for (int i = 0; i < swisList.size(); i++) {
                        //policySpinnerList.add(swisList.get(i).getPolicyNumber());
                        swissTypeString.add(swisList.get(i).getPolicyNumber());
                    }
                } else {
                    swissTypeString.add("No SWIS-F Policy");
                }
                if(count_travel!=0) {
                    for (int i = 0; i < travelList.size(); i++) {
                        // policySpinnerList.add(travelList.get(i).getPolicyNumber());
                        travelTypeString.add(travelList.get(i).getPolicyNumber());
                    }
                } else {
                    swissTypeString.add("No Travel Policy");
                }
                if(count_marine!=0) {
                    for (int i = 0; i < marineList.size(); i++) {
                        //policySpinnerList.add(marineList.get(i).getPolicyNumber());
                        marineTypeString.add(marineList.get(i).getPolicyNumber());
                    }
                } else {
                    marineTypeString.add("No Marine Policy");
                }
                if(count_allrisk!=0) {
                    for (int i = 0; i < allRiskList.size(); i++) {
                        //policySpinnerList.add(allRiskList.get(i).getPolicyNumber());
                        allriskTypeString.add(allRiskList.get(i).getPolicyNumber());
                    }
                } else {
                    allriskTypeString.add("No All-Risk Policy");
                }
                
                if(count_allrisk==0&&count_marine==0&&count_swis==0&&count_travel==0&&count_vehicle==0){
                    showMessage("You have not paid any policy");
                }


            }

            @Override
            public void onFailure(Call<PolicyHead> call, Throwable t) {
                showMessage("Fetch failed, please try again "+t.getMessage());
                Log.i("GEtError",t.getMessage());
            }
        });


    }

    private void getHistory() {


        //get client and call object for request

        Call<TransactionHead> call = client.transaction_hist("Token " + userPreferences.getUserToken());
        call.enqueue(new Callback<TransactionHead>() {
            @Override
            public void onResponse(Call<TransactionHead> call, Response<TransactionHead> response) {

                if (!response.isSuccessful()) {
                    try {
                        APIError apiError = ErrorUtils.parseError(response);

                        showMessage("Fetch Failed: " + apiError.getErrors());
                        Log.i("Invalid Fetch", String.valueOf(apiError.getErrors()));
                        //Log.i("Invalid Entry", response.errorBody().toString());

                    } catch (Exception e) {
                        Log.i("Fetch Failed", e.getMessage());
                        showMessage("Fetch Failed");

                    }

                    return;
                }

                policy_item = response.body().getHistory();


                int count = policy_item.size();

                Log.i("Re-SuccessSize", String.valueOf(policy_item.size()));

                if (count == 0) {
                    mTransactnNotify.setVisibility(View.GONE);

                } else {

                    for (int i = 0; i < count; i++) {
                        checkIncomplete = policy_item.get(i).getStatus();
                        if (checkIncomplete.equals("Pending") || checkIncomplete.equals("Initiated")) {
                            mTransactnNotify.setVisibility(View.VISIBLE);
                            String name = userPreferences.getLastName();
                            String full_note = "Hi! " + name + ", you have an incomplete transaction";
                            mTrasactnNote.setText(full_note);
                            return;
                        }
                    }

                    Log.i("SuccessChecked", checkIncomplete);
                }

            }

            @Override
            public void onFailure(Call<TransactionHead> call, Throwable t) {
                showMessage("Fetch failed, check your internet " + t.getMessage());
                Log.i("GEtError", t.getMessage());
            }
        });


    }

    private void userPolicy() {
        // Create an ArrayAdapter using the string array and a default spinner
        mUserPolicySpinner
                .setAdapter(new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        policySpinnerList));

        mUserPolicySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String policy_type = (String) parent.getItemAtPosition(position);

                if(position!=0) {
                    if(policy_type.equals("Motor")){
                        vehiclePolicy();
                        
                    }else if(policy_type.equals("Swiss-F")){
                        swissPolicy();
                    }
                    else if(policy_type.equals("Marine")){
                        marinePolicy();

                    }
                    else if(policy_type.equals("Travel")){
                        travelPolicy();

                    }
                    else if(policy_type.equals("All Risk")){
                        allriskPolicy();

                    }
                    
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUserPolicySpinner.getItemAtPosition(0);
            }
        });

    }


    private void vehiclePolicy() {
        // Create an ArrayAdapter using the string array and a default spinner
        mUserPolicySpinner
                .setAdapter(new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        vehicleTypeString));

        mUserPolicySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String user_policy_String = (String) parent.getItemAtPosition(position);

                expire_date=vehicleList.get(position).getEnd();
                policy_type="Motor Insurance";

                mPolicyStatus.setVisibility(View.VISIBLE);
                mPolicyType.setText(policy_type);
                mExpireDate.setText(expire_date);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUserPolicySpinner.getItemAtPosition(0);
                mPolicyStatus.setVisibility(View.GONE);
            }
        });

    }

    private void swissPolicy() {
        // Create an ArrayAdapter using the string array and a default spinner
        mUserPolicySpinner
                .setAdapter(new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        swissTypeString));

        mUserPolicySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String user_policy_String = (String) parent.getItemAtPosition(position);

                expire_date=swisList.get(position).getEnd();
                policy_type="Swiss-F Insurance";

                mPolicyStatus.setVisibility(View.VISIBLE);
                mPolicyType.setText(policy_type);
                mExpireDate.setText(expire_date);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUserPolicySpinner.getItemAtPosition(0);
                mPolicyStatus.setVisibility(View.VISIBLE);
            }
        });

    }

    private void marinePolicy() {
        // Create an ArrayAdapter using the string array and a default spinner
        mUserPolicySpinner
                .setAdapter(new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        marineTypeString));

        mUserPolicySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String user_policy_String = (String) parent.getItemAtPosition(position);
                expire_date=marineList.get(position).getEnd();
                policy_type="Marine Insurance";
                mPolicyStatus.setVisibility(View.VISIBLE);
                mPolicyType.setText(policy_type);
                mExpireDate.setText(expire_date);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUserPolicySpinner.getItemAtPosition(0);
                mPolicyStatus.setVisibility(View.VISIBLE);
            }
        });

    }

    private void travelPolicy() {
        // Create an ArrayAdapter using the string array and a default spinner
        mUserPolicySpinner
                .setAdapter(new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        travelTypeString));

        mUserPolicySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String user_policy_String = (String) parent.getItemAtPosition(position);

                expire_date=travelList.get(position).getEnd();
                policy_type="Travel Insurance";

                mPolicyStatus.setVisibility(View.VISIBLE);
                mPolicyType.setText(policy_type);
                mExpireDate.setText(expire_date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUserPolicySpinner.getItemAtPosition(0);
                mPolicyStatus.setVisibility(View.VISIBLE);
            }
        });

    }

    private void allriskPolicy() {
        // Create an ArrayAdapter using the string array and a default spinner
        mUserPolicySpinner
                .setAdapter(new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        allriskTypeString));

        mUserPolicySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String user_policy_String = (String) parent.getItemAtPosition(position);
                expire_date=allRiskList.get(position).getEnd();
                policy_type="AllRisk Insurance";

                mPolicyStatus.setVisibility(View.VISIBLE);
                mPolicyType.setText(policy_type);
                mExpireDate.setText(expire_date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUserPolicySpinner.getItemAtPosition(0);
                mPolicyStatus.setVisibility(View.VISIBLE);
            }
        });

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.product_tag_btn:
                fragment = new QuoteBuyFragment();
                showFragment(fragment);
                break;

            case R.id.news_update_btn:
                /*fragment = new ShopWomenFragment();
                showFragment(fragment);*/
                break;

            case R.id.refresh_policy:
                userPolicy();
                mPolicyStatus.setVisibility(View.GONE);
                break;

        }

    }


    @SuppressLint("CheckResult")
    private void SLide(){

        ArrayList<Integer> listImage = new ArrayList<>();
        ArrayList<String> listName = new ArrayList<>();

        listImage.add(R.drawable.motor);
        listName.add("Motor Insurance");

        listImage.add(R.drawable.marine);
        listName.add("Marine Insurance");

        listImage.add(R.drawable.swiss);
        listName.add("Swiss-F for you Family");

        listImage.add(R.drawable.travel);
        listName.add("Travel Insurance");

        listImage.add(R.drawable.all_risk);
        listName.add("All Risk Insurance");

        listImage.add(R.drawable.other);
        listName.add("Other Insurance");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.img_empty);
        //.placeholder(R.drawable.placeholder)


        for (int i = 0; i < listImage.size(); i++) {
            TextSliderView sliderView = new TextSliderView(getContext());
            // initialize SliderLayout
            sliderView
                    .image(listImage.get(i))
                    .description(listName.get(i))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", listName.get(i));
            mSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mSlider.setPresetTransformer(SliderLayout.Transformer.FlipHorizontal);

        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(4000);
        mSlider.addOnPageChangeListener(this);

    }



    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    @Override
    public void onStop() {
        mSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(getContext(), slider.getBundle().get("extra") +"" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void showMessage(String s) {
        Snackbar.make(mDashboardLayout, s, Snackbar.LENGTH_SHORT).show();
    }


    private void timeout_alert() {

        new AlertDialog.Builder(getContext())
                .setTitle("Error !")
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setMessage("Session Time-Out, Click Okay to re-login")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        startActivity(new Intent(getActivity(), SignIn.class));
                        getActivity().finish();
                    }
                })
                .show();

    }

}
