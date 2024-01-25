package com.glitss.teabreak_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glitss.teabreak_app.ModelClass.WallethistoryModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Ui.CreditedListFragment;
import com.glitss.teabreak_app.Ui.DebitedlistFragment;

import java.util.ArrayList;
import java.util.List;

public class CreditedListAdapter extends RecyclerView.Adapter<CreditedListAdapter.ViewHolder> {
    Context context;
    List<WallethistoryModel> nm=new ArrayList<>();
    public CreditedListAdapter(Context context, ArrayList<WallethistoryModel> nm, CreditedListFragment creditedListFragment) {
        this.context = context;
        this.nm = nm;

    }

    public CreditedListAdapter(Context context, ArrayList<WallethistoryModel> nm, DebitedlistFragment debitedlistFragment) {
        this.context = context;
        this.nm = nm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.walletcustomcard,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditedListAdapter.ViewHolder holder, int position) {
        holder.amount.setText(nm.get(position).getUsed_amount());
        holder.date.setText(nm.get(position).getOrder_date_time());
        holder.order_no.setText(nm.get(position).getOrder_id());

    }

    @Override
    public int getItemCount() {
        return nm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_no,amount,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_no=itemView.findViewById(R.id.ordernumber);
            amount=itemView.findViewById(R.id.Walletamount);
            date=itemView.findViewById(R.id.date);
        }
    }
}
