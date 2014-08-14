package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Email;
import com.jxd.oa.bean.EmailRecipient;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.ParamManager;
import com.jxd.oa.view.AttachmentViewView;
import com.jxd.oa.view.SelectEditView;
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
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import java.util.Date;
import java.util.List;

/**
 * *****************************************
 * Description ：邮件详情
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class EmailDetailActivity extends AbstractActivity {

    @ViewInject(R.id.title_tv)
    private TextView title_tv;
    @ViewInject(R.id.send_tv)
    private TextView send_tv;
    @ViewInject(R.id.date_tv)
    private TextView date_tv;
    @ViewInject(R.id.content_tv)
    private TextView content_tv;
    @ViewInject(R.id.attachment_label)
    private TextView attachment_label;
    @ViewInject(R.id.email_avv)
    private AttachmentViewView email_avv;
    @ViewInject(R.id.recipient_label)
    private TextView recipient_label;
    @ViewInject(R.id.recipient_sev)
    private SelectEditView recipient_sev;
    @ViewInject(R.id.recipientUp_line)
    private View recipientUp_line;
    @ViewInject(R.id.recipientDown_line)
    private View recipientDown_line;
    private Email email;
    private List<EmailRecipient> recipientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("邮件详情");
        initData();
    }

    private void initData() {
        String emailId = getIntent().getStringExtra("emailId");
        try {
            email = DbOperationManager.getInstance().getBeanById(Email.class, emailId);
            title_tv.setText(email.getTitle());
            if (email.getSendTime() != null) {
                date_tv.setText(DateUtil.dateTimeToString(email.getSendTime()));
            }
            content_tv.setText(Html.fromHtml(email.getContent()));
            if (!TextUtils.isEmpty(email.getAttachmentName()) && !TextUtils.isEmpty(email.getAttachmentSize())) {
                attachment_label.setVisibility(View.VISIBLE);
                email_avv.setVisibility(View.VISIBLE);
                email_avv.initData(email.getAttachmentName(), email.getAttachmentSize());
            } else {
                attachment_label.setVisibility(View.GONE);
                email_avv.setVisibility(View.GONE);
            }
            if (email.getFromId().equals(SysConfig.getInstance().getUserId())) {
                recipientUp_line.setVisibility(View.VISIBLE);
                recipientDown_line.setVisibility(View.VISIBLE);
                recipient_label.setVisibility(View.VISIBLE);
                recipient_sev.setVisibility(View.VISIBLE);
                recipientList = email.getEmailRecipientList().getList();
                StringBuffer sb = new StringBuffer();
                if (recipientList != null) {
                    for (EmailRecipient emailRecipient : recipientList) {
                        sb.append(emailRecipient.getToUser().getName()).append(";");
                    }
                }
                recipient_sev.setContent(sb.toString());
            } else {
                recipientUp_line.setVisibility(View.GONE);
                recipientDown_line.setVisibility(View.GONE);
                recipient_label.setVisibility(View.GONE);
                recipient_sev.setVisibility(View.GONE);
            }
            if (email.getFromUser() != null) {
                send_tv.setText(email.getFromUser().getName());
            }
            if(!email.getFromId().equals(SysConfig.getInstance().getUserId())){
                readSubmit();
            }
        } catch (DbException e) {
            LogUtil.e(e);
            displayToast("获取数据失败");
        }
    }

    private void readSubmit() {
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("id", email.getId());
        HttpUtil.getInstance().send(ParamManager.parseBaseUrl("readEmail.action"), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                //更新接收时间
                try {
                    String sql = "UPDATE t_email_recipient SET readTime='" + DateUtil.dateTimeToString(new Date()) + "' WHERE toId='" + SysConfig.getInstance().getUserId() + "' and emailId ='" + email.getId() + "'";
                    DbOperationManager.getInstance().execSql(sql);
                    sendBroadcast(new Intent(Constant.ACTION_REFRESH));
                } catch (DbException e) {
                    LogUtil.e(e);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
            }
        });
    }

    @OnClick(R.id.recipient_sev)
    public void recipientClick(View view) {
        Intent intent = new Intent(mContext, EmailRecipientActivity.class);
        intent.putExtra("recipientList", (java.io.Serializable) recipientList);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_copy, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy:
                Intent intent = getIntent();
                intent.setClass(mContext, EmailAddActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
