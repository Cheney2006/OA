package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.view.calendar.CalendarView;
import com.jxd.oa.view.calendar.CalendarViewViewPager;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.Date;

/**
 * *****************************************
 * Description ：今日日程
 * Created by cy on 2014/7/31.
 * *****************************************
 */
public class ScheduleActivity extends AbstractActivity implements CalendarViewViewPager.OnDateSelectListener {

    @ViewInject(R.id.calendar_view)
    private CalendarViewViewPager mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getResources().getString(R.string.txt_schedule));
        initView();
    }

    private void initView() {
        mCalendarView.setOnDateSelect(this);
        //第一次不能加载
        try {
            mCalendarView.initDataMap(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChange(CalendarView view, Date selectedDate, Date today) {
        startActivity(new Intent(mContext, ScheduleListActivity.class));
    }

    @Override
    public void onMonthChange() {
        try {
            mCalendarView.refreshView(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCalendarView.refreshView(null);
    }
}
