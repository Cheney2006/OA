package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
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
import com.yftools.view.annotation.event.OnClick;

/**
 * *****************************************
 * Description ：登录
 * Created by cy on 2014/7/28.
 * *****************************************
 */
public class LoginActivity extends AbstractActivity {

    @ViewInject(R.id.username_et)
    private EditText username_et;
    @ViewInject(R.id.password_et)
    private EditText password_et;
    @ViewInject(R.id.autoLogin_cb)
    private CheckBox autoLogin_cb;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        ViewUtil.inject(this);
        initData();
    }

    private void initData() {
        username_et.setText(SysConfig.getInstance().getUsername());
    }

    @OnClick(R.id.autoLogin_tv)
    public void autoLoginClick(View view) {
        autoLogin_cb.setChecked(!autoLogin_cb.isChecked());
    }

    @OnClick(R.id.login_btn)
    public void loginClick(View view) {
        if (checkValidate()) {
            RequestParams params = new RequestParams();
            params.addBodyParameter("userId", username);
            params.addBodyParameter("password", password);
            HttpUtil.getInstance().sendInDialog(mContext, "正在登录...", ParamManager.parseBaseUrl("login.action"), params, new RequestCallBack<Json>() {
                @Override
                public void onSuccess(ResponseInfo<Json> responseInfo) {
                    SysConfig.getInstance().setUsername(username);
                    if (autoLogin_cb.isChecked()) {//自动登录时保存密码
                        SysConfig.getInstance().setPassword(password);
                        SysConfig.getInstance().setAutoLogin(true);
                    } else {
                        SysConfig.getInstance().setPassword("");
                        SysConfig.getInstance().setAutoLogin(false);
                    }
                    String result = responseInfo.result.toString();
                    User user = GsonUtil.getInstance().getGson().fromJson(result, User.class);
                    SysConfig.getInstance().setUserId(user.getId());
                    try {
                        DbOperationManager.getInstance().save(user);
                        if (user.getDepartment() != null) {
                            DbOperationManager.getInstance().save(user.getDepartment());
                        }
                        if (user.getRole() != null) {
                            DbOperationManager.getInstance().save(user.getRole());
                        }
                        startActivity(new Intent(mContext, HomeActivity.class));
                        finish();
                    } catch (DbException e) {
                        LogUtil.e(e);
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    displayToast(msg);
                }
            });
        }
    }

    private boolean checkValidate() {
        username = username_et.getText().toString();
        if (TextUtils.isEmpty(username)) {
            displayToast("请输入用户名");
            return false;
        }
        password = password_et.getText().toString();
        if (TextUtils.isEmpty(password)) {
            displayToast("请输入密码");
            return false;
        }
        return true;
    }

}
