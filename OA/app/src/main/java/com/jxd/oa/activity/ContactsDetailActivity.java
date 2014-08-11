package com.jxd.oa.activity;

import android.os.Bundle;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.yftools.ViewUtil;

/**
 * *****************************************
 * Description ：通讯录详情
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
public class ContactsDetailActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("通讯录详情");
    }
}
