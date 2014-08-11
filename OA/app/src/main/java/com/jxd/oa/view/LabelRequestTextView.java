package com.jxd.oa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yftools.LogUtil;

/**
 * *****************************************
 * Description ：标签textView,最后追加一个红色星标
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class LabelRequestTextView extends TextView {

    public LabelRequestTextView(Context context) {
        super(context);
        initRedStar();
    }

    public LabelRequestTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRedStar();
    }

    public LabelRequestTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initRedStar();
    }

    private void initRedStar() {
        if (!TextUtils.isEmpty(getText())) {
            String txt = getText().toString() + " *";
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(txt);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), txt.length() - 1, txt.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableStringBuilder);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
