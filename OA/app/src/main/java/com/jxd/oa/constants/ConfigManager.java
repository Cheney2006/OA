package com.jxd.oa.constants;

import android.content.Context;

import com.yftools.LogUtil;
import com.yftools.config.IConfig;
import com.yftools.config.PropertiesConfig;

/**
 * 手机配置项
 */
public class ConfigManager {
    private static ConfigManager instance;

    private IConfig iConfig;

    public ConfigManager(Context context) {
        iConfig = PropertiesConfig.getPropertiesConfig(context, "config.properties");
    }

    public synchronized static ConfigManager getInstance(Context context) {
        if (instance == null) {
            instance = new ConfigManager(context);
        }
        return instance;
    }

    public boolean isLogOutFile() {
        return iConfig.getBoolean("log_out_file", Boolean.FALSE);
    }

    public String getWebUrl() {
        return iConfig.getString("web_url", "");
    }

    public int getLogLevel() {
        int level = LogUtil.INFO;
        String logLevel = iConfig.getString("log_level", null);
        if (logLevel.toUpperCase().equals("DEBUG")) {
            level = LogUtil.DEBUG;
        }
        if (logLevel.toUpperCase().equals("ERROR")) {
            level = LogUtil.ERROR;
        }
        return level;
    }

    public boolean isDbDebug() {
        return iConfig.getBoolean("db_debug", Boolean.FALSE);
    }

}