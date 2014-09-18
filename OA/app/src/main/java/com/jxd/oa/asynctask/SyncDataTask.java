package com.jxd.oa.asynctask;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.jxd.oa.adapter.SyncDataAdapter;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.CommonJson4List;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.ParamManager;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.http.ResponseStream;
import com.yftools.task.PriorityAsyncTask;

/**
 * *****************************************
 * Description ：同步数据异步任务
 * Created by cy on 2014/7/30.
 * *****************************************
 */
public class SyncDataTask extends PriorityAsyncTask<Integer, String, Bundle> {


    private final ListView listView;
    private int mSyncIndex;
    private TaskFinishedCallBack callBack;

    private RefreshHandler mRefreshHandler = new RefreshHandler();
    private SyncDataAdapter mSyncDataListAdapter;

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            int position = msg.arg1;
            mSyncDataListAdapter.setSyncingIndex(position);
            mSyncDataListAdapter.notifyDataSetChanged();
            if ((position + 1) % 8 == 0) {
                listView.setSelection(position - 1);
            }
        }
    }

    public SyncDataTask(Context context, TaskFinishedCallBack callBack, ListView listView, SyncDataAdapter adapter) {
        super();
        this.mSyncIndex = SysConfig.getInstance().getSyncIndex();
        if (mSyncIndex < 0) {
            mSyncIndex = 0;
        }
        this.callBack = callBack;
        this.listView = listView;
        this.mSyncDataListAdapter = adapter;
    }

    @Override
    protected Bundle doInBackground(Integer... params) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("success", true);
        try {
            int length = Constant.SYNC_DATA_TAG.length;
            for (; mSyncIndex < length; mSyncIndex++) {
                if (isCancelled()) {
                    break;
                }
                Message msg = new Message();
                msg.arg1 = mSyncIndex;
                mRefreshHandler.sendMessage(msg);
                syncData((Class<?>) Constant.SYNC_DATA_TAG[mSyncIndex][0]);
                msg = new Message();
                msg.arg1 = mSyncIndex;
                mRefreshHandler.sendMessage(msg);
            }
        } catch (Exception e) {
            LogUtil.e(e);
            bundle.putBoolean("success", false);
            bundle.putString("message", e.getMessage());
        }
        return bundle;
    }

    @Override
    protected void onPostExecute(Bundle result) {
        setSyncIndex();
        callBack.onFinished(result);

    }

    private <T> void syncData(Class<T> cls) throws Exception {
        String name = cls.getSimpleName();
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        ResponseStream result = HttpUtil.getInstance().sendSync(ParamManager.parseBaseUrl(name + "List.action"), ParamManager.setDefaultParams());
        String resultStr = result.readString();
        LogUtil.d("服务器数据返回:" + resultStr);
        CommonJson4List<T> commonJson4List = CommonJson4List.fromJson(resultStr, cls);
        if (commonJson4List.getSuccess()) {
            DbOperationManager.getInstance().saveOrUpdate(commonJson4List.getData());
        } else {
            throw new Exception(commonJson4List.getMessage());
        }
    }


    public void setSyncIndex() {
        if (mSyncIndex > 0) {
            SysConfig.getInstance().setSyncIndex(mSyncIndex - 1);
        }
    }

    public interface TaskFinishedCallBack {
        void onFinished(Bundle result);
    }
}
