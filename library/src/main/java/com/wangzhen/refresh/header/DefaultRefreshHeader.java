package com.wangzhen.refresh.header;

import android.content.Context;
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
        super(context);
        inflate(getContext(), R.layout.layout_refresh_default_header, this);
        textView = findViewById(R.id.tv_tip);
        init();
    }

    private void init() {
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
        init();
    }

    @Override
    public void onRefresh() {
        textView.setText(texts[2]);
    }

    @Override
    public void onRefreshComplete() {
        textView.setText(texts[3]);
    }

}
