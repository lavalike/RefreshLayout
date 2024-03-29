# refresh-layout

[![Platform](https://img.shields.io/badge/Platform-Android-00CC00.svg?style=flat)](https://www.android.com)
[![Jcenter](https://img.shields.io/badge/jcenter-RefreshLayout-red.svg?style=flat)](http://jcenter.bintray.com/com/wangzhen/refresh-layout/)
[![Download](https://api.bintray.com/packages/lavalike/maven/refresh-layout/images/download.svg) ](https://bintray.com/lavalike/maven/refresh-layout/_latestVersion)
[![API](https://img.shields.io/badge/API-17%2B-00CC00.svg?style=flat)](https://android-arsenal.com/api?level=17)
[![License](https://img.shields.io/badge/License-Apache%202-337ab7.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

> 支持任意View的刷新布局

#### 依赖导入
```gradle
implementation 'com.wangzhen:refresh-layout:0.2.5'
```

#### 属性说明
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="RefreshLayout">
        <!-- 下拉阻尼值，0~1，默认0.5 -->
        <attr name="refresh_factor" format="float" />
        <!-- 是否启用下拉刷新，默认true -->
        <attr name="refresh_enable" format="boolean" />
        <!-- 下拉刷新阈值，默认48dp -->
        <attr name="refresh_threshold" format="dimension" />
        <!-- 悬停偏移量，默认0dp-->
        <attr name="refresh_hover_offset" format="dimension" />
        <!-- 刷新完毕延迟关闭时间，默认300ms-->
        <attr name="refresh_collapse_delay" format="integer" />
    </declare-styleable>
</resources>
```

#### 代码示例
```java
//1、设置刷新回调
refreshLayout.setOnRefreshCallback(new OnRefreshCallback() {
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
refreshLayout.setRefreshFactor(0.5f);
//5、设置刷新HeaderView，可继承抽象类HeaderView实现自定义Header
refreshLayout.setHeaderView(view extends HeaderView);
//6、设置刷新完毕延迟关闭时间，默认300ms
refreshLayout.setCollapseDelay(300);
//7、设置悬停偏移量
refreshLayout.setHoverOffset(50);
//8、手动刷新
refreshLayout.startRefresh();
//9、刷新完成
refreshLayout.refreshComplete();
```