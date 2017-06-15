package com.mycompany.webscreenshot;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ddz.floatingactionbutton.FloatingActionButton;
import com.ddz.floatingactionbutton.FloatingActionMenu;
public class WebPage extends AppCompatActivity {

    private FloatingActionMenu fab_menu;

    private FloatingActionButton captureButton;

    private FloatingActionButton captureAllButton;


    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        Intent intent = getIntent();
        final String data = intent.getStringExtra("www");
        final WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        // 当网页跳转时，令目标网页仍然在当前 WebView 中显示
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://" + data);
        webView.setDrawingCacheEnabled(true);

        fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        captureButton = (FloatingActionButton)findViewById(R.id.capture_webview);
        captureAllButton = (FloatingActionButton)findViewById(R.id.capture_all_webview);
        //fab_menu.getMenuButtonColorNormal();
    }
}
