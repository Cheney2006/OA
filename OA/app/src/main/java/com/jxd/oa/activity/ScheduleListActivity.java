package com.jxd.oa.activity;

import android.os.Bundle;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.yftools.ViewUtil;

/**
 * *****************************************
 * Description ：日程列表
 * Created by cy on 2014/7/31.
 * *****************************************
 */
public class ScheduleListActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);
        ViewUtil.inject(this);
    }
}
