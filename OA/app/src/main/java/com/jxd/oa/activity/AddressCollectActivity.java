package com.jxd.oa.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.jxd.common.vo.LocationInfo;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.BaseMapActivity;
import com.jxd.oa.bean.Address;
import com.jxd.oa.bean.Message;
import com.jxd.oa.constants.Const;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.LocationProviderHelper;
import com.jxd.oa.utils.ParamManager;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.db.sqlite.WhereBuilder;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;

/**
 * *****************************************
 * Description ：考勤地址采集
 * Created by cy on 2014/7/30.
 * *****************************************
 */
public class AddressCollectActivity extends BaseMapActivity {

    private LocationProviderHelper locationProviderHelper;
    private LocationInfo lastLocation;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setTitle(getResources().getString(R.string.txt_title_address));
        address = (Address) getIntent().getSerializableExtra("address");
        mMapView = (MapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        locationProviderHelper = new LocationProviderHelper(mContext, new LocationProviderHelper.LocationFinished() {

            @Override
            public void getLocation(final LocationInfo location) {
                lastLocation = location;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                addPopWindow("名称：" + address.getName(), "定位地址：" + location.getAddress(), ll);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 19.0f);
                mBaiduMap.animateMapStatus(u);
            }
        });
        locationProviderHelper.startLocation();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationProviderHelper != null) {
            locationProviderHelper.stopLocation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (setData()) {
                    submit();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean setData() {
        address.setLatitude(lastLocation.getLatitude());
        address.setLongitude(lastLocation.getLongitude());
        address.setAddress(lastLocation.getAddress());
        address.setCoorType((Integer) Const.COOR_TYPE_BD.getValue());
        address.setAccuracy(lastLocation.getAccuracy());
        //TODO 先弄成完成。正式时应该是提交
        address.setStatus((Integer) Const.STATUS_ADDRESS_FINISHED.getValue());
        return true;
    }

    private void submit() {
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("data", GsonUtil.getInstance().getGson().toJson(address));
        HttpUtil.getInstance().sendInDialog(mContext, getString(R.string.txt_is_upload_data), ParamManager.parseBaseUrl("addressSave.action"), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                //String result = responseInfo.result.toString();
                try {
                    DbOperationManager.getInstance().saveOrUpdate(address);
                    //删除系统消息
                    DbOperationManager.getInstance().deleteBean(Message.class, WhereBuilder.b("beanId", "=", address.getId()).and("beanName", "=", Address.class.getSimpleName()));
                    sendRefresh();
                    finish();
                } catch (DbException e) {
                    LogUtil.e(e);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
            }
        });
    }
}
