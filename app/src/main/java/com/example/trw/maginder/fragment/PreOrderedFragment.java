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
import android.widget.TextView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.MainAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreOrderedFragment extends Fragment {

    private static final String TAG = "PreOrderedFragment";
    private static final String REF_FIREBASE_CHILD_TABLE = "Table";
    private static final String REF_FIREBASE_CHILD_TRANSACTION = "Transaction";
    private static final String ID_RESTAURANT = "id_restaurant";

    private RecyclerView recyclerView;
    private TextView textViewPreOrderedTotalPrice;
    private MainAdapter adapter;

    private String idRestaurant;
    private String tableId;
    private String transaction;
    private String transactionMenu;

    List<String> listTransaction = new ArrayList<>();
    List<String> listMenuTransaction = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    public PreOrderedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();

        if (getArguments() != null) {
//            orderMenu = new ArrayList<>(getArguments().getStringArrayList(LIST_MENU));
            idRestaurant = getArguments().getString(ID_RESTAURANT);
            tableId = getArguments().getString("TableId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pre_ordered, container, false);

        initializeUI(view);
        setupView();
//        createItem();
        createPreOrderedMenu();
        return view;
    }

    private void setupView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()
                , LinearLayoutManager.VERTICAL
                , false));
        adapter = new MainAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_list_pre_ordered_menu);
        textViewPreOrderedTotalPrice = view.findViewById(R.id.tv_pre_ordered_total_price);
    }

    private void createPreOrderedMenu() {
        databaseReference = database.getReference()
                .child(REF_FIREBASE_CHILD_TRANSACTION)
                .child(idRestaurant)
                .child(tableId);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                listTransaction.add(key);
//                createItem(listTransaction);
                Log.d(TAG, "onChildAdded: " + dataSnapshot.getChildren());
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
    }

    private void createItem(List<String> transaction) {
        for (int index = 0; index < transaction.size(); index++) {
            if (!transaction.get(index).equals("data")) {
                databaseReference = database.getReference()
                        .child(REF_FIREBASE_CHILD_TRANSACTION)
                        .child(idRestaurant)
                        .child(tableId)
                        .child(transaction.get(index));

                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String key = dataSnapshot.getKey();
//                        Log.d(TAG, "onChildAdded: " + dataSnapshot.getValue());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
            }
        }

    }

}
