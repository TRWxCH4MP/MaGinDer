package com.example.trw.maginder.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.activity.MenuActivity;
import com.example.trw.maginder.adapter.MainAdapter;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.callback.TableCallback;
import com.example.trw.maginder.create_item.CreateTableItem;
import com.example.trw.maginder.manager.AuthManager;
import com.example.trw.maginder.manager.TableManager;
import com.example.trw.maginder.utility.StaticStringHelper;
import com.example.trw.maginder.service.dao.TableItemCollectionDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private static final String TAG = "ManageTableActivity";

    private String restaurantId;
    private String tableName;
    private List<String> listTabLayoutZone = new ArrayList<>();
    private List<String> listZoneId = new ArrayList<>();

    private Dialog dialog;
    private TextView textViewOpenTable;
    private TextView textViewTable;
    private TextView textViewCustomerNum;
    private TextView textViewNum;
    private TextView textViewTableName;
    private Button buttonOk;
    private Button buttonCancel;
    private EditText editTextCustomerNum;
    private RecyclerView recyclerViewTable;
    private MainAdapter adapter;
    private TableItemCollectionDao listTableCollection;
    private TabLayout tabLayout;

    private ProgressDialog progressDialog;

    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getRestaurantId();

        if (restaurantId != null) {
            createTableZone(restaurantId);
        }
    }

    private void getRestaurantId() {
        restaurantId = AuthManager.getInstance().getCurrentRestaurantId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table, container, false);

        initializeUI(view);
        setupView();
        initializePopupDialog();

        return view;
    }

    private void initializeUI(View view) {
        recyclerViewTable = view.findViewById(R.id.recycler_view_table);
        tabLayout = view.findViewById(R.id.tab_title);
    }

    private void setupView() {
        TableCallback tableCallback = new TableCallback() {
            @Override
            public void onCallbackTableState(String zoneId, String tableId, String tableName, String tableState) {
                setTableData(zoneId, tableId, tableName, tableState);
                showPopup();
            }
        };
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading_table));
        progressDialog.show();

        recyclerViewTable.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new MainAdapter(tableCallback);
        recyclerViewTable.setAdapter(adapter);
    }

    private void showPopup() {
        showDialog();
        checkHasChildTable();
        if (getTableState()) {
            tableIsOpen();
        } else {
            tableIsClosed();
        }
    }

    private void setTableData(String zoneId, String tableId, String tableName, String tableState) {
        this.tableName = tableName;
        setZoneId(zoneId);
        TableManager.getInstance().setTableId(tableId);
        TableManager.getInstance().setTableState(tableState);
        TableManager.getInstance().setTableName(tableName);
    }

    private boolean getTableState() {
        String tableState = TableManager.getInstance().getTableState();

        if (tableState.equals(StaticStringHelper.TRUE)) {
            return true;
        } else {
            return false;
        }

    }

    private void setZoneId(String zoneId) {
        TableManager.getInstance().setZoneId(zoneId);
    }

    private void tableIsOpen() {
        textViewTableName.setText(tableName);
        textViewCustomerNum.setVisibility(View.VISIBLE);
        textViewNum.setVisibility(View.VISIBLE);
        editTextCustomerNum.setVisibility(View.VISIBLE);
        textViewOpenTable.setVisibility(View.INVISIBLE);
    }

    private void tableIsClosed() {
        textViewTableName.setText(tableName);
        textViewCustomerNum.setVisibility(View.INVISIBLE);
        textViewNum.setVisibility(View.INVISIBLE);
        editTextCustomerNum.setVisibility(View.INVISIBLE);
        textViewOpenTable.setVisibility(View.VISIBLE);
    }

    private void initializePopupDialog() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.manage_table_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        textViewOpenTable = dialog.findViewById(R.id.tv_open_table);
        textViewTable = dialog.findViewById(R.id.tv_table);
        textViewCustomerNum = dialog.findViewById(R.id.tv_customer_num);
        textViewNum = dialog.findViewById(R.id.tv_num);
        textViewTableName = dialog.findViewById(R.id.tv_table_name);
        buttonOk = dialog.findViewById(R.id.btn_ok);
        buttonCancel = dialog.findViewById(R.id.btn_cancel);
        editTextCustomerNum = dialog.findViewById(R.id.et_customer_num);

        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    private void createTableZone(String restaurantId) {
        TableManager.getInstance().onCreateTableZone(restaurantId, new TableManager.TableManagerCallback() {
            @Override
            public void onCreateSuccess(TableItemCollectionDao dao) {
                setupTable(dao);
            }

            @Override
            public void onCreateFailure(String exception) {
                showMessageFailure(exception);
            }
        });
    }

    private void setupTable(TableItemCollectionDao dao) {
        listTableCollection = dao;
        if (!listTableCollection.getData().isEmpty()) {
            setupTabLayout();
        }
    }

    private void setupTabLayout() {
        for (int index = 0; index < listTableCollection.getData().size(); index++) {
            listTabLayoutZone.add(listTableCollection.getData().get(index).getZoneName());
            listZoneId.add(listTableCollection.getData().get(index).getZoneId());
        }

        if (!listTabLayoutZone.isEmpty()) {
            createTabLayout(listTabLayoutZone);
            setZoneId(listTableCollection.getData().get(0).getZoneId());
            onSetupTable();
        }
    }

    private void onSetupTable() {
        TableManager.getInstance().onSetupTable(new TableManager.SetupTableCallback() {
            @Override
            public void onTableAdded(List<CreateTableItem> listTable) {
                createTable(listTable);
            }

            @Override
            public void onTableChanged(List<CreateTableItem> listTable) {
                createTable(listTable);
            }

            @Override
            public void onSuccess(boolean isSuccess) {
                if (isSuccess) {
                    hideLoading();
                } else {
                    hideLoading();
                }
            }

            @Override
            public void onZoneIdIsNotMatch(String error) {
                hideLoading();
//                Log.d(TAG, "onZoneIdIsNotMatch: " + error);
            }
        });
    }

    private void createTable(List<CreateTableItem> listTable) {
        List<BaseItem> itemList = TableManager.getInstance().onCreateTable(listTable);

        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();
    }

    private void createTabLayout(List<String> listTabLayout) {
        for (int index = 0; index < listTabLayout.size(); index++) {
            tabLayout.addTab(tabLayout.newTab().setText(listTabLayout.get(index)));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == tabLayout.getSelectedTabPosition()) {
//            Toast.makeText(this, listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId(), Toast.LENGTH_SHORT).show();
            setZoneId(listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId());
            onSetupTable();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == tabLayout.getSelectedTabPosition()) {
//            Toast.makeText(this, listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId(), Toast.LENGTH_SHORT).show();
            setZoneId(listTableCollection.getData().get(tabLayout.getSelectedTabPosition()).getZoneId());
            onSetupTable();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                handleButtonOK();
                hideDialog();
                break;
            case R.id.btn_cancel:
                hideDialog();
                break;
        }
    }

    private void handleButtonOK() {
        if (getTableState()) {
            goToMenuActivity();
        } else {
            updateTableState();
            hideDialog();
        }
    }

    private void checkHasChildTable() {
        TableManager.getInstance().onCheckHasChildTable(new TableManager.CheckHasChildTableCallback() {
            @Override
            public void onHasChildTable(boolean isSuccess) {
             if (isSuccess) {
                 checkHasChildTableTransaction();
             } else {
                 setupTableTransaction(null);
             }
            }
        });
    }

    private void checkHasChildTableTransaction() {
        TableManager.getInstance().onCheckHasChildTableTransaction(new TableManager.CheckHasChildTableTransactionCallback() {
            @Override
            public void onHasChildTableTransaction(String childTableTransaction) {
                setupTableTransaction(childTableTransaction);
            }
        });
    }

    private void setupTableTransaction(String tableTransaction) {
        if (tableTransaction != null) {
            TableManager.getInstance().setTransaction(tableTransaction);
//            Log.d(TAG, "xxxx not null : " + tableTransaction);
        } else {
            TableManager.getInstance().setTransaction(setupTransaction());
//            Log.d(TAG, "xxxx is null : new " + setupTransaction());
        }
    }

    private String setupTransaction() {
        int time = (int) (new Date().getTime() / 1000);
        return "TR-" + time;
    }

    private void goToMenuActivity() {
        Intent intent = new Intent(getContext(), MenuActivity.class);
        startActivity(intent);
    }

    private void updateTableState() {
        TableManager.getInstance().updateTableState(new TableManager.UpdateTableStateCallback() {
            @Override
            public void onUpdateSuccess(boolean isSuccess) {
                if (isSuccess) {
                    showToast(TableManager.getInstance().getTableName() + " " + getString(R.string.ready_service));
                }
            }
        });
    }

    private void showDialog() {
        dialog.show();
    }

    private void showMessageFailure(String exception) {
        showToast(exception);
    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void hideLoading() {
        progressDialog.dismiss();
    }

    private void hideDialog() {
        dialog.cancel();
    }

}
