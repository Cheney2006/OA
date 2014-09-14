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
import com.jxd.oa.adapter.TaskAdapter;
import com.jxd.oa.bean.Notice;
import com.jxd.oa.bean.Task;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.utils.DbOperationManager;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.ui.DatePickUtil;
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnItemClick;

import java.util.List;

/**
 * *****************************************
 * Description ：我的工作
 * Created by cy on 2014/9/14.
 * *****************************************
 */
@ContentView(R.layout.activity_list_view)
public class MyTaskActivity extends AbstractActivity {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    private List<Task> taskList;
    private TaskAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_my_task));
        registerForContextMenu(mListView);
        initData();
    }


    public void initData() {
        try {
            taskList = DbOperationManager.getInstance().getBeans(Selector.from(Task.class).orderBy("isFinished", true));
        } catch (DbException e) {
            LogUtil.e(e);
        }
        if (adapter == null) {
            adapter = new TaskAdapter(mContext, taskList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(taskList);
            adapter.notifyDataSetChanged();
        }
    }

    @OnItemClick(R.id.mListView)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, MyTaskDetailActivity.class);
        intent.putExtra("task", adapter.getItem(position));
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
