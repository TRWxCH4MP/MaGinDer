package com.example.trw.maginder.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trw.maginder.CreateDrawerMenu;
import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.OrderItem;
import com.example.trw.maginder.callback.OnChooseMenu;
import com.example.trw.maginder.callback.OnFragmentCallback;
import com.example.trw.maginder.fragment.AllMenuFragment;
import com.example.trw.maginder.service.dao.RestaurantItemCollectionDao;
import com.example.trw.maginder.service.dao.RestaurantItemDao;
import com.example.trw.maginder.service.dao.RestaurantMenuTypeItemCollectionDao;
import com.example.trw.maginder.service.http_manger.HttpManagerRestaurant;
import com.example.trw.maginder.service.http_manger.HttpManagerRestaurantMenuType;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, OnChooseMenu, OnFragmentCallback {
    private static final String TAG = "MenuActivity";
    private static final String PREF_NAME = "PREF_NAME";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String ID_RESTAURANT = "id_restaurant";
    private static final String LIST_MENU = "listMenu";

    private CreateDrawerMenu createDrawer;
    private ImageView imageViewDrawerMenu;
    private TabLayout tabLayout;
    private TextView textViewOrderTotal;
    private String name;
    private String type;
    private String idRestaurant;
    private String tableId;

    private RecyclerView recyclerView;
    private MainAdapter adapter;

    private List<String> listTabLayoutMenu = new ArrayList<>();
    private List<RestaurantItemDao> listRestaurant;
    private RestaurantItemCollectionDao menuDao;
    private RestaurantMenuTypeItemCollectionDao menuTypeDao;
    private List<String> listMenuId;

    private Drawer drawer;
    private PrimaryDrawerItem itemMenu;
    private PrimaryDrawerItem itemSelectedMenu;
    private PrimaryDrawerItem itemOrderedMenu;
    private PrimaryDrawerItem itemPromotions;
    private PrimaryDrawerItem itemSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new AllMenuFragment())
                    .commit();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        idRestaurant = sharedPreferences.getString(ID_RESTAURANT, null);
        name = sharedPreferences.getString(NAME, null);
        type = sharedPreferences.getString(TYPE, null);

        Intent intent = getIntent();
        tableId = intent.getStringExtra("tableId");
        Log.d(TAG, "onCreate: " + tableId);

//        if (!listMenuId.isEmpty()) {
//            for (int index = 0; index < listMenuId.size(); index++) {
//                Log.d(TAG, "onCreate: listMenu" + listMenuId.get(index));
//            }
//        }

        initializeUI();
        initializeDrawer();
    }

    private void initializeUI() {
        imageViewDrawerMenu = findViewById(R.id.imgv_hamburger_menu);
        imageViewDrawerMenu.setOnClickListener(this);

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

        itemPromotions = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName(R.string.promotion)
                .withIcon(R.drawable.promotion_icon)
                .withSelectable(false);

        itemSignOut = new PrimaryDrawerItem()
                .withIdentifier(5)
                .withName(R.string.choose_table)
                .withIcon(R.drawable.signout_icon)
                .withSelectable(false);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.maginder_deep_grey)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(type + " " + name)
                                .withIcon(R.drawable.maginder_logo)
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
                            Fragment fragment = new AllMenuFragment();
                            replaceFragment(fragment);
                        } else if (drawerItem == itemSelectedMenu) {

                        } else if (drawerItem == itemOrderedMenu) {

                        } else if (drawerItem == itemPromotions) {

                        } else if (drawerItem == itemSignOut) {
                            finish();
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
    public void onChooseMenu(List<String> menuId) {
        if (!menuId.isEmpty()) {
            drawer.updateBadge(2, new StringHolder(String.valueOf(menuId.size())));
        }
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
}
