package com.sti.sti_mobile.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sti.sti_mobile.NetworkConnection;
import com.sti.sti_mobile.R;
import com.sti.sti_mobile.UserPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetail extends AppCompatActivity {

    /**
     * ButterKnife Code
     **/
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.news_img)
    ImageView mNewImg;

    @BindView(R.id.news_title_txt)
    TextView mNewsTitle;
    @BindView(R.id.news_info_txt)
    TextView mNewsInfotxt;

    @BindView(R.id.news_datetime_txt)
    TextView mNewsDateTime;

    @BindView(R.id.progress)
    AVLoadingIndicatorView mProgress;

    /**
     * ButterKnife Code
     **/

    Animation slide_front_left;

    private UserPreferences userPreferences;
    NetworkConnection networkConnection = new NetworkConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        userPreferences = new UserPreferences(this);
        applyToolbarChildren("News Detail");


        Intent intent = getIntent();


        String news_info = intent.getStringExtra("news_info");
        //String news_img=intent.getStringExtra("news_img");
        String news_title = intent.getStringExtra("news_title");
        String news_datetime = intent.getStringExtra("news_datetime");

        mNewsInfotxt.setText(news_info);
        mNewsTitle.setText(news_title);
        mNewsDateTime.setText(news_datetime);
        mProgress.setVisibility(View.VISIBLE);
        mNewImg.setImageResource(R.drawable.thumbnail);
        mProgress.setVisibility(View.GONE);

       /* if(news_img==null) {
            mNewImg.setImageResource(R.drawable.thumbnail);
            mProgress.setVisibility(View.GONE);

        }else{
            Glide.with(this).load(news_img).apply(new RequestOptions().fitCenter().circleCrop()).into(mNewImg);
            mProgress.setVisibility(View.GONE);
        }*/

        slide_front_left = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_from_left);


    }


    private void applyToolbarChildren(String title) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
        //setting Elevation for > API 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(10f);
        }

    }


    private void showShortMsg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

}
