package com.example.trw.maginder.callback;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by _TRW on 26/1/2561.
 */

public interface ItemClickListener {

    void onClick(View view, int viewId, int adapterPosition, boolean isLongClick, MotionEvent motionEvent);

    void onClickAddMenu(View view , int viewId, String menuId);

    void onClickPreOrderAddOrRemove(View view, int viewId, int position, String menuId, int preOrderAmount);

    void onClickChooseTable(View view, int viewId, String tableId, String zoneId, String tableName, String state);

}
