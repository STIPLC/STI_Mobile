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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    /** ButterKnife Code **/
    String policyNumString;

    Fragment fragment;
    UserPreferences userPreferences;
    String firstname="";
    String lastname="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this,view);
        userPreferences=new UserPreferences(getContext());



        mReadMore.setPaintFlags(mReadMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mReadMore.setText("Click to Register");

        setAction();
        setUp();
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

    }

    private void userPolicy() {
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.user_polices,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mUserPolicySpinner.setAdapter(staticAdapter);

        mUserPolicySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String user_policy_String = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUserPolicySpinner.getItemAtPosition(0);
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
}
