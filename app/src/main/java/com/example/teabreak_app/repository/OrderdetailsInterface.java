package com.example.teabreak_app.repository;

import android.view.View;

import com.example.teabreak_app.Adapter.ItemslistAdapter;
import com.example.teabreak_app.Adapter.OrderHistoryAdapter;
import com.example.teabreak_app.Adapter.OrderedlistAdapter;

public interface OrderdetailsInterface {
      void OnItemClick(int position, OrderHistoryAdapter.ViewHolder holder, String s);
}
