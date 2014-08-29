package com.jxd.oa.utils;


import com.jxd.oa.constants.Constant;
import com.jxd.oa.constants.SysConfig;
import com.yftools.LogUtil;
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
        String urlstr = "";
        if (appPath.startsWith("/")) {
            appPath = appPath.substring(1);
        }
        if (Constant.BASE_URL.endsWith("/")) {
            urlstr = Constant.BASE_URL + appPath;
        } else {
            urlstr = Constant.BASE_URL + "/" + appPath;
        }
        return urlstr;
    }

    public static String parseBaseUrl(String path) {
        String urlstr;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (Constant.BASE_URL.endsWith("/")) {
            urlstr = Constant.BASE_URL + path;
        } else {
            urlstr = Constant.BASE_URL + "/" + path;
        }
        return urlstr;
    }

    public static RequestParams setDefaultParams() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", SysConfig.getInstance().getUsername());
        params.addBodyParameter("password", SysConfig.getInstance().getPassword());
        return params;
    }
}
