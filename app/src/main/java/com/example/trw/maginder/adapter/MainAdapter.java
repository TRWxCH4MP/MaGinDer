package com.example.trw.maginder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.holder.BaseViewHolder;
import com.example.trw.maginder.adapter.holder.OrderViewHolder;
import com.example.trw.maginder.adapter.holder.PreOrderMenuLastViewHolder;
import com.example.trw.maginder.adapter.holder.PreOrderMenuViewHolder;
import com.example.trw.maginder.adapter.holder.TableViewHolder;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.OrderItem;
import com.example.trw.maginder.adapter.item.PreOrderMenuLastItem;
import com.example.trw.maginder.adapter.item.PreOrderMenuItem;
import com.example.trw.maginder.adapter.item.TableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by _TRW on 19/12/2560.
 */

public class MainAdapter extends RecyclerView.Adapter<BaseViewHolder>{

    private List<BaseItem> itemList = new ArrayList<>();

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if (viewType == ViewType.TYPE_ORDER_VIEW) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_order, parent, false);
            return new OrderViewHolder(view);
        } else if (viewType == ViewType.TYPE_TABLE_VIEW) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_table, parent, false);
            return new TableViewHolder(view);
        } else if (viewType == ViewType.TYPE_PRE_ORDER_MENU) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_pre_order_menu, parent, false);
            return new PreOrderMenuViewHolder(view);
        } else if (viewType == ViewType.TYPE_PRE_ORDER_MENU_LAST) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_pre_order_menu_last, parent, false);
            return new PreOrderMenuLastViewHolder(view);
        }
        throw new RuntimeException("type is not match");
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        BaseItem item = itemList.get(position);
        if (holder instanceof OrderViewHolder) {
            setupOrder((OrderViewHolder) holder, (OrderItem) item);
        } else if (holder instanceof TableViewHolder) {
            setupTable((TableViewHolder) holder, (TableItem) item);
        } else if (holder instanceof PreOrderMenuViewHolder) {
            setupPreOrderMenu((PreOrderMenuViewHolder) holder, (PreOrderMenuItem) item);
        } else if (holder instanceof PreOrderMenuLastViewHolder) {
            setupPreOrderMenuLast((PreOrderMenuLastViewHolder) holder, (PreOrderMenuLastItem) item);
        }
    }

    private void setupPreOrderMenuLast(PreOrderMenuLastViewHolder holder, PreOrderMenuLastItem item) {
        holder.setTextViewPreOrderMenuPriceTotal(item.getPreOrderMenuTotal());
    }

    private void setupPreOrderMenu(PreOrderMenuViewHolder holder, PreOrderMenuItem item) {
        holder.setImageViewPreOrderMenu(item.getPreOrderMenuImage());
        holder.setTextViewPreOrderMenuName(item.getPreOrderMenuName());
        holder.setTextViewPreOrderMenuPrice(item.getPreOrderMenuPrice());
        holder.setButtonProOrderMenuAmount(item.getPreOrderMenuAmount());
    }

    private void setupTable(TableViewHolder holder, TableItem item) {
        holder.setTableName(item.getTableName());
        holder.setTableState(item.getTableState());
        holder.setTableId(item.getTableId());
        holder.setZoneId(item.getZoneId());
    }

    private void setupOrder(OrderViewHolder holder, OrderItem item) {
        holder.setImage(item.getOrderImage());
        holder.setName(item.getOrderName());
        holder.setPrice(item.getOrderPrice());
        holder.setID(item.getOrderId());
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
