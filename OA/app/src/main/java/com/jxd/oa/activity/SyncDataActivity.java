package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;

import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.SyncDataAdapter;
import com.jxd.oa.asynctask.SyncDataTask;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.service.IncrementUpdateService;
import com.jxd.oa.utils.ParamManager;
import com.yftools.HttpUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.HttpException;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;
import com.yftools.view.annotation.ViewInject;

/**
 * *****************************************
 * Description ：同步数据
 * Created by cy on 2014/7/29.
 * *****************************************
 */
public class SyncDataActivity extends AbstractActivity {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    private SyncDataAdapter adapter;
    private SyncDataTask syncDataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("同步数据");
        initData();
    }

    private void initData() {
        int length = Constant.SYNC_DATA_TAG.length;
        String[] syncNames = new String[length];
        for (int i = 0; i < length; i++) {
            syncNames[i] = (String) Constant.SYNC_DATA_TAG[i][1];
        }
        adapter = new SyncDataAdapter(mContext, syncNames);
        mListView.setAdapter(adapter);

        syncDataTask = new SyncDataTask(mContext, new SyncDataTask.TaskFinishedCallBack() {
            @Override
            public void onFinished(Bundle result) {
                if (result.getBoolean("success")) {
                    SysConfig.getInstance().setSyncFinished(true);
                    SysConfig.getInstance().setSyncIndex(0);//同步成功，下标还原
                    //更新最大版本号
                    getMaxVersion();
                    //同时对于位置中需要当前人采集的记录发送系统消息广播 TODO
                    finish();
                } else {
                    syncDataTask.cancel(true);
                    new JxdAlertDialog(mContext, getResources().getString(R.string.txt_error), "同步数据失败 : " + result.getCharSequence("message"), getResources().getString(R.string.txt_confirm)) {
                        @Override
                        protected void positive() {
                            exitApp();
                        }
                    }.show();
                }
            }
        }, mListView, adapter);
        syncDataTask.execute();
    }

    private void getMaxVersion() {
        HttpUtil.getInstance().send(ParamManager.parseBaseUrl("getMaxVersionId.action"), ParamManager.setDefaultParams(), new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
               SysConfig.getInstance().setMaxVersion(responseInfo.result.getInt("version"));
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new JxdAlertDialog(this, getResources().getString(R.string.txt_tips), "正在同步，是否确定退出？", getResources().getString(R.string.txt_confirm), getResources().getString(R.string.txt_cancel)) {
                @Override
                protected void positive() {
                    syncDataTask.cancel(true);
                    syncDataTask.setSyncIndex();
                    exitApp();
                }

            }.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

}
