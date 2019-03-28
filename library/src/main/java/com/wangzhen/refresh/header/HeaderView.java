package com.wangzhen.refresh.header;

import android.content.Context;
import android.widget.FrameLayout;

import com.wangzhen.refresh.callback.HeaderAction;

/**
 * HeaderView 抽象类
 * Created by wangzhen on 2019/3/26.
 */
public abstract class HeaderView extends FrameLayout implements HeaderAction {

    public HeaderView(Context context) {
        super(context);
    }

    @Override
    public void onScroll(float distance) {

    }

    @Override
    public void onTriggerEnter() {

    }

    @Override
    public void onTriggerExit() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onRefreshComplete() {

    }
}
