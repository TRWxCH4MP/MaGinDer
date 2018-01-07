package com.example.trw.maginder.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.trw.maginder.R;
import com.example.trw.maginder.callback.FragmentCallback;
import com.example.trw.maginder.db.InsertData;
import com.example.trw.maginder.db.entity.EmployeeEntity;
import com.example.trw.maginder.db.entity.MenuEntity;
import com.example.trw.maginder.fragment.LoginFragment;
import com.example.trw.maginder.service.dao.LoginItemDao;
import com.example.trw.maginder.service.dao.RestaurantItemCollectionDao;
import com.example.trw.maginder.service.http_manger.HttpManager;
import com.example.trw.maginder.service.http_manger.HttpManagerRestaurant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FragmentCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new LoginFragment())
                    .commit();
        }

        Call<RestaurantItemCollectionDao> call = HttpManagerRestaurant.getInstance().getService().repos("5a3b2935a73aa");
        call.enqueue(new Callback<RestaurantItemCollectionDao>() {
            @Override
            public void onResponse(Call<RestaurantItemCollectionDao> call, Response<RestaurantItemCollectionDao> response) {
                if (response.isSuccessful()) {
                    Log.d("MainActivity", "onResponse: isSuccessful");
                } else {
                    Log.d("MainActivity", "onResponse: !isSuccessful");
                }
            }

            @Override
            public void onFailure(Call<RestaurantItemCollectionDao> call, Throwable t) {
                Log.d("MainActivity", "onFailure: ");
            }
        });





//        Call<RestaurantItemCollectionDao> call = HttpManagerRestaurant.getInstance().getService().repos("5a3b2935a73aa");
//        call.enqueue(new Callback<RestaurantItemCollectionDao>() {
//            @Override
//            public void onResponse(Call<RestaurantItemCollectionDao> call, Response<RestaurantItemCollectionDao> response) {
//                if (response.isSuccessful()) {
//                    Log.d("MainActivity", "onResponse: Success");
//                    RestaurantItemCollectionDao dao = response.body();
//                    for (int index = 0; index < dao.getData().size(); index++) {
//                        Log.d("MainActivity", dao.getData().get(index).getDetail());
//                        Log.d("MainActivity", dao.getData().get(index).getName());
//                        Log.d("MainActivity", dao.getData().get(index).getPrice());
//                    }
//                } else {
//                    Log.d("MainActivity", "onResponse: No Success");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RestaurantItemCollectionDao> call, Throwable t) {
//                Log.d("MainActivity", "onFailure: Failure");
//            }
//        });

//        insertEmployee();
//        insertMenuType();
    }

    @Override
    public void handleEvent(Fragment fragment) {
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment) {
        if (fragment == null) {
            Intent intent = new Intent(this, ManageTableActivity.class);
            startActivity(intent);
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentContainer, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void insertEmployee() {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setUsername("test");
        employeeEntity.setPassword("1234");
        employeeEntity.setStatus(1);
        InsertData.onInsertEmployee(getApplicationContext(), employeeEntity);

        EmployeeEntity employeeEntity2 = new EmployeeEntity();
        employeeEntity2.setUsername("test2");
        employeeEntity2.setPassword("1234");
        employeeEntity2.setStatus(2);
        InsertData.onInsertEmployee(getApplicationContext(), employeeEntity2);
    }

    public void insertMenuType() {
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setMenuTypeName("ของคาว");
        InsertData.onInsertMenuType(getApplicationContext(), menuEntity);

        MenuEntity menuEntity2 = new MenuEntity();
        menuEntity2.setMenuTypeName("ของหวาน");
        InsertData.onInsertMenuType(getApplicationContext(), menuEntity2);

        MenuEntity menuEntity3 = new MenuEntity();
        menuEntity3.setMenuTypeName("เครื่องดื่ม");
        InsertData.onInsertMenuType(getApplicationContext(), menuEntity3);
    }
}
