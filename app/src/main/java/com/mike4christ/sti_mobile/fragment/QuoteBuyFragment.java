package com.mike4christ.sti_mobile.fragment;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.card.MaterialCardView;
import com.mike4christ.sti_mobile.Model.QuoteBuyCard;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.adapter.QuoteBuyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QuoteBuyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.recycler_quotebuy)
    RecyclerView recyclerView;

    private QuoteBuyAdapter quotebuyAdapter;
    private List<QuoteBuyCard> cardList;
    LinearLayoutManager layoutManager;



    public QuoteBuyFragment() {
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
    public static QuoteBuyFragment newInstance(String param1, String param2) {
        QuoteBuyFragment fragment = new QuoteBuyFragment();
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
        View view=inflater.inflate(R.layout.fragment_quotebuy, container, false);
        ButterKnife.bind(this,view);

        cardList = new ArrayList<>();
        quotebuyAdapter = new QuoteBuyAdapter(getContext(), cardList);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        quotebuyAdapter = new QuoteBuyAdapter(getContext(), cardList);
        recyclerView.setAdapter(quotebuyAdapter);

//        populating the card
        insertElement();


        return  view;
    }

    private void insertElement() {
//        referencing drawable for the logo
        int[] image = new int[]{
                R.drawable.motor,
                R.drawable.swiss,
                R.drawable.marine,
                R.drawable.all_risk,
                R.drawable.travel
                /* R.drawable.other*/
        };

        QuoteBuyCard m = new QuoteBuyCard("Motor Insurance", image[0]);
        cardList.add(m);

        m = new QuoteBuyCard("SWIS-F Insurance", image[1]);
        cardList.add(m);

        m = new QuoteBuyCard("Marine Insurance", image[2]);
        cardList.add(m);

        m = new QuoteBuyCard("All Risks Insurance", image[3]);
        cardList.add(m);

        m = new QuoteBuyCard("ETIC Insurance", image[4]);
        cardList.add(m);

       /* m = new QuoteBuyCard("Other Insurance", image[5]);
        cardList.add(m);*/






    }


}
