package com.jxd.oa.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jxd.common.vo.LocationInfo;
import com.jxd.oa.constants.SysConfig;
import com.yftools.LogUtil;

public class LocationProviderHelper {

    private LocationClient mLocationClient = null;
    private Context context;
    private LocationFinished callback;
    private BDLocationListener listener = null;
    private ProgressDialog pd;//加载对话框

    public LocationProviderHelper(Context context, LocationFinished locationFinished) {
        super();
        this.context = context;
        this.callback = locationFinished;
        listener = new MyBDListener();
    }

    public LocationProviderHelper(Context context, boolean isDialog, LocationFinished locationFinished) {
        super();
        this.context = context;
        this.callback = locationFinished;
        listener = new MyBDListener();
        if (isDialog) {
            pd = new ProgressDialog(context);
            pd.setMessage("正在定位。。。");
            pd.show();
        }
    }

    public LocationProviderHelper(Context context) {
        this.context = context;
    }

    public LocationProviderHelper(Context context, LocationFinished callback, BDLocationListener listener) {
        super();
        this.context = context;
        this.callback = callback;
        this.listener = listener;
    }

    public void setLocationFinished(LocationFinished callback) {
        this.callback = callback;
    }

    public void setLocationListener(BDLocationListener listener) {
        this.listener = listener;
    }

    public void startLocation() {
        startLocation(-1);
    }

    public void startLocation(int span) {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        } else {
            mLocationClient = new LocationClient(context);
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
            option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02 //114.242513,30.630916
            option.setIsNeedAddress(true);//返回的定位结果包含地址信息
            option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
            option.setOpenGps(true); // 打开gps
            option.setProdName("jxd"); // 设置产品线名称
            if (span > 0) {
                option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
            }
            mLocationClient.setLocOption(option);
            mLocationClient.registerLocationListener(listener);
            mLocationClient.start();// 将开启与获取位置分开，就可以尽量的在后面的使用中获取到位置
        }
    }

    /**
     * 停止，减少资源消耗
     */
    public void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
            mLocationClient = null;
        }
    }

    /**
     * 更新位置
     */
    public void updateListener() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
    }

    private class MyBDListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null){
                int type = mLocationClient.requestLocation();
                if (pd != null && pd.isShowing()) {
                    pd.setMessage("定位失败，正在重新定位...");
                }
            } else {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                LogUtil.d("纬度=" + location.getLatitude() + "，经度=" + location.getLongitude());
                SysConfig.getInstance().setDefaultLatitude(location.getLatitude());
                SysConfig.getInstance().setDefaultLongitude(location.getLongitude());
                LocationInfo locationInfo=new LocationInfo();
                locationInfo.setLatitude(location.getLatitude());
                locationInfo.setLongitude(location.getLongitude());
                locationInfo.setAddress(location.getAddrStr());
                callback.getLocation(locationInfo);
            }
        }
    }

    public interface LocationFinished {
        public void getLocation(LocationInfo location);
    }

}