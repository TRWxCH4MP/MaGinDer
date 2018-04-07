package com.example.trw.maginder.create_item;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.activity.MainActivity;
import com.example.trw.maginder.activity.ManageTableActivity;
import com.example.trw.maginder.callback.OnCallbackState;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by _TRW on 24/12/2560.
 */

public class CreateDrawerMenu {
    private static final String PREF_NAME = "PREF_NAME";

    private Context context;
    private Drawer drawer;
    private String name;
    private String type;

    public CreateDrawerMenu(Context context) {
        this.context = context;
    }

    public void createNavigationDrawer(String name, final String type) {
        this.name = name;
        this.type = type;

        new DrawerBuilder().withActivity((Activity) context).build();

        final PrimaryDrawerItem item = new PrimaryDrawerItem().withIdentifier(1).withName("ตะกร้าเมนู");
        final PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("โต๊ะ");
        final PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(1).withName("ของคาว");
        final PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(1).withName("ของหวาน");
        final PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(1).withName("เครื่องดื่ม");
        final PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(1).withName("โปรโมชั่น");
        final PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(1).withName("ออกจากระบบ");

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
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem == item6) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            Toast.makeText(context, "ออกจากระบบเรียบร้อย", Toast.LENGTH_SHORT).show();
                            ManageTableActivity manageTableActivity = new ManageTableActivity();
                            manageTableActivity.onLogout(true);

                        }
                        return false;
                    }
                })
                .build();
    }

    public void navigationDrawerOpen() {
        drawer.openDrawer();
    }

}
