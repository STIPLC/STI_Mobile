package com.mike4christ.sti_mobile.adapter.PoliciesAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mike4christ.sti_mobile.Constant;
import com.mike4christ.sti_mobile.Model.MyPolicies.Marine;
import com.mike4christ.sti_mobile.R;
import com.mike4christ.sti_mobile.activity.MyMarineDetail;
import com.mike4christ.sti_mobile.activity.PolicyPaymentActivity;
import com.mike4christ.sti_mobile.retrofit_interface.ItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarineAdapter extends RecyclerView.Adapter<MarineAdapter.MyViewHolder> {

    private Context context;
    private List<Marine> marineList;


    public MarineAdapter(Context context, List<Marine> marineList) {
        this.context = context;
        this.marineList = marineList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marine_policies_list, parent, false);
        ButterKnife.bind(this, view);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Marine marineOption = marineList.get(i);

        holder.mPolicyNum.setText(marineOption.getPolicyNumber());
        holder.mPrice.setText(marineOption.getPrice());
        holder.mDateTime.setText(marineOption.getCreatedAt());
        holder.mStatus.setText(marineOption.getStatus());
        holder.mPaymentStatus.setText(marineOption.getPaymentStatus());
        holder.mPolicyType.setText(marineOption.getPolicyType());

       /* "id": 331,
                "customer_id": "478",
                "policy_number": "MAR/MA/09/19/SA/00105",
                "period": "365",
                "policy_type": "Marine Insurance",
                "description": "sddsds",
                "pfi_number": "dssd",
                "pfi_date": "2019-09-18",
                "quantity": "1",
                "created_at": "2019-09-18 16:46:49",
                "updated_at": "2019-09-18 16:49:04",
                "value": "500",
                "conversion_rate": "2",
                "loading_port": "sccs",
                "discharge_port": "cs",
                "conveyance_mode": "Air",
                "start": "2019-09-18",
                "end": "2020-09-17",
                "status": "Active",
                "payment_reference": "15688252098274942250E",
                "payment_status": "paid",
                "user_id": "89",
                "interest": null,
                "price": "1.1"*/
       
        holder.setItemClickListener(pos -> {

            nextActivity(marineList.get(pos).getPolicyNumber(), marineList.get(pos).getDescription(),marineList.get(pos).getValue()
                    ,marineList.get(pos).getLoadingPort(),marineList.get(pos).getDischargePort(),marineList.get(pos).getPolicyType(),
                    marineList.get(pos).getPrice(),marineList.get(pos).getStart(),marineList.get(pos).getEnd(),marineList.get(pos).getPaymentReference()
                    ,marineList.get(pos).getStatus(),marineList.get(pos).getPfiDate(),marineList.get(pos).getPfiNumber(),marineList.get(pos).getConveyanceMode(),marineList.get(pos).getPaymentStatus(),marineList.get(pos).getConversionRate()
                    , MyMarineDetail.class);



            });
    }

    private void nextActivity(String policy_num, String desc,String value
                    ,String loadingPort,String dischargeport,String policy_type,
                    String price,String start_date,String end_date,String payment_ref
                    ,String status,String Pfi_date,String pfi_num,String covey_mode,String payment_status,String coversion_rate
                    , Class marineActivityClass) {
        Intent i = new Intent(context, marineActivityClass);

        i.putExtra("policy_num", policy_num);
        i.putExtra("desc", desc);
        i.putExtra("loadingPort", loadingPort);
        i.putExtra("dischargeport", dischargeport);
        i.putExtra("policy_type", policy_type);
        i.putExtra("start_date", start_date);
        i.putExtra("end_date", end_date);
        i.putExtra("price", price);
        i.putExtra("payment_ref", payment_ref);
        i.putExtra("payment_status", payment_status);
        i.putExtra("status", status);
        i.putExtra("Pfi_date", Pfi_date);
        i.putExtra("pfi_num", pfi_num);
        i.putExtra("value", value);
        i.putExtra("covey_mode", covey_mode);
        i.putExtra("coversion_rate", coversion_rate);

        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return marineList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /** ButterKnife Code **/
        @BindView(R.id.transaction_card)
        MaterialCardView mTransactionCard;
        @BindView(R.id.product_thumnail)
        ImageView mProductThumnail;
        @BindView(R.id.policy_num)
        TextView mPolicyNum;
        @BindView(R.id.price)
        TextView mPrice;
        @BindView(R.id.policy_type)
        TextView mPolicyType;
        @BindView(R.id.date_time)
        TextView mDateTime;
        @BindView(R.id.status)
        TextView mStatus;
        @BindView(R.id.payment_status)
        TextView mPaymentStatus;
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

