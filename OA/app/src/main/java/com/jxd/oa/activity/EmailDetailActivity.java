package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Email;
import com.jxd.oa.bean.EmailRecipient;
import com.jxd.oa.bean.User;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.view.AttachmentViewView;
import com.jxd.oa.view.SelectEditView;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.DbException;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

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
            content_tv.setText(email.getContent());
            if (!TextUtils.isEmpty(email.getAttachmentName()) && !TextUtils.isEmpty(email.getAttachmentSize())) {
                attachment_label.setVisibility(View.VISIBLE);
                email_avv.setVisibility(View.VISIBLE);
                email_avv.initData(email.getAttachmentName(), email.getAttachmentSize());
            } else {
                attachment_label.setVisibility(View.GONE);
                email_avv.setVisibility(View.GONE);
            }
            if (email.getFromId().equals(SysConfig.getInstance().getUserId())) {
                recipient_label.setVisibility(View.VISIBLE);
                recipient_sev.setVisibility(View.VISIBLE);
                recipientList = email.getEmailRecipients().getList();
                StringBuffer sb = new StringBuffer();
                if (recipientList != null) {
                    for (EmailRecipient emailRecipient : recipientList) {
                        sb.append(emailRecipient.getToUser().getName()).append(";");
                    }
                }
                recipient_sev.setContent(sb.toString());
            } else {
                recipient_label.setVisibility(View.GONE);
                recipient_sev.setVisibility(View.GONE);
            }
            if (email.getFromUser() != null) {
                send_tv.setText(email.getFromUser().getName());
            }
        } catch (DbException e) {
            LogUtil.e(e);
            displayToast("获取数据失败");
        }
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
