package com.wangzhen.refresh.header;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * HeaderView 抽象类
 * Created by wangzhen on 2019/3/26.
 */
public abstract class HeaderView extends FrameLayout {

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 执行刷新
     */
    public abstract void startRefresh();

    /**
     * 刷新完成
     */
    public abstract void completeRefresh();
}
