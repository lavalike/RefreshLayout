package com.wangzhen.refresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
    //默认刷新因子
    private static final float DEFAULT_REFRESH_FACTOR = 0.3f;
    //动画执行时间
    private static final long DEFAULT_DURATION_TIME = 250L;
    //HeaderView位置
    private static final int POSITION_HEADER_VIEW = 0;
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
    //下拉偏移量
    private float deltaY;
    private float lastX;
    private float lastY;
    private RefreshCallback callback;
    //HeaderView高度
    private int headerHeight;
    private boolean run;

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
        addView(mHeaderView = new DefaultRefreshHeader(getContext()), POSITION_HEADER_VIEW, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
            removeViewAt(POSITION_HEADER_VIEW);
            addView(mHeaderView = header, POSITION_HEADER_VIEW, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
     * 校验factor的合法性
     *
     * @param factor factor
     * @return valid factor
     */
    private float validateFactor(float factor) {
        if (factor < 0 || factor > 1) {
            factor = DEFAULT_REFRESH_FACTOR;
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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!run) {
            run = true;
            headerHeight = mHeaderView.getHeight();
            MarginLayoutParams params = (MarginLayoutParams) mHeaderView.getLayoutParams();
            params.topMargin = -headerHeight;
            mHeaderView.requestLayout();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isRefreshEnable || isRefreshing)
            return false;
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

    /**
     * 详见{@link android.support.v4.widget.SwipeRefreshLayout#requestDisallowInterceptTouchEvent(boolean)}
     *
     * @param disallowIntercept disallowIntercept
     */
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // if this is a List < L or another view that doesn't support nested
        // scrolling, ignore this request so that the vertical scroll event
        // isn't stolen
        if ((android.os.Build.VERSION.SDK_INT < 21 && mContentView instanceof AbsListView)
                || ViewCompat.isNestedScrollingEnabled(mContentView)
                || isCanPullDown()) {
            // Nope.
        } else {
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        deltaY = (event.getY() - lastY) * mRefreshFactor;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    changeHeaderMargin((int) deltaY);
                    if (deltaY >= mRefreshThreshold) {
                        mHeaderView.onTriggerEnter();
                    } else {
                        mHeaderView.onTriggerExit();
                    }
                    mHeaderView.onScroll(deltaY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isDragging) {
                    isDragging = false;
                    if (deltaY >= mRefreshThreshold) {
                        mHeaderView.onRefresh();
                        smoothChangeHeaderMargin((int) deltaY, mRefreshThreshold);
                        if (callback != null && !isRefreshing) {
                            callback.onRefresh();
                        }
                        isRefreshing = true;
                    } else {
                        smoothChangeHeaderMargin((int) deltaY, 0);
                        isRefreshing = false;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 平滑改变Header的margin
     *
     * @param start start
     * @param end   end
     */
    private void smoothChangeHeaderMargin(int start, int end) {
        if (start < 0) start = 0;
        if (end < 0) end = 0;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                changeHeaderMargin((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(DEFAULT_DURATION_TIME);
        valueAnimator.start();
    }

    /**
     * 改变HeaderView的topMargin
     *
     * @param topMargin toMargin
     */
    private void changeHeaderMargin(int topMargin) {
        if (topMargin < 0) topMargin = 0;
        MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
        layoutParams.topMargin = topMargin - headerHeight;
        mHeaderView.requestLayout();
    }

    /**
     * 判断是否滚动到顶部
     */
    private boolean isCanPullDown() {
        return mContentView != null && mContentView.canScrollVertically(-1);
    }

    /**
     * 设置刷新回调
     *
     * @param callback callback
     */
    public void setRefreshCallback(RefreshCallback callback) {
        this.callback = callback;
    }

    /**
     * 手动刷新
     */
    public void startRefresh() {
        if (!isRefreshing) {
            isDragging = true;
            isRefreshing = true;
            mHeaderView.onRefresh();
            smoothChangeHeaderMargin(0, mRefreshThreshold);
        }
    }

    /**
     * 结束刷新
     */
    public void refreshComplete() {
        if (isRefreshing) {
            isRefreshing = false;
            isDragging = false;
            mHeaderView.onRefreshComplete();
            smoothChangeHeaderMargin(mRefreshThreshold, 0);
        }
    }
}
