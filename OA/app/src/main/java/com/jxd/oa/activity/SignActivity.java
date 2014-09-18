package com.jxd.oa.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.jxd.common.vo.LocationInfo;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.SignAdapter;
import com.jxd.oa.bean.Address;
import com.jxd.oa.bean.Sign;
import com.jxd.oa.constants.Const;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.LocationProviderHelper;
import com.jxd.oa.utils.ParamManager;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;
import com.yftools.view.annotation.event.OnItemClick;

import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：考勤签到
 * Created by cy on 2014/8/12.
 * *****************************************
 */
public class SignActivity extends AbstractActivity {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    private List<Sign> signList;
    private SignAdapter adapter;
    private LocationProviderHelper locationProviderHelper;
    private List<Address> vicinityAddressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_sign_title));
        initData();
    }


    private void initData() {
        try {
            signList = DbOperationManager.getInstance().getBeans(Selector.from(Sign.class).orderBy("singTime"));
        } catch (DbException e) {
            LogUtil.e(e);
        }
        fillList();
    }


    public void fillList() {
        if (adapter == null) {
            adapter = new SignAdapter(mContext, signList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(signList);
            adapter.notifyDataSetChanged();
        }
    }

    @OnItemClick(R.id.mListView)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, SignAddressActivity.class);
        intent.putExtra("sign", adapter.getItem(position));
        startActivity(intent);
    }

    @OnClick(R.id.signIn_btn)
    public void signInClick(View view) {
        signStart((Integer) Const.TYPE_SIGN_IN.getValue());
    }

    private void signStart(final int type) {
        locationProviderHelper = new LocationProviderHelper(mContext, true, new LocationProviderHelper.LocationFinished() {
            @Override
            public void getLocation(final LocationInfo location) {
                try {
                    vicinityAddressList = new ArrayList<Address>();
                    List<Address> addressList = DbOperationManager.getInstance().getBeans(Selector.from(Address.class));
                    if (addressList != null) {
                        for (Address address : addressList) {
                            //取出100以内的考勤参考位置
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            LatLng addressLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                            double distance = DistanceUtil.getDistance(latLng, addressLatLng);
                            if (distance <= 100) {
                                address.setDistance(distance);
                                vicinityAddressList.add(address);
                            }
                        }
                        if (vicinityAddressList.size() > 0) {
                            Dialog alertDialog = new AlertDialog.Builder(mContext).setTitle("请选择参考位置")
                                    .setItems(vicinityAddressList.toArray(new String[vicinityAddressList.size()]), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            signSubmit(location, vicinityAddressList.get(which), type);
                                        }
                                    }).create();
                            alertDialog.show();
                        } else {
                            displayToast("附近无考勤参考位置");
                        }
                    } else {
                        displayToast("暂无考勤参考位置，请联系管理先采集位置");
                    }

                } catch (DbException e) {
                    LogUtil.e(e);
                }
            }
        });
        locationProviderHelper.startLocation();
    }

    private void signSubmit(LocationInfo location, Address address, int type) {
        Sign sign = new Sign();
        sign.setSignType(type);
        sign.setSignAccuracy(location.getAccuracy());
        sign.setSignAddress(location.getAddress());
        sign.setSignCoorType(location.getCoorType());
        sign.setSignLatitude(location.getLatitude());
        sign.setSignLongitude(location.getLongitude());
        sign.setSignDistance(address.getDistance());
        sign.setVicinityAddress(address);
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("data", GsonUtil.getInstance().getGson().toJson(sign));

        HttpUtil.getInstance().sendInDialog(mContext, "正在上传数据...", ParamManager.parseBaseUrl(""), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                String result = responseInfo.result.toString();
                Sign sign = GsonUtil.getInstance().getGson().fromJson(result, Sign.class);
                try {
                    DbOperationManager.getInstance().saveOrUpdate(sign);
                    initData();
                } catch (DbException e) {
                    LogUtil.e(e);
                }

                //OAApplication.getSysCurrentDate()
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    @OnClick(R.id.signOut_btn)
    public void signOutClick(View view) {
        signStart((Integer) Const.TYPE_SIGN_OUT.getValue());
    }
}
