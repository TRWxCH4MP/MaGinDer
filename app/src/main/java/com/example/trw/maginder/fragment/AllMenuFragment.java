package com.example.trw.maginder.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.OrderItem;
import com.example.trw.maginder.callback.OnChooseMenu;
import com.example.trw.maginder.callback.OnFragmentCallback;
import com.example.trw.maginder.service.dao.RestaurantItemCollectionDao;
import com.example.trw.maginder.service.dao.RestaurantItemDao;
import com.example.trw.maginder.service.dao.RestaurantMenuTypeItemCollectionDao;
import com.example.trw.maginder.service.http_manger.HttpManagerRestaurant;
import com.example.trw.maginder.service.http_manger.HttpManagerRestaurantMenuType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMenuFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private static final String TAG = "AllMenuFragment";
    private static final String PREF_NAME = "PREF_NAME";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String ID_RESTAURANT = "id_restaurant";

    private String name;
    private String type;
    private String idRestaurant;

    private RestaurantMenuTypeItemCollectionDao menuTypeDao;
    private List<String> listTabLayoutMenu = new ArrayList<>();
    private List<RestaurantItemDao> listRestaurant;
    private RestaurantItemCollectionDao menuDao;
    private List<String> listMenuId = new ArrayList<>();

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private TextView textViewOrderTotal;
    private RelativeLayout iconCart;

    private OnChooseMenu onChooseMenu;
    private OnFragmentCallback onFragmentCallback;


    public AllMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onChooseMenu = (OnChooseMenu) context;
            onFragmentCallback = (OnFragmentCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnChooseMenu or OnFragmentCallback ");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        idRestaurant = sharedPreferences.getString(ID_RESTAURANT, null);
        name = sharedPreferences.getString(NAME, null);
        type = sharedPreferences.getString(TYPE, null);

        if (!idRestaurant.isEmpty()) {
            getDataRestaurantMenuType();
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("MenuId"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_menu, container, false);

        initializeUI(view);
        setupView();
        return view;
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String menuId = intent.getStringExtra("Id");
            listMenuId.add(menuId);
            Toast.makeText(context, menuId, Toast.LENGTH_SHORT).show();
            textViewOrderTotal.setText(String.valueOf(listMenuId.size()));
            onChooseMenu.onChooseMenu(listMenuId);
        }

    };

    private void getDataRestaurantMenuType() {
        Call<RestaurantMenuTypeItemCollectionDao> call = HttpManagerRestaurantMenuType.getInstance().getService().repos(idRestaurant);
        call.enqueue(new Callback<RestaurantMenuTypeItemCollectionDao>() {
            @Override
            public void onResponse(Call<RestaurantMenuTypeItemCollectionDao> call, Response<RestaurantMenuTypeItemCollectionDao> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Connect service is successful");

                    menuTypeDao = response.body();
                    for (int index = 0; index < menuTypeDao.getData().size(); index++) {
                        Log.d("onResponse", menuTypeDao.getData().get(index).getName());
                        listTabLayoutMenu.add(menuTypeDao.getData().get(index).getName());
                    }
                    if (!listTabLayoutMenu.isEmpty()) {
                        createTabLayout(listTabLayoutMenu);
                        getDataRestaurantMenu();
                    }
                } else {
                    Log.d(TAG, "Connect service is failure");
                }
            }

            @Override
            public void onFailure(Call<RestaurantMenuTypeItemCollectionDao> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    private void getDataRestaurantMenu() {
        Call<RestaurantItemCollectionDao> call = HttpManagerRestaurant.getInstance().getService().repos(idRestaurant);
        call.enqueue(new Callback<RestaurantItemCollectionDao>() {
            @Override
            public void onResponse(Call<RestaurantItemCollectionDao> call, Response<RestaurantItemCollectionDao> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Connect service is successful");
                    menuDao = response.body();
                    listRestaurant = menuDao.getData();

                    if (!listRestaurant.isEmpty()) {
                        createItem(listRestaurant, listTabLayoutMenu.get(0));
                    } else {
                        createItem(null, null);
                    }
                } else {
                    Log.d(TAG, "Connect service is failure");
                }
            }

            @Override
            public void onFailure(Call<RestaurantItemCollectionDao> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    public void createItem(List<RestaurantItemDao> listRestaurant, String menuType) {
        List<BaseItem> itemList = new ArrayList<>();

        if (!listRestaurant.isEmpty() && !menuType.isEmpty()) {
            for (int index = 0; index < listRestaurant.size(); index++) {
                if (listRestaurant.get(index).getNameType().equals(menuType)) {
                    Log.d(TAG, "createItem: Item + " + String.valueOf(listRestaurant.get(index).getNameType()));
                    itemList.add(new OrderItem()
                            .setOrderImage("http://suttest.atwebpages.com/Project/menu_img/" + listRestaurant.get(index).getImg())
                            .setOrderName(listRestaurant.get(index).getName())
                            .setOrderPrice(listRestaurant.get(index).getPrice() + " บาท")
                            .setOrderId(listRestaurant.get(index).getIdMenu()));
                }
            }
            adapter.setItemList(itemList);
            adapter.notifyDataSetChanged();
        }
    }


    private void createTabLayout(List<String> listTabLayout) {
        for (int index = 0; index < listTabLayout.size(); index++) {
            tabLayout.addTab(tabLayout.newTab().setText(listTabLayout.get(index)));
        }
        tabLayout.setTabGravity(TabLayout.MODE_SCROLLABLE);

        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == tabLayout.getSelectedTabPosition()) {
            Toast.makeText(getContext()
                    , listTabLayoutMenu.get(tabLayout.getSelectedTabPosition())
                    , Toast.LENGTH_SHORT).show();
            createItem(listRestaurant, listTabLayoutMenu.get(tabLayout.getSelectedTabPosition()));
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == tabLayout.getSelectedTabPosition()) {
            createItem(listRestaurant, listTabLayoutMenu.get(tabLayout.getSelectedTabPosition()));
        }
    }

    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_list_menu);
        tabLayout = view.findViewById(R.id.tab_title);
        textViewOrderTotal = view.findViewById(R.id.tv_menu_count);
        iconCart = view.findViewById(R.id.icon_cart);

        iconCart.setOnClickListener(this);

    }

    public void setupView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new MainAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_cart:
                Fragment fragment = new PreOrderFragment();
                onFragmentCallback.onFragmentCallback(fragment);
                break;
        }
    }
}
