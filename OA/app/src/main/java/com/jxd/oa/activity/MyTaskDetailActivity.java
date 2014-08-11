package com.jxd.oa.activity;

import android.os.Bundle;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.yftools.ViewUtil;

/**
 * *****************************************
 * Description ：工作详情
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class MyTaskDetailActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task_detail);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("工作详情");
    }
}
