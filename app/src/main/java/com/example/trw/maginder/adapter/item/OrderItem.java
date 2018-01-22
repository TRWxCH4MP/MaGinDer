package com.example.trw.maginder.adapter.item;

import com.example.trw.maginder.adapter.ViewType;

/**
 * Created by _TRW on 19/12/2560.
 */

public class OrderItem extends BaseItem {

    private String orderImage;
    private String orderName;
    private String orderPrice;
    private String orderId;

    public OrderItem() {
        super(ViewType.TYPE_ORDER_VIEW);
    }

    public String getOrderImage() {
        return orderImage;
    }

    public OrderItem setOrderImage(String orderImage) {
        this.orderImage = orderImage;
        return this;
    }

    public String getOrderName() {
        return orderName;
    }

    public OrderItem setOrderName(String orderName) {
        this.orderName = orderName;
        return this;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public OrderItem setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderItem setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }
}
