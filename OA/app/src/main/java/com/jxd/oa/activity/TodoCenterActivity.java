package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.TodoAdapter;
import com.jxd.oa.bean.ExpenseAccount;
import com.jxd.oa.bean.LeaveApplication;
import com.jxd.oa.constants.Const;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.table.DbModel;
import com.yftools.exception.DbException;
import com.yftools.ui.DatePickUtil;
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnItemClick;

import java.util.List;

/**
 * *****************************************
 * Description ：我的待办
 * Created by cy on 2014/10/14.
 * *****************************************
 */
@ContentView(R.layout.activity_list_view)
public class TodoCenterActivity extends AbstractActivity {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    private List<DbModel> todoList;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_title_todo_center));
        registerForContextMenu(mListView);
        initData();
    }

    public void initData() {
        try {
            //合并两个表中的数据
            //请假单中待办
            String leaveAppSql = " SELECT id,userId,leaveReason title,modifiedDate,auditStatus,'" + Const.TYPE_TODO_LEAVE_APPLICATION.getValue() + "' type FROM t_leave_application WHERE auditUserId=" + SysConfig.getInstance().getUserId();
            //报销单中待办
            String expenseSql = " SELECT id,userId,itemName title,modifiedDate,auditStatus,'" + Const.TYPE_TODO_EXPENSE_ACCOUNT.getValue() + "' type FROM t_expense_account WHERE auditUserId=" + SysConfig.getInstance().getUserId();
            StringBuffer sql = new StringBuffer();
            sql.append(leaveAppSql).append(" UNION ALL ").append(expenseSql).append(" order by modifiedDate");
            todoList = DbOperationManager.getInstance().getDbModels(sql.toString());
        } catch (DbException e) {
            LogUtil.e(e);
        }
        if (adapter == null) {
            adapter = new TodoAdapter(mContext, todoList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(todoList);
            adapter.notifyDataSetChanged();
        }
    }

    @OnItemClick(R.id.mListView)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        int type = adapter.getItem(position).getInt("type");
        String dataId = adapter.getItem(position).getString("id");
        if (type == Const.TYPE_TODO_LEAVE_APPLICATION.getValue()) {
            intent = new Intent(mContext, LeaveApplicationForAuditActivity.class);
        } else if (type == Const.TYPE_TODO_EXPENSE_ACCOUNT.getValue()) {
        }
        intent.putExtra("id", dataId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sync_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:
                new DatePickUtil(mContext, "请选择开始时间", new DatePickUtil.DateSetFinished() {
                    @Override
                    public void onDateSetFinished(String pickYear, String pickMonth, String pickDay) {
                        syncData(LeaveApplication.class, pickYear + "-" + pickMonth + "-" + pickDay);
                    }
                }).showDateDialog();
                return true;
            case R.id.action_add:
                startActivity(new Intent(mContext, LeaveApplicationAddActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //添加菜单项
        menu.add(Menu.NONE, 0, 0, "删除");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(adapter.getItem(info.position).getString("title"));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int currentSelectedPosition = info.position;
        try {
            String id = adapter.getItem(currentSelectedPosition).getString("id");
            int type = adapter.getItem(currentSelectedPosition).getInt("type");
            if (type == Const.TYPE_TODO_LEAVE_APPLICATION.getValue()) {
                DbOperationManager.getInstance().deleteBean(LeaveApplication.class, id);
            } else if (type == Const.TYPE_TODO_EXPENSE_ACCOUNT.getValue()) {
                DbOperationManager.getInstance().deleteBean(ExpenseAccount.class, id);
            }
            sendRefresh();
        } catch (DbException e) {
            LogUtil.e(e);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void refreshData() {
        initData();
    }
}
