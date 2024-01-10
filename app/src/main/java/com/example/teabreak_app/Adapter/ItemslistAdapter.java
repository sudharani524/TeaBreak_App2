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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teabreak_app.ModelClass.ListItemsModel;
import com.example.teabreak_app.ModelClass.OrderHistoryModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Ui.Cartlist_Activity;
import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.repository.CartInterface;
import com.example.teabreak_app.repository.ListItemInterface;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
            holder.iv_delete.setVisibility(View.GONE);


            holder.add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //    listItemInterface.OnItemClick(position,v,"cart");
                    cartInterface.OnItemClick(position,holder,"2");
                }
            });

        }else{
            holder.Productname.setText(slm.get(position).getLine_item_name());
            holder.quantity.setText(slm.get(position).getPack_of_qty());
            holder.price.setText( "₹"+Float.parseFloat(slm.get(position).getPrice())*Float.parseFloat(slm.get(position).getQuantity()));
            holder.txtQuantity.setText(slm.get(position).getQuantity());
            Log.e("price2",slm.get(position).getPrice());
            String img= Constant.SERVER_BASE_URL+slm.get(position).getImage();

            Log.d("img",img);
            Picasso.get().load(img).fit().centerInside().into(holder.sample_image);

            Log.e("qty_array",qty_array.toString());
            holder.add_cart.setVisibility(View.VISIBLE);
            holder.iv_delete.setVisibility(View.VISIBLE);
            holder.card.setVisibility(View.GONE);

         /*   holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     dlt_item_api_call();
                   // Cartlist_Activity.dlt_item_api_call();
                }
            });
*/
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartInterface.OnItemClick(position,holder,slm.get(position).getQuantity());
                }
            });

        }



    }

    @Override
    public int getItemCount() {
        return slm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sample_image,iv_delete;
        TextView Productname,quantity,price,txtQuantity,btnIncrease,btnDecrease;
        LinearLayout add_cart,card,ll_qty;
        Spinner sp_qty;
        CheckBox cb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Productname=itemView.findViewById(R.id.name);
            quantity=itemView.findViewById(R.id.packquantity);
            price=itemView.findViewById(R.id.price);
            txtQuantity=itemView.findViewById(R.id.Quantity);
            btnIncrease=itemView.findViewById(R.id.add);
            btnDecrease=itemView.findViewById(R.id.minus);
            sample_image=itemView.findViewById(R.id.listimage);
            add_cart=itemView.findViewById(R.id.add_cart);
            card=itemView.findViewById(R.id.card);
           // ll_qty=itemView.findViewById(R.id.ll_qty);
          //  sp_qty=itemView.findViewById(R.id.sp_qty);
            iv_delete=itemView.findViewById(R.id.iv_delete);
          //  cb=itemView.findViewById(R.id.cb_item_check);

        }
    }

   /* private void dlt_item_api_call() {

        Log.e("dlt_api","dlt_api");
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("line_item_id",selected_line_item_id);
        object.addProperty("status","0");


        viewModel.dlt_item_api(object).observe(Cartlist_Activity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){

                    Log.d("TAG","complaint_names "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        String text=jsonObject1.getString("text");

                        Toast.makeText(Cartlist_Activity.this, ""+message, Toast.LENGTH_SHORT).show();
                        if(message.equalsIgnoreCase("Success")){
                            cart_list_api_call();
                        }

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(Cartlist_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Cartlist_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }*/



}
