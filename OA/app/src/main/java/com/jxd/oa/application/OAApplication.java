package com.jxd.oa.application;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.yftools.DbUtil;

public class OAApplication extends BaseApplication {

    private static OAApplication mInstance = null;
    public boolean m_bKeyRight = true;
    private DbUtil dbUtil;

    private static Context context;

    @Override
    public void onCreate() {
        //这里可以通过配置文件来设置是否启用输入日志到文件
        isLogOutFile = true;
        super.onCreate();
        context = this.getApplicationContext();
        mInstance = this;
        //initDb();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }

    private void initDb() {
        if (dbUtil == null) {
            dbUtil = DbUtil.create(context);
            dbUtil.configDebug(true);
            dbUtil.configAllowTransaction(true);
        }
    }

    @Override
    public void onTerminate() {
//        if(dbUtil!=null){
//            dbUtil.close();
//        }
        super.onTerminate();
    }

    public static OAApplication getInstance() {
        return mInstance;
    }


    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
//    public static class MyGeneralListener implements MKGeneralListener {
//
//        @Override
//        public void onGetNetworkState(int iError) {
//            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
//                Toast.makeText(context, "您的网络出错啦！", Toast.LENGTH_LONG).show();
//            } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
//                Toast.makeText(context, "输入正确的检索条件！", Toast.LENGTH_LONG).show();
//            }
//        }
//
//        @Override
//        public void onGetPermissionState(int iError) {
//            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
//                //授权Key错误：
//                Toast.makeText(context, "请输入正确的授权Key！", Toast.LENGTH_LONG).show();
//                OAApplication.getInstance().m_bKeyRight = false;
//            }
//        }
//    }
}
