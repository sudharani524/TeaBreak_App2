package com.example.teabreak_app.repository;

import android.view.View;

import com.example.teabreak_app.Adapter.ItemslistAdapter;

public interface CartInterface {
    void OnItemClick(int position, ItemslistAdapter.ViewHolder holder, String s);

}
