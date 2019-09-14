package com.mike4christ.sti_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PolicyPaymentActivity extends AppCompatActivity {

    @BindView(R.id.amount)
    TextView mAmount;
    @BindView(R.id.policy_num)
    TextView mPolicyNum;

    String amount="",poly_num="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_payment);
        ButterKnife.bind(this);

        Intent intent=getIntent();
        amount=intent.getStringExtra(Constant.TOTAL_PRICE);
        poly_num=intent.getStringExtra(Constant.POLICY_NUM);

        mAmount.setText(amount);
        mPolicyNum.setText(poly_num);

    }
}
