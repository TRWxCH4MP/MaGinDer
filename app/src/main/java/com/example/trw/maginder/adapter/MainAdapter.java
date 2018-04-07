package com.example.trw.maginder.adapter;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.holder.BaseViewHolder;
import com.example.trw.maginder.adapter.holder.MenuViewHolder;
import com.example.trw.maginder.adapter.holder.PreOrderMenuViewHolder;
import com.example.trw.maginder.adapter.holder.PreOrderedMenuViewHolder;
import com.example.trw.maginder.adapter.holder.PreOrderedUserNameViewHolder;
import com.example.trw.maginder.adapter.holder.TableViewHolder;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.MenuItem;
import com.example.trw.maginder.adapter.item.PreOrderMenuItem;
import com.example.trw.maginder.adapter.item.PreOrderedMenuItem;
import com.example.trw.maginder.adapter.item.PreOrderedUserNameItem;
import com.example.trw.maginder.adapter.item.TableItem;
import com.example.trw.maginder.callback.ItemClickListener;
import com.example.trw.maginder.callback.OnCallbackMenu;
import com.example.trw.maginder.callback.OnCallbackTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by _TRW on 19/12/2560.
 */

public class MainAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private OnCallbackTable callbackTable;
    private OnCallbackMenu callbackMenu;

    private static String TAG = "MainAdapter";

    private List<BaseItem> itemList = new ArrayList<>();

    public MainAdapter(OnCallbackTable callbackTable) {
        this.callbackTable = callbackTable;
    }

    public MainAdapter(OnCallbackMenu callbackMenu) {
        this.callbackMenu = callbackMenu;
    }

    public MainAdapter() {

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if (viewType == ViewType.TYPE_ORDER_VIEW) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_order, parent, false);
            return new MenuViewHolder(view);
        } else if (viewType == ViewType.TYPE_TABLE_VIEW) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_table, parent, false);
            return new TableViewHolder(view);
        } else if (viewType == ViewType.TYPE_PRE_ORDER_MENU) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_pre_order_menu, parent, false);
            return new PreOrderMenuViewHolder(view);
        } else if (viewType == ViewType.TYPE_PRE_ORDER_USER_NAME) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_pre_ordered_user_name, parent, false);
            return new PreOrderedUserNameViewHolder(view);
        } else if (viewType == ViewType.TYPE_PRE_ORDERED_MENU) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_pre_ordered_menu, parent, false);
            return new PreOrderedMenuViewHolder(view);
        }

        throw new RuntimeException("type is not match");
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        BaseItem item = itemList.get(position);
        if (holder instanceof MenuViewHolder) {
            setupMenu((MenuViewHolder) holder, (MenuItem) item);
        } else if (holder instanceof TableViewHolder) {
            setupTable((TableViewHolder) holder, (TableItem) item);
        } else if (holder instanceof PreOrderMenuViewHolder) {
            setupPreOrderMenu((PreOrderMenuViewHolder) holder, (PreOrderMenuItem) item);
        } else if (holder instanceof PreOrderedUserNameViewHolder) {
            setupPreOrderedUserName((PreOrderedUserNameViewHolder) holder, (PreOrderedUserNameItem) item);
        } else if (holder instanceof PreOrderedMenuViewHolder) {
            setupPreOrderedMenu((PreOrderedMenuViewHolder) holder, (PreOrderedMenuItem) item);
        }
    }

    private void setupPreOrderedMenu(final PreOrderedMenuViewHolder holder, PreOrderedMenuItem item) {
        holder.setImageMenu(item.getPreOrderedMenuImage());
        holder.setMenuId(item.getPreOrderedMenuId());
        holder.setMenuName(item.getPreOrderedMenuName());
        holder.setMenuPrice(item.getPreOrderedMenuPrice());
        holder.setTvMenuState(item.getPreOrderedMenuState());
    }

    private void setupPreOrderedUserName(final PreOrderedUserNameViewHolder holder, PreOrderedUserNameItem item) {
        holder.setUserName(item.getUserName());
    }

    private void setupPreOrderMenu(final PreOrderMenuViewHolder holder, PreOrderMenuItem item) {
        holder.setImageViewPreOrderMenu(item.getPreOrderMenuImage());
        holder.setTextViewPreOrderMenuName(item.getPreOrderMenuName());
        holder.setTextViewPreOrderMenuPrice(item.getPreOrderMenuPrice());
        holder.setButtonProOrderMenuAmount(item.getPreOrderMenuAmount());
        holder.setTextViewPreOrderMenuId(item.getPreOrderMenuId());
        holder.setTextViewPreOrderMenuPrimaryId(item.getPreOrderMenuPrimaryId());

        holder.setOnClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int viewId, int adapterPosition, boolean isLongClick, MotionEvent motionEvent) {

            }

            @Override
            public void onClickAddMenu(View view, int viewId, String menuId) {

            }

            @Override
            public void onClickPreOrderAddOrRemove(View view, int viewId, int position, String primaryId, int preOrderAmount) {
                setupPreOrderMenuOnClick(view, viewId, holder, position, primaryId, preOrderAmount);
            }

            @Override
            public void onClickChooseTable(View view, int viewId, String tableId, String zoneId, String tableName, String state) {

            }

        });
    }

    private void setupPreOrderMenuOnClick(View view, int viewId, PreOrderMenuViewHolder holder, int position, String primaryId, int preOrderAmount) {
//        int listPreOrderAmount;
//        Intent intent = new Intent("PreOrderMenuId");
//        intent.putExtra("menuId", menuId);
//        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
        Log.d(TAG, "setupPreOrderMenuOnClick: " + primaryId);
        if (viewId == R.id.btn_remove_pre_order_menu) {
            removeAt(position);
            Intent intentRemove = new Intent("RemovePreOrderMenuId");
            intentRemove.putExtra("primaryId", primaryId);
            LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intentRemove);
        }
    }

    public void removeAt(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }

    private void setupTable(TableViewHolder holder, TableItem item) {
        holder.setTableName(item.getTableName());
        holder.setTableState(item.getTableState());
        holder.setTableId(item.getTableId());
        holder.setZoneId(item.getZoneId());

        holder.setOnClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int viewId, int adapterPosition, boolean isLongClick, MotionEvent motionEvent) {

            }

            @Override
            public void onClickAddMenu(View view, int viewId, String menuId) {

            }

            @Override
            public void onClickPreOrderAddOrRemove(View view, int viewId, int position, String menuId, int preOrderAmount) {

            }

            @Override
            public void onClickChooseTable(View view, int viewId, String tableId, String zoneId, String tableName, String tableState) {
                if (viewId == R.id.card_view) {
                    callbackTable.onCallbackTableState(zoneId, tableId, tableName, tableState);
//                    Intent intent = new Intent("ChooseTable");
//                    intent.putExtra("TableId", tableId);
//                    intent.putExtra("ZoneId", zoneId);
//                    intent.putExtra("TableName", tableName);
//                    intent.putExtra("TableState", tableState);
//                    LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
                }
            }

        });
    }

    private void setupMenu(final MenuViewHolder holder, MenuItem item) {
        holder.setImage(item.getMenuImg());
        holder.setName(item.getMenuName());
        holder.setPrice(item.getMenuPrice());
        holder.setID(item.getMenuId());

        holder.setOnClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int viewId, int adapterPosition, boolean isLongClick, MotionEvent motionEvent) {

            }

            @Override
            public void onClickAddMenu(View view, int viewId, String menuId) {
                if (viewId == R.id.btn_choose_menu) {
                    callbackMenu.onCallbackMenu(menuId);
                }
            }

            @Override
            public void onClickPreOrderAddOrRemove(View view, int viewId, int position, String menuId, int preOrderAmount) {

            }

            @Override
            public void onClickChooseTable(View view, int viewId, String tableId, String zoneId, String tableName, String state) {

            }

        });
    }

    @Override
    public int getItemCount() {
        if (!itemList.isEmpty() || itemList != null) {
            return itemList.size();
        }
        return 0;
    }

    public int getItemViewType(int position) {
        return itemList.get(position).getType();
    }

    public void setItemList(List<BaseItem> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }
}
