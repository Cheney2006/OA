package com.jxd.oa.view.timepicker;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jxd.oa.R;

import java.util.Calendar;
import java.util.Date;

public class DateTimePickUtil  extends Dialog {

    private Context context;
    private DateTimeSetFinished dateTimeSetFinished;
    private String msg;
    private WheelMain wheelMain;
    private boolean isSelectTime;
    private TextView title_tv;
    private Button confirm_btn;
    private Button cancel_btn;

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
        super(context, R.style.CustomDialog);
        this.context = context;
        this.msg = msg;
        this.isSelectTime = isSelectTime;
        this.dateTimeSetFinished = dateTimeSetFinished;
        initView(date);
    }

    private void initView(Date date) {
        setContentView(R.layout.dialog_timepicker);
        title_tv= (TextView) findViewById(R.id.title_tv);
        title_tv.setText(msg);
        View timePickerView = findViewById(R.id.timePicker_ll);
        ScreenInfo screenInfo = new ScreenInfo(context);
        wheelMain = new WheelMain(timePickerView, isSelectTime);
        wheelMain.screenHeight = screenInfo.getHeight();
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
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
                //dateTimeSetFinished.onDateTimeSetFinished(null);
            }
        });
    }

    public void showDateDialog() {
        show();
    }

    public interface DateTimeSetFinished {
        void onDateTimeSetFinished(String dateTime);
    }

}
