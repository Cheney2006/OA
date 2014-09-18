package com.jxd.oa.activity;

import android.os.Bundle;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ContentView;

/**
 * *****************************************
 * Description ：消息中心
 * Created by cy on 2014/9/18.
 * *****************************************
 */
@ContentView(R.layout.activity_message_center)
public class MessageCenterActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_message_center));
    }
}
