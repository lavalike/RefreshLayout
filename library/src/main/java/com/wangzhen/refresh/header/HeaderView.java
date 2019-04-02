package com.wangzhen.refresh.header;

import android.animation.ValueAnimator;
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

    @Override
    public void onRefreshComplete() {
        animOffset();
    }

    /**
     * 改变topMargin偏移量
     */
    private void animOffset() {
        if (mHoverOffset <= 0) return;
        final MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        int start = params.topMargin;
        ValueAnimator animator = ValueAnimator.ofInt(start, start - mHoverOffset);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.topMargin = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });
        animator.setDuration(100);
        animator.start();
    }
}
