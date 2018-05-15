package com.example.trw.maginder.fragment;


import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.callback.MenuListenerCallback;
import com.example.trw.maginder.manager.AuthManager;
import com.example.trw.maginder.manager.MenuManager;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.callback.FragmentCallback;
import com.example.trw.maginder.callback.MenuCallback;
import com.example.trw.maginder.service.dao.RestaurantItemCollectionDao;
import com.example.trw.maginder.service.dao.RestaurantItemDao;
import com.example.trw.maginder.service.dao.RestaurantMenuTypeItemCollectionDao;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMenuFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private static final String TAG = "AllMenuFragment";

    private String restaurantId;

    private List<String> listTabLayoutMenu = new ArrayList<>();
    private List<RestaurantItemDao> listRestaurant;

    private RelativeLayout iconCart;
    private TextView textViewOrderTotal;
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private ProgressDialog progressDialog;
    private Button btnVerifyOrderMenu;

    private MainAdapter adapter;
    private MenuListenerCallback menuListenerCallback;
    private FragmentCallback onFragmentCallback;

    public AllMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            menuListenerCallback = (MenuListenerCallback) context;
            onFragmentCallback = (FragmentCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MenuListenerCallback or FragmentCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getRestaurantId();
        if (!restaurantId.isEmpty()) {
            getDataRestaurantMenuType();
        }

    }

    private void getRestaurantId() {
        restaurantId = AuthManager.getInstance().getCurrentRestaurantId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_menu, container, false);

        initializeUI(view);
        setupView();
        getMenuAmount();

        return view;
    }

    private void getMenuAmount() {
        MenuManager.getInstance().onGetMenuAmount(new MenuManager.MenuAmountCallback() {
            @Override
            public void onMenuAmount(String amount) {
                textViewOrderTotal.setText(amount);
            }
        });
    }

    private void getMenuAllDetail(String menuId) {
        MenuManager.getInstance().onGetMenuAllDetail(menuId, new MenuManager.MenuDetailCallback() {
            @Override
            public void onComplete(List<RestaurantItemDao> data) {
                setupMenuToPreOrder(data);
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "onFailure: " + error);
            }
        });

    }

    private void setupMenuToPreOrder(List<RestaurantItemDao> data) {
        MenuManager.getInstance().onSetMenuToSQLite(data, new MenuManager.SetupMenuToSQLiteCallback() {
            @Override
            public void onComplete(boolean isSuccess) {
                if (isSuccess) {
                    menuListenerCallback.menuListenerCallback(true);
                    getMenuAmount();
                    hideLoading();
                } else {
                    hideLoading();
                }
            }

            @Override
            public void onFailure(String error) {
                hideLoading();
                Log.d(TAG, "onFailure: " + error);
            }
        });
    }

    private void hideLoading() {
        progressDialog.dismiss();
    }

    private void getDataRestaurantMenuType() {
        MenuManager.getInstance().onGetMenuType(restaurantId, new MenuManager.GetMenuTypeCallback() {
            @Override
            public void onComplete(RestaurantMenuTypeItemCollectionDao daoMenuType) {
                RestaurantMenuTypeItemCollectionDao menuTypeDao = daoMenuType;
                setupLayoutMenuType(menuTypeDao);
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "onFailure: " + error);
            }
        });
    }

    private void setupLayoutMenuType(RestaurantMenuTypeItemCollectionDao menuTypeDao) {
        for (int index = 0; index < menuTypeDao.getData().size(); index++) {
            Log.d("onResponse", menuTypeDao.getData().get(index).getName());
            listTabLayoutMenu.add(menuTypeDao.getData().get(index).getName());
        }
        if (!listTabLayoutMenu.isEmpty()) {
            createTabLayout(listTabLayoutMenu);
            getDataRestaurantMenu();
        }
    }

    private void getDataRestaurantMenu() {
        MenuManager.getInstance().onGetMenu(restaurantId, new MenuManager.GetMenuCallback() {
            @Override
            public void onComplete(List<RestaurantItemDao> data) {
                setupMenu(data);

            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "onFailure: " + error);
            }
        });
    }

    private void setupMenu(List<RestaurantItemDao> data) {
        listRestaurant = data;
        if (!listRestaurant.isEmpty()) {
            createMenu(listRestaurant, listTabLayoutMenu.get(0));
        } else {
            createMenu(null, null);
        }
    }

    private void createMenu(List<RestaurantItemDao> listRestaurant, String menuType) {
        List<BaseItem> itemList = MenuManager.getInstance().onCreateMenu(listRestaurant, menuType);

        if (itemList != null && !itemList.isEmpty()) {
            hideLoading();
            adapter.setItemList(itemList);
            adapter.notifyDataSetChanged();
        } else {
            hideLoading();
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
            createMenu(listRestaurant, listTabLayoutMenu.get(tabLayout.getSelectedTabPosition()));
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == tabLayout.getSelectedTabPosition()) {
            createMenu(listRestaurant, listTabLayoutMenu.get(tabLayout.getSelectedTabPosition()));
        }
    }

    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_list_menu);
        tabLayout = view.findViewById(R.id.tab_title);
        textViewOrderTotal = view.findViewById(R.id.tv_menu_count);
        iconCart = view.findViewById(R.id.icon_cart);
        btnVerifyOrderMenu = view.findViewById(R.id.btn_verify_order_menu);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("กำลังโหลดรายการอาหาร");
        progressDialog.setCancelable(false);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        iconCart.setOnClickListener(this);

    }

    public void setupView() {
        MenuCallback callbackMenu = new MenuCallback() {
            @Override
            public void onCallbackMenu(String menuId) {
//                Toast.makeText(getContext(), menuId, Toast.LENGTH_SHORT).show();
                setupOrderMenu(menuId);
                showLoading();
//                MenuManager.getInstance().menuCountIsPlus(1);
//                Log.d(TAG, "onCallbackMenu: " + MenuManager.getInstance().getMenuCount());
            }
        };

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new MainAdapter(callbackMenu);
        recyclerView.setAdapter(adapter);

        showLoading();
    }

    private void showLoading() {
        progressDialog.show();
    }

    private void setupOrderMenu(String menuId) {
//        Log.d(TAG, "onReceive: ");
        getMenuAllDetail(menuId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_cart:
                Fragment fragment = new PreOrderFragment();
                onFragmentCallback.onFragmentCallback(fragment);
                break;
            case R.id.btn_verify_order_menu:

                break;
        }
    }

}
