package com.example.trw.maginder.manager;

import android.util.Log;

import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.TableItem;
import com.example.trw.maginder.create_item.CreateTableItem;
import com.example.trw.maginder.utility.StaticStringHelper;
import com.example.trw.maginder.service.dao.TableItemCollectionDao;
import com.example.trw.maginder.service.http_manger.HttpManagerTable;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableManager {

    private static TableManager manager;

    private String zoneId;
    private String tableId;
    private String tableState;
    private String tableName;
    private String transaction;
    private List<CreateTableItem> listTable = new ArrayList<>();

    public static TableManager getInstance() {
        if (manager == null) {
            manager = new TableManager();
        }
        return manager;
    }

    private TableManager() {

    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableState() {
        return tableState;
    }

    public void setTableState(String tableState) {
        this.tableState = tableState;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public void onCreateTableZone(String restaurantId, final TableManagerCallback callback) {
        Call<TableItemCollectionDao> call = HttpManagerTable.getInstance().getService().repos(restaurantId);
        call.enqueue(new Callback<TableItemCollectionDao>() {
            @Override
            public void onResponse(Call<TableItemCollectionDao> call, Response<TableItemCollectionDao> response) {
                if (response.isSuccessful()) {
                    TableItemCollectionDao dao = response.body();
                    callback.onCreateSuccess(dao);
                } else {
                    callback.onCreateFailure(String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<TableItemCollectionDao> call, Throwable t) {
                callback.onCreateFailure(t.toString());
            }
        });
    }

    public void onSetupTable(final SetupTableCallback callback) {
//        Log.d(TAG, "getDataRestaurantTable: " + getZoneId());
        listTable.clear();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference()
                .child(StaticStringHelper.REF_FIREBASE_TABLE)
                .child(AuthManager.getInstance().getCurrentRestaurantId())
                .child(zoneId);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.d(TAG, "onChildAdded: " + getZoneId());
                CreateTableItem data = dataSnapshot.getValue(CreateTableItem.class);
                listTable.add(data);
                callback.onTableAdded(listTable);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Log.d(TAG, "onChildChanged: " + getZoneId());
                CreateTableItem data = dataSnapshot.getValue(CreateTableItem.class);

                if (getZoneId().equals(data.getId_zone())) {
//                    Log.d(TAG, "onChildChanged: zone id it's match");
                    int index = updateItemOnDataChange(data);
//                    Log.d(TAG, "updateItemOnDataChange2 index: " + index);
                    listTable.set(index, data);
                    callback.onTableChanged(listTable);
                } else {
                    callback.onZoneIdIsNotMatch("ZoneId not match !");
//                    Log.d(TAG, "onChildChanged: zone id not match !");
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
        callback.onSuccess(true);
    }

    private int updateItemOnDataChange(CreateTableItem data) {
        int index = -1;
//        Log.d(TAG, "updateItemOnDataChange2 table name: " + data.getName_table());
        for (int i = 0; i < listTable.size(); i++) {
            if (listTable.get(i).getId_table().equals(data.getId_table())) {
                index = i;
//                Log.d(TAG, "updateItemOnDataChange2: " + i);
                break;
            }
        }
        return index;
    }

    public List<BaseItem> onCreateTable(List<CreateTableItem> listTable) {
        List<BaseItem> itemList = new ArrayList<>();

        for (int index = 0; index < listTable.size(); index++) {
            if (listTable.get(index).getName_table() != null && listTable.get(index).getStatus() != null) {
                itemList.add(new TableItem()
                        .setTableName(listTable.get(index).getName_table())
                        .setTableState(listTable.get(index).getStatus())
                        .setTableId(listTable.get(index).getId_table())
                        .setZoneId(listTable.get(index).getId_zone())
                );
            }
        }

        return itemList;
    }

    public void updateTableState(final UpdateTableStateCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference =
                database.getReference()
                        .child(StaticStringHelper.REF_FIREBASE_TABLE)
                        .child(AuthManager.getInstance().getCurrentRestaurantId())
                        .child(zoneId);

        databaseReference.orderByChild(StaticStringHelper.REF_FIREBASE_CHILD_TABLE_ID).equalTo(tableId)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = dataSnapshot.getChildren().iterator().next().getKey();
//                                    Log.d(TAG, "onDataChange: " + key);
                        databaseReference
                                .child(key)
                                .child(StaticStringHelper.REF_FIREBASE_CHILD_TABLE_STATUS)
                                .setValue(StaticStringHelper.TRUE, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        callback.onUpdateSuccess(true);
                                    }
                                });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void onCheckHasChildTable(final CheckHasChildTableCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference()
                .child(StaticStringHelper.TRANSACTION)
                .child(AuthManager.getInstance().getCurrentRestaurantId());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getTableId())) {
                    callback.onHasChildTable(true);
//                    Log.d(TAG, "checkTableTransaction: " + getTableId());
//                    Log.d(TAG, "checkTableTransaction: " + getTableTransaction());
                } else {
                    callback.onHasChildTable(false);
//                    Log.d(TAG, "checkTableTransaction: " + "table transaction is null");
//                    Log.d(TAG, "checkTableTransaction: " + getTableTransaction());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onCheckHasChildTableTransaction(final CheckHasChildTableTransactionCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference()
                .child(StaticStringHelper.TRANSACTION)
                .child(AuthManager.getInstance().getCurrentRestaurantId())
                .child(getTableId());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String child = ds.getKey();
                    callback.onHasChildTableTransaction(child);
//                    Log.d(TAG, "checkChild: " + child);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface TableManagerCallback {
        void onCreateSuccess(TableItemCollectionDao dao);

        void onCreateFailure(String exception);
    }

    public interface UpdateTableStateCallback {
        void onUpdateSuccess(boolean isSuccess);
    }

    public interface SetupTableCallback {
        void onTableAdded(List<CreateTableItem> listTable);

        void onTableChanged(List<CreateTableItem> listTable);

        void onSuccess(boolean isSuccess);

        void onZoneIdIsNotMatch(String error);
    }

    public interface CheckHasChildTableCallback {
        void onHasChildTable(boolean isSuccess);
    }

    public interface CheckHasChildTableTransactionCallback {
        void onHasChildTableTransaction(String childTableTransaction);
    }
}
