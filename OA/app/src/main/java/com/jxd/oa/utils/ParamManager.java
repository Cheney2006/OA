package com.jxd.oa.utils;


import com.jxd.oa.application.OAApplication;
import com.jxd.oa.constants.ConfigManager;
import com.jxd.oa.constants.SysConfig;
import com.yftools.http.RequestParams;

/**
 * *****************************************************
 *
 * @Description : 参数设置
 * @Author : cy cy20061121@163.com
 * @Creation Date : 2013-6-19 下午7:22:42
 * *****************************************************
 */

public class ParamManager {
    // 固定下载的资源路径，这里可以设置网络上的地址
    public static String parseDownUrl(String appPath) {
        String urlStr = ConfigManager.getInstance(OAApplication.getContext()).getWebUrl();
        if (appPath.startsWith("/")) {
            appPath = appPath.substring(1);
        }
        if (urlStr.endsWith("/")) {
            urlStr = urlStr + appPath;
        } else {
            urlStr = urlStr + "/" + appPath;
        }
        return urlStr;
    }

    public static String parseBaseUrl(String path) {
        String urlStr = ConfigManager.getInstance(OAApplication.getContext()).getWebUrl();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (urlStr.endsWith("/")) {
            urlStr = urlStr + path;
        } else {
            urlStr = urlStr + "/" + path;
        }
        return urlStr;
    }

    public static RequestParams setDefaultParams() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", SysConfig.getInstance().getUsername());
        params.addBodyParameter("password", SysConfig.getInstance().getPassword());
        return params;
    }
}
