package com.example.trw.maginder.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.MenuTitleItem;
import com.example.trw.maginder.adapter.item.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuTitleFragment extends Fragment {

    private RecyclerView recyclerView;
    private MainAdapter adapter;

    public MenuTitleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_title, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_menu_title);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        adapter = new MainAdapter();
        adapter.setItemList(createItem());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<BaseItem> createItem() {
        List<BaseItem> itemList = new ArrayList<>();
        itemList.add(new MenuTitleItem()
                .setMenuTitle("รวม"));
        itemList.add(new MenuTitleItem()
                .setMenuTitle("ของคาว"));
        itemList.add(new MenuTitleItem()
                .setMenuTitle("ของหวาน"));
        itemList.add(new MenuTitleItem()
                .setMenuTitle("เครื่องดื่ม"));
        return itemList;
    }

}
