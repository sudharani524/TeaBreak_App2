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

public class DeliverydetailsAdapter extends RecyclerView.Adapter<DeliverydetailsAdapter.ViewHolder>  {
    Context context;
    List<ListItemsModel> slm=new ArrayList<>();
    public DeliverydetailsAdapter(List<ListItemsModel> slm, Context context) {
        this.slm = slm;
        this.context = context;
    }
    @NonNull
    @Override
    public DeliverydetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ordereddeliverycustomcard,parent,false);
        return new DeliverydetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliverydetailsAdapter.ViewHolder holder, int position) {
        holder.price.setText(slm.get(position).getSub_total_price());
        holder.quantity.setText(slm.get(position).getQuantity());
        holder.name.setText(slm.get(position).getLine_item_name());
        String img= Constant.SERVER_BASE_URL+slm.get(position).getImage();
        Log.d("img",img);
        Picasso.get().load(img).fit().centerInside().into(holder.sample_image);

    }

    @Override
    public int getItemCount() {
        Log.e("listsize", String.valueOf(slm.size()));
        return slm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,quantity,price;
        ImageView sample_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.Productname);
            quantity=itemView.findViewById(R.id.orderedquantity);
            price=itemView.findViewById(R.id.orderedprice);
            sample_image=itemView.findViewById(R.id.listimage);
        }
    }
}
