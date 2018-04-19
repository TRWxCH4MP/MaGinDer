package com.example.trw.maginder.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class PreOrderedMenuFragment extends Fragment {
    private static final String PREF_NAME = "PREF_NAME";
    private static final String ID_RESTAURANT = "id_restaurant";
    private WebView webViewManagerOrder;
    private static final String TAG = "PreOrderedMenuFragment";

    private String idRestaurant;
    private String tableId;

    public PreOrderedMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            orderMenu = new ArrayList<>(getArguments().getStringArrayList(LIST_MENU));
            idRestaurant = getArguments().getString(ID_RESTAURANT);
            tableId = getArguments().getString("TableId");
            Log.d(TAG, "onCreate: " + tableId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_pre_ordered_menu, container, false);

        webViewManagerOrder = view.findViewById(R.id.web_view_pre_ordered_menu);
        webViewManagerOrder.getSettings().setJavaScriptEnabled(true);
        webViewManagerOrder.loadUrl("http://it2.sut.ac.th/prj60_g14/Project/Maginder/history_app.php?ID_Restaurant=" + idRestaurant +"&ID_table=" + tableId);
        webViewManagerOrder.setWebViewClient(new WebViewClient());

        return view;
    }

}
