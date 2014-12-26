package com.jxd.oa.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 通知管理类
 *
 * @author cy
 */
public class NotificationUtil {


    private static class SingletonHolder {
        static final NotificationUtil INSTANCE = new NotificationUtil();
    }

    public static NotificationUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static void showNotification(Context context, int id, int icon, String title, String text, Class<?> clazz) {
        NotificationManager mNotifMan = null;
        Notification mNotification = null;
        if (mNotifMan == null) {
            mNotifMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotification = new Notification();
            mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
            mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
            mNotification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
            mNotification.defaults = Notification.DEFAULT_ALL;//无声音跟振动
            mNotification.icon = icon;
            mNotification.when = System.currentTimeMillis();
        }
        Intent intent = new Intent();
        if (clazz != null) {
            intent = new Intent(context, clazz);
        }
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        mNotification.setLatestEventInfo(context, title, text, pi);
        mNotifMan.notify(id, mNotification);
    }

    public static void cancelNotification(Context context, int id) {
        NotificationManager mNotifMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifMan.cancel(id);
    }
}
