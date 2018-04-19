package com.example.trw.maginder.adapter.holder;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.callback.ItemClickListener;
import com.example.trw.maginder.callback.OnCallbackTable;

/**
 * Created by _TRW on 16/1/2561.
 */

public class TableViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ItemClickListener itemClickListener;

    private TextView textViewTableName;
    private TextView textViewTableState;
    private TextView textViewTableId;
    private CardView cardView;
    private TextView textViewZoneId;

    private String tableState;

    public TableViewHolder(View itemView) {
        super(itemView);

        textViewTableName = itemView.findViewById(R.id.tv_table_name);
        textViewTableState = itemView.findViewById(R.id.tv_table_state);
        textViewTableId = itemView.findViewById(R.id.tv_table_id);
        cardView = itemView.findViewById(R.id.card_view);
        textViewZoneId = itemView.findViewById(R.id.tv_zone_id);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        cardView.setOnClickListener(this);
    }

    public void setTableName(String tableName) {
        textViewTableName.setText(tableName);
    }

    public void setTableState(String tableState) {
        this.tableState = tableState.trim();
        String state = tableState.trim();
        if (state.equals("true")) {
            state = "เปิดบริการ";
            textViewTableState.setTextColor(itemView.getResources().getColor( R.color.maginder_green));
        } else {
            state = "ปิดบริการ";
            textViewTableState.setTextColor(itemView.getResources().getColor( R.color.maginder_red));
        }
        textViewTableState.setText(state);
    }

    public void setTableId(String tableId) {
        textViewTableId.setText(tableId);
    }

    public void setZoneId(String zoneId) {
        textViewZoneId.setText(zoneId);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.card_view) {
            itemClickListener.onClickChooseTable(view, view.getId()
                    , String.valueOf(textViewTableId.getText())
                    , String.valueOf(textViewZoneId.getText())
                    , String.valueOf(textViewTableName.getText())
                    , tableState);
        }
    }

    public void setOnClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public boolean onLongClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), getAdapterPosition(), true, null);
        return true;
    }
}
