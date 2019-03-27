package com.wangzhen.refresh.header;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wangzhen.refresh.R;

/**
 * 默认刷新Header
 * Created by wangzhen on 2019/3/26.
 */
public class DefaultRefreshHeader extends HeaderView {

    private TextView textView;

    private String texts[] = new String[]{"下拉刷新", "释放刷新", "正在刷新", "刷新完成"};

    public DefaultRefreshHeader(Context context) {
        this(context, null);
    }

    public DefaultRefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.layout_refresh_header, this);
        textView = findViewById(R.id.tv_tip);
        textView.setText(texts[0]);
    }

    @Override
    public void onScroll(float distance) {

    }

    @Override
    public void onTriggerEnter() {
        textView.setText(texts[1]);
    }

    @Override
    public void onTriggerExit() {
        textView.setText(texts[0]);
    }

    @Override
    public void onRefresh() {
        textView.setText(texts[2]);
    }

    @Override
    public void onRefreshComplete() {
        textView.setText(texts[3]);
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText(texts[0]);
            }
        }, 200);
    }

}
