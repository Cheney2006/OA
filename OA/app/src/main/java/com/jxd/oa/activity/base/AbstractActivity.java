package com.jxd.oa.activity.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.jxd.oa.bean.Email;
import com.jxd.oa.constants.Const;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.utils.CommonJson4List;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.ParamManager;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;

import org.w3c.dom.ls.LSOutput;

import java.lang.reflect.Type;
import java.util.List;

/**
 * *****************************************
 * Description ：基础类
 * Created by cy on 2014/7/28.
 * *****************************************
 */
public abstract class AbstractActivity extends ActionBarActivity {


    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = AbstractActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ExitAppUtil.getInstance().pushActivity(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_REFRESH);
        intentFilter.addAction(Constant.ACTION_EXIT);//广播的方式退出
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ExitAppUtil.getInstance().removeActivity(this);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_EXIT)) {
                finish();
            } else if (intent.getAction().equals(Constant.ACTION_REFRESH)) {
                refreshData();
            }
        }
    };

    protected void refreshData() {
    }

    public void displayToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void exitApp() {
        sendBroadcast(new Intent(Constant.ACTION_EXIT));
    }

    protected <T> void syncData(final Class<T> cls, String startDate) {
        String name = cls.getSimpleName();
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("startDate", startDate);
        HttpUtil.getInstance().sendInDialog(mContext, "正在同步数据...", ParamManager.parseBaseUrl(name + "List.action"), params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    LogUtil.d("服务器返回：" + responseInfo.result);
                    CommonJson4List<T> commonJson4List = CommonJson4List.fromJson(responseInfo.result, cls);
                    if (commonJson4List.getSuccess()) {
                        DbOperationManager.getInstance().save(commonJson4List.getData());
                        //refreshData();
                        sendBroadcast(new Intent(Constant.ACTION_REFRESH));
                    } else {
                        displayToast(commonJson4List.getMessage());
                    }
                } catch (DbException e) {
                    LogUtil.e(e);
                    displayToast(e.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
            }
        });
    }
}

