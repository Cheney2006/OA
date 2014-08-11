package com.jxd.oa.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jxd.oa.R;
import com.yftools.util.AndroidUtil;

import java.util.TooManyListenersException;

/**
 * *****************************************
 * Description ：打电话发短信的view
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
public class PhoneView extends LinearLayout {
    private RelativeLayout group_rl;
    private TextView phone_tv;
    private LinearLayout callPhone_ll;
    private LinearLayout sendMsg_ll;
    private boolean isExpanded;
    private LinearLayout operation_ll;

    public PhoneView(Context context) {
        super(context);
        initView();
    }

    public PhoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        this.setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.view_phone, this);
        group_rl = (RelativeLayout) findViewById(R.id.group_rl);
        group_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    operation_ll.setVisibility(GONE);
                } else {
                    operation_ll.setVisibility(VISIBLE);
                }
                isExpanded = !isExpanded;
            }
        });
        phone_tv = (TextView) findViewById(R.id.phone_tv);
        operation_ll = (LinearLayout) findViewById(R.id.operation_ll);
        callPhone_ll = (LinearLayout) findViewById(R.id.callPhone_ll);
        callPhone_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(phone_tv.getText())) {
                    AndroidUtil.callPhone(getContext(), phone_tv.getText().toString());
                } else {
                    Toast.makeText(getContext(), "号码为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sendMsg_ll = (LinearLayout) findViewById(R.id.sendMsg_ll);
        sendMsg_ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(phone_tv.getText())) {
                    AndroidUtil.sendMessage(getContext(), phone_tv.getText().toString());
                } else {
                    Toast.makeText(getContext(), "号码为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initPhone(String phone) {
        phone_tv.setText(phone);
    }
}
