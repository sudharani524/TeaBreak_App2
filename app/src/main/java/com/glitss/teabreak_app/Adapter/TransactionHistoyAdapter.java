package com.glitss.teabreak_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glitss.teabreak_app.ModelClass.OrderHistoryModel;
import com.glitss.teabreak_app.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoyAdapter extends RecyclerView.Adapter<TransactionHistoyAdapter.ViewHolder> {

    List<OrderHistoryModel> t_list=new ArrayList<>();
    Context context;

    public TransactionHistoyAdapter(Context context,List<OrderHistoryModel> t_list) {
        this.t_list = t_list;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionHistoyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoyAdapter.ViewHolder holder, int position) {

        if(t_list.get(position).getOrder_no()!=null){
            Log.e("order_num",t_list.get(position).getOrder_no());
            holder.tv_order_no.setText(t_list.get(position).getOrder_no());
        }else{
            holder.tv_order_no.setText("null");
        }


        if(t_list.get(position).getDelivery_type_name()!=null){
            holder.tv_delivery_mode.setText(t_list.get(position).getDelivery_type_name());
        }else{
            holder.tv_delivery_mode.setText("null");
        }

        if(t_list.get(position).getTotal_amount()!=null){
            holder.tv_order_total.setText("₹"+t_list.get(position).getTotal_amount());
        }else {
            holder.tv_order_total.setText("null");
        }

        if(t_list.get(position).getUsed_wallet_amount()!=null){
            holder.tv_wallet_used_amount.setText("₹"+t_list.get(position).getUsed_wallet_amount());
        }else{
            holder.tv_wallet_used_amount.setText("null");
        }


        if(t_list.get(position).getOrder_date_time()!=null){
            holder.tv_order_date.setText(t_list.get(position).getOrder_date_time());
        }else{
            holder.tv_order_date.setText("null");
        }

        if(t_list.get(position).getTotal_delivery_charges()!=null){
            holder.tv_delivery_charges.setText("₹"+t_list.get(position).getTotal_delivery_charges());
        }else{
            holder.tv_delivery_charges.setText("null");
        }


        if(t_list.get(position).getStatus_message()!=null){
            holder.tv_payment_status.setText(t_list.get(position).getStatus_message());
        }else{
            holder.tv_payment_status.setText("null");
        }



    }

    @Override
    public int getItemCount() {
        return t_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_no,tv_delivery_mode,tv_order_total,tv_wallet_used_amount,tv_order_date,tv_delivery_charges,tv_payment_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_order_no=itemView.findViewById(R.id.ordernumber);
            tv_delivery_mode=itemView.findViewById(R.id.DeliveryType);
            tv_order_total=itemView.findViewById(R.id.Totalamount);
            tv_wallet_used_amount=itemView.findViewById(R.id.WalletusedAmount);
            tv_order_date=itemView.findViewById(R.id.orderdate);
            tv_delivery_charges=itemView.findViewById(R.id.deliveryCharges);
            tv_payment_status=itemView.findViewById(R.id.payment_status);
        }
    }
}
