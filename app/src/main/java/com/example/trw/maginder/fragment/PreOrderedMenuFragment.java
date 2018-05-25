package com.example.trw.maginder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.PreOrderedMenuItem;
import com.example.trw.maginder.create_item.CreatePreOrderedMenu;
import com.example.trw.maginder.create_item.CreateTableItem;
import com.example.trw.maginder.manager.AuthManager;
import com.example.trw.maginder.manager.MenuManager;
import com.example.trw.maginder.manager.PreOrderedMenuManager;
import com.example.trw.maginder.manager.TableManager;
import com.example.trw.maginder.utility.StaticStringHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private List<String> listTransaction = new ArrayList<String>();
    private List<CreatePreOrderedMenu> listPreOrderedMenu = new ArrayList<>();

    private TextView tvUserName;
    private TextView tvUserNamePrefix;
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
        recyclerView = view.findViewById(R.id.recycler_view_list_pre_ordered_menu);
    }

    private void setupView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()
                , LinearLayoutManager.VERTICAL
                , false));
        adapter = new MainAdapter();
        recyclerView.setAdapter(adapter);

        getItem();
    }

    private void getItem() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference()
                .child(StaticStringHelper.TRANSACTION)
                .child(idRestaurant)
                .child(tableId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String child = ds.getKey();
                    Log.d(TAG, "onDataChange: " + child);
                    listTransaction.add(child);
                    transaction = child;
                }
                getData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onCreatePreOrderedMenu(List<CreatePreOrderedMenu> listPreOrderedMenu) {
        List<BaseItem> itemList = new ArrayList<>();

        for (int index = 0; index < listPreOrderedMenu.size(); index++) {
            if (listPreOrderedMenu.get(index).getImg() != null
                    && listPreOrderedMenu.get(index).getName() != null
                    && listPreOrderedMenu.get(index).getPrice() != null
                    && listPreOrderedMenu.get(index).getStatus() != null) {
                itemList.add(new PreOrderedMenuItem()
                        .setPreOrderedMenuImage(StaticStringHelper.IMAGE_URL + listPreOrderedMenu.get(index).getImg())
                        .setPreOrderedMenuName(listPreOrderedMenu.get(index).getName())
                        .setPreOrderedMenuPrice(listPreOrderedMenu.get(index).getPrice() + " บาท")
                        .setPreOrderedMenuState(listPreOrderedMenu.get(index).getStatus())
                );
            }
        }

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

    private int updateItemOnDataChange(String data) {
        Log.d(TAG, "updateItemOnDataChange: " + data);
        int index = -1;
        for (int i = 0; i < listPreOrderedMenu.size(); i++) {
            Log.d(TAG, "For loop: " + listPreOrderedMenu.get(i).getTransaction_menu());
            if (listPreOrderedMenu.get(i).getTransaction_menu() != null
                    && listPreOrderedMenu.get(i).getTransaction_menu().equals(data)) {
                Log.d(TAG, "updateItemOnDataChange: match" + i);
                index = i;
            }
        }
        return index;
    }

    private void getData() {

        if (transaction != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference()
                    .child(StaticStringHelper.TRANSACTION)
                    .child(idRestaurant)
                    .child(tableId)
                    .child(transaction);
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    CreatePreOrderedMenu data = dataSnapshot.getValue(CreatePreOrderedMenu.class);
                    listPreOrderedMenu.add(data);
                    onCreatePreOrderedMenu(listPreOrderedMenu);
                    Log.d(TAG, "onChildAdded: ");
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    CreatePreOrderedMenu data = dataSnapshot.getValue(CreatePreOrderedMenu.class);
                    Log.d(TAG, "onChildChanged: ");
                    int index = updateItemOnDataChange(data.getTransaction_menu());
//                    Log.d(TAG, "updateItemOnDataChange2 index: " + index);
                    if (index > -1) {
                        listPreOrderedMenu.set(index, data);
                        onCreatePreOrderedMenu(listPreOrderedMenu);
                    }

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        } else {
            Log.d(TAG, "getData: child is null");
        }
    }

}
