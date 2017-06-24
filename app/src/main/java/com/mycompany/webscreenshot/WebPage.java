package com.mycompany.webscreenshot;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ddz.floatingactionbutton.FloatingActionButton;
import com.ddz.floatingactionbutton.FloatingActionMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



public class WebPage extends AppCompatActivity {

    private FloatingActionMenu fab_menu;

    private FloatingActionButton captureButton;

    private FloatingActionButton captureAllButton;

    private static final String TAG = "WebPage";


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

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        captureButton = (FloatingActionButton) findViewById(R.id.capture_webview);
        captureAllButton = (FloatingActionButton) findViewById(R.id.capture_all_webview);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap a = activityShot(WebPage.this);
                String num = getRandomCode();
                saveToSD(a, "/Screenimage" + num);
                Toast.makeText(WebPage.this, "OK", Toast.LENGTH_SHORT).show();
                Toast.makeText(WebPage.this, "is" + num, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public static Bitmap activityShot(Activity activity) {
        // 获取 windows 中最顶层的 view
        View view = activity.getWindow().getDecorView();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        WindowManager windowManager = activity.getWindowManager();


        // 获取屏幕宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        // 去掉状态栏
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight, width,
                height - statusBarHeight);

        // 销毁缓存信息
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        Toast.makeText(activity, "this bitmap ok", Toast.LENGTH_SHORT).show();

        return bitmap;
    }



    /**
     * 存储到sdcard
     *
     */
    public void saveToSD(Bitmap bitmap, String bitName) {

        //判断sd卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //文件名
            String fileName = "Android/data/WebScreenshot";
            File folder = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+fileName);
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(folder + bitName + ".jpg");
            FileOutputStream out;
            if (!file.exists()) {

                try {
                    out = new FileOutputStream(file);
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)) {
                        Toast.makeText(WebPage.this, "成功存入相册", Toast.LENGTH_SHORT).show();
                        out.flush();
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Toast.makeText(WebPage.this, "oh, no", Toast.LENGTH_SHORT).show();
        }


    }

    private String getRandomCode() {
        String num = "";
        for (int i = 0; i < 10; i++) {
            int f = (int) (Math.random() * 10);
            num += f;
        }
        return num;
    }


}
