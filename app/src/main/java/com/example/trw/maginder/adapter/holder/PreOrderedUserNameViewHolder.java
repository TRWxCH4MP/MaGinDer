package com.example.trw.maginder.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.example.trw.maginder.R;

/**
 * Created by _TRW on 6/2/2561.
 */

public class PreOrderedUserNameViewHolder extends BaseViewHolder {

    private TextView tvUserName;

    public PreOrderedUserNameViewHolder(View itemView) {
        super(itemView);

        tvUserName = itemView.findViewById(R.id.tv_pre_ordered_user_name);
    }

    public void setUserName(String userName) {
        tvUserName.setText(userName);
    }

}
