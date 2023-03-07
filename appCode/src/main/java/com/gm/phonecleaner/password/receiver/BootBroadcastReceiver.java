package com.gm.phonecleaner.password.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.gm.phonecleaner.password.base.AppConstants;
import com.gm.phonecleaner.password.services.BackgroundManager;
import com.gm.phonecleaner.password.services.LoadAppListService;
import com.gm.phonecleaner.password.services.LockService;
import com.gm.phonecleaner.password.utils.LogUtil;
import com.gm.phonecleaner.password.utils.SpUtil;


public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(@NonNull Context context, Intent intent) {
        LogUtil.i("Boot service....");
        //TODO: pie compatable done
       // context.startService(new Intent(context, LoadAppListService.class));
        BackgroundManager.getInstance().init(context).startService(LoadAppListService.class);
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            BackgroundManager.getInstance().init(context).startService(LockService.class);
            BackgroundManager.getInstance().init(context).startAlarmManager();
        }
    }
}
