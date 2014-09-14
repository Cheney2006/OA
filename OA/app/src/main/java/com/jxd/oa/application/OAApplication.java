package com.jxd.oa.application;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

public class OAApplication extends BaseApplication {

    private static OAApplication mInstance = null;
    private static Context context;

    @Override
    public void onCreate() {
        //这里可以通过配置文件来设置是否启用输入日志到文件
        isLogOutFile = true;
        super.onCreate();
        context = this.getApplicationContext();
        mInstance = this;
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext,百度定位时，会重新执行application整个生命周期
        SDKInitializer.initialize(this);
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static OAApplication getInstance() {
        return mInstance;
    }


}
