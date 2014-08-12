package com.jxd.oa.activity;

import android.os.Bundle;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.yftools.ViewUtil;

/**
 * *****************************************
 * Description ：考勤签到
 * Created by cy on 2014/8/12.
 * *****************************************
 */
public class SignActivity extends AbstractActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_sign_title));
    }
}
