package com.wangzhen.refresh.callback;

/**
 * HeaderAction
 * Created by wangzhen on 2019/3/26.
 */
public interface HeaderAction {
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
    void startRefresh();

    /**
     * 刷新完成
     */
    void completeRefresh();
}
