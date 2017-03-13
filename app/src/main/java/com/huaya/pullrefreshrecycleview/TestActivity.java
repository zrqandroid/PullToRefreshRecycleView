package com.huaya.pullrefreshrecycleview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by zhuruqiao on 2017/1/16.
 * e-mail:563325724@qq.com
 */

public class TestActivity extends AppCompatActivity {

    private int padding = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final RelativeLayout tv = (RelativeLayout) findViewById(R.id.tv);
        View up = findViewById(R.id.up);
        View down = findViewById(R.id.down);

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                padding += 10;
                tv.setPadding(0, padding, 0, 0);
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                padding -= 10;
                tv.setPadding(0, padding, 0, 0);
            }
        });


    }
}
