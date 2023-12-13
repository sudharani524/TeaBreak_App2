package com.example.teabreak_app.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teabreak_app.ModelClass.ListItemsModel;
import com.example.teabreak_app.ModelClass.OrderHistoryModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.repository.CartInterface;
import com.example.teabreak_app.repository.ListItemInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemslistAdapter extends RecyclerView.Adapter<ItemslistAdapter.ViewHolder> {
    Context context;
    int quantity;
    List<ListItemsModel> slm=new ArrayList<>();
    List<ListItemsModel>Itemslist=new ArrayList<>();
    String item_type;
    ArrayList<String> qty_array=new ArrayList<>();
    ArrayAdapter qty_adapter;
    static String selected_qty;


    private ListItemInterface listItemInterface;
    private CartInterface cartInterface;

    public ItemslistAdapter(Context context, List<ListItemsModel> slm,String item_type,ListItemInterface listItemInterface) {
        this.context = context;
        this.slm = slm;
        this.listItemInterface = listItemInterface;
        this.item_type = item_type;
    }

    public ItemslistAdapter(List<ListItemsModel> slm,Context context,String item_type,CartInterface cartInterface) {
        this.context = context;
        this.slm = slm;
        this.cartInterface = cartInterface;
        this.item_type = item_type;
    }

    @NonNull
    @Override
    public ItemslistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item_activity,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemslistAdapter.ViewHolder holder, int position) {


      /*  if (quantity == 0) {
            holder.txtQuantity.setText(String.valueOf(quantity));
        }*/

      /*  holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count= Integer.parseInt(String.valueOf(holder.txtQuantity.getText()));
                count++;
                holder.txtQuantity.setText("" + count);
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count= Integer.parseInt(String.valueOf(holder.txtQuantity.getText()));
                if (count == 0) {
                    holder.txtQuantity.setText("0");
                } else {
                    count -= 1;
                    holder.txtQuantity.setText("" + count);
                }
            }
        });*/
        if(item_type.equalsIgnoreCase("list_items")){
            holder.Productname.setText(slm.get(position).getLine_item_name());
            holder.quantity.setText(slm.get(position).getPack_of_qty());
            holder.price.setText( "₹"+slm.get(position).getPrice());
            Log.e("price",slm.get(position).getPrice());
            String img= Constant.SERVER_BASE_URL+slm.get(position).getImage();

            Log.d("img",img);
            Picasso.get().load(img).fit().centerInside().into(holder.sample_image);
            holder.add_cart.setVisibility(View.VISIBLE);
            holder.card.setVisibility(View.GONE);
            holder.ll_qty.setVisibility(View.GONE);
           // holder.cb.setVisibility(View.GONE);

            holder.add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listItemInterface.OnItemClick(position,v,"cart");
//                    cartInterface.OnItemClick(position,holder,"2");
                }
            });

        }else{
            holder.Productname.setText(slm.get(position).getLine_item_name());
            holder.quantity.setText(slm.get(position).getPack_of_qty());
            holder.price.setText( "₹"+slm.get(position).getPrice());
            Log.e("price2",slm.get(position).getPrice());
            String img= Constant.SERVER_BASE_URL+slm.get(position).getImage();

            Log.d("img",img);
            Picasso.get().load(img).fit().centerInside().into(holder.sample_image);
            String available_quantity=slm.get(position).getAvailable_quantity();
            qty_array.clear();
           // qty_array.add("Select");
//            for(int i=1;i<=Integer.valueOf(available_quantity);i++){
//                qty_array.add(String.valueOf(i));
//            }
            Log.e("qty_array",qty_array.toString());
            holder.add_cart.setVisibility(View.GONE);
            holder.ll_qty.setVisibility(View.VISIBLE);
           // holder.cb.setVisibility(View.VISIBLE);
            holder.card.setVisibility(View.GONE);

       /*     holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listItemInterface.OnItemClick(position,v,"increase");
                }
            });
            holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listItemInterface.OnItemClick(position,v,"decrease");
                }
            });*/

         /*   holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/



            qty_adapter=new ArrayAdapter(context,R.layout.spinner_text,qty_array);
            qty_adapter.setDropDownViewResource(R.layout.spinner_text);
            holder.sp_qty.setAdapter(qty_adapter);

           holder.sp_qty.setSelection(Integer.parseInt(slm.get(position).getQuantity())-1);



           /* TextView textView= (TextView) holder.sp_qty.getSelectedView().;
            textView.setText("3");*/

           // holder.sp_qty.setPrompt("3");

          /*  holder.sp_qty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    errorMessage(holder.sp_qty,""+slm.get(position).getQuantity());

                }
            });*/


          //  errorMessage(holder.sp_qty,""+slm.get(position).getQuantity());

            holder.sp_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                     selected_qty=parent.getSelectedItem().toString();
                   /* if(selected_qty.equalsIgnoreCase("Select")){
                        return;
                    }*/

                   // errorMessage(holder.sp_qty,slm.get(position).getQuantity());
                    Float qty=Float.valueOf(parent.getSelectedItem().toString());
                   // holder.price.setText("₹"+qty*Float.valueOf(slm.get(position).getPrice()));
                  //  listItemInterface.OnItemClick(position,holder.price,selected_qty);
                    cartInterface.OnItemClick(position,holder,selected_qty);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });





        }



    }
    public void filter(String charText){

        if (charText.length() == 1) {
            this.Itemslist.clear();
            this.Itemslist.addAll(slm);
        }

        charText = charText.toLowerCase(Locale.getDefault());
        slm.clear();

        if (charText.length() == 0) {
            slm.addAll(Itemslist);
            notifyDataSetChanged();
        } else {
            for (ListItemsModel ts : Itemslist) {

                if (ts.getLine_item_name().trim().toLowerCase(Locale.getDefault()).contains(charText) ||
                        ts.getQuantity().trim().toLowerCase(Locale.getDefault()).contains(charText)) {
                    slm.add(ts);

                }
            }
            notifyDataSetChanged();
        }

    }
    @Override
    public int getItemCount() {
        return slm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sample_image;
        TextView Productname,quantity,price,txtQuantity,btnIncrease,btnDecrease;
        LinearLayout add_cart,card,ll_qty;
        Spinner sp_qty;
        CheckBox cb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Productname=itemView.findViewById(R.id.name);
            quantity=itemView.findViewById(R.id.packquantity);
            price=itemView.findViewById(R.id.price);
            txtQuantity=itemView.findViewById(R.id.txtQuantity);
            btnIncrease=itemView.findViewById(R.id.add);
            btnDecrease=itemView.findViewById(R.id.minus);
            sample_image=itemView.findViewById(R.id.listimage);
            add_cart=itemView.findViewById(R.id.add_cart);
            card=itemView.findViewById(R.id.card);
            ll_qty=itemView.findViewById(R.id.ll_qty);
            sp_qty=itemView.findViewById(R.id.sp_qty);
          //  cb=itemView.findViewById(R.id.cb_item_check);

        }
    }


    public void errorMessage(Spinner spinner,String s){

        TextView error = (TextView) spinner.getSelectedView();
        error.setText(s);
        error.setError("");
        error.requestFocus();
        error.setTextColor(Color.RED);

    }
}
