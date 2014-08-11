package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.EmailAdapter;
import com.jxd.oa.bean.Email;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.ui.DatePickUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：邮件
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class EmailActivity extends AbstractActivity {

    private static final int CODE_EMAIL_ADD = 101;
    private boolean isInBox = true;
    @ViewInject(R.id.mListView)
    private ListView mListView;
    private EmailAdapter adapter;
    private List<Email> emailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ViewUtil.inject(this);
        initTopBar();
        registerForContextMenu(mListView);
        initData();
    }

    private void initData() {
        //取收件箱中数据
        try {
            emailList = DbOperationManager.getInstance().getBeans(Selector.from(Email.class).where("formId", "!=", SysConfig.getInstance().getUserId()));
        } catch (DbException e) {
            LogUtil.e(e);
        }
        fillList();
    }

    private void fillList() {
        if (adapter == null) {
            adapter = new EmailAdapter(mContext, emailList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setObjects(emailList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isInBox) {
            getMenuInflater().inflate(R.menu.menu_sync_read, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_sync_add, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivityForResult(new Intent(mContext, EmailAddActivity.class), CODE_EMAIL_ADD);
                return true;
            case R.id.action_sync:
                new DatePickUtil(mContext, "请选择开始时间", new DatePickUtil.DateSetFinished() {
                    @Override
                    public void onDateSetFinished(String pickYear, String pickMonth, String pickDay) {
                        syncData(Email.class, pickYear + "-" + pickMonth + "-" + pickDay);
                    }
                }).showDateDialog();
                return true;
            case R.id.action_read:
                startActivityForResult(new Intent(mContext, EmailDetailActivity.class), CODE_EMAIL_ADD);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initTopBar() {
        ActionBar actionBar = getSupportActionBar();
        //这里可以设置下拉的文本跟下拉的item文本不一样
        /*android.R.layout.simple_spinner_dropdown_item*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.menu_email));
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.menu_email));
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.custom_spinner, getResources().getStringArray(R.array.menu_email));
        //adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ActionBar.OnNavigationListener callback = new MyOnNavigationListener();
        actionBar.setListNavigationCallbacks(adapter, callback);//new ActionMenuAdapter(this,new String[]{"全部通讯录","分类通讯录"})
    }

    private class MyOnNavigationListener implements ActionBar.OnNavigationListener {
        @Override
        public boolean onNavigationItemSelected(int itemPosition, long itemId) {
            switch (itemPosition) {
                case 0://收件箱
                    isInBox = true;
                    try {
                        emailList = DbOperationManager.getInstance().getBeans(Selector.from(Email.class).where("formId", "!=", SysConfig.getInstance().getUserId()));
                    } catch (DbException e) {
                        LogUtil.e(e);
                    }
                    fillList();
                    supportInvalidateOptionsMenu();
                    break;
                case 1://发件箱
                    isInBox = false;
                    try {
                        emailList = DbOperationManager.getInstance().getBeans(Selector.from(Email.class).where("formId", "=", SysConfig.getInstance().getUserId()));
                    } catch (DbException e) {
                        LogUtil.e(e);
                    }
                    fillList();
                    supportInvalidateOptionsMenu();
                    break;
                case 2://草稿箱
                    isInBox = false;
                    supportInvalidateOptionsMenu();
                    break;
            }
            return true;
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
            }
        }.show();
        return super.onContextItemSelected(item);
    }
}
