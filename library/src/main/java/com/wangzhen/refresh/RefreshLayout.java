package com.wangzhen.refresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wangzhen.refresh.callback.RefreshCallback;
import com.wangzhen.refresh.header.DefaultRefreshHeader;
import com.wangzhen.refresh.header.HeaderView;
import com.wangzhen.refresh.util.UIUtils;

/**
 * 下拉刷新布局 RefreshLayout
 * Created by wangzhen on 2019/3/26.
 */
public final class RefreshLayout extends LinearLayout {
    //默认拖动因子
    private static final float DEFAULT_REFRESH_FACTOR = 0.3f;
    //动画执行时间
    private static final long DEFAULT_DURATION_TIME = 250L;
    //触发刷新阈值
    private int mRefreshThreshold;
    private float mRefreshFactor;
    //是否正在拖动
    private boolean isDragging = false;
    //是否正在刷新
    private boolean isRefreshing = false;
    //是否启用下拉刷新
    private boolean isRefreshEnable;
    //HeaderView
    private HeaderView mHeaderView;
    //ContentView
    private View mContentView;

    private float deltaY;
    private float lastX;
    private float lastY;
    private RefreshCallback callback;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout);
        mRefreshFactor = validateFactor(typedArray.getFloat(R.styleable.RefreshLayout_refresh_factor, DEFAULT_REFRESH_FACTOR));
        isRefreshEnable = typedArray.getBoolean(R.styleable.RefreshLayout_refresh_enable, true);
        mRefreshThreshold = (int) typedArray.getDimension(R.styleable.RefreshLayout_refresh_threshold, UIUtils.dp2px(getContext(), 48));
        typedArray.recycle();
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        isDragging = false;
        isRefreshing = false;
        createDefaultHeader();
    }

    /**
     * 添加默认透明HeaderView
     */
    private void createDefaultHeader() {
        mHeaderView = new DefaultRefreshHeader(getContext());
        addView(mHeaderView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
    }

    /**
     * 设置是否启用下拉刷新
     *
     * @param enable enable
     */
    public void setRefreshEnable(boolean enable) {
        this.isRefreshEnable = enable;
    }

    /**
     * 设置HeaderView
     *
     * @param header HeaderView
     */
    public void setHeaderView(HeaderView header) {
        if (header != null) {
            this.mHeaderView = header;
            removeViewAt(0);
            addView(mHeaderView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        }
    }

    /**
     * 设置拖动因子 0~1
     *
     * @param factor factor
     */
    public void setRefreshFactor(float factor) {
        this.mRefreshFactor = validateFactor(factor);
    }

    /**
     * 设置下拉刷新阈值
     *
     * @param threshold threshold in pixel
     */
    public void setRefreshThreshold(int threshold) {
        this.mRefreshThreshold = threshold;
    }

    /**
     * 校验DragFactor的合法性
     *
     * @param factor 拖拽因子
     * @return factor
     */
    private float validateFactor(float factor) {
        if (factor < 0) {
            factor = DEFAULT_REFRESH_FACTOR;
        }
        if (factor > 1) {
            factor = 1;
        }
        return factor;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            mContentView = getChildAt(getChildCount() - 1);
            if (mContentView != null) {
                mContentView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isRefreshEnable) return super.onInterceptTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //存在情况：mContentView内容未在顶部，下拉到顶部时mContentView会直接跳过前面下拉距离
                //解决方法：下拉过程中不断更新lastX、lastY坐标为当前坐标，达到从顶部下拉的效果
                if (isCanPullDown()) {
                    lastX = ev.getX();
                    lastY = ev.getY();
                }
                float diffX = ev.getX() - lastX;
                float diffY = ev.getY() - lastY;
                if (diffY > 0 && diffY > Math.abs(diffX)) {
                    isDragging = true;
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        deltaY = (event.getY() - lastY) * mRefreshFactor;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    changeHeaderHeight((int) deltaY);
                    if (deltaY >= mRefreshThreshold) {
                        mHeaderView.onTriggerEnter();
                    } else {
                        mHeaderView.onTriggerExit();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isDragging) {
                    isDragging = false;
                    if (deltaY >= mRefreshThreshold) {
                        mHeaderView.startRefresh();
                        smoothChangeHeaderHeight((int) deltaY, mRefreshThreshold);
                        if (callback != null && !isRefreshing) {
                            callback.onRefresh();
                        }
                        isRefreshing = true;
                    } else {
                        smoothChangeHeaderHeight((int) deltaY, 0);
                        isRefreshing = false;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 将HeaderView高度从start平滑变化到end
     *
     * @param start start
     * @param end   end
     */
    private void smoothChangeHeaderHeight(int start, int end) {
        if (start < 0) start = 0;
        if (end < 0) end = 0;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                changeHeaderHeight((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(DEFAULT_DURATION_TIME);
        valueAnimator.start();
    }

    /**
     * 改变HeaderView的高度
     *
     * @param height 高度
     */
    private void changeHeaderHeight(int height) {
        if (height < 0) height = 0;
        ViewGroup.LayoutParams layoutParams = mHeaderView.getLayoutParams();
        layoutParams.height = height;
        mHeaderView.requestLayout();
    }

    /**
     * 判断是否滚动到顶部
     */
    private boolean isCanPullDown() {
        return mContentView != null && mContentView.canScrollVertically(-1);
    }

    public void setRefreshCallback(RefreshCallback callback) {
        this.callback = callback;
    }

    /**
     * 手动调用刷新
     */
    public void startRefresh() {
        if (!isRefreshing) {
            isDragging = true;
            isRefreshing = true;
            mHeaderView.startRefresh();
            smoothChangeHeaderHeight(0, mRefreshThreshold);
        }
    }

    /**
     * 手动结束刷新
     */
    public void refreshComplete() {
        if (isRefreshing) {
            isRefreshing = false;
            isDragging = false;
            mHeaderView.completeRefresh();
            smoothChangeHeaderHeight(mRefreshThreshold, 0);
        }
    }
}
