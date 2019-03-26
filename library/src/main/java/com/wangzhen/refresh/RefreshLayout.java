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
import com.wangzhen.refresh.header.HeaderView;
import com.wangzhen.refresh.header.RefreshHeader;
import com.wangzhen.refresh.util.UIUtils;

/**
 * 下拉刷新布局 RefreshLayout
 * Created by wangzhen on 2019/3/26.
 */
public final class RefreshLayout extends LinearLayout {
    //默认拖动因子
    private static final float DEFAULT_DRAG_FACTOR = 0.3f;
    //动画执行时间
    private static final long DURATION_TIME = 250L;
    //触发刷新阈值
    private int mRefreshThreshold;
    private float mDragFactor;
    //是否正在拖动
    private boolean isDragging = false;
    //是否正在刷新
    private boolean isRefreshing = false;
    //是否启用下拉刷新
    private boolean isEnable;
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
        mDragFactor = validateFactor(typedArray.getFloat(R.styleable.RefreshLayout_drag_factor, DEFAULT_DRAG_FACTOR));
        isEnable = typedArray.getBoolean(R.styleable.RefreshLayout_drag_enable, true);
        typedArray.recycle();
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        isDragging = false;
        mRefreshThreshold = UIUtils.dp2px(getContext(), 150);
        createDefaultHeader();
    }

    /**
     * 添加默认透明HeaderView
     */
    private void createDefaultHeader() {
        mHeaderView = new RefreshHeader(getContext());
        addView(mHeaderView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
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
    public void setDragFactor(float factor) {
        this.mDragFactor = validateFactor(factor);
    }

    /**
     * 校验DragFactor的合法性
     *
     * @param factor 拖拽因子
     * @return factor
     */
    private float validateFactor(float factor) {
        if (factor < 0) {
            factor = DEFAULT_DRAG_FACTOR;
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
        if (!isEnable) return super.onInterceptTouchEvent(ev);
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
        deltaY = event.getY() - lastY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    changeHeaderHeight((int) deltaY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isDragging) {
                    isDragging = false;
                    if (deltaY >= mRefreshThreshold) {
                        mHeaderView.startRefresh();
                        animChangeHeaderHeight((int) deltaY, mRefreshThreshold);
                        if (callback != null && !isRefreshing) {
                            callback.onRefresh();
                        }
                        isRefreshing = true;
                    } else {
                        animChangeHeaderHeight((int) deltaY, 0);
                        isRefreshing = false;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 将HeaderView高度从start变化到end
     *
     * @param start start
     * @param end   end
     */
    private void animChangeHeaderHeight(int start, int end) {
        if (start < 0) start = 0;
        if (end < 0) end = 0;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                changeHeaderHeight((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(DURATION_TIME);
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
        layoutParams.height = (int) (height * mDragFactor);
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
        isDragging = true;
        isRefreshing = true;
        mHeaderView.startRefresh();
        animChangeHeaderHeight(0, mRefreshThreshold);
    }

    /**
     * 手动结束刷新
     */
    public void refreshComplete() {
        isRefreshing = false;
        isDragging = false;
        mHeaderView.completeRefresh();
        animChangeHeaderHeight(mRefreshThreshold, 0);
    }
}
