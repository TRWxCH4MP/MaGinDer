package com.example.trw.maginder.adapter.item;

import com.example.trw.maginder.adapter.ViewType;

/**
 * Created by _TRW on 19/12/2560.
 */

public class MenuItem extends BaseItem {

    private String orderMenuId;
    private String orderKitchenId;
    private String orderName;
    private String orderPrice;
    private String orderDate;
    private String orderImg;

    public MenuItem() {
        super(ViewType.TYPE_ORDER_VIEW);
    }

    public String getMenuId() {
        return orderMenuId;
    }

    public MenuItem setOrderMenuId(String orderMenuId) {
        this.orderMenuId = orderMenuId;
        return this;
    }

    public String getOrderKitchenId() {
        return orderKitchenId;
    }

    public MenuItem setOrderKitchenId(String orderKitchenId) {
        this.orderKitchenId = orderKitchenId;
        return this;
    }

    public String getMenuName() {
        return orderName;
    }

    public MenuItem setOrderName(String orderName) {
        this.orderName = orderName;
        return this;
    }

    public String getMenuPrice() {
        return orderPrice;
    }

    public MenuItem setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
        return this;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public MenuItem setOrderDate(String orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public String getMenuImg() {
        return orderImg;
    }

    public MenuItem setOrderImg(String orderImg) {
        this.orderImg = orderImg;
        return this;
    }
}
