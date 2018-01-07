package com.example.trw.maginder.adapter.holder;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.callback.OnClickTitle;

/**
 * Created by _TRW on 22/12/2560.
 */

public class MenuTitleViewHolder extends BaseViewHolder implements View.OnClickListener {

    private TextView textViewMenuTitle;
    private View viewTitle;
    private OnClickTitle onClickTitle;

    public MenuTitleViewHolder(View itemView) {
        super(itemView);

        textViewMenuTitle = itemView.findViewById(R.id.tv_menu_title);
        viewTitle = itemView.findViewById(R.id.view_title);
        textViewMenuTitle.setOnClickListener(this);
    }

    public void setMenuTitle(String menuTitle) {
        textViewMenuTitle.setText(menuTitle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_menu_title:
                viewTitle.setVisibility(View.VISIBLE);
                Toast.makeText(view.getContext()
                        , String.valueOf(textViewMenuTitle.getText())
                        , Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
