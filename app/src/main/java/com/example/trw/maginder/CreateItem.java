package com.example.trw.maginder;

import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by _TRW on 25/12/2560.
 */

public class CreateItem {

    private static List<BaseItem> itemList = new ArrayList<>();

    public static List<BaseItem> createItem(int menuType) {
        if (menuType == 0) {
            itemList.add(new OrderItem()
                    .setOrderImage(R.drawable.food)
                    .setOrderName("พิซซ่า")
                    .setOrderPrice("199 บาท"));
            itemList.add(new OrderItem()
                    .setOrderImage(R.drawable.food)
                    .setOrderName("พิซซ่า")
                    .setOrderPrice("100 บาท"));
            itemList.add(new OrderItem()
                    .setOrderImage(R.drawable.food)
                    .setOrderName("พิซซ่า")
                    .setOrderPrice("120 บาท"));
        } else if (menuType == 1) {
            itemList.add(new OrderItem()
                    .setOrderImage(R.drawable.food)
                    .setOrderName("พิซซ่า")
                    .setOrderPrice("199 บาท"));
        } else if (menuType == 2) {
            itemList.add(new OrderItem()
                    .setOrderImage(R.drawable.food)
                    .setOrderName("พิซซ่า")
                    .setOrderPrice("100 บาท"));
        } else if (menuType == 3) {
            itemList.add(new OrderItem()
                    .setOrderImage(R.drawable.food)
                    .setOrderName("พิซซ่า")
                    .setOrderPrice("120 บาท"));
        } else {
            itemList.clear();
            return itemList;
        }
        return itemList;
    }
}
