package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Schedule;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.view.calendar.CalendarView;
import com.jxd.oa.view.calendar.CalendarViewViewPager;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.ui.DatePickUtil;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * *****************************************
 * Description ：今日日程
 * Created by cy on 2014/7/31.
 * *****************************************
 */
public class ScheduleActivity extends AbstractActivity implements CalendarViewViewPager.OnDateSelectListener {

    private static final int CODE_SCHEDULE_LIST = 101;
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
        mCalendarView.initDataMap(getDataMap());
    }

    private Map<String, Map<String, Integer>> getDataMap() {
        //取得数据
        Map<String, Map<String, Integer>> dataMap = new HashMap<String, Map<String, Integer>>();
        try {
            Map<String, Integer> data = new HashMap<String, Integer>();
            Date startDate = mCalendarView.getStarDate();
            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            for (int i = 0; i < 42; i++) {
                int count = (int) DbOperationManager.getInstance().count(Selector.from(Schedule.class).where("startDate", "<=", c.getTime().getTime()).and("endDate", ">=", c.getTime().getTime()));
                if (count > 0) {
                    data.put("planCount", count);
                    data.put("finishedCount", count);
                    dataMap.put(DateUtil.dateToString(c.getTime()), data);
                }
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return dataMap;
    }

    @Override
    public void onChange(CalendarView view, Date selectedDate, Date today) {
        Intent intent = new Intent(mContext, ScheduleListActivity.class);
        intent.putExtra("currentDate", selectedDate);
        startActivityForResult(intent, CODE_SCHEDULE_LIST);
    }

    @Override
    public void onMonthChange() {
        mCalendarView.refreshView(getDataMap());
    }

    @Override
    protected void refreshData() {
        mCalendarView.refreshView(getDataMap());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_SCHEDULE_LIST) {
            mCalendarView.refreshView(getDataMap());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sync_date, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:
                new DatePickUtil(mContext, "请选择开始时间", new DatePickUtil.DateSetFinished() {
                    @Override
                    public void onDateSetFinished(String pickYear, String pickMonth, String pickDay) {
                        syncData(Schedule.class, pickYear + "-" + pickMonth + "-" + pickDay);
                    }
                }).showDateDialog();
                return true;
            case R.id.action_date:
                mCalendarView.setToday();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
