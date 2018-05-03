package com.example.trw.maginder.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.model.StaticStringHelper;
import com.example.trw.maginder.activity.MenuActivity;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.TableItem;
import com.example.trw.maginder.callback.TableCallback;
import com.example.trw.maginder.create_item.CreateTableItem;
import com.example.trw.maginder.service.dao.TableItemCollectionDao;
import com.example.trw.maginder.service.http_manger.HttpManagerTable;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private static final String PREF_NAME = "PREF_NAME";
    private static final String TAG = "ManageTableActivity";
    private static final String REF_FIREBASE_CHILD_TABLE = "Table";
    private static final String STATE_TRUE = "true";
    private static final String STATE_FALSE = "false";
    private static final String REF_FIREBASE_CHILD_STATUS = "Status";

    private String employeeName;
    private String employeeType;
    private String restaurantId;
    private String popupState;
    private String zoneId;
    private String tableId;
    private List<String> listTabLayoutZone = new ArrayList<>();
    private List<String> listZoneId = new ArrayList<>();
    private List<CreateTableItem> listTable = new ArrayList<>();

    private Dialog dialog;
    private TextView textViewOpenTable;
    private TextView textViewTable;
    private TextView textViewCustomerNum;
    private TextView textViewNum;
    private TextView textViewTableName;
    private Button buttonOk;
    private Button buttonCancel;
    private EditText editTextCustomerNum;
    private RecyclerView recyclerViewTable;
    private MainAdapter adapter;
    private TableItemCollectionDao listTableCollection;
    private TabLayout tabLayout;

    private Intent intent;

    private ProgressDialog progressDialog;

    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        restaurantId = sharedPreferences.getString(StaticStringHelper.RESTAURANT_ID, null);
        employeeName = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_NAME, null);
        employeeType = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_TYPE, null);


        intent = new Intent(getContext(), MenuActivity.class);

        if (restaurantId != null) {
            createTableZone();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table, container, false);

        initializeUI(view);
        setupView();
        initializePopupDialog();

        return view;
    }

    private void initializeUI(View view) {
        recyclerViewTable = view.findViewById(R.id.recycler_view_table);
        tabLayout = view.findViewById(R.id.tab_title);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("กำลังโหลดข้อมูลโต๊ะอาหาร");
        progressDialog.show();

    }

    private void setupView() {
        TableCallback callbackTable = new TableCallback() {
            @Override
            public void onCallbackTableState(String zoneId, String tableId, String tableName, String tableState) {
                showPopup(zoneId, tableId, tableName, tableState);
            }
        };

        recyclerViewTable.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new MainAdapter(callbackTable);
        recyclerViewTable.setAdapter(adapter);

    }

    private void showPopup(String zoneId, String tableId, String tableName, String tableState) {
        dialog.show();
        this.zoneId = zoneId;
        this.tableId = tableId;
        popupState = tableState;

        if (tableState.equals("true")) {
            int time = (int) (new Date().getTime() / 1000);
            String timeStampTransaction = "TR-" + time;

            intent.putExtra("tableId", tableId);
            intent.putExtra("timeStamp", timeStampTransaction);
            intent.putExtra("tableName", tableName);

            textViewCustomerNum.setVisibility(View.VISIBLE);
            textViewNum.setVisibility(View.VISIBLE);
            editTextCustomerNum.setVisibility(View.VISIBLE);
            textViewOpenTable.setVisibility(View.INVISIBLE);

        } else if (tableState.equals("false")) {
            textViewCustomerNum.setVisibility(View.INVISIBLE);
            textViewNum.setVisibility(View.INVISIBLE);
            editTextCustomerNum.setVisibility(View.INVISIBLE);
            textViewOpenTable.setVisibility(View.VISIBLE);
        }
    }

    private void initializePopupDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.manage_table_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        textViewOpenTable = dialog.findViewById(R.id.tv_open_table);
        textViewTable = dialog.findViewById(R.id.tv_table);
        textViewCustomerNum = dialog.findViewById(R.id.tv_customer_num);
        textViewNum = dialog.findViewById(R.id.tv_num);
        textViewTableName = dialog.findViewById(R.id.tv_table_name);
        buttonOk = dialog.findViewById(R.id.btn_ok);
        buttonCancel = dialog.findViewById(R.id.btn_cancel);
        editTextCustomerNum = dialog.findViewById(R.id.et_customer_num);

        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

    }

    private void createTableZone() {
        Call<TableItemCollectionDao> call = HttpManagerTable.getInstance().getService().repos(restaurantId);
        call.enqueue(new Callback<TableItemCollectionDao>() {
            @Override
            public void onResponse(Call<TableItemCollectionDao> call, Response<TableItemCollectionDao> response) {
                if (response.isSuccessful()) {
                    listTableCollection = response.body();

                    if (!listTableCollection.getData().isEmpty()) {
                        for (int index = 0; index < listTableCollection.getData().size(); index++) {
                            listTabLayoutZone.add(listTableCollection.getData().get(index).getZoneName());
                            listZoneId.add(listTableCollection.getData().get(index).getZoneId());
                        }

                        if (!listTabLayoutZone.isEmpty()) {
                            createTabLayout(listTabLayoutZone);
                            zoneId = listTableCollection.getData().get(0).getZoneId();
                            createTable();
                        }
                    }
                    Log.d(TAG, "onResponse: Success");
//                    Toast.makeText(ManageTableActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onResponse: !Success");
//                    Toast.makeText(ManageTableActivity.this, "!Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TableItemCollectionDao> call, Throwable t) {
                Log.d(TAG, "onFailure: Failure");
//                Toast.makeText(ManageTableActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createTable() {
        Log.d(TAG, "getDataRestaurantTable: " + zoneId);
        listTable.clear();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference()
                .child("Table")
                .child(restaurantId)
                .child(zoneId);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: " + zoneId);
                CreateTableItem data = dataSnapshot.getValue(CreateTableItem.class);
                listTable.add(data);
                createItem(listTable);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: " + zoneId);
                CreateTableItem data = dataSnapshot.getValue(CreateTableItem.class);

                if (zoneId.equals(data.getId_zone())) {
                    Log.d(TAG, "onChildChanged: zone id it's match");
                    int index = updateItemOnDataChange(data);
                    Log.d(TAG, "updateItemOnDataChange2 index: " + index);
                    listTable.set(index, data);
                    createItem(listTable);
                } else {
                    Log.d(TAG, "onChildChanged: zone id not match !");
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

        progressDialog.dismiss();
    }

    private void createItem(List<CreateTableItem> listTable) {
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
        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();
    }

    private int updateItemOnDataChange(CreateTableItem data) {
        int index = -1;
        Log.d(TAG, "updateItemOnDataChange2 table name: " + data.getName_table());
        for (int i = 0; i < listTable.size(); i++) {
            if (listTable.get(i).getId_table().equals(data.getId_table())) {
                index = i;
                Log.d(TAG, "updateItemOnDataChange2: " + i);
                break;
            }
        }
        return index;
    }

    private void createTabLayout(List<String> listTabLayout) {
        for (int index = 0; index < listTabLayout.size(); index++) {
            tabLayout.addTab(tabLayout.newTab().setText(listTabLayout.get(index)));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == tabLayout.getSelectedTabPosition()) {
//            Toast.makeText(this, listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId(), Toast.LENGTH_SHORT).show();
            zoneId = listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId();
            createTable();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == tabLayout.getSelectedTabPosition()) {
//            Toast.makeText(this, listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId(), Toast.LENGTH_SHORT).show();
            zoneId = listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId();
            createTable();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                handleButtonOK();
                dialog.cancel();
                break;
            case R.id.btn_cancel:
                dialog.cancel();
                break;
        }
    }

    private void handleButtonOK() {
        if (popupState.equals(STATE_TRUE)) {
            startActivity(intent);
        } else if (popupState.equals(STATE_FALSE)) {
            dialog.cancel();
            updateTableState();
        }
    }

    private void updateTableState() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference =
                database.getReference()
                        .child(REF_FIREBASE_CHILD_TABLE)
                        .child(restaurantId)
                        .child(zoneId);

        databaseReference.orderByChild("Id_table").equalTo(tableId)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = dataSnapshot.getChildren().iterator().next().getKey();
//                                    Log.d(TAG, "onDataChange: " + key);
                        databaseReference.child(key).child(REF_FIREBASE_CHILD_STATUS).setValue(STATE_TRUE);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


}
