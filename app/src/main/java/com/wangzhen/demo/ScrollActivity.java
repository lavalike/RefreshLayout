package com.wangzhen.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * ScrollActivity
 * Created by wangzhen on 2019/2/22.
 */
public class ScrollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        setTitle("Fragment刷新");

        FragmentManager manager = getSupportFragmentManager();
        Fragment tag = manager.findFragmentByTag("a");
        if (tag == null) {
            manager.beginTransaction().add(R.id.frame, new RefreshFragment(), "a").commit();
        } else {
            manager.beginTransaction().show(tag).commit();
        }
    }
}
