package com.sti.sti_mobile.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.sti.sti_mobile.R;
import com.sti.sti_mobile.UserPreferences;
import com.sti.sti_mobile.adapter.PoliciesAdapter.AllRiskAdapter;
import com.sti.sti_mobile.adapter.PoliciesAdapter.EticAdapter;
import com.sti.sti_mobile.adapter.PoliciesAdapter.MarineAdapter;
import com.sti.sti_mobile.adapter.PoliciesAdapter.SwissAdapter;
import com.sti.sti_mobile.adapter.PoliciesAdapter.VehicleAdapter;
import com.sti.sti_mobile.retrofit_interface.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPoliciesFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /** ButterKnife Code **/
    @BindView(R.id.my_policies_layout)
    LinearLayout mMyPoliciesLayout;
    @BindView(R.id.avi1)
    com.wang.avi.AVLoadingIndicatorView mAvi1;
    @BindView(R.id.search_not_found_layout)
    LinearLayout mSearchNotFoundLayout;
    @BindView(R.id.vehicle_policy_layout)
    LinearLayout mVehiclePolicyLayout;
    @BindView(R.id.recycler_vehicle)
    RecyclerView mRecyclerVehicle;
    @BindView(R.id.swiss_policy_layout)
    LinearLayout mSwissPolicyLayout;
    @BindView(R.id.recycler_swiss)
    RecyclerView mRecyclerSwiss;
    @BindView(R.id.marine_policy_layout)
    LinearLayout mMarinePolicyLayout;
    @BindView(R.id.recycler_marine)
    RecyclerView mRecyclerMarine;
    @BindView(R.id.etic_policy_layout)
    LinearLayout mEticPolicyLayout;
    @BindView(R.id.recycler_etic)
    RecyclerView mRecyclerEtic;
    @BindView(R.id.allrisk_policy_layout)
    LinearLayout mAllriskPolicyLayout;
    @BindView(R.id.recycler_allrisk)
    RecyclerView mRecyclerAllrisk;
    @BindView(R.id.allPolicyLayout)
    ScrollView mAllPolicyLayout;

    @BindView(R.id.vehicle_count)
    TextView vehicle_count;
    @BindView(R.id.swiss_count)
    TextView swiss_count;
    @BindView(R.id.marine_count)
    TextView marine_count;
    @BindView(R.id.allrisk_count)
    TextView allrisk_count;
    @BindView(R.id.travel_count)
    TextView travel_count;







    /** ButterKnife Code **/

    //Adapters
    private VehicleAdapter vehicleAdapter;
    private AllRiskAdapter allRiskAdapter;
    private EticAdapter eticAdapter;
    private MarineAdapter marineAdapter;
    private SwissAdapter swissAdapter;

    //list
    List<Vehicle> vehicleList;
    List<Swis> swisList;
    List<Marine> marineList;
    List<Travel> travelList;
    List<AllRisk> allRiskList;

    
    LinearLayoutManager layoutManager;
    UserPreferences userPreferences;

    ApiInterface client= ServiceGenerator.createService(ApiInterface.class);



    public MyPoliciesFragment() {
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
    public static MyPoliciesFragment newInstance(String param1, String param2) {
        MyPoliciesFragment fragment = new MyPoliciesFragment();
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
        View view=inflater.inflate(R.layout.fragment_my_policies, container, false);
        ButterKnife.bind(this,view);
        userPreferences=new UserPreferences(getContext());

        getPolicies();
        
        return  view;
    }



    private void getPolicies(){

        mAvi1.setVisibility(View.VISIBLE);
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
                        mAvi1.setVisibility(View.GONE);

                    }

                    mAvi1.setVisibility(View.GONE);

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

                mAvi1.setVisibility(View.GONE);

                if(count_vehicle==0){
                    //mVehiclePolicyLayout.setVisibility(View.GONE);
                    vehicle_count.setText("Vehicle Insurance(0)");
                }
                if (count_swis == 0) {
                    // mSwissPolicyLayout.setVisibility(View.GONE);
                    swiss_count.setText("SWIS-F Insurance(0)");
                }
                if (count_travel == 0) {
                    // mEticPolicyLayout.setVisibility(View.GONE);
                    travel_count.setText("ETIC(Travel) Insurance(0)");
                }
                if (count_allrisk == 0) {
                    //mAllriskPolicyLayout.setVisibility(View.GONE);
                    allrisk_count.setText("All-Risk Insurance(0)");
                }
                if (count_marine == 0) {
                    //mMarinePolicyLayout.setVisibility(View.GONE);
                    marine_count.setText("Marine Insurance(0)");
                }

                if(count_allrisk==0&&count_marine==0&&count_swis==0&&count_travel==0&&count_vehicle==0){
                    mSearchNotFoundLayout.setVisibility(View.VISIBLE);
                    //mAllPolicyLayout.setVisibility(View.GONE);
                    return;
                }

                //vehicle
                layoutManager = new LinearLayoutManager(getContext());
                mRecyclerVehicle.setLayoutManager(layoutManager);
                vehicleAdapter = new VehicleAdapter(getContext(), vehicleList);
                mRecyclerVehicle.setAdapter(vehicleAdapter);
                vehicleAdapter.notifyDataSetChanged();

                //swiss
                layoutManager = new LinearLayoutManager(getContext());
                mRecyclerSwiss.setLayoutManager(layoutManager);
                swissAdapter = new SwissAdapter(getContext(), swisList);
                mRecyclerSwiss.setAdapter(swissAdapter);
                swissAdapter.notifyDataSetChanged();

                //marine
                layoutManager = new LinearLayoutManager(getContext());
                mRecyclerMarine.setLayoutManager(layoutManager);
                marineAdapter = new MarineAdapter(getContext(), marineList);
                mRecyclerMarine.setAdapter(marineAdapter);
                marineAdapter.notifyDataSetChanged();

                //etic
                layoutManager = new LinearLayoutManager(getContext());
                mRecyclerEtic.setLayoutManager(layoutManager);
                eticAdapter = new EticAdapter(getContext(), travelList);
                mRecyclerEtic.setAdapter(eticAdapter);
                eticAdapter.notifyDataSetChanged();

                //all risk
                layoutManager = new LinearLayoutManager(getContext());
                mRecyclerAllrisk.setLayoutManager(layoutManager);
                allRiskAdapter = new AllRiskAdapter(getContext(), allRiskList);
                mRecyclerAllrisk.setAdapter(allRiskAdapter);
                allRiskAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<PolicyHead> call, Throwable t) {
                showMessage("Fetch failed, please try again "+t.getMessage());
                Log.i("GEtError",t.getMessage());
            }
        });


    }

    private void showMessage(String s) {
        Snackbar.make(mMyPoliciesLayout, s, Snackbar.LENGTH_SHORT).show();
    }


}
