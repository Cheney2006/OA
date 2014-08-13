package com.jxd.oa.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.EmailRecipientAdapter;
import com.jxd.oa.bean.EmailRecipient;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：邮件接收人查看情况
 * Created by cy on 2014/8/12.
 * *****************************************
 */
public class EmailRecipientActivity extends AbstractActivity {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    private EmailRecipientAdapter adapter;
    private List<EmailRecipient> emailRecipientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_title_email_recipient));
        emailRecipientList = (List<EmailRecipient>) getIntent().getSerializableExtra("recipientList");
        initData();
    }

    public void initData() {
        if (adapter == null) {
            adapter = new EmailRecipientAdapter(mContext, emailRecipientList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(emailRecipientList);
            adapter.notifyDataSetChanged();
        }
    }


}
