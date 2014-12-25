package com.jxd.oa.application;

import android.app.Application;
import android.content.Context;
import android.os.PowerManager;

import com.jxd.oa.constants.ConfigManager;
import com.jxd.oa.constants.SysConfig;
import com.yftools.LogUtil;
import com.yftools.datetimestate.DateTimeChangeObserver;
import com.yftools.datetimestate.DateTimeStateReceiver;
import com.yftools.exception.BaseException;
import com.yftools.exception.CustomCrashHandler;
import com.yftools.log.PrintToFileLogger;
import com.yftools.netstate.NetChangeObserver;
import com.yftools.netstate.NetWorkUtil;
import com.yftools.netstate.NetworkStateReceiver;
import com.yftools.sdstate.SDChangeObserver;
import com.yftools.sdstate.SDStateReceiver;
import com.yftools.util.DateUtil;
import com.yftools.util.StorageUtil;
import com.yftools.util.ntp.NtpUtil;

import java.io.File;
import java.util.Date;

/**
 * BaseApplication
 */
public abstract class BaseApplication extends Application {
    private static final String LOG_PREFIX = "jxd_";
    protected static Context context;
    protected static String packageName;
    protected static Application application;
    protected static NetChangeObserver netChangeObserver;
    protected static SDChangeObserver sdChangeObserver;
    protected static DateTimeChangeObserver dateTimeChangeObserver;
    protected static PowerManager.WakeLock wl;

    protected boolean isLogOutFile = false;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApplication.application = this;
        context = getApplicationContext();
        LogUtil.customTagPrefix = LOG_PREFIX;
        if (isLogOutFile) {
            PrintToFileLogger.setLogFilePath(StorageUtil.getDiskCacheDir(context, "log") + File.separator + LOG_PREFIX);
            LogUtil.addLogger(new PrintToFileLogger());
        }
        LogUtil.setLogLevel(ConfigManager.getInstance(this).getLogLevel());
        packageName = getPackageName();
        //设置异常处理类
        CustomCrashHandler.getInstance().setCustomCrashHandler(this);

        BaseApplication.netChangeObserver = new NetChangeObserver() {
            @Override
            public void onConnect(NetWorkUtil.NetType type) {
                super.onConnect(type);
                LogUtil.d("网络连接开启:" + type);
            }

            @Override
            public void onDisConnect() {
                super.onDisConnect();
                LogUtil.d("网络连接关闭");
            }
        };
        NetworkStateReceiver.registerObserver(BaseApplication.netChangeObserver);
        NetworkStateReceiver.registerNetworkStateReceiver(context);

        BaseApplication.sdChangeObserver = new SDChangeObserver() {
            @Override
            public void onMountSD() {
                super.onMountSD();
                LogUtil.d("sdcard载人成功");
            }

            @Override
            public void onRemoveSD() {
                super.onRemoveSD();
                LogUtil.d("sdcard没有载入");
            }
        };
        SDStateReceiver.registerObserver(BaseApplication.sdChangeObserver);
        SDStateReceiver.registerSDStateReceiver(context);

        BaseApplication.dateTimeChangeObserver = new DateTimeChangeObserver() {
            @Override
            public void onChange() {
                LogUtil.d("日期时间发生改变...");
                startNtpTimeServer();
            }
        };
        DateTimeStateReceiver.registerObserver(BaseApplication.dateTimeChangeObserver);
        DateTimeStateReceiver.registerDateTimeStateReceiver(context);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        NetworkStateReceiver.unRegisterNetworkStateReceiver(context);
        SDStateReceiver.unRegisterSDStateReceiver(context);
        DateTimeStateReceiver.unRegisterDateTimeStateReceiver(context);
    }

    public static void startNtpTimeServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    LogUtil.d("开始时间校准......");
//                    setCpuKeepOn();
                    Date sysDate = new Date();
                    Date ntpDate = NtpUtil.getInstance().getNtpTime();
//                    releaseCpuWL();
                    if (ntpDate != null) {
                        long differTime = ntpDate.getTime() - sysDate.getTime();
                        SysConfig.getInstance().setDifferTime(differTime);
                        SysConfig.getInstance().setSyncDate(true);
                        LogUtil.d("校准时间:" + DateUtil.dateTimeToString(getSysCurrentDate()));
                    } else {
                        SysConfig.getInstance().setSyncDate(false);
                    }
                } catch (Exception e) {
                    SysConfig.getInstance().setSyncDate(false);
                    LogUtil.e("NTP时间校准失败！", e);
                }
            }
        }.start();
    }

    /**
     * 获取系统当前时间
     */
    public static Date getSysCurrentDate() throws BaseException {
        if (!SysConfig.getInstance().getSyncDate()) {
            try {
                //开始时间校准
                startNtpTimeServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new BaseException("系统时间未校准");
        } else {
            return new Date(new Date().getTime() + SysConfig.getInstance().getDifferTime());
        }
    }

    /**
     * 获取一个应用上下文
     *
     * @return Context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 当前网络是否可用
     *
     * @return false为无网络 true为网络可用
     */
    public static Boolean isNetworkAvailable() {
        return NetworkStateReceiver.isNetworkAvailable();
    }

    /**
     * 当前sdcard是否可用
     *
     * @return null为不可用 否则返回sdcard操作目录路径
     */
    public static String getSdcardAvailable() {
        return SDStateReceiver.getSdcardPath();
    }

    public static Application getApplication() {
        return BaseApplication.application;
    }

    public static String getPackName() {
        return BaseApplication.packageName;
    }

    /**
     * 保持Cpu唤醒状态
     */
    public static void setCpuKeepOn() {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyTag");
        wl.acquire();
    }

    /**
     * 释放WL
     */
    public static void releaseCpuWL() {
        if (wl != null) {
            wl.release();
            wl = null;
        }
    }
}
