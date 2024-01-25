package com.glitss.teabreak_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glitss.teabreak_app.ModelClass.ListItemsModel;
import com.glitss.teabreak_app.Utils.Constant;
import com.glitss.teabreak_app.databinding.DashboardListItemsBinding;
import com.glitss.teabreak_app.repository.ListItemInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.ViewHolder> {

    Context context;
    List<ListItemsModel> lim=new ArrayList<>();
    private ListItemInterface listItemInterface;

    public ListItemsAdapter(Context context, ArrayList<ListItemsModel> lim, ListItemInterface listItemInterface) {
        this.context = context;
        this.lim = lim;
        this.listItemInterface = listItemInterface;
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
        holder.binding.listItemName.setText(lim.get(position).getLine_item_name());
        holder.binding.quantity.setText(lim.get(position).getPack_of_qty());

        String img= Constant.SERVER_BASE_URL+lim.get(position).getImage();
//        Log.e("img",img);
        Picasso.get().load(img).fit().centerInside().into(holder.binding.dashboardImg);

        holder.binding.listCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listItemInterface.OnItemClick(position,v,"card");
            }
        });

        holder.binding.cartDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemInterface.OnItemClick(position,v,"cart");
            }
        });

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
