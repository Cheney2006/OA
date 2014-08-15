package com.jxd.oa.activity;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.jxd.common.vo.LocationInfo;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.BaseMapActivity;
import com.jxd.oa.utils.LocationProviderHelper;

/**
 * *****************************************
 * Description ：考勤地址地图
 * Created by cy on 2014/7/30.
 * *****************************************
 */
public class AddressCollectActivity extends BaseMapActivity {

    private BaiduMap mBaiduMap;
    private LocationProviderHelper locationProviderHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setTitle(getResources().getString(R.string.txt_title_address));
        mMapView = (MapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        locationProviderHelper= new LocationProviderHelper(mContext,new LocationProviderHelper.LocationFinished() {
            @Override
            public void getLocation(LocationInfo location) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 13.0f);
                mBaiduMap.animateMapStatus(u);
            }
        });
        locationProviderHelper.startLocation(1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationProviderHelper!=null){
            locationProviderHelper.stopLocation();
        }
    }
}
