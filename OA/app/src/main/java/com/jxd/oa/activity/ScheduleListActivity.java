package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.ScheduleAdapter;
import com.jxd.oa.bean.Schedule;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.view.weekcalendar.WeekCalendarView;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnItemClick;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * *****************************************
 * Description ：日程列表
 * Created by cy on 2014/7/31.
 * *****************************************
 */
public class ScheduleListActivity extends AbstractActivity implements WeekCalendarView.OnDateSelectedListener, WeekCalendarView.OnWeekChangeListener {

    private static final int CODE_SCHEDULE_ADD = 1011;
    private static final int CODE_SCHEDULE_DETAIL = 1012;
    @ViewInject(R.id.weekCalendar_view)
    private WeekCalendarView weekCalendarView;
    @ViewInject(R.id.mListView)
    private ListView mListView;
    private ScheduleAdapter adapter;
    private List<Schedule> scheduleList;
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);
        ViewUtil.inject(this);
        currentDate = (Date) getIntent().getSerializableExtra("currentDate");
        initView();
        initData();
    }

    private void initView() {
        weekCalendarView.setOnDateChangeListener(this);
        weekCalendarView.setOnWeekChangeListener(this);
        weekCalendarView.initDate(currentDate);
        onDateChange(DateUtil.getYear(currentDate), DateUtil.getMonth(currentDate), DateUtil.getDay(currentDate));
        registerForContextMenu(mListView);
    }

    private void initData() {
        try {
            //取得当前时间
//            String time = DateUtil.dateToString("HH:mm:ss", new Date());
//            String dayTime = DateUtil.dateToString(currentDate) + " " + time;
//            currentDate = DateUtil.stringToDateTime(dayTime);

            //scheduleList = DbOperationManager.getInstance().getBeans(Selector.from(Schedule.class).where("date(startDate)", "<=", DateUtil.dateToString(currentDate)).or("date(endDate)", ">=", DateUtil.dateToString(currentDate)));
            scheduleList = DbOperationManager.getInstance().getBeans(Selector.from(Schedule.class).where("startDate", "<=", currentDate.getTime()).and("endDate", ">=", currentDate.getTime()));
        } catch (DbException e) {
            LogUtil.e(e);
        }
        fillList();
    }

    private void fillList() {
        if (adapter == null) {
            adapter = new ScheduleAdapter(mContext, scheduleList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(scheduleList);
            adapter.notifyDataSetChanged();
        }
    }

    @OnItemClick(R.id.mListView)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, ScheduleDetailActivity.class);
        intent.putExtra("schedule", adapter.getItem(position));
        startActivityForResult(intent, CODE_SCHEDULE_DETAIL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivityForResult(new Intent(mContext, ScheduleAddActivity.class), CODE_SCHEDULE_ADD);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == CODE_SCHEDULE_ADD || requestCode == CODE_SCHEDULE_DETAIL) {
            initData();
        }
    }

    @Override
    public void onDateChange(int year, int month, int day) {
        //取得当前时间
        String time = DateUtil.dateToString("HH:mm:ss", new Date());
        String currentDateStr = year + "-" + month + "-" + day + " " + time;
        LogUtil.d("currentDateStr=" + currentDateStr);
        currentDate = DateUtil.stringToDateTime(currentDateStr);
        getSupportActionBar().setTitle(year + "年" + month + "月");
        initData();
    }

    @Override
    public void onWeekChange(Date weekStartDate, Date weekEndDate) {
        LogUtil.d("weekStartDate=" + DateUtil.dateTimeToString(weekStartDate));
        weekCalendarView.refreshView(getDataMap(weekStartDate));
    }

    private Map<String, Map<String, Integer>> getDataMap(Date weekStartDate) {
        //取得数据
        Map<String, Map<String, Integer>> dataMap = new HashMap<String, Map<String, Integer>>();
        try {
            Map<String, Integer> data = new HashMap<String, Integer>();
            Calendar c = Calendar.getInstance();
            c.setTime(weekStartDate);
            for (int i = 0; i < 7; i++) {
                //int count = (int) DbOperationManager.getInstance().count(Selector.from(Schedule.class).where("date(startDate)", "<=", DateUtil.dateToString(c.getTime())).and("date(endDate)", ">=", DateUtil.dateToString(c.getTime())));
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
        LogUtil.d("dataMap=" + dataMap);
        return dataMap;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //添加菜单项
        menu.add(Menu.NONE, 0, 0, "删除");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(adapter.getItem(info.position).getTitle());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int currentSelectedPosition = info.position;
        //删除
        new JxdAlertDialog(this, getString(R.string.txt_tips), "确定删除？", getString(R.string.txt_confirm), getString(R.string.txt_cancel)) {
            @Override
            protected void positive() {
                try {
                    DbOperationManager.getInstance().deleteBean(adapter.getItem(currentSelectedPosition));
                    initData();
                } catch (DbException e) {
                    LogUtil.e(e);
                }
            }
        }.show();
        return super.onContextItemSelected(item);
    }
}
