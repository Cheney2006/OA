package com.jxd.oa.activity;

import android.os.Bundle;

import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.BaseMapActivity;
import com.jxd.oa.bean.Sign;
import com.yftools.util.DateUtil;

/**
 * *****************************************
 * Description ：考勤位置采集
 * Created by cy on 2014/7/30.
 * *****************************************
 */
public class SignAddressActivity extends BaseMapActivity {

    private Sign sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setTitle(getString(R.string.txt_title_sign_address));
        sign = (Sign) getIntent().getSerializableExtra("sign");

        LatLng ll = new LatLng(sign.getSignLatitude(), sign.getSignLongitude());
        mMapView = (MapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        addPopWindow("考勤时间：" + DateUtil.dateTimeToString(sign.getSignTime()), "考勤地址：" + sign.getSignAddress(), ll);
        //添加圆
        float accuracy = sign.getSignAccuracy();
        CircleOptions circleOptions = new CircleOptions().center(ll).radius(((int) accuracy)).fillColor(0x10000000).stroke(new Stroke(3, 0xAA0099CC));
        //在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(circleOptions);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 19f);//.newLatLng(ll);
        mBaiduMap.setMapStatus(u);

    }


}
