package com.jxd.oa.activity.base;

import com.baidu.mapapi.map.MapView;

/**
 * *****************************************
 * Description ：地图基础类
 * Created by cy on 2014/7/30.
 * *****************************************
 */
public class BaseMapActivity extends AbstractActivity {

    protected MapView mMapView;

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }
}
