package com.wangzhen.refresh.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * UIUtils
 * Created by wangzhen on 2019/3/26.
 */
public class UIUtils {
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
