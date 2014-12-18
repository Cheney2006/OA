package com.jxd.oa.activity.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.application.OAApplication;
import com.jxd.oa.bean.ExpenseAccount;
import com.jxd.oa.bean.LeaveApplication;
import com.jxd.oa.bean.Todo;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.utils.CommonJson4List;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.ParamManager;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.exception.BaseException;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.exception.JsonException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;
import com.yftools.util.AndroidUtil;
import com.yftools.util.StorageUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * *****************************************
 * Description ：基础类
 * Created by cy on 2014/7/28.
 * *****************************************
 */
public abstract class AbstractActivity extends ActionBarActivity {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = AbstractActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ExitAppUtil.getInstance().pushActivity(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_REFRESH);
        intentFilter.addAction(Constant.ACTION_EXIT);//广播的方式退出
        registerReceiver(broadcastReceiver, intentFilter);
        getOverflowMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ExitAppUtil.getInstance().removeActivity(this);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 强制显示overflow menu 3.0以下还是不是显示使用Popup Menu代替
     */
    private void getOverflowMenu() {
        ViewConfiguration viewConfig = ViewConfiguration.get(this);
        try {
            Field overflowMenuField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (null != overflowMenuField) {
                overflowMenuField.setAccessible(true);
                overflowMenuField.setBoolean(viewConfig, false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * overflow menu 显示图标与文字
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    public void showPopup(int itemId, int menuResId, PopupMenu.OnMenuItemClickListener listener) {
        View view = findViewById(itemId);
        PopupMenu popupMenu = new PopupMenu(getSupportActionBar().getThemedContext(), view);
        if(listener!=null){
            popupMenu.setOnMenuItemClickListener(listener);
        }
        popupMenu.getMenuInflater().inflate(menuResId, popupMenu.getMenu());
        //强制显示菜单图标
        try {
            Field mpopup = popupMenu.getClass().getDeclaredField("mPopup");
            mpopup.setAccessible(true);
            MenuPopupHelper mPopup = (MenuPopupHelper) mpopup.get(popupMenu);
            mPopup.setForceShowIcon(true);
        } catch (Exception e) {

        }
        popupMenu.show();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_EXIT)) {
                finish();
            } else if (intent.getAction().equals(Constant.ACTION_REFRESH)) {
                refreshData();
            }
        }
    };

    protected void refreshData() {
    }

    public void displayToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void exitApp() {
        sendBroadcast(new Intent(Constant.ACTION_EXIT));
        DbOperationManager.getInstance().close();
    }

    protected <T> void syncData(final Class<T> cls, String startDate) {
        String name = cls.getSimpleName();
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("startDate", startDate);
        String path = name + "List.action";
        if (cls.isAssignableFrom(Todo.class)) {
            path = "getMyAgent.action";
        }
        HttpUtil.getInstance().sendInDialog(mContext, "正在同步数据...", ParamManager.parseBaseUrl(path), params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    Json json = new Json(responseInfo.result);
                    if (json.getBoolean("success")) {
                        if (cls.isAssignableFrom(Todo.class)) {//我的待办
                            Json datas = json.getJson("data");
                            for (int i = 0, len = datas.getLength(); i < len; i++) {
                                json = (Json) datas.getItem(i);
                                String beanName = json.getString("beanName");
                                if (beanName.equals("LeaveApplication")) {//beanData是多个
                                    List<LeaveApplication> leaveApplicationList = GsonUtil.getInstance().getGson().fromJson(json.getString("beanData"), new TypeToken<List<LeaveApplication>>() {
                                    }.getType());
                                    DbOperationManager.getInstance().saveOrUpdate(leaveApplicationList);
                                } else if (beanName.equals("ExpenseAccount")) {
                                    List<ExpenseAccount> expenseAccountList = GsonUtil.getInstance().getGson().fromJson(json.getString("beanData"), new TypeToken<List<ExpenseAccount>>() {
                                    }.getType());
                                    DbOperationManager.getInstance().saveOrUpdate(expenseAccountList);
                                }
                            }
                        } else {
                            CommonJson4List<T> commonJson4List = CommonJson4List.fromJson(responseInfo.result, cls);
                            DbOperationManager.getInstance().saveOrUpdate(commonJson4List.getData());
                        }
                        sendBroadcast(new Intent(Constant.ACTION_REFRESH));
                    } else {
                        displayToast(json.getString("message"));
                    }
                } catch (DbException e) {
                    LogUtil.e(e);
                    displayToast(e.getMessage());
                } catch (JsonException e) {
                    LogUtil.e(e);
                    displayToast(e.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
            }
        });
    }

    protected void sendRefresh() {
        sendBroadcast(new Intent(Constant.ACTION_REFRESH));
    }

    /**
     * @Description : 版本检测
     */
    protected void checkVersion() {
        HttpUtil.getInstance().sendInDialog(mContext, "正在检查版本信息...", ParamManager.parseBaseUrl("mobileVersion!version.action"), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result != null) {
                    try {
                        Json json = new Json(responseInfo.result);
                        if (json.getBoolean("success")) {
                            validateVersion(json);
                        } else {
                            displayToast(json.getString("message"));
                        }
                    } catch (JsonException e) {
                        LogUtil.e(e);
                        displayToast("JSON格式解析错误");
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
            }
        });
    }

    protected void validateVersion(Json data) {
        try {
            int version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            int versionServer = data.getInt("version");
            final String apkPath = data.getString("filePath");
            LogUtil.d("version=" + version + ",versionServer=" + versionServer);
            if (version < versionServer) {
                downloadConfirm(apkPath);
            } else {
                displayToast("已经为最新版本");
            }
        } catch (PackageManager.NameNotFoundException e) {
            displayToast("数据解析失败");
        }
    }

    private void downloadConfirm(final String url) {
        new JxdAlertDialog(mContext, "提示", "检查到新的版本，是否升级?", "确定", null, "取消") {
            @Override
            protected void positive() {
                downloadApk(ParamManager.parseDownUrl(url));
            }
        }.show();
    }

    private void downloadApk(String url) {
        // 更新软件
        File downloadFile = new File(StorageUtil.getDiskCacheDir(mContext), new Date().getTime() + ".apk");
        HttpUtil.getInstance().downloadInDialog(mContext, "正在下载", ProgressDialog.STYLE_HORIZONTAL, url, downloadFile.getAbsolutePath(),
                false, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        AndroidUtil.viewFile(mContext, responseInfo.result);
                        displayToast("下载成功");
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        displayToast(msg);
                    }
                });
    }

    protected Date getNowDate() {
        try {
            return OAApplication.getSysCurrentDate();
        } catch (BaseException e) {
            LogUtil.e(e);
            new JxdAlertDialog(mContext, "错误", "系统错误 : 系统时间未校准") {
                @Override
                protected void positive() {
                    super.positive();
                    finish();
                }
            }.show();
        }
        return new Date();
    }
}

