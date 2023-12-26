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

import com.example.teabreak_app.ModelClass.ItemsorderedModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.repository.OrderListitems;
import com.example.teabreak_app.repository.itemsorderInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemsOrderAdapter extends RecyclerView.Adapter<ItemsOrderAdapter.ViewHolder>{
    Context context;
    List<ItemsorderedModel> slm=new ArrayList<>();
    OrderListitems orderListitems;
    public ItemsOrderAdapter(List<ItemsorderedModel> slm, Context context, String ordered_list, OrderListitems orderListitems) {
        this.slm = slm;
        this.context = context;
        this.orderListitems=orderListitems;
    }
    @NonNull
    @Override
    public ItemsOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_ordered_list_card,parent,false);
        return new ItemsOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsOrderAdapter.ViewHolder holder, int position) {
        holder.price.setText( "₹"+slm.get(position).getSub_total_price());
        holder.quantity.setText(slm.get(position).getPack_of_qty());
        holder.name.setText(slm.get(position).getLine_item_name());
        holder.orderedquantity.setText(slm.get(position).getQuantity());
        String img= Constant.SERVER_BASE_URL+slm.get(position).getImage();
        Log.d("img",img);
        Picasso.get().load(img).fit().centerInside().into(holder.sample_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderListitems.OnItemClick(position,holder,"ordercount".toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.e("listsize", String.valueOf(slm.size()));
        return slm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,quantity,price,orderedquantity;
        ImageView sample_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.D_name);
            quantity=itemView.findViewById(R.id.D_quantity);
            price=itemView.findViewById(R.id.D_price);
            orderedquantity=itemView.findViewById(R.id.D_Quantity);
            sample_image=itemView.findViewById(R.id.listimage);
        }
    }
}
