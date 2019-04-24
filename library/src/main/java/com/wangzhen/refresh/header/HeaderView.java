package com.wangzhen.refresh.header;

import android.content.Context;
import android.widget.FrameLayout;

import com.wangzhen.refresh.callback.HeaderAction;

/**
 * HeaderView 抽象类
 * Created by wangzhen on 2019/3/26.
 */
public abstract class HeaderView extends FrameLayout implements HeaderAction {
    // HeaderView悬停偏移量
    protected int mHoverOffset;

    public HeaderView(Context context) {
        super(context);
    }

    /**
     * 设置HeaderView悬停偏移量
     *
     * @param offset offset
     */
    public void setHoverOffset(int offset) {
        this.mHoverOffset = offset;
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

    /**
     * <p>
     * 继承HeaderView时请确保调用{@code super.onRefreshComplete()}
     * </p>
     */
    @Override
    public void onRefreshComplete() {
        offset();
    }

    /**
     * 改变topMargin偏移量
     */
    private void offset() {
        if (mHoverOffset > 0) {
            MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
            params.topMargin -= mHoverOffset;
            setLayoutParams(params);
        }
    }
}
