package com.glitss.teabreak_app.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.glitss.teabreak_app.ModelClass.ListItemsModel;

import java.util.ArrayList;
import java.util.List;

public class LAdapter extends RecyclerView.Adapter<LAdapter.ViewHolder> {
    Context context;
    List<ListItemsModel> lim=new ArrayList<>();

    public LAdapter(Context context, List<ListItemsModel> lim) {
        this.context = context;
        this.lim = lim;
    }

    @NonNull
    @Override
    public LAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
