# RefreshLayout
> 支持任意View的刷新布局

#### 属性介绍
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="RefreshLayout">
        <!-- 下拉阻尼值，0~1，默认0.3 -->
        <attr name="refresh_factor" format="float" />
        <!-- 是否启用下拉刷新，默认true -->
        <attr name="refresh_enable" format="boolean" />
        <!-- 下拉刷新阈值，默认48dp -->
        <attr name="refresh_threshold" format="dimension" />
    </declare-styleable>
</resources>
```


#### XML配置
```xml
<com.wangzhen.refresh.RefreshLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:refresh_enable="true"
    app:refresh_factor="0.3"
    app:refresh_threshold="48dp">
</com.wangzhen.refresh.RefreshLayout>
```

#### Java配置
```java
//1、设置刷新回调
refreshLayout.setRefreshCallback(new RefreshCallback() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.refreshComplete();
                    }
                }, 1500);
            }
        });
//2、设置是否启用下拉刷新
refreshLayout.setRefreshEnable(true);
//3、设置刷新阈值
refreshLayout.setRefreshThreshold(100);
//4、设置下拉阻尼
refreshLayout.setRefreshFactor(0.3f);
//5、设置刷新HeaderView，可继承抽象类HeaderView实现自定义Header
refreshLayout.setHeaderView(view extends HeaderView);
//6、手动刷新
refreshLayout.startRefresh();
//7、刷新完成
refreshLayout.refreshComplete();
```