package com.mycompany.webscreenshot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EditText enter = (EditText)findViewById(R.id.enter_view);
        Button button1 = (Button)findViewById(R.id.button);
        button1.setOnClickListener(this);
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

}
