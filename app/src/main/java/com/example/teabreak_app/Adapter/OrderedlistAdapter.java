package com.example.teabreak_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teabreak_app.ModelClass.OrderedlistModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Ui.VendorOrderlist;
import com.example.teabreak_app.repository.OrderdetailsInterface;
import com.example.teabreak_app.repository.itemsorderInterface;

import java.util.ArrayList;
import java.util.List;

public class OrderedlistAdapter extends RecyclerView.Adapter<OrderedlistAdapter.ViewHolder> {
    Context context;
    List<OrderedlistModel> slm=new ArrayList<>();
    itemsorderInterface itemsorderInterface;
    public OrderedlistAdapter(List<OrderedlistModel> slm, Context context,itemsorderInterface itemsorderInterface) {
        this.slm = slm;
        this.context = context;
        this.itemsorderInterface=itemsorderInterface;
    }
    @NonNull
    @Override
    public OrderedlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vendorordercustomcard,parent,false);
        return new OrderedlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Ordernumber.setText(slm.get(position).getOrder_no());
        holder.VendorName.setText(slm.get(position).getVendor_name());
        holder.totalamount.setText("₹"+slm.get(position).getTotal_amount());
        holder.quantity.setText(slm.get(position).getTotal_quantity());
        holder.date.setText(slm.get(position).getOrder_date_time());
        holder.Wallet_Amount_Used.setText("₹"+slm.get(position).getUsed_wallet_amount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemsorderInterface.OnItemClick(position,holder,"orders");
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.e("listsize", String.valueOf(slm.size()));
        return slm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Ordernumber,VendorName,totalamount,date,quantity,Wallet_Amount_Used;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Ordernumber=itemView.findViewById(R.id.v_ordernumber);
            VendorName=itemView.findViewById(R.id.V_Name);
            totalamount=itemView.findViewById(R.id.V_Totalamount);
            Wallet_Amount_Used=itemView.findViewById(R.id.V_WalletAmountUsed);
            date=itemView.findViewById(R.id.V_orderdate);
            quantity=itemView.findViewById(R.id.V_orderquantity);


        }
    }
}
