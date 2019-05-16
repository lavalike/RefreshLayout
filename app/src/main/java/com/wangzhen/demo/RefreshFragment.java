package com.wangzhen.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangzhen.refresh.RefreshLayout;
import com.wangzhen.refresh.callback.OnRefreshCallback;

/**
 * RefreshFragment
 * Created by wangzhen on 2019-05-16.
 */
public class RefreshFragment extends Fragment implements OnRefreshCallback {

    private View itemView;
    private RefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return itemView = inflater.inflate(R.layout.fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout = itemView.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshCallback(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.refreshComplete();
            }
        }, 1000);
    }
}
