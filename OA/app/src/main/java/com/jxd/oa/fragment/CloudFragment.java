package com.jxd.oa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jxd.oa.R;
import com.jxd.oa.activity.CloudActivity;
import com.jxd.oa.activity.EmailAddActivity;
import com.jxd.oa.activity.EmailDetailActivity;
import com.jxd.oa.adapter.CloudAdapter;
import com.jxd.oa.bean.Cloud;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.fragment.base.AbstractFragment;
import com.jxd.oa.utils.DbOperationManager;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.util.AndroidUtil;
import com.yftools.util.DigitUtil;
import com.yftools.util.StorageUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnItemClick;

import java.io.File;
import java.util.List;

/**
 * *****************************************
 * Description ：企业云——公司文档
 * Created by cy on 2014/8/18.
 * *****************************************
 */
public class CloudFragment extends AbstractFragment {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    private List<Cloud> cloudList;
    private CloudAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.activity_list_view, container, false);
        ViewUtil.inject(this, convertView);
        return convertView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillList();
    }

    @OnItemClick(R.id.mListView)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {
        final CloudAdapter.ViewHolder childView = (CloudAdapter.ViewHolder) view.getTag();
        Cloud data = adapter.getItem(position);
        String fileName = data.getSavePath().substring(data.getSavePath().lastIndexOf("/"));
        File file = new File(StorageUtil.getDiskCacheDir(mContext, Constant.FOLDER_DOWNLOAD), fileName);
        if (file.exists()) {
            AndroidUtil.viewFile(mContext, file);
        } else {
            //如果已经下载，直接打开
            HttpUtil.getInstance().download("", file.getAbsolutePath(), new RequestCallBack<File>() {
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    childView.download_tv.setText(DigitUtil.getPercent(current / total));
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    AndroidUtil.viewFile(mContext, responseInfo.result);
                    ((CloudActivity) getActivity()).setPageIndex(1);
                    mContext.sendBroadcast(new Intent(Constant.ACTION_REFRESH));
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fillList() {
        try {
            cloudList = DbOperationManager.getInstance().getBeans(Cloud.class);
        } catch (DbException e) {
            LogUtil.e(e);
        }
        if (adapter == null) {
            adapter = new CloudAdapter(mContext, cloudList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(cloudList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void refreshData() {
        fillList();
    }
}
