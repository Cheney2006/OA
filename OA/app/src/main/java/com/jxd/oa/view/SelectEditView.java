package com.jxd.oa.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jxd.oa.R;

/**
 * *****************************************
 * Description ：列表选择输入框
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class SelectEditView extends LinearLayout {
    private TextView content_tv;

    public SelectEditView(Context context) {
        super(context);
        initView();
    }

    public SelectEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setClickable(true);
        this.setBackgroundResource(R.drawable.bg_layout_white_corner);
        LayoutInflater.from(getContext()).inflate(R.layout.view_select, this);
        content_tv = (TextView) findViewById(R.id.content_tv);
    }

    public void setContent(String context) {
        if (!TextUtils.isEmpty(context)) {
            content_tv.setText(context);
            content_tv.setTag(context);
        }
    }

    public void setContent(String context, String value) {
        content_tv.setText(context);
        if (!TextUtils.isEmpty(value)) {
            content_tv.setTag(value);
        }
    }

    public void setHit(String hit) {
        content_tv.setHint(hit);
    }

    public String getContent() {
        return content_tv.getText().toString();
    }

    public Object getValue() {
        return content_tv.getTag();
    }

}
