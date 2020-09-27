package com.wangzhen.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangzhen.refresh.RefreshLayout;
import com.wangzhen.refresh.callback.OnRefreshCallback;
import com.wangzhen.refresh.header.CommonRefreshHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerActivity
 * Created by wangzhen on 2019/2/22.
 */
public class RecyclerActivity extends AppCompatActivity implements OnRefreshCallback {

    private int TYPE_BANNER = 0;
    private int TYPE_ITEM = 1;
    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        setTitle("RecyclerView示例");
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setHeaderView(new CommonRefreshHeader(this));
        refreshLayout.setOnRefreshCallback(this);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        List<Object> list = new ArrayList<>();
        list.add("Banner");
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(list));
    }

    @Override
    public void onRefresh() {
        refreshLayout.refreshComplete();
    }

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Object> datas;

        public Adapter(List<Object> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemViewType(int position) {
            Object o = datas.get(position);
            if ("Banner".equals(o)) {
                return TYPE_BANNER;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_BANNER) {
                return new BannerHolder(parent);
            } else {
                return new ItemHolder(parent);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ItemHolder) {
                ((ItemHolder) holder).tvContent.setText(String.valueOf(datas.get(position)));
            }
            if (holder instanceof BannerHolder) {
                ((BannerHolder) holder).bind();
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class BannerHolder extends RecyclerView.ViewHolder {

            private ViewPager viewPager;

            public BannerHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_layout, parent, false));
                viewPager = itemView.findViewById(R.id.view_pager);
            }

            public void bind() {
                viewPager.setAdapter(new PagerAdapter() {
                    @Override
                    public int getCount() {
                        return 3;
                    }

                    @NonNull
                    @Override
                    public Object instantiateItem(@NonNull ViewGroup container, int position) {
                        TextView textView = new TextView(container.getContext());
                        textView.setText("position " + position);
                        textView.setGravity(Gravity.CENTER);
                        container.addView(textView);
                        return textView;
                    }

                    @Override
                    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                        container.removeView((View) object);
                    }

                    @Override
                    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                        return view == o;
                    }
                });
            }
        }

        class ItemHolder extends RecyclerView.ViewHolder {
            TextView tvContent;

            public ItemHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
                tvContent = itemView.findViewById(R.id.tv_content);
            }
        }
    }
}
