package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.NoticeAdapter;
import com.jxd.oa.bean.Email;
import com.jxd.oa.bean.EmailRecipient;
import com.jxd.oa.bean.Notice;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
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
import com.yftools.ui.DatePickUtil;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;
import com.yftools.view.annotation.event.OnItemClick;

import java.util.Date;
import java.util.List;

/**
 * *****************************************
 * Description ：通知公告
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
public class NoticeActivity extends AbstractActivity {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    private List<Notice> noticeList;
    private NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_notice));
        registerForContextMenu(mListView);
        initData();
    }


    public void initData() {
        try {
            noticeList = DbOperationManager.getInstance().getBeans(Selector.from(Notice.class).orderBy("publishTime", true));
        } catch (DbException e) {
            LogUtil.e(e);
        }
        if (adapter == null) {
            adapter = new NoticeAdapter(mContext, noticeList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(noticeList);
            adapter.notifyDataSetChanged();
        }
    }

    @OnItemClick(R.id.mListView)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, NoticeDetailActivity.class);
        intent.putExtra("notice", adapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sync_read, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:
                new DatePickUtil(mContext, "请选择开始时间", new DatePickUtil.DateSetFinished() {
                    @Override
                    public void onDateSetFinished(String pickYear, String pickMonth, String pickDay) {
                        syncData(Notice.class, pickYear + "-" + pickMonth + "-" + pickDay);
                    }
                }).showDateDialog();
                return true;
            case R.id.action_read:
                readSubmit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readSubmit() {
        //取得未读的邮件
        try {
            final List<Notice> notReadNoticeList = DbOperationManager.getInstance().getBeans(Selector.from(Notice.class).where("isRead", "=", false));
            if (notReadNoticeList == null) {
                displayToast("暂无未读通知");
                return;
            }
            final StringBuffer sb = new StringBuffer();
            for (Notice notice : notReadNoticeList) {
                sb.append(notice.getId()).append(",");
                notice.setRead(true);
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            RequestParams params = ParamManager.setDefaultParams();
            params.addBodyParameter("id", sb.toString());
            HttpUtil.getInstance().send(ParamManager.parseBaseUrl("readNotice.action"), params, new RequestCallBack<Json>() {
                @Override
                public void onSuccess(ResponseInfo<Json> responseInfo) {
                    //更新接收时间
                    try {
                        DbOperationManager.getInstance().save(notReadNoticeList);
                        sendBroadcast(new Intent(Constant.ACTION_REFRESH));//要刷新未读数
                        displayToast("通知全部已读");
                    } catch (DbException e) {
                        LogUtil.e(e);
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    displayToast(msg);
                }
            });
        } catch (DbException e) {
            LogUtil.e(e);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //添加菜单项
        menu.add(Menu.NONE, 0, 0, "删除");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(adapter.getItem(info.position).getTitle());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int currentSelectedPosition = info.position;
        //删除
        new JxdAlertDialog(this, getString(R.string.txt_tips), "确定删除？", getString(R.string.txt_confirm), getString(R.string.txt_cancel)) {
            @Override
            protected void positive() {
                try {
                    DbOperationManager.getInstance().deleteBean(adapter.getItem(currentSelectedPosition));
                    sendBroadcast(new Intent(Constant.ACTION_REFRESH));//要刷新未读数
                } catch (DbException e) {
                    LogUtil.e(e);
                }
            }
        }.show();
        return super.onContextItemSelected(item);
    }

    @Override
    protected void refreshData() {
        initData();
    }
}
