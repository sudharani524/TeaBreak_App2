package com.example.teabreak_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teabreak_app.ModelClass.ListItemsModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemslistAdapter extends RecyclerView.Adapter<ItemslistAdapter.ViewHolder> {
    Context context;
    List<ListItemsModel> slm=new ArrayList<>();
    public ItemslistAdapter(Context context, List<ListItemsModel> slm) {
        this.context = context;
        this.slm = slm;
    }

    @NonNull
    @Override
    public ItemslistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item_activity,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemslistAdapter.ViewHolder holder, int position) {
        holder.Productname.setText(slm.get(position).getLineItems());
        holder.quantity.setText(slm.get(position).getPackQty());
        holder.price.setText( "₹"+slm.get(position).getPrice());
        String img= Constant.SERVER_BASE_URL+slm.get(position).getImage();

        Log.d("img",img);
        Picasso.get().load(img).fit().centerInside().into(holder.sample_image);
    }

    @Override
    public int getItemCount() {
        return slm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sample_image;
        TextView Productname,quantity,price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Productname=itemView.findViewById(R.id.name);
            quantity=itemView.findViewById(R.id.packquantity);
            price=itemView.findViewById(R.id.price);
            sample_image=itemView.findViewById(R.id.listimage);
        }
    }
}
