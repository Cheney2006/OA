package com.jxd.oa.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

/**
 * *****************************************
 * Description ：考勤签到
 * Created by cy on 2014/8/12.
 * *****************************************
 */
public class SignActivity extends AbstractActivity {

    @ViewInject(R.id.mListView)
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_sign_title));
    }

    @OnClick(R.id.signIn_btn)
    public void signInClick(View view){

    }

    @OnClick(R.id.signOut_btn)
    public void signOutClick(View view){

    }
}
