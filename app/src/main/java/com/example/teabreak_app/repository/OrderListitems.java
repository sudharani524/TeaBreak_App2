package com.example.teabreak_app.repository;

import com.example.teabreak_app.Adapter.ItemsOrderAdapter;
import com.example.teabreak_app.Adapter.ItemslistAdapter;

public interface OrderListitems {
    void OnItemClick(int position, ItemsOrderAdapter.ViewHolder holder, String s);

}
