package com.gm.phonecleaner.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.gm.phonecleaner.PhoneCleanerApp;
import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.rec.AlarmRec;
import com.gm.phonecleaner.rec.BatteryStatusRec;
import com.gm.phonecleaner.rec.PackageRec;
import com.gm.phonecleaner.ui.antivirus.ScanAppInstallActivity;
import com.gm.phonecleaner.ui.antivirus.ScanAppUninstallActivity;
import com.gm.phonecleaner.ui.cleanNotification.NotificationCleanActivity;
import com.gm.phonecleaner.ui.junkfile.JunkFileActivity;
import com.gm.phonecleaner.ui.main.MainActivity;
import com.gm.phonecleaner.ui.phoneboost.PhoneBoostActivity;
import com.gm.phonecleaner.ui.smartCharger.SmartChargerBoostActivity;
import com.gm.phonecleaner.utils.AlarmUtils;
import com.gm.phonecleaner.utils.Config;
import com.gm.phonecleaner.utils.PreferenceUtils;
import com.gm.phonecleaner.utils.Toolbox;

import java.util.Random;

public class ServiceManager extends Service {

    private static ServiceManager instance;
    private BatteryStatusRec mBatteryStatusRec;
    private PackageRec mPackageRecerver;
    private AlarmRec mAlarmRec;
    private NotificationManager mNotificationManager;
    private boolean restartService = true;


    public void setRestartService(boolean restartService) {
        this.restartService = restartService;
    }

    public static ServiceManager getInstance() {
        return instance;
    }

