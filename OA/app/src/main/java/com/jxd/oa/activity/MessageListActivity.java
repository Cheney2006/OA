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
import com.jxd.oa.adapter.MessageAdapter;
import com.jxd.oa.bean.Email;
import com.jxd.oa.bean.Message;
import com.jxd.oa.bean.base.AbstractBean;
import com.jxd.oa.constants.Const;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.utils.DbOperationManager;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnItemClick;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * *****************************************
 * Description ：消息列表
 * Created by cy on 2014/9/23.
 * *****************************************
 */
@ContentView(R.layout.activity_list_view)
public class MessageListActivity extends AbstractActivity {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    private int type;
    private MessageAdapter adapter;
    private List<Message> messageList;
    public static final Map<String, Class> typeClassMap = new HashMap<String, Class>();

    static {
        typeClassMap.put("Email", EmailDetailActivity.class);//邮件
        typeClassMap.put("Address", AddressCollectActivity.class);//位置采集
        typeClassMap.put("Cloud", CloudActivity.class);//企业云
        typeClassMap.put("Task", MyTaskDetailActivity.class);//我的工作
        typeClassMap.put("Schedule", ScheduleDetailActivity.class);//今日日程
        typeClassMap.put("Notice", NoticeDetailActivity.class);//通知公告
        typeClassMap.put("Contact", ContactDetailActivity.class);//通讯录
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        type = getIntent().getIntExtra("type", 0);
        getSupportActionBar().setTitle(Const.getName("TYPE_MESSAGE_", type));
        registerForContextMenu(mListView);
        initData();
    }

    private void initData() {
        try {
            messageList = DbOperationManager.getInstance().getBeans(Selector.from(Message.class).where("type", "=", type));
        } catch (DbException e) {
            LogUtil.e(e);
        }
        if (adapter == null) {
            adapter = new MessageAdapter(mContext, messageList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(messageList);
            adapter.notifyDataSetChanged();
        }
    }

    @OnItemClick(R.id.mListView)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {
        Message message = adapter.getItem(position);
        read(message);
        startActivity(message);
    }

    private void read(Message message) {
        message.setRead(true);
        try {
            DbOperationManager.getInstance().saveOrUpdate(message);
            sendRefresh();
        } catch (DbException e) {
            LogUtil.e(e);
        }
    }

    private void startActivity(Message message) {
        try {
            String beanName = message.getBeanName();
            String clazzName = "com.jxd.oa.bean." + beanName;
            Class clazz = Class.forName(clazzName).newInstance().getClass();
            AbstractBean data = (AbstractBean) DbOperationManager.getInstance().getBeanById(clazz, message.getBeanId());
            if (data != null) {
                Intent intent = new Intent(mContext, typeClassMap.get(beanName));
                if (intent != null && (message.getOperation().equals(Constant.OPERATION_ADD) || message.getOperation().equals(Constant.OPERATION_EDIT))) {
                    if (beanName.equals("Email")) {
                        Email email = (Email) data;
                        intent.putExtra("emailId", email.getId());
                    } else {
                        String paramName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
                        intent.putExtra(paramName, data);
                    }
                    startActivity(intent);
                }
            } else {
                displayToast("数据不存在");
            }
        } catch (DbException e) {
            LogUtil.e(e);
        } catch (ClassNotFoundException e) {
            LogUtil.e(e);
        } catch (InstantiationException e) {
            LogUtil.e(e);
        } catch (IllegalAccessException e) {
            LogUtil.e(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete_read, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                try {
                    DbOperationManager.getInstance().deleteBeans(messageList);
                    sendRefresh();
                } catch (DbException e) {
                    LogUtil.e(e);
                }
                return true;
            case R.id.action_read:
                try {
                    DbOperationManager.getInstance().execSql("update t_message set isRead=1 where type=" + type + " and isRead=0");
                    sendRefresh();
                } catch (DbException e) {
                    LogUtil.e(e);
                }
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
        try {
            DbOperationManager.getInstance().deleteBean(adapter.getItem(currentSelectedPosition));
            sendBroadcast(new Intent(Constant.ACTION_REFRESH));//要刷新未读数
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
