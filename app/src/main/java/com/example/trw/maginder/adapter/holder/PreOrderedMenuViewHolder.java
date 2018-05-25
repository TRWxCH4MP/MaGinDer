package com.example.trw.maginder.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trw.maginder.R;
import com.example.trw.maginder.utility.StaticStringHelper;

/**
 * Created by _TRW on 6/2/2561.
 */

public class PreOrderedMenuViewHolder extends BaseViewHolder{

    private TextView tvMenuId;
    private TextView tvMenuName;
    private TextView tvMenuPrice;
    private TextView tvMenuState;
    private ImageView imgvMenu;

    public PreOrderedMenuViewHolder(View itemView) {
        super(itemView);

        tvMenuId = itemView.findViewById(R.id.tv_pre_ordered_menu_id);
        tvMenuName = itemView.findViewById(R.id.tv_pre_ordered_menu_name);
        tvMenuPrice = itemView.findViewById(R.id.tv_pre_ordered_menu_price);
        tvMenuState = itemView.findViewById(R.id.tv_pre_ordered_menu_state);
        imgvMenu = itemView.findViewById(R.id.imgv_pre_ordered_menu);
    }

    public void setMenuId(String menuId) {
        tvMenuId.setText(menuId);
    }

    public void setMenuName(String menuName) {
        tvMenuName.setText(menuName);
    }

    public void setMenuPrice(String menuPrice) {
        tvMenuPrice.setText(menuPrice);
    }

    public void setTvMenuState(String state) {
        if (state.equals(StaticStringHelper.STATUS_IN_PROCEED)) {
            tvMenuState.setBackgroundResource(R.drawable.bg_btn_radius_soft_orange);
        } else if (state.equals(StaticStringHelper.STATUS_IN_SUCCESSFUL)) {
            tvMenuState.setBackgroundResource(R.drawable.bg_btn_radius_green);
        }

        tvMenuState.setText(state);
    }

    public void setImageMenu(String image) {
        Glide.with(imgvMenu.getContext())
                .load(image)
                .into(imgvMenu);
    }
}
