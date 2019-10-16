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

import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.Model.Etic.EticPolicy;
import com.mike4christ.sti_mobile.Model.Etic.Travel_Info;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.forms_fragment.Etic.EticFragment1;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class EticForm extends AppCompatActivity {



    @BindView(R.id.eticform_toolbar)
    Toolbar toolBar;

   /* @BindView(R.id.message)
    TextView mTextMessage;*/
   UserPreferences userPreferences;


    Fragment fragment;

    String title="";
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etic_form);
        ButterKnife.bind(this);
        userPreferences=new UserPreferences(this);


        Intent intent = getIntent();
        title=intent.getStringExtra(Constant.CARD_OPTION_TITLE);
        applyToolbarChildren(title);


        fragment = new EticFragment1();
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
        userPreferences.setTempEticQuotePrice("0.0");
        finish();
        return super.onOptionsItemSelected(item);
    }


    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_etic_form_container, fragment);
        ft.commit();
    }


    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
