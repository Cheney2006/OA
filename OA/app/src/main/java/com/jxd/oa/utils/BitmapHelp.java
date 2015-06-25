package com.jxd.oa.utils;

import android.content.Context;

import com.yftools.BitmapUtil;

/**
 * *****************************************
 * Description ：
 * Created by cy on 2015/6/11.
 * *****************************************
 */
public class BitmapHelp {
    private static BitmapUtil bitmapUtil;

    /**
     * BitmapUtil不是单例的 根据需要重载多个获取实例的方法
     *
     * @param appContext application context
     * @return
     */
    public static BitmapUtil getBitmapUtil(Context appContext) {
        if (bitmapUtil == null) {
            bitmapUtil = new BitmapUtil(appContext);
        }
        return bitmapUtil;
    }
}
