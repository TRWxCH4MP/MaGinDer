package com.example.trw.maginder.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.callback.OnFragmentCallback;
import com.example.trw.maginder.fragment.ChooseFoodTypeFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class ChooseMenuActivity extends AppCompatActivity implements View.OnClickListener, OnFragmentCallback {

    private String[] mDrawerTitle = {"ตะกร้าเมนู", "ของคาว", "ของหวาน", "เครื่องดื่ม", "โปรโมชั่น", "เมนูที่สั่ง"};
    private ImageView mImageViewHamburgerMenu;
    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private Toolbar mToolbarMenu;

    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_menu);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new ChooseFoodTypeFragment())
                    .commit();
        }

        initializeUI();
        createNavigationDrawer();

    }

    private void initializeUI() {
        mToolbarMenu = findViewById(R.id.tb_menu);
        mImageViewHamburgerMenu = findViewById(R.id.imgv_hamburger_menu);

        if (mImageViewHamburgerMenu != null) {
            mImageViewHamburgerMenu.setOnClickListener(this);
        }
    }

    private void createNavigationDrawer() {
        new DrawerBuilder().withActivity(this).build();

        PrimaryDrawerItem item = new PrimaryDrawerItem().withIdentifier(1).withName("ตะกร้าเมนู");
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("โต๊ะ");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(1).withName("ของคาว");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(1).withName("ของหวาน");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(1).withName("เครื่องดื่ม");
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(1).withName("โปรโมชั่น");
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(1).withName("ออกจากระบบ");


        drawer = new DrawerBuilder()
                .withActivity(this)
                .addDrawerItems(
                        item,
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        item6
                )
                .build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_hamburger_menu:
//                mDrawerLayout.openDrawer(GravityCompat.START);
                drawer.openDrawer();
        }
    }

    @Override
    public void onFragmentCallback(Fragment fragment) {
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
