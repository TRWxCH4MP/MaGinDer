package com.example.trw.maginder;

import android.app.Activity;
import android.content.Context;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

/**
 * Created by _TRW on 24/12/2560.
 */

public class CreateDrawerMenu {
    private Context context;
    private Drawer drawer;
    private String name;
    private String type;

    public CreateDrawerMenu(Context context) {
        this.context = context;
    }

    public void createNavigationDrawer(String name, String type) {
        this.name = name;
        this.type = type;

        new DrawerBuilder().withActivity((Activity) context).build();

        PrimaryDrawerItem item = new PrimaryDrawerItem().withIdentifier(1).withName("ตะกร้าเมนู");
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("โต๊ะ");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(1).withName("ของคาว");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(1).withName("ของหวาน");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(1).withName("เครื่องดื่ม");
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(1).withName("โปรโมชั่น");
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(1).withName("ออกจากระบบ");

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity((Activity) context)
                .withHeaderBackground(R.color.maginder_deep_grey)
                .addProfiles(
                        new ProfileDrawerItem().withName(type + " " + name).withIcon(R.mipmap.ic_launcher_round)
                )
                .build();


        drawer = new DrawerBuilder()
                .withSelectedItem(-1)
                .withAccountHeader(headerResult)
                .withActivity((Activity) context)
                .withSliderBackgroundColorRes(R.color.maginder_deep_grey)
                .addDrawerItems(
                        item.withTextColorRes(R.color.maginder_soft_white),
                        item1.withTextColorRes(R.color.maginder_soft_white),
                        item2.withTextColorRes(R.color.maginder_soft_white),
                        item3.withTextColorRes(R.color.maginder_soft_white),
                        item4.withTextColorRes(R.color.maginder_soft_white),
                        item5.withTextColorRes(R.color.maginder_soft_white),
                        item6.withTextColorRes(R.color.maginder_soft_white)
                )
                .build();
    }

    public void navigationDrawerOpen() {
        drawer.openDrawer();
    }
}
