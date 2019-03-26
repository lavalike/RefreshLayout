package com.wangzhen.refresh.header;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wangzhen.refresh.R;

/**
 * RefreshHeader 头部刷新View
 * Created by wangzhen on 2019/3/26.
 */
public class RefreshHeader extends HeaderView {

    private TextView textView;

    private String texts[] = new String[]{"下拉刷新", "正在刷新", "刷新完成"};

    public RefreshHeader(Context context) {
        this(context, null);
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.layout_refresh_header, this);
        textView = findViewById(R.id.tv_tip);
        textView.setText(texts[0]);
    }

    @Override
    public void startRefresh() {
        textView.setText(texts[1]);
    }

    @Override
    public void completeRefresh() {
        textView.setText(texts[2]);
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText(texts[0]);
            }
        }, 200);
    }

}
