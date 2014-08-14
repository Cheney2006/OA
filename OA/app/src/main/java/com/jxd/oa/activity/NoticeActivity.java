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
import com.jxd.oa.bean.Notice;
import com.jxd.oa.utils.DbOperationManager;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.DbException;
import com.yftools.ui.DatePickUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

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
        getSupportActionBar().setTitle("通知公告");
        initView();
        initData();

    }

    private void initView() {
        registerForContextMenu(mListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(mContext,NoticeDetailActivity.class);
                intent.putExtra("notice",adapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    public void initData() {
        try {
            noticeList = DbOperationManager.getInstance().getBeans(Notice.class);
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
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                    refreshData();
                } catch (DbException e) {
                    LogUtil.e(e);
                }
            }
        }.show();
        return super.onContextItemSelected(item);
    }

}
