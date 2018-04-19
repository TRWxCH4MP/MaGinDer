package com.example.trw.maginder.adapter.holder;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trw.maginder.R;
import com.example.trw.maginder.callback.ItemClickListener;

/**
 * Created by _TRW on 22/1/2561.
 */

public class PreOrderMenuViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ItemClickListener itemClickListener;

    private ImageView imageViewPreOrderMenu;
    private TextView textViewPreOrderMenuName;
    private TextView textViewPreOrderMenuPrice;
    private Button buttonProOrderMenuAmount;
    private Button buttonRemovePreOrderMenu;
    private TextView textViewPreOrderMenuId;
    private TextView textViewPreOrderMenuPrimaryId;

    public PreOrderMenuViewHolder(View itemView) {
        super(itemView);

        imageViewPreOrderMenu = itemView.findViewById(R.id.imgv_pre_order_menu);
        textViewPreOrderMenuName = itemView.findViewById(R.id.tv_pre_order_menu_name);
        textViewPreOrderMenuPrice = itemView.findViewById(R.id.tv_pre_order_menu_price);
        buttonProOrderMenuAmount = itemView.findViewById(R.id.btn_pre_order_menu_amount);
        buttonRemovePreOrderMenu = itemView.findViewById(R.id.btn_remove_pre_order_menu);
        textViewPreOrderMenuId = itemView.findViewById(R.id.tv_order_id);
        textViewPreOrderMenuPrimaryId = itemView.findViewById(R.id.tv_order_primary_id);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        buttonRemovePreOrderMenu.setOnClickListener(this);
    }

    public void setImageViewPreOrderMenu(String image) {
        Glide.with(imageViewPreOrderMenu.getContext())
                .load(image)
                .into(imageViewPreOrderMenu);
    }

    public void setTextViewPreOrderMenuName(String name) {
        textViewPreOrderMenuName.setText(name);
    }

    public void setTextViewPreOrderMenuPrice(String price) {
        textViewPreOrderMenuPrice.setText(price);
    }

    public void setButtonProOrderMenuAmount(int amount) {
        buttonProOrderMenuAmount.setText(String.valueOf(amount));
    }

    public void setTextViewPreOrderMenuId(String orderMenuId) {
        textViewPreOrderMenuId.setText(orderMenuId);
    }

    public void setTextViewPreOrderMenuPrimaryId(int primaryId) {
        textViewPreOrderMenuPrimaryId.setText(String.valueOf(primaryId));
    }

    public void setOnClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        int preOrderAmount = Integer.parseInt(String.valueOf(buttonProOrderMenuAmount.getText()));
        itemClickListener.onClickPreOrderAddOrRemove(view
                , view.getId()
                , getAdapterPosition()
                , String.valueOf(textViewPreOrderMenuPrimaryId.getText())
                , preOrderAmount);
    }

    @Override
    public boolean onLongClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), getAdapterPosition(), true, null);
        return true;
    }
}
