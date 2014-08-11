package com.jxd.oa.activity;

import android.os.Bundle;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

/**
 * *****************************************
 * Description ：
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class EmailDetailActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("邮件详情");
    }
}
