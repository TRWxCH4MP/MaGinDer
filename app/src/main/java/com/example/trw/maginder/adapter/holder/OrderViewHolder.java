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
import com.example.trw.maginder.callback.OnCallbackOrderTotal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by _TRW on 19/12/2560.
 */

public class OrderViewHolder extends BaseViewHolder implements View.OnClickListener {

    private ImageView imageView;
    private TextView tvName;
    private TextView tvPrice;
    private Button btnAddOrder;
    private TextView tvID;
    private Button btnRemoveOrder;
    private Button btnAddMoreOrder;
    private Button btnAddOrderTotal;

    private List<String> listItem = new ArrayList<>();

    public OrderViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.order_img);
        tvName = itemView.findViewById(R.id.tv_order_name);
        tvPrice = itemView.findViewById(R.id.tv_order_price);
        btnAddOrder = itemView.findViewById(R.id.btn_add_order);
        tvID = itemView.findViewById(R.id.tv_order_id);
        btnRemoveOrder = itemView.findViewById(R.id.btn_remove_order);
        btnAddMoreOrder = itemView.findViewById(R.id.btn_add_more_order);
        btnAddOrderTotal = itemView.findViewById(R.id.btn_add_order_total);

        btnAddOrder.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_order:
                Toast.makeText(view.getContext(), tvName.getText(), Toast.LENGTH_SHORT).show();
//                listItem.add(String.valueOf(tvID.getText()));
//                Toast.makeText(view.getContext()
//                        , String.valueOf(listItem.size())
//                        , Toast.LENGTH_SHORT).show();
//                for (int index = 0; index < listItem.size(); index++) {
//                    Log.d("onRecyclerView", listItem.get(index));
//                }
                Intent intent = new Intent("MenuId");
                intent.putExtra("Id", tvID.getText());
                LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
                break;
        }
    }

}
