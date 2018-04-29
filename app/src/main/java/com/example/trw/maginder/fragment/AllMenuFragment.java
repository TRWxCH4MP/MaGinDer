package com.example.trw.maginder.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.StaticStringHelper;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.MenuItem;
import com.example.trw.maginder.callback.ChooseMenuCallback;
import com.example.trw.maginder.callback.FragmentCallback;
import com.example.trw.maginder.callback.MenuCallback;
import com.example.trw.maginder.db.InsertData;
import com.example.trw.maginder.db.QueryData;
import com.example.trw.maginder.db.callback.OnStateCallback;
import com.example.trw.maginder.db.callback.SendListDataCallback;
import com.example.trw.maginder.db.entity.MenuEntity;
import com.example.trw.maginder.service.dao.RestaurantItemCollectionDao;
import com.example.trw.maginder.service.dao.RestaurantItemDao;
import com.example.trw.maginder.service.dao.RestaurantMenuTypeItemCollectionDao;
import com.example.trw.maginder.service.http_manger.HttpManagerMenu;
import com.example.trw.maginder.service.http_manger.HttpManagerRestaurant;
import com.example.trw.maginder.service.http_manger.HttpManagerRestaurantMenuType;

import java.util.ArrayList;
import java.util.Date;
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
    private static final String IMAGE_URL = "http://it2.sut.ac.th/prj60_g14/Project/menu_img/";

    private String restaurantId;
    private String employeeName;
    private String employeeType;
    private String tableId;
    private String transaction;
    private String tableName;
    private String timeStampOrderMenu;

    private List<String> listTabLayoutMenu = new ArrayList<>();
    private List<RestaurantItemDao> listRestaurant;
    private List<String> listMenuId = new ArrayList<>();

    private RelativeLayout iconCart;
    private TextView textViewOrderTotal;
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private ProgressDialog progressDialog;
    private Button btnVerifyOrderMenu;

    private RestaurantMenuTypeItemCollectionDao menuTypeDao;
    private RestaurantItemCollectionDao menuDao;
    private MainAdapter adapter;
    private ChooseMenuCallback chooseMenuCallback;
    private FragmentCallback onFragmentCallback;

    public AllMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            chooseMenuCallback = (ChooseMenuCallback) context;
            onFragmentCallback = (FragmentCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ChooseMenuCallback or FragmentCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        employeeName = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_NAME, null);
        employeeType = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_TYPE, null);
        restaurantId = sharedPreferences.getString(StaticStringHelper.RESTAURANT_ID, null);

        if (getArguments() != null) {
            tableId = getArguments().getString("TableId");
            transaction = getArguments().getString("Transaction");
            tableName = getArguments().getString("TableName");
            Log.d(TAG, "onCreate: " + tableId);
            Log.d(TAG, "onCreate: " + tableName);
            Log.d(TAG, "onCreate: " + transaction);
        }

        if (!restaurantId.isEmpty()) {
            getDataRestaurantMenuType();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_menu, container, false);

        initializeUI(view);
        setupView();
        getPreOrderMenuAmount();

        return view;
    }

    private void getPreOrderMenuAmount() {
        QueryData.onLoadMenu(getContext(), new SendListDataCallback() {
            @Override
            public void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
                if (isSuccess) {
                    textViewOrderTotal.setText(String.valueOf(menuEntities.size()));
                }
            }
        });
    }

    private void getMenuAllDetail(final String menuId) {
        Log.d(TAG, "getMenuAllDetail: ");
        retrofit2.Call<RestaurantItemCollectionDao> call = HttpManagerMenu.getInstance().getService().repos(menuId);
        call.enqueue(new Callback<RestaurantItemCollectionDao>() {
            @Override
            public void onResponse(retrofit2.Call<RestaurantItemCollectionDao> call, Response<RestaurantItemCollectionDao> response) {
                if (response.isSuccessful()) {
                    RestaurantItemCollectionDao dao = response.body();
                    Log.d(TAG, "onResponse: success");
                    setPreOrderMenuToSQLite(dao.getData());
                    progressDialog.show();
                } else {
                    Log.d(TAG, "onResponse: failure");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<RestaurantItemCollectionDao> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void setPreOrderMenuToSQLite(List<RestaurantItemDao> data) {

        MenuEntity menuEntity = new MenuEntity();
        for (int index = 0; index < data.size(); index++) {
            menuEntity.setDate(data.get(index).getDate());
            menuEntity.setIdMenu(data.get(index).getIdMenu());
            menuEntity.setIdKitchen(data.get(index).getIdKitchen());
            menuEntity.setImg(data.get(index).getImg());
            menuEntity.setName(data.get(index).getName());
            menuEntity.setPrice(data.get(index).getPrice());
        }
        InsertData.onInsertMenu(getContext(), menuEntity, new OnStateCallback() {
            @Override
            public void stateCallback(boolean isSuccess) {
                if (isSuccess) {
//                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    QueryData.onLoadMenu(getContext(), new SendListDataCallback() {
                        @Override
                        public void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
                            if (isSuccess) {
                                textViewOrderTotal.setText(String.valueOf(menuEntities.size()));
                            } else {

                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void getDataRestaurantMenuType() {
        Call<RestaurantMenuTypeItemCollectionDao> call = HttpManagerRestaurantMenuType.getInstance().getService().repos(restaurantId);
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
        Call<RestaurantItemCollectionDao> call = HttpManagerRestaurant.getInstance().getService().repos(restaurantId);
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
                    Log.d(TAG, "createItemTotal: Item + " + String.valueOf(listRestaurant.get(index).getNameType()));
                    itemList.add(new MenuItem()
                            .setOrderDate(listRestaurant.get(index).getDate())
                            .setOrderMenuId(listRestaurant.get(index).getIdMenu())
                            .setOrderKitchenId(listRestaurant.get(index).getIdKitchen())
                            .setOrderImg(IMAGE_URL + listRestaurant.get(index).getImg())
                            .setOrderName(listRestaurant.get(index).getName())
                            .setOrderPrice(listRestaurant.get(index).getPrice() + " บาท")
                    );
                }
            }
            adapter.setItemList(itemList);
            adapter.notifyDataSetChanged();
        }

        progressDialog.dismiss();
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
            }
        };

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new MainAdapter(callbackMenu);
        recyclerView.setAdapter(adapter);

        progressDialog.show();
    }

    private void setupOrderMenu(String menuId) {
        int time = (int) (new Date().getTime() / 1000);
        timeStampOrderMenu = "MT-" + time;

        listMenuId.add(timeStampOrderMenu);
        Log.d(TAG, "onReceive: ");

        chooseMenuCallback.ChooseMenuCallback(menuId, timeStampOrderMenu);
//        ChooseMenuCallback.onMenuAmount(listMenuId.size());

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
