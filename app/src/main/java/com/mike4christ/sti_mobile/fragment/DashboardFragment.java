package com.mike4christ.sti_mobile.fragment;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.MyPolicies.AllRisk;
import com.mike4christ.sti_mobile.Model.MyPolicies.Marine;
import com.mike4christ.sti_mobile.Model.MyPolicies.PolicyHead;
import com.mike4christ.sti_mobile.Model.MyPolicies.Swis;
import com.mike4christ.sti_mobile.Model.MyPolicies.Travel;
import com.mike4christ.sti_mobile.Model.MyPolicies.Vehicle;
import com.mike4christ.sti_mobile.Model.ServiceGenerator;
import com.mike4christ.sti_mobile.Model.Vehicle.VehicleBrand.VehicleData;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.adapter.PoliciesAdapter.AllRiskAdapter;
import com.mike4christ.sti_mobile.adapter.PoliciesAdapter.EticAdapter;
import com.mike4christ.sti_mobile.adapter.PoliciesAdapter.MarineAdapter;
import com.mike4christ.sti_mobile.adapter.PoliciesAdapter.SwissAdapter;
import com.mike4christ.sti_mobile.adapter.PoliciesAdapter.VehicleAdapter;
import com.mike4christ.sti_mobile.retrofit_interface.ApiInterface;

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

    Fragment fragment;
    UserPreferences userPreferences;

    //The variables for VehincleData
    List<Vehicle> vehicleList;
    List<Swis> swisList;
    List<Marine> marineList;
    List<Travel> travelList;
    List<AllRisk> allRiskList;
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
        userPolicy();
        SLide();
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
                        //Log.i("Invalid Entry", response.errorBody().toString());

                    } catch (Exception e) {
                        Log.i("Fetch Failed", e.getMessage());
                        showMessage("Fetch Failed");

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
                    for(int i=0; i<vehicleList.size();i++){
                       // policySpinnerList.add(vehicleList.get(i).getPolicyNumber());
                        vehicleTypeString.add(vehicleList.get(i).getPolicyNumber());
                    }
                }
                if(count_swis!=0) {
                    for (int i = 0; i < swisList.size(); i++) {
                        //policySpinnerList.add(swisList.get(i).getPolicyNumber());
                        swissTypeString.add(swisList.get(i).getPolicyNumber());
                    }
                }
                if(count_travel!=0) {
                    for (int i = 0; i < travelList.size(); i++) {
                       // policySpinnerList.add(travelList.get(i).getPolicyNumber());
                        travelTypeString.add(travelList.get(i).getPolicyNumber());
                    }
                }
                if(count_marine!=0) {
                    for (int i = 0; i < marineList.size(); i++) {
                        //policySpinnerList.add(marineList.get(i).getPolicyNumber());
                        marineTypeString.add(marineList.get(i).getPolicyNumber());
                    }
                }
                if(count_allrisk!=0) {
                    for (int i = 0; i < allRiskList.size(); i++) {
                        //policySpinnerList.add(allRiskList.get(i).getPolicyNumber());
                        allriskTypeString.add(allRiskList.get(i).getPolicyNumber());
                    }
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
        Toast.makeText(getContext(), slider.getBundle().get("extra") +"" , Toast.LENGTH_SHORT).show();
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
}
