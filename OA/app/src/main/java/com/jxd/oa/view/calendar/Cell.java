package com.jxd.oa.view.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jxd.oa.application.OAApplication;
import com.yftools.util.AndroidUtil;

public class Cell {
    private static final String TAG = "Cell";
    protected Rect mBound = null;
    protected int mDayOfMonth = 1;    // from 1 to 31
    protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            | Paint.ANTI_ALIAS_FLAG);
    int dx, dy;
    int minDxAll;
    int minDxFinish;
    protected int mAllPlanNum = -1;
    protected int mFinishPlanNum = -1;

    protected Paint mFinishPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG| Paint.ANTI_ALIAS_FLAG);

    protected Paint mAllPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG| Paint.ANTI_ALIAS_FLAG);

    private int month;
    private int default_color = 0xff474747;
    private int circle_color = 0xff0099cc;
    private int circle_red_color = 0xffd86969;

    public Cell(int dayOfMon, Rect rect, float textSize, float minSize, int isMonth) {
        mDayOfMonth = dayOfMon;
        mBound = rect;
        mPaint.setTextSize(textSize);
        mPaint.setColor(default_color);
        mFinishPaint.setTextSize(minSize);
        mFinishPaint.setARGB(255, 17, 0x8d, 0);
        mAllPaint.setTextSize(minSize);
        mAllPaint.setColor(Color.RED);
        dx = (int) mPaint.measureText(String.valueOf(mDayOfMonth)) / 2;
        //ascent：是baseline之上至字符最高处的距离
        //descent：是baseline之下至字符最低处的距离
        dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
        month = isMonth;
    }

    public void setDrawColor(int color) {
        mPaint.setColor(color);
        // mPaint.setARGB(0xb0,0x0C,0xBF,0x47);
    }


    public void setNum(int allPlanNum, int finishNum) {
        mAllPlanNum = allPlanNum;
        mFinishPlanNum = finishNum;
        minDxAll = (int) mAllPaint.measureText(String.valueOf(mAllPlanNum)) / 2;
        minDxFinish = (int) mFinishPaint.measureText(String.valueOf(mFinishPlanNum)) / 2;

    }

    public void drawCircle(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(circle_color);// 设置红色
        p.setAntiAlias(true);
       // KeerLogger.d(this,"dy="+dy);
        canvas.drawCircle(mBound.centerX()+dip2px(1), mBound.centerY() - dip2px(7), dip2px(16), p);
    }

    public static int dip2px(float dipValue) {
        final float scale = AndroidUtil.getDensity(OAApplication.getContext());
        return (int) (dipValue * scale + 0.5f);
    }

    public void drawPoint(Canvas canvas, int color) {
        Paint p = new Paint();
        p.setColor(color);// 设置红色
        p.setAntiAlias(true);
        canvas.drawCircle(mBound.centerX(), mBound.centerY() + dy + dip2px(7), dip2px(3), p);
    }

    protected void draw(Canvas canvas) {
        canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, mBound.centerY() + dy - dip2px(10), mPaint);
        if (mAllPlanNum != -1) {
            if (mFinishPlanNum == mAllPlanNum) {
                drawPoint(canvas, circle_color);
            } else {
                drawPoint(canvas, circle_red_color);
            }
            // canvas.drawText(String.valueOf(mFinishPlanNum), mBound.centerX() + ((mBound.right - mBound.centerX()) / 2) - minDxFinish, mBound.centerY() + dy + 15, mFinishPaint);
            //  canvas.drawText(String.valueOf(mAllPlanNum), mBound.left + ((mBound.right - mBound.centerX()) / 2) - minDxAll, mBound.centerY() + dy + 15, mAllPaint);
        }

    }

    public int getDayOfMonth() {
        return mDayOfMonth;
    }

    public boolean hitTest(int x, int y) {
        return mBound.contains(x, y);
    }

    public Rect getBound() {
        return mBound;
    }

    public String toString() {
        return String.valueOf(mDayOfMonth) + "(" + mBound.toString() + ")";
    }

    public int getMonth() {
        return month;
    }
}

