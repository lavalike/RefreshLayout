package com.wangzhen.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wangzhen.refresh.RefreshLayout;
import com.wangzhen.refresh.callback.RefreshCallback;

/**
 * ScrollActivity
 * Created by wangzhen on 2019/2/22.
 */
public class ScrollActivity extends AppCompatActivity implements RefreshCallback, View.OnClickListener {

    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        setTitle("ScrollView示例");
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setRefreshCallback(this);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                refreshLayout.startRefresh();
                break;
            case R.id.btn_stop:
                refreshLayout.refreshComplete();
                break;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.refreshComplete();
            }
        }, 1500);
    }
}
