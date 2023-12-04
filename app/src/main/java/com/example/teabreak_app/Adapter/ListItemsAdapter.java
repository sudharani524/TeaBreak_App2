package com.example.teabreak_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teabreak_app.ModelClass.ListItemsModel;
import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.databinding.DashboardListItemsBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.ViewHolder> {

    Context context;
    List<ListItemsModel> lim=new ArrayList<>();

    public ListItemsAdapter(Context context, List<ListItemsModel> lim) {
        this.context = context;
        this.lim = lim;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DashboardListItemsBinding customCardBinding = DashboardListItemsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(customCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binding.price.setText("₹"+lim.get(position).getPrice());
        holder.binding.listItemName.setText(lim.get(position).getLineItems());
        holder.binding.quantity.setText(lim.get(position).getPackQty());

        String img= Constant.SERVER_BASE_URL+lim.get(position).getImage();
//        Log.e("img",img);
        Picasso.get().load(img).fit().centerInside().into(holder.binding.dashboardImg);

    }

    @Override
    public int getItemCount() {
        return lim.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DashboardListItemsBinding binding;
        public ViewHolder(@NonNull DashboardListItemsBinding dashboardListItemsBinding) {
            super(dashboardListItemsBinding.getRoot());
            this.binding = dashboardListItemsBinding;

        }
    }
}
