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

import com.example.teabreak_app.ModelClass.OrderdetailsModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Orderdetailsadapter extends RecyclerView.Adapter<Orderdetailsadapter.ViewHolder> {
    Context context;
    List<OrderdetailsModel> slm=new ArrayList<>();
    public Orderdetailsadapter(List<OrderdetailsModel> slm, Context context) {
        this.slm = slm;
        this.context = context;
    }
    @NonNull
    @Override
    public Orderdetailsadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_list_card,parent,false);
        return new Orderdetailsadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Orderdetailsadapter.ViewHolder holder, int position) {
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
            name=itemView.findViewById(R.id.name);
            quantity=itemView.findViewById(R.id.packquantity);
            price=itemView.findViewById(R.id.price);
            sample_image=itemView.findViewById(R.id.listimage);
        }
    }
}
