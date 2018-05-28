package com.example.trw.maginder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.create_item.CreatePreOrderedMenu;
import com.example.trw.maginder.manager.AuthManager;
import com.example.trw.maginder.manager.PreOrderedMenuManager;
import com.example.trw.maginder.manager.TableManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreOrderedMenuFragment extends Fragment {

    private static final String TAG = "PreOrderedMenuFragment";

    private String idRestaurant;
    private String tableId;
    private String transaction;
    private String tableName;

    private TextView tvUserName;
    private TextView tvUserNamePrefix;
    private TextView tvTableName;
    private RecyclerView recyclerView;
    private MainAdapter adapter;

    public PreOrderedMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idRestaurant = AuthManager.getInstance().getCurrentRestaurantId();
        tableId = TableManager.getInstance().getTableId();
        tableName = TableManager.getInstance().getTableName();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pre_ordered_menu, container, false);

        initializeUI(view);
        setupView();

        return view;
    }

    private void initializeUI(View view) {
        tvUserName = view.findViewById(R.id.tv_pre_ordered_user_name);
        tvUserNamePrefix = view.findViewById(R.id.tv_pre_ordered_user_name_prefix);
        tvTableName = view.findViewById(R.id.tv_table_name);
        recyclerView = view.findViewById(R.id.recycler_view_list_pre_ordered_menu);
    }

    private void setupView() {
        tvTableName.setText(tableName);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()
                , LinearLayoutManager.VERTICAL
                , false));
        adapter = new MainAdapter();
        recyclerView.setAdapter(adapter);

        checkHasChildTableTransaction();
    }

    private void checkHasChildTableTransaction() {
        TableManager.getInstance().onCheckHasChildTableTransaction(new TableManager.CheckHasChildTableTransactionCallback() {
            @Override
            public void onHasChildTableTransaction(String childTableTransaction) {
                setupPreOrderedMenu(childTableTransaction);
            }
        });
    }

    private void setupPreOrderedMenu(String transaction) {
        PreOrderedMenuManager.getInstance()
                .onGetPreOrderedMenu(idRestaurant, tableId, transaction, new PreOrderedMenuManager.SetupPreOrderedCallback() {
            @Override
            public void onPreOrderedAdded(List<CreatePreOrderedMenu> listPreOrderedMenu) {
                createPreOrderedMenu(listPreOrderedMenu);
            }

            @Override
            public void onPreOrderedChanged(List<CreatePreOrderedMenu> listPreOrderedMenu) {
                createPreOrderedMenu(listPreOrderedMenu);
            }
        });

    }

    public void createPreOrderedMenu(List<CreatePreOrderedMenu> listPreOrderedMenu) {
        List<BaseItem> itemList = PreOrderedMenuManager.getInstance().OnCreatePreOrderedMenu(listPreOrderedMenu);

        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();
        setupUserName(listPreOrderedMenu);
    }

    private void setupUserName(List<CreatePreOrderedMenu> listPreOrderedMenu) {
        for (int index = 0; index < listPreOrderedMenu.size(); index++) {
            if (listPreOrderedMenu.get(index).getName_user() != null) {
                tvUserNamePrefix.setVisibility(View.VISIBLE);
                tvUserName.setText(String.valueOf(listPreOrderedMenu.get(index).getName_user()));
            }
        }
    }

}
