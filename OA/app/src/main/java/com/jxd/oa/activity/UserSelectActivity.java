package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.UserSelectAdapter;
import com.jxd.oa.bean.User;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnItemClick;

import java.util.HashMap;
import java.util.List;

/**
 * *****************************************
 * Description ：选择用户
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class UserSelectActivity extends AbstractActivity {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    private UserSelectAdapter adapter;
    private List<User> userList;
    private HashMap<String, User> selectedMap;
    private boolean isMulti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("选择用户");
        selectedMap = (HashMap<String, User>) getIntent().getSerializableExtra("selectedData");
        isMulti = getIntent().getBooleanExtra("isMulti", true);
        initData();
    }

    @OnItemClick(R.id.mListView)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.select(position);
    }


    private void initData() {
        try {
            //不包括自己
            userList = DbOperationManager.getInstance().getBeans(Selector.from(User.class).where("id", "!=", SysConfig.getInstance().getUserId()));
            fillList();
        } catch (DbException e) {
            LogUtil.e(e);
        }
    }

    public void fillList() {
        if (adapter == null) {
            adapter = new UserSelectAdapter(mContext, userList);
            if (selectedMap != null) {
                adapter.setSelectedMap(selectedMap);
            }
            adapter.setMulti(isMulti);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(userList);
            adapter.setSelectedMap(selectedMap);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                Intent intent = new Intent();
                intent.putExtra("selectedData", adapter.getSelectedMap());
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
