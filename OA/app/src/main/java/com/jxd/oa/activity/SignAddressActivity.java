package com.jxd.oa.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.jxd.common.vo.LocationInfo;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.BaseMapActivity;
import com.jxd.oa.bean.Sign;
import com.jxd.oa.utils.LocationProviderHelper;

/**
 * *****************************************
 * Description ：考勤位置采集
 * Created by cy on 2014/7/30.
 * *****************************************
 */
public class SignAddressActivity extends BaseMapActivity {

    private BaiduMap mBaiduMap;
    private Sign sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setTitle(getString(R.string.txt_title_sign_address));
        sign = (Sign) getIntent().getSerializableExtra("sign");
        //真正的数据时，要删除 TODO
        sign = new Sign();
        sign.setSignLatitude(30.629508);
        sign.setSignLongitude(114.242831);
        sign.setSignAccuracy(80.0f);

        LatLng ll = new LatLng(sign.getSignLatitude(), sign.getSignLongitude());
        mMapView = (MapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        OverlayOptions options = new MarkerOptions()
                .position(ll)  //设置marker的位置
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker))  //设置marker图标
                .zIndex(9);  //设置marker所在层级
        //将marker添加到地图上
        mBaiduMap.addOverlay(options);
        //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //创建InfoWindow展示的view
                View view = LayoutInflater.from(mContext).inflate(R.layout.popup_sign_address, null);
                view.setBackgroundResource(R.drawable.popup);
                //定义用于显示该InfoWindow的坐标点
                final LatLng ll = marker.getPosition();
                Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                p.y -= 25;
                LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                //创建InfoWindow的点击事件监听者
                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        //添加点击后的事件响应代码
                    }
                };
                //创建InfoWindow
                InfoWindow mInfoWindow = new InfoWindow(view, llInfo, listener);
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
        //添加圆
        float accuracy = sign.getSignAccuracy();
        CircleOptions circleOptions = new CircleOptions().center(ll).radius(((int) accuracy)).fillColor(0x10000000).stroke(new Stroke(3, 0xAA0099CC));
        //在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(circleOptions);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 19f);//.newLatLng(ll);
        mBaiduMap.setMapStatus(u);

    }


}
