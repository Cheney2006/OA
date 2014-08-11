package com.jxd.oa.view.calendar;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jxd.oa.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CalendarViewViewPager extends LinearLayout {

    private Context mContext;
    private LayoutInflater mInflater;
    private ImageView btnPrevious;
    private ImageView btnNext;
    private TextView monthView;
    private ViewPager mViewPager;
    private Date mTodayDate;
    private int mTodayPos = 12 * 5;
    private OnDateSelectListener mOnDateSelectListener;
    private MonthDisplayHelper mHelper;
    private Date starDate;
    private Date endDate;
    private MonthPagerAdapter monthPagerAdapter;
    private int currentPosition = mTodayPos;
    private Map<String, Map<String, Integer>> dataMap;
    private boolean isFirst = true;


    public CalendarViewViewPager(Context context) {
        this(context, null);
    }

    public CalendarViewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTodayDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mTodayDate);
        mHelper = new MonthDisplayHelper(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        getCalendarStartAndEndDate();
        initView();
    }

    private void getCalendarStartAndEndDate() {
        Calendar calendar = Calendar.getInstance();
        int year = mHelper.getYear();
        int month = mHelper.getMonth();
        int monthDay = mHelper.getDayAt(0, 0);
        if (monthDay != 1)
            calendar.set(year, month - 1, monthDay);
        else
            calendar.set(year, month, monthDay);
        starDate = calendar.getTime();
        calendar.set(year, month + 1, mHelper.getDayAt(5, 6));
        endDate = calendar.getTime();
    }

    public void setToday() {
        mViewPager.setCurrentItem(mTodayPos);
    }

    private void initView() {
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.view_calendar, this);
        //注册按钮事件
        btnPrevious = (ImageView) findViewById(R.id.month_previous_iv);
        btnPrevious.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
            }
        });
        btnNext = (ImageView) findViewById(R.id.next_iv);
        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
            }
        });
        monthView = (TextView) findViewById(R.id.calendar_view_monthview);
        monthView.setText(DateFormat.format("yyyy年MM月", mTodayDate));

        mViewPager = (ViewPager) findViewById(R.id.calendar_view_flipper);
        monthPagerAdapter = new MonthPagerAdapter();
        mViewPager.setAdapter(monthPagerAdapter);
        mViewPager.setCurrentItem(mTodayPos);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                isFirst = false;
                if (position < currentPosition) {
                    for (int i = 0; i < currentPosition - position; i++)
                        mHelper.previousMonth();
                    getCalendarStartAndEndDate();
                } else if (position > currentPosition) {
                    for (int i = 0; i < position - currentPosition; i++)
                        mHelper.nextMonth();
                    getCalendarStartAndEndDate();
                }
                if (mOnDateSelectListener != null) {
                    mOnDateSelectListener.onMonthChange();
                }
                currentPosition = position;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mTodayDate);
                calendar.add(Calendar.MONTH, position - mTodayPos);
                monthView.setText(DateFormat.format("yyyy年MM月", calendar.getTime()));

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }


    public class MonthPagerAdapter extends PagerAdapter {
        private View mCurrentView;

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public int getCount() {
            return mTodayPos * 2;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            final CalendarView calendarView = new CalendarView(mContext, mTodayDate);
            calendarView.setId(position);
            calendarView.setClickable(true);
            if (position < mTodayPos) {
                for (int i = 0; i < mTodayPos - position; i++)
                    calendarView.previousMonth();
            } else if (position > mTodayPos) {
                for (int i = 0; i < position - mTodayPos; i++)
                    calendarView.nextMonth();
            }
            if (dataMap != null && isFirst && position == mTodayPos) {//第一次运行时。
                calendarView.setDateMap(dataMap);
            }
            calendarView.upDataCalendarView();
            calendarView.setOnCellTouchListener(new CalendarView.OnCellTouchListener() {
                public void onTouch(Cell cell) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(calendarView.getYear(), cell.getMonth(), cell.getDayOfMonth());
                    mOnDateSelectListener.onChange(calendarView, calendar.getTime(), mTodayDate);
                }
            });
            ((ViewPager) view).addView(calendarView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            return calendarView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }


    }

    public void upDataView() {
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    public void refreshView(Map<String, Map<String, Integer>> dataMap) {
        CalendarView calendarView = ((CalendarView) mViewPager.findViewById(mViewPager.getCurrentItem()));
        calendarView.setDateMap(dataMap);
        calendarView.upDataCalendarView();
    }

    public void initDataMap(Map<String, Map<String, Integer>> dataMap) {
        this.dataMap = dataMap;
    }

    public void setOnDateSelect(OnDateSelectListener onDateSelectListener) {
        mOnDateSelectListener = onDateSelectListener;
    }

    //日期选择事件处理
    public interface OnDateSelectListener {

        public void onChange(CalendarView view, Date selectedDate, Date today);

        public void onMonthChange();

    }


    public Date getStarDate() {
        return starDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
