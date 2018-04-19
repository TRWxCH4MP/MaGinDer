package com.example.trw.maginder.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.trw.maginder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageOrderFragment extends Fragment {
    private static final String PREF_NAME = "PREF_NAME";
    private static final String ID_RESTAURANT = "id_restaurant";
    private WebView webViewManagerOrder;

    private String idRestaurant;

    public ManageOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        idRestaurant = sharedPreferences.getString(ID_RESTAURANT, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_manage_order, container, false);

        webViewManagerOrder = view.findViewById(R.id.web_view_manage_order);
        webViewManagerOrder.getSettings().setJavaScriptEnabled(true);
        webViewManagerOrder.loadUrl("http://it2.sut.ac.th/prj60_g14/Project/cashier/order_app.php?ID_Restaurant=" + idRestaurant);
        webViewManagerOrder.setWebViewClient(new WebViewClient());

        return view;
    }

}
