package com.wangzhen.refresh.util;

import com.wangzhen.refresh.common.C;

/**
 * ValidatorUtils 校验工具类
 * Created by wangzhen on 2019/3/28.
 */
public class ValidatorUtils {

    /**
     * 校验factor的合法性
     *
     * @param factor factor
     * @return valid factor
     */
    public static float validateFactor(float factor) {
        if (factor < 0 || factor > 1) {
            factor = C.DEFAULT_REFRESH_FACTOR;
        }
        return factor;
    }

    /**
     * 校验延迟关闭时间
     *
     * @param delay delay
     * @return delay
     */
    public static int validateCollapseDelay(int delay) {
        if (delay < 0)
            delay = C.DEFAULT_COLLAPSE_DELAY;
        return delay;
    }
}
