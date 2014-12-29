package com.jxd.oa.view.timepicker;

import android.text.TextUtils;
import android.view.View;

import com.jxd.oa.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class WheelMain {
    // 添加大小月月份并将其转换为list,方便之后的判断
    private static String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    private static String[] months_little = {"4", "6", "9", "11"};
    private static int START_YEAR = 1980, END_YEAR = 2100;
    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;
    public int screenHeight;
    private boolean hasSelectTime;

    private Date minDate;//显示最小日期
    private Date maxDate;//显示最大日期
    private String minTime;//最小时间
    private String maxTime;//最大时间
    private int minYear, maxYear;
    private int minMonth, maxMonth;
    private int minDay, maxDay;
    private int minHour, maxHour;
    private int minMinute, maxMinute;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public static int getSTART_YEAR() {
        return START_YEAR;
    }

    public static void setSTART_YEAR(int sTART_YEAR) {
        START_YEAR = sTART_YEAR;
    }

    public static int getEND_YEAR() {
        return END_YEAR;
    }

    public static void setEND_YEAR(int eND_YEAR) {
        END_YEAR = eND_YEAR;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public WheelMain(View view) {
        super();
        this.view = view;
        hasSelectTime = false;
        setView(view);
    }

    public WheelMain(View view, boolean hasSelectTime) {
        super();
        this.view = view;
        this.hasSelectTime = hasSelectTime;
        setView(view);
    }

    public void initDateTimePicker(int year, int month, int day) {
        this.initDateTimePicker(year, month, day, 0, 0);
    }

    /**
     * 弹出日期时间选择器
     */
    public void initDateTimePicker(int year, int month, int day, int hour, int minute) {
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH);
//		int day = calendar.get(Calendar.DATE);

        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        //设置年数据
        Calendar calendar = Calendar.getInstance();
        //取得设置的最小日期 年
        minYear = START_YEAR;
        if (minDate != null) {
            calendar.setTime(minDate);
            minYear = calendar.get(Calendar.YEAR);
            if (minYear < START_YEAR) {
                minYear = START_YEAR;
            }
        }
        //取得设置的最大日期 年
        maxYear = END_YEAR;
        if (maxDate != null) {
            calendar.setTime(maxDate);
            maxYear = calendar.get(Calendar.YEAR);
            if (maxYear > END_YEAR) {
                maxYear = END_YEAR;
            }
        }
        wv_year.setAdapter(new NumericWheelAdapter(minYear, maxYear));
        if (minDate == null && maxDate == null) {
            wv_year.setCyclic(true);// 可循环滚动
        }
        //if (!hasSelectTime) {
        wv_year.setLabel("年");// 添加文字
        //}
        if (year < minYear) {
            year = minYear;
        } else if (year > maxYear) {
            year = maxYear;
        }
        wv_year.setCurrentItem(year - minYear);// 初始化时显示的数据
        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        //设置月数据

        wv_month.setAdapter(getMonthAdapter(year));
        if (minDate == null && maxDate == null) {
            wv_month.setCyclic(true);
        }
        wv_month.setLabel("月");
        if (month < minMonth) {
            month = minMonth;
        } else if (month > maxMonth) {
            month = maxMonth;
        }
        wv_month.setCurrentItem(month - minMonth);

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        if (minDate == null && maxDate == null) {
            wv_day.setCyclic(true);
        }
        wv_day.setAdapter(getDayAdapter(year, month));
        wv_day.setLabel("日");
        if (day < minDay) {
            day = minDay;
        } else if (day > maxDay) {
            day = maxDay;
        }
        wv_day.setCurrentItem(day - minDay);

        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_mins = (WheelView) view.findViewById(R.id.min);
        if (hasSelectTime) {
            wv_hours.setVisibility(View.VISIBLE);
            wv_mins.setVisibility(View.VISIBLE);
            minHour = 0;
            maxHour = 23;
            if (!TextUtils.isEmpty(minTime)) {
                String[] times = minTime.split(":");
                if (times != null && times.length > 0) {
                    minHour = Integer.valueOf(times[0]);
                }
            }
            if (!TextUtils.isEmpty(maxTime)) {
                String[] times = maxTime.split(":");
                if (times != null && times.length > 0) {
                    maxHour = Integer.valueOf(times[0]);
                }
            }
            wv_hours.setAdapter(new NumericWheelAdapter(minHour, maxHour));
            if (TextUtils.isEmpty(minTime) && TextUtils.isEmpty(maxTime)) {
                wv_hours.setCyclic(true);// 可循环滚动
            }
            wv_hours.setLabel("时");// 添加文字
            if (hour < minHour) {
                hour = minHour;
            } else if (hour > maxHour) {
                hour = maxHour;
            }
            wv_hours.setCurrentItem(hour - minHour);

            wv_mins.setAdapter(getMinuteAdapter(hour));
            if (TextUtils.isEmpty(minTime) && TextUtils.isEmpty(maxTime)) {
                wv_mins.setCyclic(true);// 可循环滚动
            }
            wv_mins.setLabel("分");// 添加文字
            if (minute < minMinute) {
                minute = minMinute;
            } else if (minute > maxMinute) {
                minute = maxMinute;
            }
            wv_mins.setCurrentItem(minute - minMinute);
        } else {
            wv_hours.setVisibility(View.GONE);
            wv_mins.setVisibility(View.GONE);
        }

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                //int year_num = newValue + START_YEAR;
                int year_num = Integer.valueOf(wv_year.getAdapter().getItem(newValue));
                //设置月
                int currentItem = wv_month.getCurrentItem();
                NumericWheelAdapter monthAdapter = getMonthAdapter(year_num);
                wv_month.setAdapter(monthAdapter);
                if (currentItem > monthAdapter.getItemsCount() - 1) {
                    wv_month.setCurrentItem(monthAdapter.getItemsCount() - 1);
                }
                int month_num = Integer.valueOf(wv_month.getAdapter().getItem(wv_month.getCurrentItem()));
                // 判断大小月及是否闰年,用来确定"日"的数据
                currentItem = wv_day.getCurrentItem();
                NumericWheelAdapter dayMonth = getDayAdapter(year_num, month_num);
                wv_day.setAdapter(dayMonth);
                if (currentItem > dayMonth.getItemsCount() - 1) {//如果从1月31转到2月时，2月31不存在的情况
                    wv_day.setCurrentItem(dayMonth.getItemsCount() - 1);
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = Integer.valueOf(wv_year.getAdapter().getItem(wv_year.getCurrentItem()));
                int month_num = Integer.valueOf(wv_month.getAdapter().getItem(wv_month.getCurrentItem()));
                // 判断大小月及是否闰年,用来确定"日"的数据
                int currentItem = wv_day.getCurrentItem();
                NumericWheelAdapter dayMonth = getDayAdapter(year_num, month_num);
                wv_day.setAdapter(dayMonth);
                if (currentItem > dayMonth.getItemsCount() - 1) {//从1月31转到2月时，2月31不存在
                    wv_day.setCurrentItem(dayMonth.getItemsCount() - 1);
                }
            }
        };

        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        // 添加"小时"监听
        OnWheelChangedListener wheelListener_hour = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int hour_num = Integer.valueOf(wv_hours.getAdapter().getItem(wv_hours.getCurrentItem()));
                // 判断时大小,用来确定"分"的数据
                int currentItem = wv_mins.getCurrentItem();
                NumericWheelAdapter minuteAdapter = getMinuteAdapter(hour_num);
                wv_mins.setAdapter(minuteAdapter);
                if (currentItem > minuteAdapter.getItemsCount() - 1) {
                    wv_mins.setCurrentItem(minuteAdapter.getItemsCount() - 1);
                }
            }
        };
        wv_hours.addChangingListener(wheelListener_hour);
        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 0;
        if (hasSelectTime)
            textSize = (screenHeight / 100) * 3;
        else
            textSize = (screenHeight / 100) * 4;
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_mins.TEXT_SIZE = textSize;

    }

    private NumericWheelAdapter getMonthAdapter(int year) {
        Calendar calendar = Calendar.getInstance();
        minMonth = 1;
        maxMonth = 12;
        //String currentYear = wv_year.getAdapter().getItem(wv_year.getCurrentItem());
        //如果显示年是最小日期的年，则要根据最小日期来生成月数据
        if (year == minYear && minDate != null) {
            calendar.setTime(minDate);
            minMonth = calendar.get(Calendar.MONTH) + 1;
        }
        //如果显示年是最大日期的年，则要根据最大日期来生成月数据
        if (year == maxYear && maxDate != null) {
            calendar.setTime(maxDate);
            maxMonth = calendar.get(Calendar.MONTH) + 1;
        }

        return new NumericWheelAdapter(minMonth, maxMonth);
    }

    private NumericWheelAdapter getDayAdapter(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        // 判断大小月及是否闰年,用来确定"日"的数据
        minDay = 1;
        maxDay = 30;
        if (Arrays.asList(months_big).contains(String.valueOf(month))) {
            maxDay = 31;
        } else if (Arrays.asList(months_little).contains(String.valueOf(month))) {
            maxDay = 30;
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                maxDay = 29;
            } else {
                maxDay = 28;
            }
        }
        //如果显示年月是最小日期的年月，则要根据最小日期来生成日数据
        if (year == minYear && month == minMonth && minDate != null) {
            calendar.setTime(minDate);
            minDay = calendar.get(Calendar.DAY_OF_MONTH);
        }
        //如果显示的年月是最大日期的年月，则要根据最大日期来生成日数据
        if (year == maxYear && month == maxMonth && maxDate != null) {
            calendar.setTime(maxDate);
            maxDay = calendar.get(Calendar.DAY_OF_MONTH);
        }
        return new NumericWheelAdapter(minDay, maxDay);
    }

    private NumericWheelAdapter getMinuteAdapter(int hour) {
        //如果时是最小时间时，则根据最小时间来加载数据
        minMinute = 0;
        maxMinute = 59;

        if (!TextUtils.isEmpty(minTime) && hour == minHour) {
            String[] times = minTime.split(":");
            if (times != null && times.length > 1) {
                minMinute = Integer.valueOf(times[1]);
            }
        }
        //如果时是最大时间时，则根据最大时间来加载数据
        if (!TextUtils.isEmpty(maxTime) && hour == maxHour) {
            String[] times = maxTime.split(":");
            if (times != null && times.length > 1) {
                maxMinute = Integer.valueOf(times[1]);
            }
        }
        return new NumericWheelAdapter(minMinute, maxMinute);
    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();
        String year = wv_year.getAdapter().getItem(wv_year.getCurrentItem());
        int month = Integer.valueOf(wv_month.getAdapter().getItem(wv_month.getCurrentItem()));
        int day = Integer.valueOf(wv_day.getAdapter().getItem(wv_day.getCurrentItem()));
        if (!hasSelectTime)
            sb.append(year).append("-")
                    .append(month < 10 ? "0" + month : month).append("-")
                    .append(day < 10 ? "0" + day : day);
        else {
            int hour = Integer.valueOf(wv_hours.getAdapter().getItem(wv_hours.getCurrentItem()));
            int minute = Integer.valueOf(wv_mins.getAdapter().getItem(wv_mins.getCurrentItem()));
            sb.append(year).append("-")
                    .append(month < 10 ? "0" + month : month).append("-")
                    .append(day < 10 ? "0" + day : day).append(" ")
                    .append(hour < 10 ? "0" + hour : hour).append(":")
                    .append(minute < 10 ? "0" + minute : minute);
        }
        return sb.toString();
    }
}
