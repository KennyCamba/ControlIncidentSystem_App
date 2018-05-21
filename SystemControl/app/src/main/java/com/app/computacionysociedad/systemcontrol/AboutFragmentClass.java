package com.app.computacionysociedad.systemcontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Kenny Camba Torres on 09/01/2018.
 */

public class AboutFragmentClass extends Fragment {
    WebView webView;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = view.findViewById(R.id.about);
        webView.loadUrl("https://cysgone.github.io/sipeci911/");
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        //webSettings.setJavaScriptEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}
