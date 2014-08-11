package com.jxd.oa.activity;

import android.os.Bundle;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.yftools.ViewUtil;

/**
 * *****************************************
 * Description ：通知公告
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
public class NoticeActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("通知公告");
    }

}
