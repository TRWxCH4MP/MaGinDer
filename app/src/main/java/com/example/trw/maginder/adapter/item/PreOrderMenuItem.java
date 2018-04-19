package com.example.trw.maginder.adapter.item;

import com.example.trw.maginder.adapter.ViewType;

/**
 * Created by _TRW on 22/1/2561.
 */

public class PreOrderMenuItem extends BaseItem {

    private String preOrderMenuImage;
    private String preOrderMenuName;
    private String preOrderMenuPrice;
    private String preOrderMenuId;
    private int preOrderMenuAmount;
    private int preOrderMenuPrimaryId;


    public PreOrderMenuItem() {
        super(ViewType.TYPE_PRE_ORDER_MENU);
    }

    public String getPreOrderMenuImage() {
        return preOrderMenuImage;
    }

    public PreOrderMenuItem setPreOrderMenuImage(String preOrderMenuImage) {
        this.preOrderMenuImage = preOrderMenuImage;
        return this;
    }

    public String getPreOrderMenuName() {
        return preOrderMenuName;
    }

    public PreOrderMenuItem setPreOrderMenuName(String preOrderMenuName) {
        this.preOrderMenuName = preOrderMenuName;
        return this;
    }

    public String getPreOrderMenuPrice() {
        return preOrderMenuPrice;
    }

    public PreOrderMenuItem setPreOrderMenuPrice(String preOrderMenuPrice) {
        this.preOrderMenuPrice = preOrderMenuPrice;
        return this;
    }

    public int getPreOrderMenuAmount() {
        return preOrderMenuAmount;
    }

    public PreOrderMenuItem setPreOrderMenuAmount(int preOrderMenuAmount) {
        this.preOrderMenuAmount = preOrderMenuAmount;
        return this;
    }

    public String getPreOrderMenuId() {
        return preOrderMenuId;
    }

    public PreOrderMenuItem setPreOrderMenuId(String preOrderMenuId) {
        this.preOrderMenuId = preOrderMenuId;
        return this;
    }

    public int getPreOrderMenuPrimaryId() {
        return preOrderMenuPrimaryId;
    }

    public PreOrderMenuItem setPreOrderMenuPrimaryId(int preOrderMenuPrimaryId) {
        this.preOrderMenuPrimaryId = preOrderMenuPrimaryId;
        return this;
    }
}
