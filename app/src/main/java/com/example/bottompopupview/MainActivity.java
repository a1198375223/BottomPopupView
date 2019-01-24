package com.example.bottompopupview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bottompopupview.view.BottomPopupView;

// 注意这种自定义的view如果点击button, button会优先处理点击事件
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomPopupView popupView = (BottomPopupView) findViewById(R.id.popup_view);
        popupView.setOnItemClickListener(new BottomPopupView.OnItemClickListenr() {
            @Override
            public void onLiveClickListener() {
                Toast.makeText(MainActivity.this, "live click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVideoClickListener() {
                Toast.makeText(MainActivity.this, "video click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRadioClickListener() {
                Toast.makeText(MainActivity.this, "radio click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDynamicClickListener() {
                Toast.makeText(MainActivity.this, "dynamic click", Toast.LENGTH_SHORT).show();
            }
        });

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupView.isShown()) {
                    popupView.exit();
                }else {
                    popupView.start();
                }
            }
        });

        ((ImageView) findViewById(R.id.image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: image click");
                Toast.makeText(MainActivity.this, "image click", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
