package com.example.trw.maginder.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.model.StaticStringHelper;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.PreOrderMenuItem;
import com.example.trw.maginder.callback.ChooseMenuCallback;
import com.example.trw.maginder.callback.FragmentCallback;
import com.example.trw.maginder.callback.PrimaryKeyMenuCallback;
import com.example.trw.maginder.db.DeleteData;
import com.example.trw.maginder.db.QueryData;
import com.example.trw.maginder.db.callback.OnStateCallback;
import com.example.trw.maginder.db.callback.SendListDataCallback;
import com.example.trw.maginder.db.entity.MenuEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreOrderFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PreOrderFragment";
    private static final String PREF_NAME = "PREF_NAME";
    private static final String ID_RESTAURANT = "id_restaurant";
    private static final String IMAGE_URL = "http://it2.sut.ac.th/prj60_g14/Project/menu_img/";
    private static final String TRANSACTION = "Transaction";
    private static final String STATUS_IN_PROCEED = "กำลังดำเนินการ";

    private String employeeName;
    private String restaurantId;
    private String tableId;
    private String transaction;
    private String tableName;

    private ArrayList<String> transactionMenu;
    private List<MenuEntity> listPreOrderMenu = new ArrayList<>();

    private RecyclerView recyclerView;
    private TextView textViewMenuTotal;
    private TextView textViewMenuTotalPrice;
    private Button btnAddOrder;
    private Button btnVerifyOrderMenu;
    private MainAdapter adapter;
    private Dialog dialog;
    private Button btnOk;
    private Button btnCancel;
    private ProgressDialog progressDialog;

    private ChooseMenuCallback chooseMenuCallback;
    private FragmentCallback callback;

    public PreOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            chooseMenuCallback = (ChooseMenuCallback) context;
            callback = (FragmentCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ChooseMenuCallback or FragmentCallback or PrimaryKeyMenuCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        restaurantId = sharedPreferences.getString(StaticStringHelper.RESTAURANT_ID, null);
        employeeName = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_NAME, null);

        if (getArguments() != null) {
//            orderMenu = new ArrayList<>(getArguments().getStringArrayList(LIST_MENU));
            restaurantId = getArguments().getString(ID_RESTAURANT);
            tableId = getArguments().getString("TableId");
            tableName = getArguments().getString("TableName");
            transaction = getArguments().getString("Transaction");
            transactionMenu = getArguments().getStringArrayList("TransactionMenu");

            if (!transactionMenu.isEmpty()) {
                for (int index = 0; index < transactionMenu.size(); index++) {
                    Log.d(TAG, "onCreate: " + transactionMenu.get(index));
                }
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pre_order, container, false);

        initializeUI(view);
        setupView();
        createPreOrderMenu();

        return view;
    }

    private void createPreOrderMenu() {
        QueryData.onLoadMenu(getContext(), new SendListDataCallback() {
            @Override
            public void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
                if (isSuccess) {
                    listPreOrderMenu = menuEntities;
                    createItemPreOrder(listPreOrderMenu);
                    setupPreOrderMenuAmount(listPreOrderMenu.size());
                    setupPreOrderMenuTotalPrice(listPreOrderMenu);
                } else {

                }
            }
        });
    }

    private void setupPreOrderMenuTotalPrice(List<MenuEntity> listPreOrderMenu) {
        int totalPrice = 0;

        for (int index = 0; index < listPreOrderMenu.size(); index++) {
            totalPrice = totalPrice + Integer.parseInt(listPreOrderMenu.get(index).getPrice());
        }

        textViewMenuTotalPrice.setText(String.valueOf(totalPrice + " บาท"));
    }

    private void createItemPreOrder(List<MenuEntity> listPreOrderMenu) {
        List<BaseItem> itemList = new ArrayList<>();


        for (int index = 0; index < listPreOrderMenu.size(); index++) {
            itemList.add(new PreOrderMenuItem()
                    .setPreOrderMenuId(listPreOrderMenu.get(index).getIdMenu())
                    .setPreOrderMenuImage(IMAGE_URL + listPreOrderMenu.get(index).getImg())
                    .setPreOrderMenuName(listPreOrderMenu.get(index).getName())
                    .setPreOrderMenuPrice(listPreOrderMenu.get(index).getPrice() + " บาท")
                    .setPreOrderMenuAmount(1)
                    .setPreOrderMenuPrimaryId(listPreOrderMenu.get(index).getPrimaryId())
            );
        }

        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();

    }

    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_list_pre_order_menu);
        textViewMenuTotal = view.findViewById(R.id.tv_pre_order_menu_total);
        textViewMenuTotalPrice = view.findViewById(R.id.tv_pre_order_menu_price_total);
        btnAddOrder = view.findViewById(R.id.btn_add_order);
        btnVerifyOrderMenu = view.findViewById(R.id.btn_verify_order_menu);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("กำลังทำการรายการ");
        progressDialog.setCancelable(false);

        btnVerifyOrderMenu.setOnClickListener(this);
        btnAddOrder.setOnClickListener(this);
    }

    private void setupView() {
        PrimaryKeyMenuCallback callbackPrimaryKey = new PrimaryKeyMenuCallback() {
            @Override
            public void onCallbackPrimaryKeyMenu(String primaryKey) {
                onDeletePreOrderMenu(primaryKey);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()
                , LinearLayoutManager.VERTICAL
                , false));
        adapter = new MainAdapter(callbackPrimaryKey);
        recyclerView.setAdapter(adapter);
    }

    private void onDeletePreOrderMenu(String primaryKeyMenu) {
        DeleteData.onDeleteMenuById(getContext(), primaryKeyMenu, new OnStateCallback() {
            @Override
            public void stateCallback(boolean isSuccess) {
                if (isSuccess) {
                    updatePreOrderMenu();
                }
            }
        });
    }

    private void setupPreOrderMenuAmount(int preOrderMenuTotal) {
        chooseMenuCallback.onMenuAmount(preOrderMenuTotal);
        if (preOrderMenuTotal < 10) {
            textViewMenuTotal.setText(String.valueOf("0" + preOrderMenuTotal));
        } else {
            textViewMenuTotal.setText(String.valueOf(preOrderMenuTotal));
        }
    }

    private void updatePreOrderMenu() {
        QueryData.onLoadMenu(getContext(), new SendListDataCallback() {
            @Override
            public void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
                if (isSuccess) {
                    setupPreOrderMenuAmount(menuEntities.size());
                    setupPreOrderMenuTotalPrice(menuEntities);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify_order_menu:
                showPopup();
                break;
            case R.id.btn_ok:
                sendPreOrderMenuToVerify();
                dialog.cancel();
                removePreOrderMenu();
                break;
            case R.id.btn_cancel:
                dialog.cancel();
                break;
            case R.id.btn_add_order:
                callback.onFragmentCallback(new AllMenuFragment());
                break;
        }
    }

    private void removePreOrderMenu() {
        DeleteData.onDeleteMenu(getContext(), new OnStateCallback() {
            @Override
            public void stateCallback(boolean isSuccess) {
                if (isSuccess) {
                    textViewMenuTotal.setText(String.valueOf(0));
                    textViewMenuTotalPrice.setText(String.valueOf(0 + " บาท"));
                }
            }
        });
    }

    private void sendPreOrderMenuToVerify() {
        dialog.cancel();
        QueryData.onLoadMenu(getContext(), new SendListDataCallback() {
            @Override
            public void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
                if (isSuccess) {
                    Log.d(TAG, "sendPreOrderMenuToVerify: " + transaction);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference()
                            .child(TRANSACTION)
                            .child(restaurantId)
                            .child(tableId)
                            .child(transaction);

                    for (int index = 0; index < menuEntities.size(); index++) {
                        int time = (int) (new Date().getTime() / 1000);
                        String timeStampOrderMenu = "MT-" + time;
                        databaseReference.child(timeStampOrderMenu);
                        databaseReference.child(timeStampOrderMenu).child("date").setValue(menuEntities.get(index).getDate());
                        databaseReference.child(timeStampOrderMenu).child("id").setValue(transaction);
                        databaseReference.child(timeStampOrderMenu).child("id_menu").setValue(menuEntities.get(index).getIdMenu());
                        databaseReference.child(timeStampOrderMenu).child("id_zone").setValue(menuEntities.get(index).getIdKitchen());
                        databaseReference.child(timeStampOrderMenu).child("img").setValue(menuEntities.get(index).getImg());
                        databaseReference.child(timeStampOrderMenu).child("name").setValue(menuEntities.get(index).getName());
                        databaseReference.child(timeStampOrderMenu).child("price").setValue(menuEntities.get(index).getPrice());
                        databaseReference.child(timeStampOrderMenu).child("status").setValue(STATUS_IN_PROCEED);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int timeStamp = (int) (new Date().getTime() / 1000);
                    databaseReference.child("name_user").setValue(employeeName);
                    databaseReference.child("status").setValue(STATUS_IN_PROCEED);
                    databaseReference.child("timestamp").setValue(String.valueOf(timeStamp));

                    Fragment fragment = new PreOrderedMenuFragment();
                    callback.onFragmentCallback(fragment);
                }
            }
        });

    }

    private void showPopup() {

        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_pre_order_verify);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btnOk = dialog.findViewById(R.id.btn_ok);
        btnCancel = dialog.findViewById(R.id.btn_cancel);
        int time = (int) (new Date().getTime() / 1000);

        String timeStampTransaction = "TR-" + time;

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }
}


