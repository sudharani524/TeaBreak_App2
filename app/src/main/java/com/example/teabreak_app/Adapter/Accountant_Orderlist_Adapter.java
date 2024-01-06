package com.example.teabreak_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teabreak_app.ModelClass.OrderdetailsModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Ui.Checkout;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.repository.ListItemInterface;

import java.util.ArrayList;
import java.util.List;

import kotlin.reflect.KVisibility;

public class Accountant_Orderlist_Adapter extends RecyclerView.Adapter<Accountant_Orderlist_Adapter.ViewHolder> {

    Context context;
    List<OrderdetailsModel> od_list=new ArrayList<>();
    AlertDialog alertDialog;

    private ListItemInterface listItemInterface;
    String list_type;

    public Accountant_Orderlist_Adapter(Context context, List<OrderdetailsModel> od_list,String list_type,ListItemInterface listItemInterface) {
        this.context = context;
        this.od_list = od_list;
        this.listItemInterface = listItemInterface;
        this.list_type=list_type;
    }

    @NonNull
    @Override
    public Accountant_Orderlist_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_ordered_list_card_accounts,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Accountant_Orderlist_Adapter.ViewHolder holder, int position) {
        holder.order_id.setText(od_list.get(position).getOrder_id());
        holder.transaction_id.setText(od_list.get(position).getPayment_trans_id());
        holder.payment_mode.setText(od_list.get(position).getPayment_mode());
        holder.paid_amount.setText("₹"+od_list.get(position).getPaid_amount());
        holder.wallet_used_amt.setText("₹"+od_list.get(position).getUsed_wallet_amount());
        holder.time_stamp.setText(od_list.get(position).getOrder_date_time());
        holder.vendor_name.setText(SaveAppData.getLoginData().getUsername());
        holder.no_of_items.setText(od_list.get(position).getNo_of_order_items());


        if(list_type.equalsIgnoreCase("PendingList")){
            holder.tv_status.setText("Pending");
            holder.orders_edit_btn.setVisibility(View.VISIBLE);
            holder.orders_edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listItemInterface.OnItemClick(position,v,"s");

                }
            });
        }else{
            holder.tv_status.setText("Approved");
            holder.orders_edit_btn.setVisibility(View.GONE);
        }


    }



    @Override
    public int getItemCount() {
        return od_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_id,transaction_id,payment_mode,paid_amount,time_stamp,vendor_name,no_of_items,tv_status,wallet_used_amt;
        AppCompatButton orders_edit_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id=itemView.findViewById(R.id.order_id);
            transaction_id=itemView.findViewById(R.id.transaction_id);
            payment_mode=itemView.findViewById(R.id.payment_mode);
            paid_amount=itemView.findViewById(R.id.paid_amount);
            time_stamp=itemView.findViewById(R.id.time_stamp);
            vendor_name=itemView.findViewById(R.id.vendor_name);
            no_of_items=itemView.findViewById(R.id.no_of_items);
            tv_status=itemView.findViewById(R.id.tv_status);
            orders_edit_btn=itemView.findViewById(R.id.orders_edit_btn);
            wallet_used_amt=itemView.findViewById(R.id.wallet_used_amt);
        }
    }
}
