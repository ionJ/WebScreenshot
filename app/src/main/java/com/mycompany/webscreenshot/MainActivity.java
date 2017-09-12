package com.mycompany.webscreenshot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ddz.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;

    private ArrayList<String> selectedPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glide_menu);
        // 将活动添加至活动管理器中
        ActivityCollector.addActivity(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EditText enter = (EditText)findViewById(R.id.enter_view);
        FloatingActionButton button1 = (FloatingActionButton)findViewById(R.id.button);
        button1.setOnClickListener(this);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
        }


        navView.setCheckedItem(R.id.nav_web);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
//                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_screen:
                        Intent intent = new Intent(MainActivity.this, WebPage.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_picture:
                        PhotoPicker.builder()
                                .setPhotoCount(9)
                                .start(MainActivity.this);
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
    public void onClick(View view){
        switch(view.getId()){
            case R.id.button:
                String webSite = " ";
                EditText enter = (EditText)findViewById(R.id.enter_view);
                webSite = enter.getText().toString();
                Intent intent = new Intent(MainActivity.this, WebPage.class);
                intent.putExtra("www", webSite);
                startActivity(intent);
                break;
            default:
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
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
