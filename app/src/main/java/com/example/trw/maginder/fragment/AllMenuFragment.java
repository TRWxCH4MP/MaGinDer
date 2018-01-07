package com.example.trw.maginder.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trw.maginder.CreateItem;
import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.callback.OnClickTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMenuFragment extends Fragment implements OnClickTitle {

    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private List<BaseItem> itemList = new ArrayList<>();

    int defaultPosition = 1;

    public AllMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemList.clear();
        Log.d("AllFragment", "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        if (getArguments() != null) {
////            setupView(getArguments().getInt("position"));
//            defaultPosition = getArguments().getInt("position");
//            Log.d("AllFragment", "Argument != null" + "="+defaultPosition+"");
//        } else {
//
//        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_menu, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_all_menu);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        setAdapterItem();
        return view;
    }

    public void setAdapterItem() {
//        if (position > 0) {
//            defaultPosition = position;
//        }
        adapter = new MainAdapter();
        adapter.setItemList(CreateItem.createItem(1));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickMenuTitle(boolean status) {
        if (status) {
            recyclerView.removeAllViews();
//            Log.d("onclick", "onclick");
//            itemList.clear();
//            adapter.notifyDataSetChanged();
        }
    }

    public void clearItem() {
        itemList.clear();
        adapter.notifyDataSetChanged();
    }
}
