package com.wangzhen.refresh.callback;

/**
 * HeaderAction
 * Created by wangzhen on 2019/3/26.
 */
public interface HeaderAction {
    /**
     * 滑动
     *
     * @param distance 滑动距离 = 手指实际滑动距离 * factor
     */
    void onScroll(float distance);

    /**
     * 到达刷新界限
     */
    void onTriggerEnter();

    /**
     * 离开刷新界限
     */
    void onTriggerExit();

    /**
     * 执行刷新
     */
    void onRefresh();

    /**
     * 刷新完成
     */
    void onRefreshComplete();
}
