package com.example.trw.maginder.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.trw.maginder.callback.MenuListenerCallback;
import com.example.trw.maginder.manager.AuthManager;
import com.example.trw.maginder.manager.MenuManager;
import com.example.trw.maginder.manager.TableManager;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.callback.FragmentCallback;
import com.example.trw.maginder.callback.PrimaryKeyMenuCallback;
import com.example.trw.maginder.db.DeleteData;
import com.example.trw.maginder.db.QueryData;
import com.example.trw.maginder.db.callback.OnStateCallback;
import com.example.trw.maginder.db.callback.SendListDataCallback;
import com.example.trw.maginder.db.entity.MenuEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreOrderFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PreOrderFragment";

    private String employeeName;
    private String restaurantId;
    private String tableId;
    private String transaction;
    private String tableName;

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

    private MenuListenerCallback menuListenerCallback;
    private FragmentCallback callback;

    public PreOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            menuListenerCallback = (MenuListenerCallback) context;
            callback = (FragmentCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MenuListenerCallback or FragmentCallback or PrimaryKeyMenuCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pre_order, container, false);

        initializeUI(view);
        setupView();
        getCurrentData();
        createPreOrderMenu();

        return view;
    }

    private void getCurrentData() {
        restaurantId = AuthManager.getInstance().getCurrentRestaurantId();
        employeeName = AuthManager.getInstance().getCurrentUserName();
        tableId = TableManager.getInstance().getTableId();
        tableName = TableManager.getInstance().getTableName();
        transaction = TableManager.getInstance().getTransaction();
    }

    private void createPreOrderMenu() {
        QueryData.onLoadMenu(getContext(), new SendListDataCallback() {
            @Override
            public void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
                if (isSuccess) {
                    buttonVerifyOrderMenuIsUseAble();
                    listPreOrderMenu = menuEntities;
                    createItemPreOrder(listPreOrderMenu);
                    setupPreOrderMenuAmount(listPreOrderMenu.size());
                    setupPreOrderMenuTotalPrice(listPreOrderMenu);
                } else {
                    buttonVerifyOrderMenuIsDisAble();
                    setupPreOrderMenuAmount(0);
                    setupPreOrderMenuTotalPrice(listPreOrderMenu);
                }
            }
        });
    }

    private void buttonVerifyOrderMenuIsUseAble() {
        btnVerifyOrderMenu.setEnabled(true);
    }

    private void buttonVerifyOrderMenuIsDisAble() {
        btnVerifyOrderMenu.setEnabled(false);
    }

    private void setupPreOrderMenuTotalPrice(List<MenuEntity> listPreOrderMenu) {
        int totalPrice = MenuManager.getInstance().getPreOrderMenuTotalPrice(listPreOrderMenu);

        textViewMenuTotalPrice.setText(String.valueOf(totalPrice + " " + getString(R.string.baht)));
    }

    private void createItemPreOrder(List<MenuEntity> listPreOrderMenu) {
        List<BaseItem> itemList = MenuManager.getInstance().onCreatePreOrderMenu(listPreOrderMenu);

        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();

    }

    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_list_pre_order_menu);
        textViewMenuTotal = view.findViewById(R.id.tv_pre_order_menu_total);
        textViewMenuTotalPrice = view.findViewById(R.id.tv_pre_order_menu_price_total);
        btnAddOrder = view.findViewById(R.id.btn_add_order);
        btnVerifyOrderMenu = view.findViewById(R.id.btn_verify_order_menu);
    }

    private void setupView() {
        PrimaryKeyMenuCallback callbackPrimaryKey = new PrimaryKeyMenuCallback() {
            @Override
            public void onCallbackPrimaryKeyMenu(String primaryKey) {
                onDeletePreOrderMenu(primaryKey);
            }
        };

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.pre_order_process));
        progressDialog.setCancelable(false);

        btnVerifyOrderMenu.setOnClickListener(this);
        btnAddOrder.setOnClickListener(this);

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
        textViewMenuTotal.setText(MenuManager.getInstance().getPreOrderMenuAmount(preOrderMenuTotal));
    }

    private void updatePreOrderMenu() {
        QueryData.onLoadMenu(getContext(), new SendListDataCallback() {
            @Override
            public void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
                if (isSuccess) {
                    setupPreOrderMenuAmount(menuEntities.size());
                    setupPreOrderMenuTotalPrice(menuEntities);
                    menuListenerCallback.menuListenerCallback(true);
                } else {
                    setupPreOrderMenuAmount(0);
                    setupPreOrderMenuTotalPrice(menuEntities);
                    menuListenerCallback.menuListenerCallback(false);
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
                hideDialog();
                removePreOrderMenu();
                break;
            case R.id.btn_cancel:
                hideDialog();
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
        QueryData.onLoadMenu(getContext(), new SendListDataCallback() {
            @Override
            public void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
                if (isSuccess) {
                    setOnFirebase(menuEntities);
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

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void setOnFirebase(List<MenuEntity> menuEntities) {
        showLoading();

        MenuManager.getInstance().onSetPreOrderMenuToFirebase(menuEntities
                , restaurantId
                , tableId
                , tableName
                , employeeName
                , transaction
                , new MenuManager.SetupPreOrderMenuToFirebaseCallback() {
                    @Override
                    public void onComplete(boolean isSuccess) {
                        hideLoading();
                        goToPreOrderedMenuScreen();
                        menuListenerCallback.menuListenerCallback(false);
                    }

                    @Override
                    public void onFailure() {

                    }
                });

    }

    private void showLoading() {
        progressDialog.show();
    }

    private void goToPreOrderedMenuScreen() {
        Fragment fragment = new PreOrderedMenuFragment();
        callback.onFragmentCallback(fragment);
    }

    private void hideLoading() {
        progressDialog.dismiss();
    }

    private void hideDialog() {
        dialog.cancel();
    }

}


