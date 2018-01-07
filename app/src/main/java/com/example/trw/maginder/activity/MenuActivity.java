package com.example.trw.maginder.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trw.maginder.CreateDrawerMenu;
import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.OrderItem;
import com.example.trw.maginder.callback.OnClickTitle;
import com.example.trw.maginder.db.QueryData;
import com.example.trw.maginder.db.SendListDataCallback;
import com.example.trw.maginder.db.entity.EmployeeEntity;
import com.example.trw.maginder.db.entity.MenuEntity;
import com.example.trw.maginder.fragment.AllMenuFragment;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener
        , TabLayout.OnTabSelectedListener
        , SendListDataCallback {

    private CreateDrawerMenu createDrawer;
    private ImageView imageViewDrawerMenu;
    private TabLayout tabLayout;
    OnClickTitle callback;

    private Bundle bundle;
    private String name;
    private String type;

    private RecyclerView recyclerView;
    private MainAdapter adapter;

    private List<String> listTabLayout = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bundle = getIntent().getExtras();
        name  = bundle.getString("name");
        type = bundle.getString("type");

        QueryData.onLoadMenuType(this, this);
        initializeUI();
        handelOnclick();
        setupView();
//        createTabLayout(listTabLayout);
    }

    private void createTabLayout(List<String> listTabLayout) {
        tabLayout.addTab(tabLayout.newTab().setText("รวม"));
        for (int index = 0; index < listTabLayout.size(); index++) {
            tabLayout.addTab(tabLayout.newTab().setText(listTabLayout.get(index)));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(this);
    }

    private void handelOnclick() {
        imageViewDrawerMenu.setOnClickListener(this);
    }

    private void initializeUI() {
        recyclerView = findViewById(R.id.recycler_view_list_menu);
        imageViewDrawerMenu = findViewById(R.id.imgv_hamburger_menu);
        tabLayout = findViewById(R.id.tab_title);

        createDrawer = new CreateDrawerMenu(this);
        createDrawer.createNavigationDrawer(name, type);
    }

    public void setupView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MainAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void createItem(int menuType) {
        List<BaseItem> itemList = new ArrayList<>();

        if (menuType == 0) {
            itemList.add(new OrderItem()
                    .setOrderImage(R.drawable.food)
                    .setOrderName("พิซซ่า")
                    .setOrderPrice("120 บาท"));
        } else if (menuType == 1) {
            itemList.clear();
            adapter.notifyDataSetChanged();
            Log.d("create Item", "createItem: menuType = 1");
        }

        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_hamburger_menu:
                createDrawer.navigationDrawerOpen();
                break;
        }
    }

    public void replaceFragment(AllMenuFragment fragment, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer_item, fragment);
        transaction.commit();

        fragment.setArguments(bundle);
//        fragment.setupView(position);
    }

    public void removeFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(new AllMenuFragment()).commit();
        Log.d("MenuActivity", "remove fragment");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            Log.d("TabSelected", String.valueOf(tabLayout.getSelectedTabPosition()));
        } else if (tab.getPosition() == 1) {
            Log.d("TabSelected", String.valueOf(tabLayout.getSelectedTabPosition()));
        } else if (tab.getPosition() == 2) {
            Log.d("TabSelected", String.valueOf(tabLayout.getSelectedTabPosition()));
        } else if (tab.getPosition() == 3) {
            Log.d("TabSelected", String.valueOf(tabLayout.getSelectedTabPosition()));
        }
        Toast.makeText(this, String.valueOf(tabLayout.getSelectedTabPosition()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            Toast.makeText(this, String.valueOf(tabLayout.getSelectedTabPosition()), Toast.LENGTH_SHORT).show();
        } else if (tab.getPosition() == 1) {
            Toast.makeText(this, String.valueOf(tabLayout.getSelectedTabPosition()), Toast.LENGTH_SHORT).show();
        } else if (tab.getPosition() == 2) {
            Toast.makeText(this, String.valueOf(tabLayout.getSelectedTabPosition()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadEmployeeDataCallback(List<EmployeeEntity> employeeEntities, boolean isSuccess) {

    }

    @Override
    public void loadMenuTypeDataCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
        if (isSuccess) {
            for (int index = 0; index < menuEntities.size(); index++) {
                listTabLayout.add(menuEntities.get(index).getMenuTypeName());
                Log.d("MenuType", menuEntities.size() + "");
            }
            createTabLayout(listTabLayout);
        }
    }
}
