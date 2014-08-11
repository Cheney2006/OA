package com.jxd.oa.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Email;
import com.jxd.oa.bean.User;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.view.AttachmentViewView;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.DbException;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;

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
    @ViewInject(R.id.email_avv)
    private AttachmentViewView email_avv;
    private Email email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("邮件详情");
        email = (Email) getIntent().getSerializableExtra("email");
        initData();
    }

    private void initData() {
        title_tv.setText(email.getTitle());
        try {
            User user = DbOperationManager.getInstance().getBeanById(User.class, email.getFromId());
            if (user != null) {
                send_tv.setText(user.getName());
            }
        } catch (DbException e) {
            LogUtil.e(e);
        }
        if (email.getSendTime() != null) {
            date_tv.setText(DateUtil.dateTimeToString(email.getSendTime()));
        }
        content_tv.setText(email.getContent());
        if (!TextUtils.isEmpty(email.getAttachmentName()) && !TextUtils.isEmpty(email.getAttachmentSize())) {
            email_avv.initData(email.getAttachmentName(), email.getAttachmentSize());
        }
    }
}
