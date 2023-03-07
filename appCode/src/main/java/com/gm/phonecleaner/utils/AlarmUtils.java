package com.gm.phonecleaner.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.AlarmManagerCompat;

import com.gm.phonecleaner.rec.AlarmRec;

public class AlarmUtils {

    public static final String ACTION_AUTOSTART_ALARM = "com.app.action.alarmmanager";
    public static final String ALARM_PHONE_BOOOST = "alarm phone boost";
    public static final String ALARM_PHONE_CPU_COOLER = "alarm cpu cooler";
    public static final String ALARM_PHONE_BATTERY_SAVE = "alarm battery save";
    public static final String ALARM_PHONE_JUNK_FILE = "alarm junk file";
    public static final String ACTION_REPEAT_SERVICE = "action_repeat_service";
    public static final int TIME_REPREAT_SERVICE = 1000;
    public static final int REPREAT_SERVICE_CODE = 1001;

    public static final int ALARM_REQ_PHONE_BOOST = 123;
    public static final int ALARM_REQ_CPU_COOLER = 124;

    public static final long TIME_PHONE_BOOST = 30 * 60 * 1000;
    public static final long TIME_PHONE_BOOST_CLICK = 2 * 60 * 60 * 1000;

    public static final long TIME_CPU_COOLER = 30 * 60 * 1000;
    public static final long TIME_CPU_COOLER_CLICK = 2 * 60 * 60 * 1000;

    public static final long TIME_BATTERY_SAVE = 30 * 60 * 1000;
    public static final long TIME_BATTERY_SAVE_CLICK = 2 * 60 * 60 * 1000;

    public static final int TIME_JUNK_FILE_FIRST_START = 30;

    public static final int BATTERY_HOT = 40;
    public static final int BATTERY_LOW = 20;
    public static final int RAM_USE = 70;

    public static void cancel(Context context, String mAction) {
        Intent intent = new Intent(context, AlarmRec.class);
        intent.setAction(ACTION_AUTOSTART_ALARM);


        int flags;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        } else
            flags = PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent sender = PendingIntent.getBroadcast(context, getRequestCode(mAction), intent, flags);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public static void setAlarm(Context ctx, String mAction, long mTime) {
        Intent intent = new Intent(ctx, AlarmRec.class);
        intent.setAction(ACTION_AUTOSTART_ALARM);
        intent.putExtra(mAction, Boolean.TRUE);
        int flags;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            flags = PendingIntent.FLAG_UPDATE_CURRENT  | PendingIntent.FLAG_IMMUTABLE;
        } else
            flags = PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent sender = PendingIntent.getBroadcast(ctx, getRequestCode(mAction), intent, flags);
        AlarmManager alarm = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(sender);
        long when = System.currentTimeMillis() + mTime;
        AlarmManagerCompat.setExactAndAllowWhileIdle(alarm, AlarmManager.RTC_WAKEUP, when, sender);
    }

    public static int getRequestCode(String action) {
        switch (action) {
            case ACTION_REPEAT_SERVICE:
                return REPREAT_SERVICE_CODE;

            case ALARM_PHONE_BOOOST:
                return ALARM_REQ_PHONE_BOOST;
            case ALARM_PHONE_CPU_COOLER:
                return ALARM_REQ_CPU_COOLER;
        }
        return 0;
    }
}
