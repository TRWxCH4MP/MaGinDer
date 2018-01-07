package com.example.trw.maginder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.holder.BaseViewHolder;
import com.example.trw.maginder.adapter.holder.MenuTitleViewHolder;
import com.example.trw.maginder.adapter.holder.OrderViewHolder;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.MenuTitleItem;
import com.example.trw.maginder.adapter.item.OrderItem;

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
        } else if (viewType == ViewType.TYPE_MENU_ITEM) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_menu_title, parent, false);
            return new MenuTitleViewHolder(view);
        }

        throw new RuntimeException("type is not match");
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        BaseItem i = itemList.get(position);
        if (holder instanceof OrderViewHolder) {
            OrderItem orderItem = (OrderItem) i;
            ((OrderViewHolder) holder).setImage(orderItem.getOrderImage());
            ((OrderViewHolder) holder).setName(orderItem.getOrderName());
            ((OrderViewHolder) holder).setPrice(orderItem.getOrderPrice());
        } else if (holder instanceof MenuTitleViewHolder) {
            MenuTitleItem titleItem = (MenuTitleItem) i;
            ((MenuTitleViewHolder) holder).setMenuTitle(titleItem.getMenuTitle());
        }
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
