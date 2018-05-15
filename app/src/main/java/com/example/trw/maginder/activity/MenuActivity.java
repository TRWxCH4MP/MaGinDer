package com.example.trw.maginder.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.callback.MenuListenerCallback;
import com.example.trw.maginder.manager.AuthManager;
import com.example.trw.maginder.manager.MenuManager;
import com.example.trw.maginder.callback.FragmentCallback;
import com.example.trw.maginder.db.DeleteData;
import com.example.trw.maginder.db.callback.OnStateCallback;
import com.example.trw.maginder.fragment.AllMenuFragment;
import com.example.trw.maginder.fragment.PreOrderFragment;
import com.example.trw.maginder.fragment.PreOrderedMenuFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class MenuActivity extends AppCompatActivity implements View.OnClickListener, MenuListenerCallback, FragmentCallback {
    private static final String TAG = "MenuActivity";

    private ImageView imageViewDrawerMenu;
    private Drawer drawer;
    private PrimaryDrawerItem itemTable;
    private PrimaryDrawerItem itemMenu;
    private PrimaryDrawerItem itemSelectedMenu;
    private PrimaryDrawerItem itemOrderedMenu;
    private PrimaryDrawerItem itemSignOut;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new AllMenuFragment())
                    .commit();
        }

        initializeUI();
        setupView();
        initializeDrawer();
    }

    private void initializeUI() {
        imageViewDrawerMenu = findViewById(R.id.imgv_hamburger_menu);
        imageViewDrawerMenu.setOnClickListener(this);
    }

    private void setupView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_menu));
        progressDialog.setCancelable(false);

        getMenuAmount();
    }

    private String getUserType() {
        return AuthManager.getInstance().getCurrentUserType();
    }

    private String getUserName() {
        return AuthManager.getInstance().getCurrentUserName();
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
    public void menuListenerCallback(boolean isSuccess) {
        if (isSuccess) {
            Log.d(TAG, "menuListenerCallback: ");
            getMenuAmount();
        } else {
            drawer.updateBadge(2, new StringHolder(String.valueOf(0)));
        }
    }

    private void getMenuAmount() {
        MenuManager.getInstance().onGetMenuAmount(new MenuManager.MenuAmountCallback() {
            @Override
            public void onMenuAmount(String amount) {
                drawer.updateBadge(2, new StringHolder(amount));
            }
        });
    }

    @Override
    public void onFragmentCallback(Fragment fragment) {
        if (fragment != null) {
            replaceFragment(fragment);
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, fragment);
        transaction.commit();
    }

    private void onLogOut() {
        DeleteData.onDeleteMenu(getApplicationContext(), new OnStateCallback() {
            @Override
            public void stateCallback(boolean isSuccess) {
                if (isSuccess) {
                    goToLoginScreen();
                }
            }
        });
    }

    private void goToLoginScreen() {
        AuthManager.getInstance().clearCurrentUser();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void onBackToTableActivity() {
        DeleteData.onDeleteMenu(getApplicationContext(), new OnStateCallback() {
            @Override
            public void stateCallback(boolean isSuccess) {
                if (isSuccess) {
                    finish();
                }
            }
        });
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
                        itemMenu.withTextColorRes(R.color.maginder_soft_white),
                        itemSelectedMenu.withTextColorRes(R.color.maginder_soft_white),
                        itemOrderedMenu.withTextColorRes(R.color.maginder_soft_white),
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
                onBackToTableActivity();
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
                onLogOut();
            }
            return false;
        }
    };
}
