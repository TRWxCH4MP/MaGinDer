package com.example.trw.maginder.manager;

import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.PreOrderedMenuItem;
import com.example.trw.maginder.create_item.CreatePreOrderedMenu;
import com.example.trw.maginder.utility.StaticStringHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreOrderedMenuManager {

    private static PreOrderedMenuManager manager;
    private List<CreatePreOrderedMenu> listPreOrderedMenu = new ArrayList<>();
    private String transaction;
    private String idRestaurant;
    private String tableId;

    public static PreOrderedMenuManager getInstance() {
        if (manager == null) {
            manager = new PreOrderedMenuManager();
        }
        return manager;
    }

    private PreOrderedMenuManager() {

    }

    public void onSetupTransactionOrdered(String idRestaurant, String tableId) {
        this.idRestaurant = idRestaurant;
        this.tableId = tableId;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference()
                .child(StaticStringHelper.TRANSACTION)
                .child(idRestaurant)
                .child(tableId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    transaction = ds.getKey();
                }
//                getData();
//                callback.onSetupTransactionSuccess(transaction);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                callback.onSetupTransactionFailure(databaseError.toString());
            }
        });
    }

    public void getData() {

        if (transaction != null) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
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
//                    callback.onPreOrderedMenuAdded(listPreOrderedMenu);
//                    onCreatePreOrderedMenu();
//                    Log.d(TAG, "onChildAdded: ");
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    CreatePreOrderedMenu data = dataSnapshot.getValue(CreatePreOrderedMenu.class);
//                    Log.d(TAG, "onChildChanged: ");
                    int index = updateItemOnDataChange(data.getTransaction_menu());
//                    Log.d(TAG, "updateItemOnDataChange2 index: " + index);
                    if (index > -1) {
                        listPreOrderedMenu.set(index, data);
//                        callback.onPreOrderedMenuChanged(listPreOrderedMenu);
//                        onCreatePreOrderedMenu();
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
//            Log.d(TAG, "getData: child is null");
        }
    }

    private int updateItemOnDataChange(String data) {
//        Log.d(TAG, "updateItemOnDataChange: " + data);
        int index = -1;
        for (int i = 0; i < listPreOrderedMenu.size(); i++) {
//            Log.d(TAG, "For loop: " + listPreOrderedMenu.get(i).getTransaction_menu());
            if (listPreOrderedMenu.get(i).getTransaction_menu() != null
                    && listPreOrderedMenu.get(i).getTransaction_menu().equals(data)) {
//                Log.d(TAG, "updateItemOnDataChange: match" + i);
                index = i;
            }
        }
        return index;
    }

    public List<BaseItem> onCreatePreOrderedMenu() {
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

        return itemList;
    }


    public interface PreOrderedMenuCallback {
        void onPreOrderedMenuAdded(List<CreatePreOrderedMenu> listPreOrderedMenu);

        void onPreOrderedMenuChanged(List<CreatePreOrderedMenu> listPreOrderedMenu);
    }

    public interface SetupPreOrderedMenuCallback {
        void onSetupTransactionSuccess(String transaction);

        void onSetupTransactionFailure(String error);
    }

}
