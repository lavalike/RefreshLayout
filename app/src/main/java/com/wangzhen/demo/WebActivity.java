package com.wangzhen.demo;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.wangzhen.refresh.RefreshLayout;
import com.wangzhen.refresh.callback.OnRefreshCallback;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * WebActivity
 * Created by wangzhen on 2019/2/22.
 */
public class WebActivity extends AppCompatActivity implements OnRefreshCallback {
    private RefreshLayout refreshLayout;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setTitle("WebView示例");

        refreshLayout = findViewById(R.id.pull_layout);
        refreshLayout.setOnRefreshCallback(this);
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        String url = "https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&amp;mid=2247483998&amp;idx=1&amp;sn=03cb1942533247ac23876448fdf1b39a&amp;chksm=96cda313a1ba2a050f8cc2325e36b468f620d3d167d9f0dbc3108127c5ba16d14dc83cabc3c6&amp;scene=21#wechat_redirect";
        webView.loadUrl(url);
    }


    /**
     * 获取url的Host
     *
     * @param url url
     * @return Host
     */
    private static String getUrlHost(String url) {
        if (TextUtils.isEmpty(url) || url.trim().equals("")) {
            return "";
        }
        String host = "";
        try {
            URI uri = new URI(url);
            host = uri.getHost();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return host;
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
