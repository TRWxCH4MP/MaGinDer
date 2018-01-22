package com.example.trw.maginder.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trw.maginder.CreateDrawerMenu;
import com.example.trw.maginder.CreateTable;
import com.example.trw.maginder.CreateTableItem;
import com.example.trw.maginder.CreateZoneTable;
import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.TableItem;
import com.example.trw.maginder.callback.OnCallbackState;
import com.example.trw.maginder.service.dao.TableItemCollectionDao;
import com.example.trw.maginder.service.dao.TableItemDao;
import com.example.trw.maginder.service.dao.TableZoneItemDao;
import com.example.trw.maginder.service.http_manger.HttpManagerTable;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageTableActivity extends AppCompatActivity implements View.OnClickListener, OnCallbackState, TabLayout.OnTabSelectedListener {
    private static final String PREF_NAME = "PREF_NAME";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String ID_RESTAURANT = "id_restaurant";
    private static final String TAG = "ManageTableActivity";
    private static final String REF_FIREBASE_CHILD_TABLE = "Table";
    private static final String STATE_TRUE = "true";
    private static final String REF_FIREBASE_CHILD_STATUS = "Status";

    private String[] mDrawerTitle = {"ตะกร้าเมนู", "ของคาว", "ของหวาน", "เครื่องดื่ม", "โปรโมชั่น", "เมนูที่สั่ง"};
    private ImageView mImageViewHamburgerMenu;
    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private Toolbar mToolbarMenu;

    private Dialog dialog;

    private TextView textViewTable;
    private TextView textViewCustomerNum;
    private TextView textViewNum;
    private TextView textViewTableName;
    private Button buttonOk;
    private Button buttonCancel;
    private EditText editTextCustomerNum;

    private CreateDrawerMenu createDrawer;
    private Bundle bundle;
    private String name;
    private String type;
    private String idRestaurant;

    private RecyclerView recyclerViewTable;
    private MainAdapter adapter;
    private TableItemCollectionDao listTableCollection;
    private List<TableItemDao> listTable;
    private List<TableZoneItemDao> listZoneTable;
    private List<String> listZone;
    private List<String> listTabLayoutZone = new ArrayList<>();
    private TabLayout tabLayout;

    private DatabaseReference mRootRef;

    List<CreateZoneTable> zoneTables = new ArrayList<>();
    List<String> listZoneId = new ArrayList<>();
    CreateTable createTable;
    List<CreateTableItem> listTTT = new ArrayList<>();
    List<String> listTTT2 = new ArrayList<>();

    private Intent intent;
    private Drawer drawer;
    private PrimaryDrawerItem itemMenu;
    private PrimaryDrawerItem itemSelectedMenu;
    private PrimaryDrawerItem itemOrderedMenu;
    private PrimaryDrawerItem itemPromotions;
    private PrimaryDrawerItem itemSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_table);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        idRestaurant = sharedPreferences.getString(ID_RESTAURANT, null);
        name = sharedPreferences.getString(NAME, null);
        type = sharedPreferences.getString(TYPE, null);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        if (idRestaurant != null) {
            getDataRestaurantTableAndZone();
        }

        initializeUI();
        initializeDrawer();
        handelOnclick();
        setupView();
    }

    private void getDataRestaurantTableAndZoneByFirebase(final String zoneId, final String tableName, final String tableId) {
        Query myTopPostsQuery = mRootRef.child("Table").child(idRestaurant).child(zoneId);
        myTopPostsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getKey().equals("data")) {
                    Log.d(TAG, "onChildAdded not data: " + dataSnapshot.child(REF_FIREBASE_CHILD_STATUS).getValue());
                    if (dataSnapshot.child(REF_FIREBASE_CHILD_STATUS).getValue().equals(STATE_TRUE)) {
                        showPopup(STATE_TRUE, tableName, tableId);
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: " + dataSnapshot.getKey());
                if (dataSnapshot.child(REF_FIREBASE_CHILD_STATUS).getValue().equals(STATE_TRUE)) {
                    showPopup(STATE_TRUE, tableName, tableId);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: " + dataSnapshot.getKey());
                if (dataSnapshot.child(REF_FIREBASE_CHILD_STATUS).getValue().equals(STATE_TRUE)) {
                    showPopup(STATE_TRUE, tableName, tableId);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved: " + dataSnapshot.getKey());
                if (dataSnapshot.child(REF_FIREBASE_CHILD_STATUS).getValue().equals(STATE_TRUE)) {
                    showPopup(STATE_TRUE, tableName, tableId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDataRestaurantTableAndZone() {
        Call<TableItemCollectionDao> call = HttpManagerTable.getInstance().getService().repos(idRestaurant);
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
                            createItem(listTableCollection.getData()
                                    , listTabLayoutZone.get(0)
                                    , listTableCollection.getData().get(0).getZoneId());
                        }
                    }
                    Toast.makeText(ManageTableActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ManageTableActivity.this, "!Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TableItemCollectionDao> call, Throwable t) {
                Toast.makeText(ManageTableActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeUI() {
        mToolbarMenu = findViewById(R.id.tb_menu);
        mImageViewHamburgerMenu = findViewById(R.id.imgv_hamburger_menu);
        recyclerViewTable = findViewById(R.id.recycler_view_table);
        tabLayout = findViewById(R.id.tab_title);

        if (mImageViewHamburgerMenu != null) {
            mImageViewHamburgerMenu.setOnClickListener(this);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("ChooseTable"));
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String tableId = intent.getStringExtra("TableId");
            String zoneId = intent.getStringExtra("ZoneId");
            String tableName = intent.getStringExtra("TableName");
            Toast.makeText(context, tableId + " " + zoneId, Toast.LENGTH_SHORT).show();
            getDataRestaurantTableAndZoneByFirebase(zoneId, tableName, tableId);
        }

    };

    private void showPopup(String state, String tableName, String tableId) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.manage_table_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        textViewTable = dialog.findViewById(R.id.tv_table);
        textViewCustomerNum = dialog.findViewById(R.id.tv_customer_num);
        textViewNum = dialog.findViewById(R.id.tv_num);
        textViewTableName = dialog.findViewById(R.id.tv_table_name);
        textViewTableName.setText(tableName);
        buttonOk = dialog.findViewById(R.id.btn_ok);
        buttonCancel = dialog.findViewById(R.id.btn_cancel);
        editTextCustomerNum = dialog.findViewById(R.id.et_customer_num);

        intent = new Intent(this, MenuActivity.class);
        intent.putExtra("tableId", tableId);
        Log.d(TAG, "showPopup: " + tableId);
        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    private void handelOnclick() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_hamburger_menu:
                drawer.openDrawer();
                break;
            case R.id.btn_ok:
                startActivity(intent);
                break;
            case R.id.btn_cancel:
                dialog.cancel();
                break;
        }
    }

    @Override
    public void onCallbackStateLogin(boolean state) {
        if (state) {
            finish();
        }
    }

    public void onLogout(boolean state) {
        finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == tabLayout.getSelectedTabPosition()) {
            Toast.makeText(this, listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId(), Toast.LENGTH_SHORT).show();
//            getDataRestaurantTableAndZoneByFirebase(listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId());
            createItem(listTableCollection.getData()
                    , listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneName()
                    , listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == tabLayout.getSelectedTabPosition()) {
            Toast.makeText(this, listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId(), Toast.LENGTH_SHORT).show();
//            getDataRestaurantTableAndZoneByFirebase(listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId());
            createItem(listTableCollection.getData()
                    , listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneName()
                    , listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId());
        }
    }

    private void initializeDrawer() {
        itemMenu = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.menu)
                .withIcon(R.drawable.menu_icon)
                .withSelectable(false);

        itemSelectedMenu = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.selected_menu)
                .withIcon(R.drawable.cart_icon)
                .withSelectable(false);

        itemOrderedMenu = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName(R.string.ordered_menu)
                .withIcon(R.drawable.history_icon)
                .withSelectable(false);

        itemPromotions = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName(R.string.promotion)
                .withIcon(R.drawable.promotion_icon)
                .withSelectable(false);

        itemSignOut = new PrimaryDrawerItem()
                .withIdentifier(5)
                .withName(R.string.sign_out)
                .withIcon(R.drawable.signout_icon)
                .withSelectable(false);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.maginder_deep_grey)
                .addProfiles(
                        new ProfileDrawerItem().withName(type + " " + name).withIcon(R.drawable.maginder_logo)
                )
                .build();


        drawer = new DrawerBuilder()
                .withSelectedItem(-1)
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withSliderBackgroundColorRes(R.color.maginder_deep_grey)
                .addDrawerItems(
                        itemMenu.withTextColorRes(R.color.maginder_soft_white),
                        itemSelectedMenu.withTextColorRes(R.color.maginder_soft_white),
                        itemOrderedMenu.withTextColorRes(R.color.maginder_soft_white),
                        itemPromotions.withTextColorRes(R.color.maginder_soft_white),
                        itemSignOut.withTextColorRes(R.color.maginder_soft_white)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem == itemMenu) {

                        } else if (drawerItem == itemSelectedMenu) {

                        } else if (drawerItem == itemOrderedMenu) {

                        } else if (drawerItem == itemPromotions) {

                        } else if (drawerItem == itemSignOut) {
                            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            finish();
                        }
                        return false;
                    }
                })
                .build();
    }

    private void setupView() {
        recyclerViewTable.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MainAdapter();
        recyclerViewTable.setAdapter(adapter);
    }

    private void createItem(List<TableZoneItemDao> data, String zoneName, String zoneId) {
        List<BaseItem> itemList = new ArrayList<>();

        for (int index = 0; index < data.size(); index++) {
//            Log.d(TAG, "createItem: " + data.get(index).getZoneName());
            if (data.get(index).getZoneName().equals(zoneName)) {
//                Log.d(TAG, "size: " + data.get(index).getTable().size());
                for (int index2 = 0; index2 < data.get(index).getTable().size(); index2++) {
//                    Log.d(TAG, "Table Name : " + data.get(index).getTable().get(index2).getTableName());
                    itemList.add(new TableItem()
                            .setTableName(data.get(index).getTable().get(index2).getTableName())
                            .setTableId(data.get(index).getTable().get(index2).getTableId())
                            .setZoneId(zoneId)
                    );
                }
            }
        }

        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();
    }

    private void createTabLayout(List<String> listTabLayout) {
        for (int index = 0; index < listTabLayout.size(); index++) {
            tabLayout.addTab(tabLayout.newTab().setText(listTabLayout.get(index)));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(this);
    }
}
