package com.mycompany.webscreenshot;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.ddz.floatingactionbutton.FloatingActionButton;
import com.ddz.floatingactionbutton.FloatingActionMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;


public class WebPage extends AppCompatActivity {

    private FloatingActionMenu fab_menu;

    private FloatingActionButton captureButton;

    private FloatingActionButton captureAllButton;

    private DrawerLayout mDrawerLayout;

    private static final String TAG = "WebPage";

    private ArrayList<String> selectedPhotos = new ArrayList<>();

    ImageView imageView;


    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        //  将活动添加到活动管理器中
        ActivityCollector.addActivity(this);

        Intent intent = getIntent();
        imageView = (ImageView) findViewById(R.id.image_view);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        String data = intent.getStringExtra("www");
        if (data == null) {
            data = "";
        }
        final WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        // 当网页跳转时，令目标网页仍然在当前 WebView 中显示
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.baidu.com/s?wd=" + data + "&ie=utf-8&src=eg_newtab");
        webView.setDrawingCacheEnabled(true);

        // 此段代码用于适应 android 5.0 以上版本，保证网页截图正常运行
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        captureButton = (FloatingActionButton) findViewById(R.id.capture_webview);
        captureAllButton = (FloatingActionButton) findViewById(R.id.capture_all_webview);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideButton();
                Bitmap a = activityShot(WebPage.this);
                showButton();
                String num = getRandomCode();
                saveToSD(a, "/Screenimage" + num);
                Toast.makeText(WebPage.this, Environment.getExternalStorageDirectory()+"/Android/data/WebScreenshot/Screenimage"+num+".jpg", Toast.LENGTH_SHORT).show();
            }
        });

        captureAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    WebView.enableSlowWholeDocumentDraw();
                }
                hideButton();
                Bitmap b = webshot(webView);
                showButton();
                String num = getRandomCode();
                saveToSD(b, "/AllScreenimage" + num);
                Toast.makeText(WebPage.this, Environment.getExternalStorageState()+num+".jpg", Toast.LENGTH_SHORT).show();
            }
        });

        navView.setCheckedItem(R.id.nav_screen);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
//                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_web:
                        Intent intent = new Intent(WebPage.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_picture:
                        PhotoPicker.builder()
                                .setPhotoCount(9)
                                .start(WebPage.this);
                        break;
                    case R.id.nav_out:
                        ActivityCollector.finishAll();
                        return true;
                }
                return true;
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView webView = (WebView) findViewById(R.id.web_view);

        if (keyCode == android.view.KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();   //返回上一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event); //退出整个应用程序
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
     * 页面截取
     */


        public static Bitmap webshot (WebView webView) {
            Picture picture = webView.capturePicture();
            int width = picture.getWidth();
            int height = picture.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            picture.draw(canvas);
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

                //String fileName = "Android/data/WebScreenshot";
            String fileName = "WebADScreenshot";

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
            Toast.makeText(WebPage.this, "Where are the SDcard?", Toast.LENGTH_SHORT).show();
        }


    }

    private void hideButton() {
        captureButton.setVisibility(View.INVISIBLE);
        captureAllButton.setVisibility(View.INVISIBLE);
        fab_menu.setVisibility(View.INVISIBLE);
    }

    private void showButton() {
        captureButton.setVisibility(View.VISIBLE);
        captureAllButton.setVisibility(View.VISIBLE);
        fab_menu.setVisibility(View.VISIBLE);
    }



    private String getRandomCode() {
        String num = "";
        for (int i = 0; i < 10; i++) {
            int f = (int) (Math.random() * 10);
            num += f;
        }
        return num;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
