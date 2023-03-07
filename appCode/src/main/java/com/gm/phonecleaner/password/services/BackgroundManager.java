package com.gm.phonecleaner.password.services;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.gm.phonecleaner.password.receiver.LockRestarterBroadcastReceiver;

public class BackgroundManager {

    private static final int period = 15 * 1000;//15 minutes;
    private static BackgroundManager mInstance;
    private Context ctx;

    private BackgroundManager() {
    }


    public static BackgroundManager getInstance() {
        if (mInstance == null) {
            mInstance = new BackgroundManager();
            return mInstance;
        } else return mInstance;


    }

    public BackgroundManager init(Context context) {
        ctx = context;
        return this;
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        checkContext();
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void startService(Class<?> serviceClass) {
        checkContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundServices(serviceClass);
        } else
            ctx.startService(new Intent(ctx, serviceClass));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForegroundServices(Class<?> serviceClass) {
        ctx.startForegroundService(new Intent(ctx, serviceClass));
    }

    public void startAlarmManager() {
        checkContext();
        Intent intent = new Intent(ctx, LockRestarterBroadcastReceiver.class);
        intent.putExtra("type", "startlockserviceFromAM");

        int flags;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        } else
            flags = PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 95374, intent, flags);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + period, pendingIntent);

    }

    public void stopAlarmManager() {
        checkContext();
        Intent intent = new Intent(ctx, LockRestarterBroadcastReceiver.class);
        intent.putExtra("type", "startlockserviceFromAM");


        int flags;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        } else
            flags = PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 95374, intent, flags);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void checkContext() {
        if (ctx == null)
            throw new RuntimeException("Context can not be null: Initialize context first");
    }

    public void stopService(Class<?> serviceClass) {
        checkContext();
        if (isServiceRunning(LockService.class))
            ctx.stopService(new Intent(ctx, serviceClass));
    }
}
