package com.jxd.oa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.R;
import com.jxd.oa.activity.CloudActivity;
import com.jxd.oa.activity.EmailAddActivity;
import com.jxd.oa.activity.EmailDetailActivity;
import com.jxd.oa.adapter.CloudAdapter;
import com.jxd.oa.bean.Cloud;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.fragment.base.AbstractFragment;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.ParamManager;
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
        final Cloud data = adapter.getItem(position);
        if (data.isDownload()) {
            //AndroidUtil.viewFile(mContext, file);
            new JxdAlertDialog(mContext, getString(R.string.txt_tips), "文件已存在“我的下载”中，是否重新下载？", getString(R.string.txt_confirm), getString(R.string.txt_cancel)) {
                @Override
                protected void positive() {
                    downloadFile(childView, data);
                }
            }.show();
        } else {
            downloadFile(childView, data);
        }
    }

    private void downloadFile(final CloudAdapter.ViewHolder childView, final Cloud data) {
        if (TextUtils.isEmpty(data.getSavePath())) {
            return;
        }
        String fileName = data.getSavePath().substring(data.getSavePath().lastIndexOf("/"));
        final File file = new File(StorageUtil.getDiskCacheDir(mContext, Constant.FOLDER_DOWNLOAD), fileName);
        HttpUtil.getInstance().download(ParamManager.parseDownUrl(data.getSavePath()), file.getAbsolutePath(), new RequestCallBack<File>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                childView.download_tv.setText(DigitUtil.getPercent(current * 1.0 / total));
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                data.setDownload(true);
                try {
                    DbOperationManager.getInstance().save(data);
                } catch (DbException e) {
                    LogUtil.e(e);
                }
                ((CloudActivity) getActivity()).setPageIndex(1);
                mContext.sendBroadcast(new Intent(Constant.ACTION_REFRESH));
                displayToast("已下载到\"我的下载\"");
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
            }
        });
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
