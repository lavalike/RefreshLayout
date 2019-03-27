package com.wangzhen.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangzhen.refresh.RefreshLayout;
import com.wangzhen.refresh.callback.RefreshCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerActivity
 * Created by wangzhen on 2019/2/22.
 */
public class RecyclerActivity extends AppCompatActivity implements RefreshCallback {

    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        setTitle("RecyclerView示例");
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setRefreshCallback(this);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(list));
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.refreshComplete();
            }
        }, 1500);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
        private List<Integer> datas;

        public Adapter(List<Integer> datas) {
            this.datas = datas;
        }

        @Override
        public Adapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(parent);
        }

        @Override
        public void onBindViewHolder(Adapter.Holder holder, int position) {
            holder.tvContent.setText(String.valueOf(datas.get(position)));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvContent;

            public Holder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
                tvContent = itemView.findViewById(R.id.tv_content);
            }
        }
    }
}
