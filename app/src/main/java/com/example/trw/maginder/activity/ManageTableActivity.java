package com.example.trw.maginder.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.fragment.ManageOrderFragment;
import com.example.trw.maginder.fragment.TableFragment;
import com.example.trw.maginder.manager.AuthManager;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class ManageTableActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ManageTableActivity";

    private ImageView imgvHamburgerMenu;
    private Toolbar toolbarMenu;
    private TextView tvTitleRestaurantName;

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

        initializeUI();
        setupView();
        initializeDrawer();
    }

    private void initializeUI() {
        toolbarMenu = findViewById(R.id.tb_menu);
        imgvHamburgerMenu = findViewById(R.id.imgv_hamburger_menu);
        tvTitleRestaurantName = findViewById(R.id.tv_title_restaurant_name);
    }

    private void setupView() {
        tvTitleRestaurantName.setText(getRestaurantName());

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

    private String getRestaurantName() {
        return AuthManager.getInstance().getCurrentRestaurantName();
    }

    private String getUserType() {
        return AuthManager.getInstance().getCurrentUserType();
    }

    private String getUserName() {
        return AuthManager.getInstance().getCurrentUserName();
    }

    private void onLogOut() {
        AuthManager.getInstance().clearCurrentUser();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void replaceFragment(android.support.v4.app.Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, fragment);
        transaction.commit();
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
                        new ProfileDrawerItem()
                                .withName(getUserType() + " " + getUserName())
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
                        itemManageOrder.withTextColorRes(R.color.maginder_soft_white),
                        itemSignOut.withTextColorRes(R.color.maginder_soft_white)
                )
                .withOnDrawerItemClickListener(onDrawerItemClickListener)
                .build();
    }

    Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            Fragment fragment;
            if (drawerItem == itemTable) {
                fragment = new TableFragment();
                replaceFragment(fragment);
            } else if (drawerItem == itemManageOrder) {
                fragment = new ManageOrderFragment();
                replaceFragment(fragment);
            } else if (drawerItem == itemSignOut) {
                onLogOut();
            }
            return false;
        }
    };
}