    private static void setInstance(ServiceManager instance) {
        ServiceManager.instance = instance;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*restart arlamManager*/
        if (PreferenceUtils.getTimeAlarmPhoneBoost() != 0) {
            long time = new Random().nextInt(30) * 60 * 1000;
            PreferenceUtils.setTimeAlarmPhoneBoost(time);
            AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_BOOOST, time);
        }
        if (PreferenceUtils.getTimeAlarmCpuCooler() != 0) {
            long time = new Random().nextInt(30) * 60 * 1000;
            PreferenceUtils.setTimeAlarmCpuCooler(time);
            AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_CPU_COOLER, time);
        }
        if (PreferenceUtils.getTimeAlarmBatterySave() != 0) {
            long time = new Random().nextInt(30) * 60 * 1000;
            PreferenceUtils.setTimeAlarmBatterySave(time);
            AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_BATTERY_SAVE, time);
        }
        if (PreferenceUtils.getTimeRemindJunkFile() != 0) {
            AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_JUNK_FILE, Toolbox.getTimeAlarmJunkFile(true));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (getInstance() == null) {
            restartService = false;
            setInstance(ServiceManager.this);
            createNotificationChannel(this);
            updateNotification();
        }
        if (mBatteryStatusRec == null) {
            mBatteryStatusRec = new BatteryStatusRec();
            mBatteryStatusRec.OnCreate(this);
        }
        if (mPackageRecerver == null) {
            mPackageRecerver = new PackageRec();
            mPackageRecerver.OnCreate(this);
        }
        if (mAlarmRec == null) {
            mAlarmRec = new AlarmRec();
            mAlarmRec.OnCreate(this);
        }
        return START_STICKY;
    }

    public void createNotificationChannel(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel(NotificationUtil.ID_NOTIFI_MANAGER + "", name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public Notification showNotification(Context mContext) {
        RemoteViews notificationLayout =
                new RemoteViews(mContext.getPackageName(), R.layout.layout_notification_manager);
        setupViewNotifi(notificationLayout);
//        notificationLayout.setOnClickPendingIntent(R.id.ll_home,
//                NotificationUtil.getInstance().onButtonNotificationClick(mContext, R.id.ll_home));
        notificationLayout.setOnClickPendingIntent(R.id.ll_cleanup,
                NotificationUtil.getInstance().onButtonNotificationClick(mContext, R.id.ll_cleanup));
        notificationLayout.setOnClickPendingIntent(R.id.ll_boost,
                NotificationUtil.getInstance().onButtonNotificationClick(mContext, R.id.ll_boost));
        notificationLayout.setOnClickPendingIntent(R.id.ll_cooldown,
                NotificationUtil.getInstance().onButtonNotificationClick(mContext, R.id.ll_cooldown));
        notificationLayout.setOnClickPendingIntent(R.id.ll_pin,
                NotificationUtil.getInstance().onButtonNotificationClick(mContext, R.id.ll_pin));
        notificationLayout.setOnClickPendingIntent(R.id.llGame,
                NotificationUtil.getInstance().onButtonNotificationClick(mContext, R.id.llGame));
        IntentFilter intentFilterControl = new IntentFilter();
        intentFilterControl.addAction(NotificationUtil.ACTION_NOTIFICATION_BUTTON_CLICK);
        try {
            mContext.unregisterReceiver(receiver);
        } catch (Exception e) {

        }
        mContext.registerReceiver(receiver, intentFilterControl);

        Notification
                notification = new NotificationCompat.Builder(mContext, NotificationUtil.ID_NOTIFI_MANAGER + "")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.logo)
                .setCustomContentView(notificationLayout)
                .build();
        return notification;
    }

    private void setupViewNotifi(RemoteViews notificationLayout) {

    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra(NotificationUtil.EXTRA_BUTTON_CLICKED, -1);
            Config.FUNCTION mFunction = null;
            boolean nothingAction = false;
            if (id == R.id.ll_cleanup) {
                if (PhoneCleanerApp.getInstance().getTopActivity() instanceof JunkFileActivity) {
                    nothingAction = true;
                }
                mFunction = Config.FUNCTION.JUNK_FILES;
            } else if (id == R.id.ll_boost) {
                if (PhoneCleanerApp.getInstance().getTopActivity() instanceof PhoneBoostActivity) {
                    nothingAction = true;
                }
                mFunction = Config.FUNCTION.PHONE_BOOST;
            } else if (id == R.id.ll_cooldown) {
                if (PhoneCleanerApp.getInstance().getTopActivity() instanceof PhoneBoostActivity) {
                    nothingAction = true;
                }
                mFunction = Config.FUNCTION.CPU_COOLER;
            } else if (id == R.id.ll_pin) {
                if (PhoneCleanerApp.getInstance().getTopActivity() instanceof PhoneBoostActivity) {
                    nothingAction = true;
                }
                mFunction = Config.FUNCTION.POWER_SAVING;
            } else if (id == R.id.llGame) {
                mFunction = Config.FUNCTION.GAME_BOOSTER_MAIN;
            } else if (id == R.id.tv_clean_spam) {
                if (PhoneCleanerApp.getInstance().getTopActivity() instanceof NotificationCleanActivity) {
                    nothingAction = true;
                }
                mFunction = Config.FUNCTION.NOTIFICATION_MANAGER;
            }
            if (!Toolbox.isAppOnForeground(context))
                nothingAction = false;
            if (!nothingAction)
                openScreenFromNoti(mFunction);
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
        }
    };

    public void updateNotification() {
        startForeground(NotificationUtil.ID_NOTIFI_MANAGER, showNotification(this));
        if (!PreferenceUtils.isShowHideNotificationManager())
            deleteViewNotifi();
    }

    public void deleteViewNotifi() {
        if (mNotificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //mNotificationManager.deleteNotificationChannel(String.valueOf(NotificationUtil.ID_NOTIFI_MANAGER));
            }
            stopForeground(true);
            mNotificationManager.cancel(NotificationUtil.ID_NOTIFI_MANAGER);
        }
    }

    public void openScreenFromNoti(Config.FUNCTION mFunction) {
        Intent intentAct = new Intent(this, MainActivity.class);
        intentAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (mFunction != null)
            intentAct.putExtra(Config.DATA_OPEN_FUNCTION, mFunction.id);
        startActivity(intentAct);
    }

    public void startSmartRecharger() {
        Intent intentAct = new Intent(this, SmartChargerBoostActivity.class);
        intentAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentAct);
    }

    public void startUninstall(String pkgName) {
        NotificationUtil.getInstance().showNotificationInstallUninstallApk(this, pkgName, NotificationUtil.ID_NOTIFICATTION_UNINSTALL);
        Intent intentAct = new Intent(this, ScanAppUninstallActivity.class);
        intentAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentAct.putExtra(Config.PKG_RECERVER_DATA, pkgName);
        startActivity(intentAct);
    }

    public void startInstall(String pkgName) {
        NotificationUtil.getInstance().showNotificationInstallUninstallApk(this, pkgName, NotificationUtil.ID_NOTIFICATTION_INSTALL);
        Intent intentAct = new Intent(this, ScanAppInstallActivity.class);
        intentAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentAct.putExtra(Config.PKG_RECERVER_DATA, pkgName);
        startActivity(intentAct);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {

        }
        if (mBatteryStatusRec != null) {
            mBatteryStatusRec.OnDestroy(this);
            mBatteryStatusRec = null;
        }

        if (mPackageRecerver != null) {
            mPackageRecerver.OnDestroy(this);
            mPackageRecerver = null;
        }

        if (mAlarmRec != null) {
            mAlarmRec.OnDestroy(this);
            mAlarmRec = null;
        }
        setInstance(null);
        if (restartService)
            AlarmUtils.setAlarm(this, AlarmUtils.ACTION_REPEAT_SERVICE, AlarmUtils.TIME_REPREAT_SERVICE);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        AlarmUtils.setAlarm(this, AlarmUtils.ACTION_REPEAT_SERVICE, AlarmUtils.TIME_REPREAT_SERVICE);
        super.onTaskRemoved(rootIntent);
    }
}
