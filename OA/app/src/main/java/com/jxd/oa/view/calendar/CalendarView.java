package com.jxd.oa.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.MonthDisplayHelper;
import android.view.MotionEvent;
import android.view.View;

import com.yftools.util.AndroidUtil;
import com.yftools.util.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalendarView extends View {

    private static int CELL_WIDTH;
    private static int CELL_HEIGHT;
    private static int CELL_MARGIN_TOP;
    private static int CELL_MARGIN_LEFT;
    private static float CELL_TEXT_SIZE;
    private static float CELL_MIN_SIZE;

    private static final String TAG = "CalendarView";
    private final int COLOR_BLUE = 0xff0099CC;
    private final int COLOR_GRAY = 0xff8D8F8F;
    public static final int COLOR_CLICK = 0xb0C5EAF8;
    private Calendar mRightNow = null;
    private Cell mToday = null;
    private Cell[][] mCells = new Cell[6][7];
    private OnCellTouchListener mOnCellTouchListener = null;
    private MonthDisplayHelper mHelper;


    protected Paint mLinePaint;
    protected Paint mWeekTitlePaint;
    protected Paint mClickPaint;
    protected Paint mBgPaint;

    protected String[] weekTitles = {"日", "一", "二", "三", "四", "五", "六"};

    protected Rect mBgRect;

    protected Rect mWeekTitleRect;

    protected Rect mClickRect;

    protected Context mContext;

    private Map<String, Map<String, Integer>> mDateMap;


    private boolean isCellClick;//是否需要绘制点击的View

    private Calendar today;

    public interface OnCellTouchListener {
        public void onTouch(Cell cell);
    }

    public CalendarView(Context context, Date todayDate) {
        super(context);
        mContext = context;
        mRightNow = Calendar.getInstance();
        mRightNow.setTime(todayDate);
        today = Calendar.getInstance();
        today.setTime(mRightNow.getTime());
        mHelper = new MonthDisplayHelper(mRightNow.get(Calendar.YEAR), mRightNow.get(Calendar.MONTH));
        initCalendarView();
    }


    private void initCalendarView() {
        //CELL_MARGIN_LEFT = AndroidUtil.dip2px(mContext, 5);
        CELL_MARGIN_LEFT = 0;

        CELL_WIDTH = (AndroidUtil.getDisplayWidth(mContext) - CELL_MARGIN_LEFT * 2) / 7;
        CELL_HEIGHT = AndroidUtil.dip2px(mContext, 45);
        //CELL_MARGIN_TOP = CELL_HEIGHT/2+30;
        CELL_MARGIN_TOP = CELL_HEIGHT;

        CELL_TEXT_SIZE = AndroidUtil.sp2px(mContext, 18.0f);
        CELL_MIN_SIZE = AndroidUtil.sp2px(mContext, 10.0f);
        //单元格线
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setARGB(255, 0xff, 0xff, 0xff);

        //单元格背景
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(Color.parseColor("#ffffff"));
        //星期标题
        mWeekTitlePaint = new Paint();
        mWeekTitlePaint.setAntiAlias(true);
        mWeekTitlePaint.setColor(Color.LTGRAY);
        mWeekTitlePaint.setTextSize(CELL_TEXT_SIZE);
        //点击背景
        mClickPaint = new Paint();
        mClickPaint.setAntiAlias(true);
        mClickPaint.setColor(COLOR_CLICK);
        mBgRect = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_MARGIN_LEFT + CELL_WIDTH * 7, CELL_MARGIN_TOP + CELL_HEIGHT * 6);
        //mWeekTitleRect = new Rect(CELL_MARGIN_LEFT, 10, CELL_MARGIN_LEFT + CELL_WIDTH * 7, 10 + CELL_HEIGHT / 2 + 10);
        mWeekTitleRect = new Rect(CELL_MARGIN_LEFT, 0, CELL_MARGIN_LEFT + CELL_WIDTH * 7, CELL_HEIGHT);
        setBackgroundColor(Color.parseColor("#ffffffff"));
    }

    private void initCells() {
        int thisDay = 0;
        if (mHelper.getYear() == today.get(Calendar.YEAR) && mHelper.getMonth() == today.get(Calendar.MONTH)) {
            thisDay = today.get(Calendar.DAY_OF_MONTH);
        }
        Calendar sqlCalendar = Calendar.getInstance();
        int year = mHelper.getYear();
        int month = mHelper.getMonth();
        Rect Bound = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_WIDTH + CELL_MARGIN_LEFT, CELL_HEIGHT + CELL_MARGIN_TOP);
        for (int week = 0; week < mCells.length; week++) {
            int n[] = mHelper.getDigitsForRow(week);
            for (int day = 0; day < mCells[week].length; day++) {
                if (mHelper.isWithinCurrentMonth(week, day)) {
                    if (day == 0 || day == 6) {
                        mCells[week][day] = new BlueCell(n[day], new Rect(Bound), CELL_TEXT_SIZE, CELL_MIN_SIZE, month);
                    } else {
                        mCells[week][day] = new Cell(n[day], new Rect(Bound), CELL_TEXT_SIZE, CELL_MIN_SIZE, month);
                    }
                    sqlCalendar.set(year, month, mCells[week][day].getDayOfMonth());
                } else {
                    if (week == 0) {
                        mCells[week][day] = new GrayCell(n[day], new Rect(Bound), CELL_TEXT_SIZE, CELL_MIN_SIZE, month - 1);
                        sqlCalendar.set(year, month - 1, mCells[week][day].getDayOfMonth());
                    } else {
                        mCells[week][day] = new GrayCell(n[day], new Rect(Bound), CELL_TEXT_SIZE, CELL_MIN_SIZE, month + 1);
                        sqlCalendar.set(year, month + 1, mCells[week][day].getDayOfMonth());
                    }
                }
                String timeString = DateUtil.dateToString(sqlCalendar.getTime());
                if (mDateMap != null && mDateMap.containsKey(timeString)) {
                    HashMap<String, Integer> itemMap = (HashMap<String, Integer>) mDateMap.get(timeString);
                    mCells[week][day].setNum(itemMap.get("planCount"), itemMap.get("finishedCount"));
                }
                Bound.offset(CELL_WIDTH, 0); // move to next column
                // get today
                if (mCells[week][day].getDayOfMonth() == thisDay && mCells[week][day].getMonth() == mHelper.getMonth()) {
                    mToday = mCells[week][day];
                }
            }
            Bound.offset(0, CELL_HEIGHT); // move to next row and first column
            Bound.left = CELL_MARGIN_LEFT;
            Bound.right = CELL_MARGIN_LEFT + CELL_WIDTH;

        }
    }

    public int getYear() {
        return mHelper.getYear();
    }

    public int getMonth() {
        return mHelper.getMonth();
    }

    public void nextMonth() {
        mHelper.nextMonth();
    }

    public void previousMonth() {
        mHelper.previousMonth();
    }

    public void upDataCalendarView() {
        initCells();
        invalidate();
    }

    public boolean firstDay(int day) {
        return day == 1;
    }

    public boolean lastDay(int day) {
        return mHelper.getNumberOfDaysInMonth() == day;
    }

    public void goToday() {
        Calendar cal = Calendar.getInstance();
        mHelper = new MonthDisplayHelper(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        initCells();
        invalidate();
    }

    public Calendar getDate() {
        return mRightNow;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Cell clickCell = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mOnCellTouchListener != null) {
                    for (Cell[] week : mCells) {
                        for (Cell day : week) {
                            if (day.hitTest((int) event.getX(), (int) event.getY())) {
                                isCellClick = true;
                                int x = day.getBound().centerX() + AndroidUtil.dip2px(getContext(), 1);
                                int y = day.getBound().centerY() - AndroidUtil.dip2px(getContext(), 7);
                                int size = AndroidUtil.dip2px(getContext(), 16);
                                mClickRect = new Rect(x - size, y - size, x + size, y + size);
                                invalidate();
                                mOnCellTouchListener.onTouch(day);
                            }
                        }
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    public void setOnCellTouchListener(OnCellTouchListener p) {
        mOnCellTouchListener = p;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int startRow = CELL_MARGIN_TOP;
        int startColumn = CELL_MARGIN_LEFT;
        canvas.drawRect(mWeekTitleRect, mBgPaint);
        canvas.drawRect(mBgRect, mBgPaint);
        for (int i = 0; i < 6; i++) {
            canvas.drawLine(CELL_MARGIN_LEFT, startRow, CELL_MARGIN_LEFT + CELL_WIDTH * 7, startRow, mLinePaint);
            startRow = startRow + CELL_HEIGHT;
            if (i == 5)
                canvas.drawLine(CELL_MARGIN_LEFT, startRow, CELL_MARGIN_LEFT + CELL_WIDTH * 7, startRow, mLinePaint);
        }
        for (int i = 0; i < 7; i++) {
            canvas.drawLine(startColumn, CELL_MARGIN_TOP, startColumn, CELL_MARGIN_TOP + CELL_HEIGHT * 6, mLinePaint);
            startColumn = startColumn + CELL_WIDTH;
            if (i == 6)
                canvas.drawLine(startColumn, CELL_MARGIN_TOP, startColumn, CELL_MARGIN_TOP + CELL_HEIGHT * 6, mLinePaint);
        }
        if (mToday != null) {
            mToday.setDrawColor(0xffffffff);
            //mDecoration.draw(canvas);
            mToday.drawCircle(canvas);
        }
        for (Cell[] week : mCells) {
            for (Cell day : week) {
                day.draw(canvas);
            }
        }
        for (int i = 0; i < weekTitles.length; i++) {
            final float xPos = CELL_WIDTH * i + CELL_MARGIN_LEFT + ((float) CELL_WIDTH - mWeekTitlePaint.measureText(weekTitles[i])) / 2.0f;
            int dy = (int) (-mWeekTitlePaint.ascent() + mWeekTitlePaint.descent()) / 2;
            if (i == 0 || i == 6) {
                mWeekTitlePaint.setColor(COLOR_BLUE);
            } else {
                mWeekTitlePaint.setColor(COLOR_GRAY);
            }
            canvas.drawText(weekTitles[i], xPos, mWeekTitleRect.centerY() + dy - 5, mWeekTitlePaint);
        }
        if (isCellClick) {
            canvas.drawRect(mClickRect, mClickPaint);
            isCellClick = false;
        }
        drawLastLine(canvas, startRow);
    }

    private void drawLastLine(Canvas canvas, int startRow) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xffE3E4E8);
        canvas.drawLine(CELL_MARGIN_LEFT, startRow, CELL_MARGIN_LEFT + CELL_WIDTH * 7, startRow, paint);
    }

    public class GrayCell extends Cell {
        public GrayCell(int dayOfMon, Rect rect, float s, float min, int isMonth) {
            super(dayOfMon, rect, s, min, isMonth);

            mPaint.setColor(COLOR_GRAY);
        }
    }

    private class BlueCell extends Cell {
        public BlueCell(int dayOfMon, Rect rect, float s, float min, int isMonth) {
            super(dayOfMon, rect, s, min, isMonth);

            mPaint.setColor(COLOR_BLUE);
        }
    }

    public void setDateMap(Map<String, Map<String, Integer>> mDateMap) {
        this.mDateMap = mDateMap;
    }
}
