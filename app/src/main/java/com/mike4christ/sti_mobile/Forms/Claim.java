package com.mike4christ.sti_mobile.Forms;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.forms_fragment.Claim.SubFragment_Claim;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Claim extends AppCompatActivity {



    @BindView(R.id.makeclaimform_toolbar)
    Toolbar toolBar;




    Fragment fragment;

    String title="Make a Claim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeclaim_form);
        ButterKnife.bind(this);


        applyToolbarChildren(title);


        fragment = new SubFragment_Claim();
        showFragment(fragment);


    }


    private void applyToolbarChildren(String title) {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
        //setting Elevation for > API 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolBar.setElevation(10f);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_makeclaim_form_container, fragment);
        ft.commit();
    }
}
