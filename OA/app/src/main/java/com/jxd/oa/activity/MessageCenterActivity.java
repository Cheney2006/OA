package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Message;
import com.jxd.oa.constants.Const;
import com.jxd.oa.utils.DbOperationManager;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

/**
 * *****************************************
 * Description ：消息中心
 * Created by cy on 2014/9/18.
 * *****************************************
 */
@ContentView(R.layout.activity_message_center)
public class MessageCenterActivity extends AbstractActivity {

    @ViewInject(R.id.workNum_tv)
    private TextView workNum_tv;
    @ViewInject(R.id.operationRemindNum_tv)
    private TextView operationRemindNum_tv;
    @ViewInject(R.id.tipsNum_tv)
    private TextView tipsNum_tv;
    @ViewInject(R.id.baseNum_tv)
    private TextView baseNum_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_message_center));
        initData();
    }

    private void initData() {
        try {
            long num = DbOperationManager.getInstance().count(Selector.from(Message.class).where("type", "=", Const.TYPE_MESSAGE_WORK.getValue()).and("isRead","=",false));
            if (num > 0) {
                workNum_tv.setVisibility(View.VISIBLE);
                workNum_tv.setText(num + "");
            } else {
                workNum_tv.setVisibility(View.GONE);
            }
            num = DbOperationManager.getInstance().count(Selector.from(Message.class).where("type", "=", Const.TYPE_MESSAGE_OPERATION_REMIND.getValue()).and("isRead","=",false));
            if (num > 0) {
                operationRemindNum_tv.setVisibility(View.VISIBLE);
                operationRemindNum_tv.setText(num + "");
            } else {
                operationRemindNum_tv.setVisibility(View.GONE);
            }
            num = DbOperationManager.getInstance().count(Selector.from(Message.class).where("type", "=", Const.TYPE_MESSAGE_TIPS.getValue()).and("isRead","=",false));
            if (num > 0) {
                tipsNum_tv.setVisibility(View.VISIBLE);
                tipsNum_tv.setText(num + "");
            } else {
                tipsNum_tv.setVisibility(View.GONE);
            }
            num = DbOperationManager.getInstance().count(Selector.from(Message.class).where("type", "=", Const.TYPE_MESSAGE_BASE.getValue()).and("isRead","=",false));
            if (num > 0) {
                baseNum_tv.setVisibility(View.VISIBLE);
                baseNum_tv.setText(num + "");
            } else {
                baseNum_tv.setVisibility(View.GONE);
            }
        } catch (DbException e) {
            LogUtil.e(e);
        }
    }

    @OnClick(value = {R.id.work_ll, R.id.operationRemind_ll, R.id.tips_ll, R.id.base_ll})
    public void typeClick(View view) {
        Intent intent = new Intent(mContext, MessageListActivity.class);
        int type = 0;
        switch (view.getId()) {
            case R.id.work_ll:
                type = (Integer) Const.TYPE_MESSAGE_WORK.getValue();
                break;
            case R.id.operationRemind_ll:
                type = (Integer) Const.TYPE_MESSAGE_OPERATION_REMIND.getValue();
                break;
            case R.id.tips_ll:
                type = (Integer) Const.TYPE_MESSAGE_TIPS.getValue();
                break;
            case R.id.base_ll:
                type = (Integer) Const.TYPE_MESSAGE_BASE.getValue();
                break;
        }
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @Override
    protected void refreshData() {
        initData();
    }
}
