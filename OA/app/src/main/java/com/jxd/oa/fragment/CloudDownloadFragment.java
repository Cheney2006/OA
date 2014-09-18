package com.jxd.oa.fragment;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.CloudDownloadAdapter;
import com.jxd.oa.bean.Cloud;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.fragment.base.AbstractFragment;
import com.jxd.oa.utils.DbOperationManager;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.util.AndroidUtil;
import com.yftools.util.StorageUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnItemClick;

import java.io.File;
import java.util.List;

/**
 * *****************************************
 * Description ：企业云——我的下载
 * Created by cy on 2014/8/18.
 * *****************************************
 */
public class CloudDownloadFragment extends AbstractFragment {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    private List<Cloud> cloudDownloadList;
    private CloudDownloadAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.activity_list_view, container, false);
        ViewUtil.inject(this, convertView);
        return convertView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(mListView);
        fillList();
    }

    @OnItemClick(R.id.mListView)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cloud data = adapter.getItem(position);
        String fileName = data.getSavePath().substring(data.getSavePath().lastIndexOf("/"));
        File file = new File(StorageUtil.getDiskCacheDir(mContext, Constant.FOLDER_DOWNLOAD), fileName);
        if (file.exists()) {
            AndroidUtil.viewFile(mContext, file);
        } else {
            displayToast("文件不存在，请重新下载");
        }
    }

    private void fillList() {
        try {
            cloudDownloadList = DbOperationManager.getInstance().getBeans(Selector.from(Cloud.class).where("isDownload", "=", true));
        } catch (DbException e) {
            LogUtil.e(e);
        }
        if (adapter == null) {
            adapter = new CloudDownloadAdapter(mContext, cloudDownloadList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(cloudDownloadList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void refreshData() {
        fillList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //添加菜单项
        menu.add(Menu.NONE, 0, 0, "删除");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(adapter.getItem(info.position).getFileName());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int currentSelectedPosition = info.position;
        try {
            DbOperationManager.getInstance().deleteBean(adapter.getItem(currentSelectedPosition));
            fillList();
        } catch (DbException e) {
            LogUtil.e(e);
        }
        return super.onContextItemSelected(item);
    }

}
