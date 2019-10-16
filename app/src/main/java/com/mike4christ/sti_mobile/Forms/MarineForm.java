package com.mike4christ.sti_mobile.Forms;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.Model.Marine.CargoDetail;
import com.mike4christ.sti_mobile.Model.Marine.MarinePolicy;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.forms_fragment.Marine.MarineFragment1;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class MarineForm extends AppCompatActivity {



    @BindView(R.id.marineform_toolbar)
    Toolbar toolBar;

   /* @BindView(R.id.message)
    TextView mTextMessage;*/


    Fragment fragment;
    UserPreferences userPreferences;

    String title="";
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marine_form);
        ButterKnife.bind(this);
        userPreferences=new UserPreferences(this);
        realm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        title=intent.getStringExtra(Constant.CARD_OPTION_TITLE);
        applyToolbarChildren(title);


        fragment = new MarineFragment1();
        showFragment(fragment);


    }


    private void applyToolbarChildren(String title) {
        setSupportActionBar(toolBar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(title);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
        //setting Elevation for > API 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolBar.setElevation(10f);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        userPreferences.setTempMarineQuotePrice("0.0");
        finish();
        return super.onOptionsItemSelected(item);
    }


    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_marine_form_container, fragment);
        ft.commit();
    }


    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }



}
