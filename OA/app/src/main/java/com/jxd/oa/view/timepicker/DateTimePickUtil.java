package com.jxd.oa.view.timepicker;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jxd.oa.R;

import java.util.Calendar;
import java.util.Date;

public class DateTimePickUtil extends Dialog {

    private Context context;
    private DateTimeSetFinished dateTimeSetFinished;
    private String msg;
    private WheelMain wheelMain;
    private boolean isSelectTime;
    private TextView title_tv;
    private Button confirm_btn;
    private Button cancel_btn;
    private Date nowDate;
    private Date minDate;
    private Date maxDate;
    private String minTime;
    private String maxTime;

    public DateTimePickUtil(Context context, String msg, DateTimeSetFinished dateTimeSetFinished) {
        this(context, msg, null, false, dateTimeSetFinished);
    }

    public DateTimePickUtil(Context context, String msg, boolean isSelectTime, DateTimeSetFinished dateTimeSetFinished) {
        this(context, msg, null, isSelectTime, dateTimeSetFinished);
    }

    public DateTimePickUtil(Context context, String msg, Date date, DateTimeSetFinished dateTimeSetFinished) {
        this(context, msg, date, false, dateTimeSetFinished);
    }

    public DateTimePickUtil(Context context, String msg, Date date, boolean isSelectTime, DateTimeSetFinished dateTimeSetFinished) {
        this(context, msg, date, null, null, isSelectTime, dateTimeSetFinished);
    }

    public DateTimePickUtil(Context context, String msg, Date date, Date minDate, Date maxDate, DateTimeSetFinished dateTimeSetFinished) {
        this(context, msg, date, minDate, maxDate, false, dateTimeSetFinished);
    }

    public DateTimePickUtil(Context context, String msg, Date date, Date minDate, Date maxDate, boolean isSelectTime, DateTimeSetFinished dateTimeSetFinished) {
        //super(context, Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ? R.style.DateTimeDialog : 0);//在4.0不能设置样式，不然在只显示年月日时不能全屏
        //super(context);
        super(context, R.style.DateTimeDialog);
        this.context = context;
        this.msg = msg;
        this.isSelectTime = isSelectTime;
        this.dateTimeSetFinished = dateTimeSetFinished;
        this.nowDate = date;
        this.minDate = minDate;
        this.maxDate = maxDate;
        //initView();
    }

    public DateTimePickUtil initView() {
        setContentView(R.layout.dialog_timepicker);
        title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText(msg);
        ScreenInfo screenInfo = new ScreenInfo(context);

        View timePickerView = findViewById(R.id.timePicker_ll);

        wheelMain = new WheelMain(timePickerView, isSelectTime);
        wheelMain.setMinDate(minDate);
        wheelMain.setMaxDate(maxDate);
        wheelMain.setMinTime(minTime);
        wheelMain.setMaxTime(maxTime);
        wheelMain.screenHeight = screenInfo.getHeight();
        Calendar c = Calendar.getInstance();
        if (nowDate != null) {
            c.setTime(nowDate);
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (isSelectTime) {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            wheelMain.initDateTimePicker(year, month, day, hour, minute);
        } else {
            wheelMain.initDateTimePicker(year, month, day);
        }
        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                dateTimeSetFinished.onDateTimeSetFinished(wheelMain.getTime());
            }
        });
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                dateTimeSetFinished.onDateTimeSetFinished(null);
            }
        });
        //将对话框的大小按屏幕大小的百分比设置（因为当只显示日期时，不能全屏）
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (screenInfo.getWidth() * 0.95); // 宽度设置为全屏
        dialogWindow.setAttributes(p);

        return this;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    public void showDateDialog() {
        show();
    }

    public interface DateTimeSetFinished {
        void onDateTimeSetFinished(String dateTime);
    }

}
