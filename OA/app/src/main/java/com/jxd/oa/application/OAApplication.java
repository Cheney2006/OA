package com.jxd.oa.application;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.jxd.oa.constants.ConfigManager;

import cn.jpush.android.api.JPushInterface;

public class OAApplication extends BaseApplication {

    @Override
    public void onCreate() {
        //这里可以通过配置文件来设置是否启用输入日志到文件
        isLogOutFile = ConfigManager.getInstance(this).isLogOutFile();
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext,百度定位时，会重新执行application整个生命周期
        SDKInitializer.initialize(this);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
