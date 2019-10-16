package com.mike4christ.sti_mobile.Forms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.MainActivity;
import com.mike4christ.sti_mobile.Model.Auth.ChangePassPost;
import com.mike4christ.sti_mobile.Model.Auth.UserPassword;
import com.mike4christ.sti_mobile.Model.Errors.APIError;
import com.mike4christ.sti_mobile.Model.Errors.ErrorUtils;
import com.mike4christ.sti_mobile.Model.Swiss.AdditionInsured;
import com.mike4christ.sti_mobile.Model.Swiss.SwissInsured;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.SignIn;
import com.mike4christ.sti_mobile.UserPreferences;
import com.mike4christ.sti_mobile.forms_fragment.Swiss.SwissFragment1;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwissForm extends AppCompatActivity {



    @BindView(R.id.swissform_toolbar)
    Toolbar toolBar;



    Fragment fragment;

    Realm realm;
    String title="";
    UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiiss_form);
        ButterKnife.bind(this);
        userPreferences=new UserPreferences(this);


        Intent intent = getIntent();
        title=intent.getStringExtra(Constant.CARD_OPTION_TITLE);
        applyToolbarChildren(title);

        showNotify();
        fragment = new SwissFragment1();
        showFragment(fragment);


    }


    private void applyToolbarChildren(String title) {
        setSupportActionBar(toolBar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
        //setting Elevation for > API 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolBar.setElevation(10f);
        }

    }

    private void showNotify() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Note!");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.swiss_notification, null);
        builder.setView(dialogView);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.create().show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        userPreferences.setTempSwissQuotePrice("0.0");
        userPreferences.setSwissIPersonal_QuotePrice(0);
        return super.onOptionsItemSelected(item);
    }


    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_swiss_form_container, fragment);
        ft.commit();
    }


    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


}
