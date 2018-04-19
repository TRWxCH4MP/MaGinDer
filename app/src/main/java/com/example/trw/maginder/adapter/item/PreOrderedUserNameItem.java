package com.example.trw.maginder.adapter.item;

import com.example.trw.maginder.adapter.ViewType;

/**
 * Created by _TRW on 6/2/2561.
 */

public class PreOrderedUserNameItem extends BaseItem {

    private String userName;

    public PreOrderedUserNameItem() {
        super(ViewType.TYPE_PRE_ORDER_USER_NAME);
    }

    public String getUserName() {
        return userName;
    }

    public PreOrderedUserNameItem setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
