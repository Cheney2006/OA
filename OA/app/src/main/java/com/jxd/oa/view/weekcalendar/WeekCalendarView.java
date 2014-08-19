package com.jxd.oa.view.weekcalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.jxd.oa.R;
import com.yftools.LogUtil;
import com.yftools.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * *****************************************
 * Description ：周日历
 * Created by cy on 2014/7/11.
 * *****************************************
 */
public class WeekCalendarView extends LinearLayout implements GestureDetector.OnGestureListener {

    private ViewFlipper viewFlipper = null;
    private GridView gridView = null;
    private GestureDetector gestureDetector = null;
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private int week_c = 0;
    private int week_num = 0;
    private String currentDate = "";
    private static int jumpWeek = 0;
    private static int jumpMonth = 0;
    private static int jumpYear = 0;
    private DateAdapter dateAdapter;
    private int daysOfMonth = 0; // 某月的天数
    private int dayOfWeek = 0; // 具体某一天是星期几
    private int weeksOfMonth = 0;
    private SpecialCalendar sc = null;
    private boolean isLeapyear = false; // 是否为闰年
    private int selectPostion = 0;
    private String dayNumbers[] = new String[7];
    private int currentYear;
    private int currentMonth;
    private int currentWeek;
    private int currentDay;
    private int currentNum;
    private boolean isStart;// 是否是交接的月初
    private LayoutInflater mInflater;
    private OnDateSelectedListener onDateSelectedListener;
    private OnWeekChangeListener OnWeekChangeListener;

    public WeekCalendarView(Context context) {
        super(context);
        initView();
    }

