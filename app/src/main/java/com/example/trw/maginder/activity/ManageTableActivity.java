package com.example.trw.maginder.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trw.maginder.CreateDrawerMenu;
import com.example.trw.maginder.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class ManageTableActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] mDrawerTitle = {"ตะกร้าเมนู", "ของคาว", "ของหวาน", "เครื่องดื่ม", "โปรโมชั่น", "เมนูที่สั่ง"};
    private ImageView mImageViewHamburgerMenu;
    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private Toolbar mToolbarMenu;

    private ImageView mTable1;
    private ImageView mTable2;
    private ImageView mTable3;
    private ImageView mTable4;
    private ImageView mTable5;
    private ImageView mTable6;

    private Dialog dialog;

    private TextView textViewTable;
    private TextView textViewCustomerNum;
    private TextView textViewNum;
    private ImageView imageViewTable;
    private Button buttonOk;
    private Button buttonCancel;
    private EditText editTextCustomerNum;

    private CreateDrawerMenu createDrawer;
    private Bundle bundle;
    private String name;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_table);

        bundle = getIntent().getExtras();
        name  = bundle.getString("name");
        type = bundle.getString("type");

        initializeUI();
        handelOnclick();
    }

    private void initializeUI() {
        createDrawer = new CreateDrawerMenu(this);
        createDrawer.createNavigationDrawer(name,type);

        mToolbarMenu = findViewById(R.id.tb_menu);
        mImageViewHamburgerMenu = findViewById(R.id.imgv_hamburger_menu);

        if (mImageViewHamburgerMenu != null) {
            mImageViewHamburgerMenu.setOnClickListener(this);
        }
        mTable1 = findViewById(R.id.imgv_table_1);
        mTable2 = findViewById(R.id.imgv_table_2);
        mTable3 = findViewById(R.id.imgv_table_3);
        mTable4 = findViewById(R.id.imgv_table_4);
        mTable5 = findViewById(R.id.imgv_table_5);
        mTable6 = findViewById(R.id.imgv_table_6);

    }

    private void showPopup(int tableNumber) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.manage_table_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        textViewTable = dialog.findViewById(R.id.tv_table);
        textViewCustomerNum = dialog.findViewById(R.id.tv_customer_num);
        textViewNum = dialog.findViewById(R.id.tv_num);
        imageViewTable = dialog.findViewById(R.id.imgv_table);
        imageViewTable.setImageResource(tableNumber);
        buttonOk = dialog.findViewById(R.id.btn_ok);
        buttonCancel = dialog.findViewById(R.id.btn_cancel);
        editTextCustomerNum = dialog.findViewById(R.id.et_customer_num);

        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    private void handelOnclick() {
        mTable1.setOnClickListener(this);
        mTable2.setOnClickListener(this);
        mTable3.setOnClickListener(this);
        mTable4.setOnClickListener(this);
        mTable5.setOnClickListener(this);
        mTable6.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("type", type);
        switch (view.getId()) {
            case R.id.imgv_hamburger_menu:
                createDrawer.navigationDrawerOpen();
                break;
            case R.id.imgv_table_1:
                showPopup(R.drawable.number_one);
                break;
            case R.id.imgv_table_2:
                showPopup(R.drawable.number_two);
                break;
            case R.id.imgv_table_3:
                showPopup(R.drawable.number_three);
                break;
            case R.id.imgv_table_4:
                showPopup(R.drawable.number_four);
                break;
            case R.id.imgv_table_5:
                showPopup(R.drawable.number_five);
                break;
            case R.id.imgv_table_6:
                showPopup(R.drawable.number_six);
                break;
            case R.id.btn_ok:
                startActivity(intent);
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }
}
