package com.glitss.teabreak_app.repository;

import com.glitss.teabreak_app.Adapter.ItemslistAdapter;

public interface CartInterface {
    void OnItemClick(int position, ItemslistAdapter.ViewHolder holder, String s);

}
