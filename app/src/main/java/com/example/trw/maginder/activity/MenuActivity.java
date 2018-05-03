package com.example.trw.maginder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.model.StaticStringHelper;
import com.example.trw.maginder.callback.ChooseMenuCallback;
import com.example.trw.maginder.callback.FragmentCallback;
import com.example.trw.maginder.db.DeleteData;
import com.example.trw.maginder.db.QueryData;
import com.example.trw.maginder.db.callback.OnStateCallback;
import com.example.trw.maginder.db.callback.SendListDataCallback;
import com.example.trw.maginder.db.entity.MenuEntity;
import com.example.trw.maginder.fragment.AllMenuFragment;
import com.example.trw.maginder.fragment.PreOrderFragment;
import com.example.trw.maginder.fragment.PreOrderedMenuFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, ChooseMenuCallback, FragmentCallback {
    private static final String TAG = "MenuActivity";
    private static final String PREF_NAME = "PREF_NAME";
    private static final String ID_RESTAURANT = "id_restaurant";

    private ImageView imageViewDrawerMenu;
    private String employeeName;
    private String employeeType;
    private String restaurantId;
    private String tableId;
    private String timeStampTransaction;
    private String tableName;

    private Drawer drawer;
    private PrimaryDrawerItem itemTable;
    private PrimaryDrawerItem itemMenu;
    private PrimaryDrawerItem itemSelectedMenu;
    private PrimaryDrawerItem itemOrderedMenu;
    private PrimaryDrawerItem itemSignOut;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mRootRef;
    private String timeStampOrderMenu;
    private ArrayList<String> listTimeStampOrderMenu = new ArrayList<>();

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bundle = new Bundle();

        getCurrentUser();

        Intent intent = getIntent();
        tableId = intent.getStringExtra("tableId");
        timeStampTransaction = intent.getStringExtra("timeStamp");
        tableName = intent.getStringExtra("tableName");
        Log.d(TAG, "onCreate: " + tableId);
        Log.d(TAG, "onCreate: " + timeStampTransaction);
        Log.d(TAG, "onCreate: " + tableName);

        if (savedInstanceState == null) {
            Fragment fragment = new AllMenuFragment();
            bundle.putString(ID_RESTAURANT, restaurantId);
            bundle.putString("TableId", tableId);
            bundle.putString("TableName", tableName);
            bundle.putString("Transaction", timeStampTransaction);
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new AllMenuFragment())
                    .commit();
        }

        mRootRef = FirebaseDatabase.getInstance().getReference();


        initializeUI();
        initializeDrawer();

    }

    private void getCurrentUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        employeeName = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_NAME, null);
        employeeType = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_TYPE, null);
        restaurantId = sharedPreferences.getString(StaticStringHelper.RESTAURANT_ID, null);
    }

    private void initializeUI() {
        imageViewDrawerMenu = findViewById(R.id.imgv_hamburger_menu);
        imageViewDrawerMenu.setOnClickListener(this);

    }

    private void initializeDrawer() {

        itemTable = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withName(R.string.table)
                .withIcon(R.drawable.tray)
                .withSelectable(false);

        itemMenu = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.menu)
                .withIcon(R.drawable.menu_icon)
                .withSelectable(false);

        itemSelectedMenu = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.selected_menu)
                .withIcon(R.drawable.cart_icon)
                .withBadge("0")
                .withBadgeStyle(new BadgeStyle()
                        .withTextColor(Color.WHITE)
                        .withColorRes(R.color.maginder_soft_yellow))
                .withSelectable(false);

        itemOrderedMenu = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName(R.string.ordered_menu)
                .withIcon(R.drawable.history_icon)
                .withSelectable(false);

        itemSignOut = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName(R.string.sign_out)
                .withIcon(R.drawable.signout_icon)
                .withSelectable(false);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.maginder_deep_grey)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(employeeType + " " + employeeName)
                                .withIcon(R.drawable.maginder_logo)
                )
                .build();


        drawer = new DrawerBuilder()
                .withSelectedItem(-1)
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withSliderBackgroundColorRes(R.color.maginder_deep_grey)
                .addDrawerItems(
                        itemTable.withTextColorRes(R.color.maginder_soft_white),
                        itemMenu.withTextColorRes(R.color.maginder_soft_white),
                        itemSelectedMenu.withTextColorRes(R.color.maginder_soft_white),
                        itemOrderedMenu.withTextColorRes(R.color.maginder_soft_white),
                        itemSignOut.withTextColorRes(R.color.maginder_soft_white)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Fragment fragment;
                        if (drawerItem == itemTable) {
                            DeleteData.onDeleteMenu(getApplicationContext(), new OnStateCallback() {
                                @Override
                                public void stateCallback(boolean isSuccess) {
                                    if (isSuccess) {
                                        finish();
                                    }
                                }
                            });
                        } else if (drawerItem == itemMenu) {
                            fragment = new AllMenuFragment();
                            replaceFragment(fragment);
                        } else if (drawerItem == itemSelectedMenu) {
                            fragment = new PreOrderFragment();
                            replaceFragment(fragment);
                        } else if (drawerItem == itemOrderedMenu) {
                            fragment = new PreOrderedMenuFragment();
                            replaceFragment(fragment);
                        } else if (drawerItem == itemSignOut) {
                            DeleteData.onDeleteMenu(getApplicationContext(), new OnStateCallback() {
                                @Override
                                public void stateCallback(boolean isSuccess) {
                                    if (isSuccess) {
                                        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.apply();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                        return false;
                    }
                })
                .build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_hamburger_menu:
                drawer.openDrawer();
                break;
        }
    }


    @Override
    public void ChooseMenuCallback(String menuId, String timeStampOrderMenu) {
        Log.d(TAG, "ChooseMenuCallback: ");
        this.timeStampOrderMenu = timeStampOrderMenu;
        listTimeStampOrderMenu.add(timeStampOrderMenu);
        Log.d(TAG, "ChooseMenuCallback: ");
        Log.d(TAG, "ChooseMenuCallback: " + listTimeStampOrderMenu.size());
        Log.d(TAG, "ChooseMenuCallback: " + menuId);
        getPreOrderMenuAmount();
    }

    @Override
    public void onMenuAmount(int menuCount) {
        if (menuCount >= 0) {
            drawer.updateBadge(2, new StringHolder(String.valueOf(menuCount)));
        }
    }

    private void getPreOrderMenuAmount() {
        QueryData.onLoadMenu(this, new SendListDataCallback() {
            @Override
            public void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
                if (isSuccess) {
                    drawer.updateBadge(2, new StringHolder(String.valueOf(menuEntities.size())));
                }
            }
        });
    }

    @Override
    public void onFragmentCallback(Fragment fragment) {
        Log.d(TAG, "onFragmentCallback: " + listTimeStampOrderMenu.size());
        if (fragment != null) {
            replaceFragment(fragment);
        }
    }

    public void replaceFragment(Fragment fragment) {

        ArrayList<String> listMenu = new ArrayList<>(listTimeStampOrderMenu);
        bundle.putString(ID_RESTAURANT, restaurantId);
        bundle.putString("TableId", tableId);
        bundle.putString("TableName", tableName);
        bundle.putString("Transaction", timeStampTransaction);
        bundle.putStringArrayList("TransactionMenu", listMenu);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, fragment);
        transaction.commit();
    }

}
