package com.example.trw.maginder.adapter.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.holder.BaseViewHolder;

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

    private List<String> listItem = new ArrayList<>();

    public OrderViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.order_img);
        tvName = itemView.findViewById(R.id.tv_order_name);
        tvPrice = itemView.findViewById(R.id.tv_order_price);
        btnAddOrder = itemView.findViewById(R.id.btn_add_order);

        btnAddOrder.setOnClickListener(this);
    }

    public void setImage(int image) {
        imageView.setImageResource(image);
    }

    public void setName(String name) {
        tvName.setText(name);
    }

    public void setPrice(String price) {
        tvPrice.setText(price);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_order:
                listItem.add(String.valueOf(tvName.getText()));
                Toast.makeText(view.getContext()
                        , String.valueOf(listItem.size())
                        , Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
