package com.huaya.pullrefreshrecycleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huaya.library.adapter.HeaderAndFooterWrapper;
import com.huaya.library.view.CleanTips;
import com.huaya.library.view.CustomToast;
import com.huaya.library.view.FloatToast;
import com.huaya.library.view.PullToRefreshRecycleView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View addHead = findViewById(R.id.addHead);
        View addFoot = findViewById(R.id.addFoot);
        View viewById = findViewById(R.id.toast);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View inflate = View.inflate(MainActivity.this, R.layout.item, null);
                FloatToast cleanTips = FloatToast.getInstatce(MainActivity.this,inflate)
                        .setDuration(5000)
                        .setAnimations(R.style.CleanToastAnimation)
                        .setShowLocation(FloatToast.getMetrics(MainActivity.this).widthPixels, FloatToast.getMetrics(MainActivity.this).heightPixels * 3 / 4);
                cleanTips.show();
            }
        });


        PullToRefreshRecycleView rv = (PullToRefreshRecycleView) findViewById(R.id.recycleView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        TestAdapter testAdapter = new TestAdapter(this);
        final HeaderAndFooterWrapper objectHeaderAndFooterWrapper = new HeaderAndFooterWrapper(this, testAdapter);
        rv.setAdapter(objectHeaderAndFooterWrapper);
        addHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(MainActivity.this);
                textView.setText("我是头");
                objectHeaderAndFooterWrapper.addHeaderView(textView);

            }
        });

        addFoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(MainActivity.this);
                textView.setText("我是尾");
                objectHeaderAndFooterWrapper.addFootView(textView);

            }
        });
    }
}

