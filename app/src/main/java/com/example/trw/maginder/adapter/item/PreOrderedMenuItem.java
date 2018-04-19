package com.example.trw.maginder.adapter.item;

import com.example.trw.maginder.adapter.ViewType;

/**
 * Created by _TRW on 6/2/2561.
 */

public class PreOrderedMenuItem extends BaseItem {

    private String preOrderedMenuImage;
    private String preOrderedMenuName;
    private String preOrderedMenuPrice;
    private String preOrderedMenuId;
    private String preOrderedMenuState;

    public PreOrderedMenuItem() {
        super(ViewType.TYPE_PRE_ORDERED_MENU);
    }

    public String getPreOrderedMenuImage() {
        return preOrderedMenuImage;
    }

    public PreOrderedMenuItem setPreOrderedMenuImage(String preOrderedMenuImage) {
        this.preOrderedMenuImage = preOrderedMenuImage;
        return this;
    }

    public String getPreOrderedMenuName() {
        return preOrderedMenuName;
    }

    public PreOrderedMenuItem setPreOrderedMenuName(String preOrderedMenuName) {
        this.preOrderedMenuName = preOrderedMenuName;
        return this;
    }

    public String getPreOrderedMenuPrice() {
        return preOrderedMenuPrice;
    }

    public PreOrderedMenuItem setPreOrderedMenuPrice(String preOrderedMenuPrice) {
        this.preOrderedMenuPrice = preOrderedMenuPrice;
        return this;
    }

    public String getPreOrderedMenuId() {
        return preOrderedMenuId;
    }

    public PreOrderedMenuItem setPreOrderedMenuId(String preOrderedMenuId) {
        this.preOrderedMenuId = preOrderedMenuId;
        return this;
    }

    public String getPreOrderedMenuState() {
        return preOrderedMenuState;
    }

    public PreOrderedMenuItem setPreOrderedMenuState(String preOrderedMenuState) {
        this.preOrderedMenuState = preOrderedMenuState;
        return this;
    }
}
