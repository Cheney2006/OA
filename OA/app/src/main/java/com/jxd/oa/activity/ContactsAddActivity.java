package com.jxd.oa.activity;

import android.os.Bundle;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.yftools.ViewUtil;

/**
 * *****************************************
 * Description ：个人通讯录新增、修改、删除
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
public class ContactsAddActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_add);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("增加联系人");
    }
}
