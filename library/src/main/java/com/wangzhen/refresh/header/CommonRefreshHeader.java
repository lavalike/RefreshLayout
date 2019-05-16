package com.wangzhen.refresh.header;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangzhen.refresh.R;

/**
 * 通用刷新Header
 * Created by wangzhen on 2019-05-16.
 */
public class CommonRefreshHeader extends HeaderView {

    private TextView tvRefreshing;
    private ImageView indicator;
    private String[] texts = new String[]{getResources().getString(R.string.refresh_pull), getResources().getString(R.string.refresh_release), getResources().getString(R.string.refresh_refreshing), getResources().getString(R.string.refresh_finish)};

    public CommonRefreshHeader(Context context) {
        super(context);
        inflate(context, R.layout.common_refresh_header, this);
        indicator = findViewById(R.id.indicator);
        tvRefreshing = findViewById(R.id.tv_refreshing);
        init();
    }

    private void init() {
        if (indicator.getVisibility() == View.GONE)
            indicator.setVisibility(View.VISIBLE);
        if (tvRefreshing.getVisibility() == View.GONE)
            tvRefreshing.setVisibility(View.VISIBLE);
        if (indicator.getAnimation() != null)
            indicator.getAnimation().cancel();
        indicator.setImageResource(R.mipmap.icon_refresh_down);
        tvRefreshing.setText(texts[0]);
    }

    @Override
    public void onTriggerEnter() {
        tvRefreshing.setText(texts[1]);
        indicator.setImageResource(R.mipmap.icon_refresh_up);
    }

    @Override
    public void onTriggerExit() {
        init();
    }

    @Override
    public void onRefresh() {
        tvRefreshing.setText(texts[2]);
        indicator.setImageResource(R.mipmap.icon_refreshing);
        RotateAnimation animation = new RotateAnimation(
                0f,
                359f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(500);
        animation.setRepeatCount(Animation.INFINITE);
        indicator.startAnimation(animation);
    }

    @Override
    public void onRefreshComplete() {
        super.onRefreshComplete();
        init();
        indicator.setImageResource(R.mipmap.icon_refresh_done);
        tvRefreshing.setText(texts[3]);
    }

}
