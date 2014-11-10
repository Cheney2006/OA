package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.application.OAApplication;
import com.jxd.oa.bean.User;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.ParamManager;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;
import com.yftools.view.annotation.ViewInject;

import java.util.Date;


/**
 * *****************************************
 * Description ：欢迎界面
 * Created by cy on 2014/7/28.
 * *****************************************
 */
public class WelcomeActivity extends AbstractActivity {

    public static final int DELAY_MILLIS = 2000;
    @ViewInject(R.id.loading_tv)
    private TextView loading_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开始时间校准
        OAApplication.startNtpTimeServer();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ViewUtil.inject(this);
        getSupportActionBar().hide();
        loading_tv.setText(getResources().getText(R.string.txt_is_start));
        initData();
    }

    private void initData() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(SysConfig.getInstance().getAutoLogin()&&!"".equals(SysConfig.getInstance().getUsername())&&!"".equals(SysConfig.getInstance().getPassword())){
                    validateLogin();
                }else{
                    goLogin();
                }
            }
        }, DELAY_MILLIS);
    }

    private void goLogin() {
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }

    private void validateLogin() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", SysConfig.getInstance().getUsername());
        params.addBodyParameter("password", SysConfig.getInstance().getPassword());
        HttpUtil.getInstance().send( ParamManager.parseBaseUrl("login.action"), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                String result = responseInfo.result.toString();
                User user = GsonUtil.getInstance().getGson().fromJson(result, User.class);
                //设置服务器时间时间
                SysConfig.getInstance().setSyncDate(true);
                SysConfig.getInstance().setDifferTime(new Date().getTime()-user.loginTime.getTime());
                try {
                    DbOperationManager.getInstance().saveOrUpdate(user);
                    if (user.getDepartment() != null) {
                        DbOperationManager.getInstance().saveOrUpdate(user.getDepartment());
                    }
                    if (user.getRole() != null) {
                        DbOperationManager.getInstance().saveOrUpdate(user.getRole());
                    }
                    goHome();
                } catch (DbException e) {
                    LogUtil.e(e);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
                goLogin();
            }
        });
    }

    private void goHome() {
        startActivity(new Intent(mContext, HomeActivity.class));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            loading_tv.setText(getResources().getText(R.string.txt_is_exit));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
