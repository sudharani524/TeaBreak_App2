package com.example.teabreak_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teabreak_app.ModelClass.OrderHistoryModel;
import com.example.teabreak_app.R;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    List<OrderHistoryModel> ls=new ArrayList<>();
    Context context;
    public OrderHistoryAdapter(List<OrderHistoryModel> ls, Context context) {
        this.ls = ls;
        this.context = context;
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
       holder.totalamount.setText(ls.get(position).getTotal_amount());
      holder.deliverytype.setText(ls.get(position).getDelivery_type_name());


    }

    @Override
    public int getItemCount() {
        Log.e("listsize", String.valueOf(ls.size()));
        return ls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ordernumber,deliverytype,totalamount,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ordernumber=itemView.findViewById(R.id.ordernumber);
            deliverytype=itemView.findViewById(R.id.DeliveryType);
            totalamount=itemView.findViewById(R.id.Totalamount);
            date=itemView.findViewById(R.id.orderdate);
        }
    }
}
