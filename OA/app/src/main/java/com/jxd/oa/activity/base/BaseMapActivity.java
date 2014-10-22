package com.jxd.oa.activity.base;

import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.jxd.oa.R;

/**
 * *****************************************
 * Description ：地图基础类
 * Created by cy on 2014/7/30.
 * *****************************************
 */
public class BaseMapActivity extends AbstractActivity {

    protected MapView mMapView;
    protected BaiduMap mBaiduMap;

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

    protected void addPopWindow(final String name, final String address, LatLng ll) {
        OverlayOptions options = new MarkerOptions()
                .position(ll)  //设置marker的位置
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker))  //设置marker图标
                .zIndex(9) //设置marker所在层级
                .draggable(true);//设置手势拖拽
        //将marker添加到地图上
        mBaiduMap.addOverlay(options);
        //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //创建InfoWindow展示的view
                View view = LayoutInflater.from(mContext).inflate(R.layout.popup_sign_address, null);
                TextView name_tv = (TextView) view.findViewById(R.id.name_tv);
                name_tv.setText(name);
                TextView address_tv = (TextView) view.findViewById(R.id.address_tv);
                address_tv.setText(address);
                //定义用于显示该InfoWindow的坐标点
                final LatLng ll = marker.getPosition();
                //创建InfoWindow
                InfoWindow mInfoWindow = new InfoWindow(view, ll, -25);
                //显示InfoWindow
                mBaiduMap.showInfoWindow(mInfoWindow);
                return false;
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }
}
