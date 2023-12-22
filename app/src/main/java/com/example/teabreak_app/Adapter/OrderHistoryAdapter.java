package com.example.teabreak_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teabreak_app.ModelClass.OrderHistoryModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Ui.Cartlist_Activity;
import com.example.teabreak_app.Ui.Checkout;
import com.example.teabreak_app.Ui.Orderdetails;
import com.example.teabreak_app.repository.OrderdetailsInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    List<OrderHistoryModel> ls=new ArrayList<>();
    List<OrderHistoryModel> Orderslist=new ArrayList<>();
    Context context;
    OrderdetailsInterface orderdetailsInterface;
    public OrderHistoryAdapter(List<OrderHistoryModel> ls, Context context,OrderdetailsInterface orderdetailsInterface) {
        this.ls = ls;
        this.context = context;
        this.orderdetailsInterface = orderdetailsInterface;
    }
    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderscustomcard,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ordernumber.setText(ls.get(position).getOrder_no());
       holder.date.setText(ls.get(position).getOrder_date_time());
       holder.totalamount.setText( "₹"+ls.get(position).getTotal_amount());
      holder.deliverytype.setText(ls.get(position).getDelivery_type_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               orderdetailsInterface.OnItemClick(position,holder,"order");

            }
        });

    }
    public void filter(String charText){

        if (charText.length() == 1) {
            this.Orderslist.clear();
            this.Orderslist.addAll(ls);
        }

        charText = charText.toLowerCase(Locale.getDefault());
        ls.clear();

        if (charText.length() == 0) {
            ls.addAll(Orderslist);
            notifyDataSetChanged();
        } else {
            for (OrderHistoryModel ts : Orderslist) {

                if (ts.getOrder_no().trim().toLowerCase(Locale.getDefault()).contains(charText) ||
                        ts.getOrder_id().trim().toLowerCase(Locale.getDefault()).contains(charText)) {
                    ls.add(ts);

                }
            }
            notifyDataSetChanged();
        }

    }


    @Override
    public int getItemCount() {
        Log.e("listsize", String.valueOf(ls.size()));
        return ls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ordernumber,deliverytype,totalamount,date;
        ImageView iv_whatsapp_share;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ordernumber=itemView.findViewById(R.id.ordernumber);
            deliverytype=itemView.findViewById(R.id.DeliveryType);
            totalamount=itemView.findViewById(R.id.Totalamount);
            date=itemView.findViewById(R.id.orderdate);
            iv_whatsapp_share=itemView.findViewById(R.id.iv_whatsapp_share);

        }
    }
}