    public WeekCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.view_week_calendar, this);
        gestureDetector = new GestureDetector(getContext(), this);
        viewFlipper = (ViewFlipper) findViewById(R.id.flipper1);
        addGridView();
        viewFlipper.addView(gridView, 0);
        //initDate(null);
    }

    public void initDate(Date mDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        if (mDate == null) {
            mDate = new Date();
        }
        currentDate = sdf.format(mDate);
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
        currentYear = year_c;
        currentMonth = month_c;
        currentDay = day_c;
        sc = new SpecialCalendar();
        getCalendar(year_c, month_c);
        week_num = getWeeksOfMonth();
        currentNum = week_num;
        if (dayOfWeek == 7) {
            week_c = day_c / 7 + 1;
        } else {
            if (day_c <= (7 - dayOfWeek)) {
                week_c = 1;
            } else {
                if ((day_c - (7 - dayOfWeek)) % 7 == 0) {
                    week_c = (day_c - (7 - dayOfWeek)) / 7 + 1;
                } else {
                    week_c = (day_c - (7 - dayOfWeek)) / 7 + 2;
                }
            }
        }
        currentWeek = week_c;
        getCurrent();
        dateAdapter = new DateAdapter(getContext(), currentYear,
                currentMonth, currentWeek, currentNum, selectPostion,
                currentWeek == 1 ? true : false);
        dayNumbers = dateAdapter.getDayNumbers();
        selectPostion = dateAdapter.getDatePosition(mDate);
        gridView.setAdapter(dateAdapter);
        gridView.setSelection(selectPostion);
        setDateMap();
    }


    /**
     * 判断某年某月所有的星期数
     *
     * @param year
     * @param month
     */
    public int getWeeksOfMonth(int year, int month) {
        // 先判断某月的第一天为星期几
        int preMonthRelax = 0;
        int dayFirst = getWhichDayOfWeek(year, month);
        int days = sc.getDaysOfMonth(sc.isLeapYear(year), month);
        if (dayFirst != 7) {
            preMonthRelax = dayFirst;
        }
        if ((days + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (days + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (days + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;

    }

    /**
     * 判断某年某月的第一天为星期几
     *
     * @param year
     * @param month
     * @return
     */
    public int getWhichDayOfWeek(int year, int month) {
        return sc.getWeekdayOfMonth(year, month);

    }

    /**
     * @param year
     * @param month
     */
    public int getLastDayOfWeek(int year, int month) {
        return sc.getWeekDayOfLastMonth(year, month,
                sc.getDaysOfMonth(isLeapyear, month));
    }

    public void getCalendar(int year, int month) {
        isLeapyear = sc.isLeapYear(year); // 是否为闰年
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
        dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
    }

    public int getWeeksOfMonth() {
        // getCalendar(year, month);
        int preMonthRelax = 0;
        if (dayOfWeek != 7) {
            preMonthRelax = dayOfWeek;
        }
        if ((daysOfMonth + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;
    }

    private void addGridView() {
        LayoutParams params = new LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);//固定高度能显示
        gridView = new GridView(getContext());
        gridView.setNumColumns(7);
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPostion = position;
                dateAdapter.setSeclection(position);
                dateAdapter.notifyDataSetChanged();
                refreshDateData(position);
            }
        });
        gridView.setLayoutParams(params);
    }

    private void refreshDateData(int position) {
        onDateSelectedListener.onDateChange(dateAdapter.getCurrentYear(selectPostion), dateAdapter.getCurrentMonth(selectPostion), Integer.parseInt(dayNumbers[position]));
    }

    public void refreshView(Map<String, Map<String, Integer>> dataMap) {
        dateAdapter.setDataMap(dataMap);
        dateAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * 重新计算当前的年月
     */
    public void getCurrent() {
        if (currentWeek > currentNum) {
            if (currentMonth + 1 <= 12) {
                currentMonth++;
            } else {
                currentMonth = 1;
                currentYear++;
            }
            currentWeek = 1;
            currentNum = getWeeksOfMonth(currentYear, currentMonth);
        } else if (currentWeek == currentNum) {
            if (getLastDayOfWeek(currentYear, currentMonth) == 6) {
            } else {
                if (currentMonth + 1 <= 12) {
                    currentMonth++;
                } else {
                    currentMonth = 1;
                    currentYear++;
                }
                currentWeek = 1;
                currentNum = getWeeksOfMonth(currentYear, currentMonth);
            }

        } else if (currentWeek < 1) {
            if (currentMonth - 1 >= 1) {
                currentMonth--;
            } else {
                currentMonth = 12;
                currentYear--;
            }
            currentNum = getWeeksOfMonth(currentYear, currentMonth);
            currentWeek = currentNum - 1;
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        int gvFlag = 0;
        if (e1.getX() - e2.getX() > 80) {
            // 向左滑
            addGridView();
            currentWeek++;
            getCurrent();
            dateAdapter = new DateAdapter(getContext(), currentYear, currentMonth, currentWeek, currentNum, selectPostion, currentWeek == 1 ? true : false);
            dayNumbers = dateAdapter.getDayNumbers();

            gridView.setAdapter(dateAdapter);
            setDateMap();
            refreshDateData(selectPostion);
            gvFlag++;
            viewFlipper.addView(gridView, gvFlag);
            dateAdapter.setSeclection(selectPostion);
            this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in));
            this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_out));
            this.viewFlipper.showNext();
            viewFlipper.removeViewAt(0);
            return true;

        } else if (e1.getX() - e2.getX() < -80) {
            addGridView();
            currentWeek--;
            getCurrent();
            dateAdapter = new DateAdapter(getContext(), currentYear,
                    currentMonth, currentWeek, currentNum, selectPostion,
                    currentWeek == 1 ? true : false);
            dayNumbers = dateAdapter.getDayNumbers();
            gridView.setAdapter(dateAdapter);
            setDateMap();
            refreshDateData(selectPostion);
            gvFlag++;
            viewFlipper.addView(gridView, gvFlag);
            dateAdapter.setSeclection(selectPostion);
            this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_in));
            this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_out));
            this.viewFlipper.showPrevious();
            viewFlipper.removeViewAt(0);
            return true;
            // }
        }
        return false;
    }

    private void setDateMap() {
        Date startDate = DateUtil.stringToDate(dateAdapter.getCurrentYear(0) + "-" + dateAdapter.getCurrentMonth(0) + "-" + dayNumbers[0]);
        Date endDate = DateUtil.stringToDate(dateAdapter.getCurrentYear(6) + "-" + dateAdapter.getCurrentMonth(6) + "-" + dayNumbers[6]);
        OnWeekChangeListener.onWeekChange(startDate, endDate);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.gestureDetector.onTouchEvent(event);
    }

    public void setOnDateChangeListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public interface OnDateSelectedListener {
        public void onDateChange(int year, int month, int day);
    }

    public void setOnWeekChangeListener(WeekCalendarView.OnWeekChangeListener onWeekChangeListener) {
        OnWeekChangeListener = onWeekChangeListener;
    }

    public interface OnWeekChangeListener {
        public void onWeekChange(Date weekStartDate, Date weekEndDate);
    }

}
