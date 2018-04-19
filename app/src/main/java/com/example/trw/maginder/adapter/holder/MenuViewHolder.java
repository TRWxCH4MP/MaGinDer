package com.example.trw.maginder.adapter.holder;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.holder.BaseViewHolder;
import com.example.trw.maginder.callback.ItemClickListener;
import com.example.trw.maginder.callback.OnCallbackOrderTotal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by _TRW on 19/12/2560.
 */

public class MenuViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ItemClickListener itemClickListener;

    private ImageView imageView;
    private TextView tvID;
    private TextView tvName;
    private TextView tvPrice;
    private Button btnChooseMenu;

    public MenuViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.order_img);
        tvID = itemView.findViewById(R.id.tv_order_id);
        tvName = itemView.findViewById(R.id.tv_order_name);
        tvPrice = itemView.findViewById(R.id.tv_order_price);
        btnChooseMenu = itemView.findViewById(R.id.btn_choose_menu);

        btnChooseMenu.setOnClickListener(this);
    }

    public void setImage(String image) {
        Glide.with(imageView.getContext())
                .load(image)
                .into(imageView);
    }

    public void setName(String name) {
        tvName.setText(name);
    }

    public void setPrice(String price) {
        tvPrice.setText(price);
    }

    public void setID(String id) {
        tvID.setText(id);
    }

    public void setOnClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_choose_menu) {
            itemClickListener.onClickAddMenu(view, view.getId(), String.valueOf(tvID.getText()));
        }
    }



    @Override
    public boolean onLongClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), getAdapterPosition(), true, null);
        return true;
    }


}
