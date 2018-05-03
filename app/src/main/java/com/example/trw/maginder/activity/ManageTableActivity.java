package com.example.trw.maginder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.model.StaticStringHelper;
import com.example.trw.maginder.fragment.ManageOrderFragment;
import com.example.trw.maginder.fragment.TableFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class ManageTableActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ManageTableActivity";
    private static final String REF_FIREBASE_CHILD_TABLE = "Table";
    private static final String STATE_TRUE = "true";
    private static final String STATE_FALSE = "false";
    private static final String REF_FIREBASE_CHILD_STATUS = "Status";

    private ImageView imgvHamburgerMenu;
    private Toolbar toolbarMenu;
    private TextView tvTitleRestaurantName;

    private Bundle bundle;
    private String employeeName;
    private String employeeType;
    private String restaurantId;
    private String restaurantName;
    private String popupState;
    private String zoneId;
    private String tableId;

    private Drawer drawer;
    private PrimaryDrawerItem itemTable;
    private PrimaryDrawerItem itemManageOrder;
    private PrimaryDrawerItem itemSignOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_table);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new TableFragment())
                    .commit();
        }

        getCurrentUser();

        initializeUI();
        initializeDrawer();
    }

    private void getCurrentUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        employeeName = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_NAME, null);
        employeeType = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_TYPE, null);
        restaurantId = sharedPreferences.getString(StaticStringHelper.RESTAURANT_ID, null);
        restaurantName = sharedPreferences.getString(StaticStringHelper.RESTAURANT_NAME, null);
    }

    private void initializeUI() {
        toolbarMenu = findViewById(R.id.tb_menu);
        imgvHamburgerMenu = findViewById(R.id.imgv_hamburger_menu);
        tvTitleRestaurantName = findViewById(R.id.tv_title_restaurant_name);
        tvTitleRestaurantName.setText(restaurantName);

        if (imgvHamburgerMenu != null) {
            imgvHamburgerMenu.setOnClickListener(this);
        }

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
    public void onBackPressed() {

    }

    private void initializeDrawer() {

        itemTable = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withName(R.string.table)
                .withIcon(R.drawable.tray)
                .withSelectable(false);

        itemManageOrder = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.manage_order_menu)
                .withIcon(R.drawable.alarm)
                .withSelectable(false);

        itemSignOut = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.sign_out)
                .withIcon(R.drawable.signout_icon)
                .withSelectable(false);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.maginder_deep_grey)
                .addProfiles(
                        new ProfileDrawerItem().withName(employeeType + " " + employeeName).withIcon(R.drawable.maginder_logo)
                )
                .build();


        drawer = new DrawerBuilder()
                .withSelectedItem(-1)
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withSliderBackgroundColorRes(R.color.maginder_deep_grey)
                .addDrawerItems(
                        itemTable.withTextColorRes(R.color.maginder_soft_white),
                        itemManageOrder.withTextColorRes(R.color.maginder_soft_white),
                        itemSignOut.withTextColorRes(R.color.maginder_soft_white)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        android.support.v4.app.Fragment fragment;
                        if (drawerItem == itemTable) {
                            fragment = new TableFragment();
                            replaceFragment(fragment);
                        } else if (drawerItem == itemManageOrder) {
                            fragment = new ManageOrderFragment();
                            replaceFragment(fragment);
                        } else if (drawerItem == itemSignOut) {
                            SharedPreferences sharedPreferences = getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        return false;
                    }
                })
                .build();
    }

    private void replaceFragment(android.support.v4.app.Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, fragment);
        transaction.commit();
    }

}
