package com.jxd.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * *****************************************************
 * @Description : 自定义水平滚动类 
 * @Author : cy cy20061121@163.com
 * @Creation Date : 2013-5-26 下午10:19:12
 * *****************************************************
 */
public class MyHorizontalScrollView extends HorizontalScrollView {
    private OnComputeScrollListener listener;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (listener != null) {
            listener.computeScroll(this);
        }
    }

    public void setOnComputeScrollListener(OnComputeScrollListener onComputeScrollListener) {
        this.listener = onComputeScrollListener;
    }

    public interface OnComputeScrollListener {
        public void computeScroll(View view);
    }

}
