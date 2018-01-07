package com.example.trw.maginder.adapter.item;

import com.example.trw.maginder.adapter.ViewType;

/**
 * Created by _TRW on 22/12/2560.
 */

public class MenuTitleItem extends BaseItem{

    private String menuTitle;

    public MenuTitleItem() {
        super(ViewType.TYPE_MENU_ITEM);
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public MenuTitleItem setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
        return this;
    }
}
