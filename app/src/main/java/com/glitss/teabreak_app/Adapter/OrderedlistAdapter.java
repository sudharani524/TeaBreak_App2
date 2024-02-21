package com.glitss.teabreak_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glitss.teabreak_app.ModelClass.OrderedlistModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.repository.itemsorderInterface;

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
        holder.approved_date.setText(slm.get(position).getAccounts_updated_date_time());
        holder.outlet_code.setText(slm.get(position).getOutlet_code());
        holder.delivery_type.setText(slm.get(position).getDelivery_mode());
        holder.tracking_id.setText(slm.get(position).getTracking_id());
        holder.payment_status.setText(slm.get(position).getPayment_status());


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
        TextView Ordernumber,VendorName,totalamount,date,quantity,Wallet_Amount_Used,approved_date,outlet_code;
        TextView tracking_id,payment_status,delivery_type,payment_mode;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Ordernumber=itemView.findViewById(R.id.v_ordernumber);
            VendorName=itemView.findViewById(R.id.V_Name);
            totalamount=itemView.findViewById(R.id.V_Totalamount);
            Wallet_Amount_Used=itemView.findViewById(R.id.V_WalletAmountUsed);
            date=itemView.findViewById(R.id.V_orderdate);
            quantity=itemView.findViewById(R.id.V_orderquantity);
            approved_date=itemView.findViewById(R.id.V_approved_date);
            outlet_code=itemView.findViewById(R.id.tv_outlet_code);
            tracking_id=itemView.findViewById(R.id.tracking_id);
            payment_status=itemView.findViewById(R.id.payment_status);
            delivery_type=itemView.findViewById(R.id.delivery_type);
         //   payment_mode=itemView.findViewById(R.id.payment_mode);

        }
    }

}
