package com.mike4christ.sti_mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.Forms.AllRiskForm;
import com.mike4christ.sti_mobile.Forms.EticForm;
import com.mike4christ.sti_mobile.Forms.MarineForm;
import com.mike4christ.sti_mobile.Forms.MotorInsuredForm;
import com.mike4christ.sti_mobile.Forms.OtherInsuredForm;
import com.mike4christ.sti_mobile.Forms.SwissForm;
import com.mike4christ.sti_mobile.Model.QuoteBuyCard;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.retrofit_interface.ItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuoteBuyAdapter extends RecyclerView.Adapter<QuoteBuyAdapter.MyViewHolder> {

    private Context context;
    private List<QuoteBuyCard> cardList;


    public QuoteBuyAdapter(Context context, List<QuoteBuyCard> cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotebuy_list, parent, false);
        ButterKnife.bind(this, view);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        QuoteBuyCard cardOption = cardList.get(i);

        holder.mProductName.setText(cardOption.getTitle());
        holder.mQuoteBuyImg.setImageResource(cardOption.getThumbnail());

        holder.setItemClickListener(pos -> {

            switch (cardList.get(pos).getTitle()){
                case "Motor Insurance":
                    nextActivity(cardList.get(pos).getTitle(), MotorInsuredForm.class);
                    break;
                case "SWIS-F Insurance":

                    nextActivity(cardList.get(pos).getTitle(), SwissForm.class);

                    break;
                case "Marine Insurance":
                    nextActivity(cardList.get(pos).getTitle(), MarineForm.class);

                    break;
                case "All Risks Insurance":
                    nextActivity(cardList.get(pos).getTitle(), AllRiskForm.class);

                    break;
                case "ETIC Insurance":
                    nextActivity(cardList.get(pos).getTitle(), EticForm.class);
                    break;

                case "Other Insurance":
                    nextActivity(cardList.get(pos).getTitle(), OtherInsuredForm.class);
                    break;

            }

        });
    }

    private void nextActivity(String title, Class productActivityClass) {
        Intent i = new Intent(context, productActivityClass);
        i.putExtra(Constant.CARD_OPTION_TITLE, title);
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /** ButterKnife Code **/
        @BindView(R.id.product_layout)
        LinearLayout mProductLayout;
        @BindView(R.id.quote_buy_img)
        ImageView mQuoteBuyImg;
        @BindView(R.id.product_name)
        TextView mProductName;
        /** ButterKnife Code **/

        ItemClickListener itemClickListener;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }

    }
}

