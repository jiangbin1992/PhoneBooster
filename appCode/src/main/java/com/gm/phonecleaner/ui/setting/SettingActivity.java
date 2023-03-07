package com.gm.phonecleaner.ui.setting;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.ui.BaseActivity;
import com.gm.phonecleaner.ui.notDissturb.NotDissturbActivity;
import com.gm.phonecleaner.service.ServiceManager;
import com.gm.phonecleaner.utils.AlarmUtils;
import com.gm.phonecleaner.utils.PreferenceUtils;
import com.gm.phonecleaner.utils.Toolbox;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R2.id.im_back_toolbar)
    ImageView imBack;
    @BindView(R2.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R2.id.sw_uninstall_scan)
    SwitchCompat swUninstall;
    @BindView(R2.id.sw_install_scan)
    SwitchCompat swInstall;
    @BindView(R2.id.sw_phone_boost)
    SwitchCompat swPhoneBoost;
    @BindView(R2.id.sw_cpu_cooler)
    SwitchCompat swCpuCooler;
    @BindView(R2.id.sw_battery_save)
    SwitchCompat swBatterySave;
    @BindView(R2.id.sw_protection_real_time)
    SwitchCompat swProtectRealTime;
    @BindView(R2.id.sw_notificaiton_toggle)
    SwitchCompat swNotificationToggle;
    @BindView(R2.id.tv_time_junk_remind)
    TextView tvTimeRemind;
    @BindView(R2.id.tv_time_dnd)
    TextView tvTimeDnd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        imBack.setVisibility(View.VISIBLE);
        imBack.setColorFilter(getResources().getColor(R.color.color_222222), PorterDuff.Mode.SRC_IN);
        tvToolbar.setText(getString(R.string.setting));
        tvToolbar.setTextColor(getResources().getColor(R.color.color_222222));
        switch (PreferenceUtils.getTimeRemindJunkFile()) {
            case 0:
                tvTimeRemind.setText(getString(R.string.never_reminder));
                break;
            case 1:
                tvTimeRemind.setText(getString(R.string.every_day));
                break;
            case 3:
                tvTimeRemind.setText(getString(R.string.every_3days));
                break;
            case 7:
                tvTimeRemind.setText(getString(R.string.every_7days));
                break;
        }
    }

    public void initData() {
        swUninstall.setChecked(PreferenceUtils.isScanUninstaillApk());
        swInstall.setChecked(PreferenceUtils.isScanInstaillApk());
        swProtectRealTime.setChecked(PreferenceUtils.isProtectionRealTime());
        swNotificationToggle.setChecked(PreferenceUtils.isShowHideNotificationManager());
        /**/
        swPhoneBoost.setChecked(PreferenceUtils.getTimeAlarmPhoneBoost() != 0);
        swCpuCooler.setChecked(PreferenceUtils.getTimeAlarmCpuCooler() != 0);
        swBatterySave.setChecked(PreferenceUtils.getTimeAlarmBatterySave() != 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        tvTimeDnd.setText(Toolbox.intTimeOff(PreferenceUtils.getDNDStart()) + " - " + Toolbox.intTimeOn(PreferenceUtils.getDNDEnd()));
    }

    @OnClick({R2.id.ll_create_shortcut, R2.id.ll_app_protected, R2.id.ll_ignore_list, R2.id.sw_uninstall_scan, R2.id.sw_install_scan
            , R2.id.sw_phone_boost, R2.id.sw_cpu_cooler, R2.id.sw_battery_save, R2.id.sw_protection_real_time, R2.id.sw_notificaiton_toggle
            , R2.id.ll_junk_reminder, R2.id.ll_dissturb})
    void click(View mView) {
        int id = mView.getId();
        if (id == R.id.ll_create_shortcut) {
            Toolbox.creatShorCutNormal(this);
        } else if (id == R.id.ll_app_protected) {
            openIgnoreScreen();
        } else if (id == R.id.ll_ignore_list) {
            openWhileListVirusSceen();
        } else if (id == R.id.sw_uninstall_scan) {
            if (PreferenceUtils.isScanUninstaillApk()) {
                PreferenceUtils.setScanUninstaillApk(false);
            } else {
                swUninstall.setChecked(false);
                try {
                    askPermissionStorage(() -> {
                        PreferenceUtils.setScanUninstaillApk(true);
                        swUninstall.setChecked(true);
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (id == R.id.sw_install_scan) {
            if (PreferenceUtils.isScanInstaillApk()) {
                PreferenceUtils.setScaninstaillApk(false);
            } else {
                swInstall.setChecked(false);
                try {
                    askPermissionStorage(() -> {
                        PreferenceUtils.setScaninstaillApk(true);
                        swInstall.setChecked(true);
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (id == R.id.sw_protection_real_time) {
            if (PreferenceUtils.isProtectionRealTime()) {
                PreferenceUtils.setProtectionRealTime(false);
            } else {
                swProtectRealTime.setChecked(false);
                try {
                    checkdrawPermission(() -> {
                        askPermissionStorage(() -> {
                            swProtectRealTime.setChecked(true);
                            PreferenceUtils.setProtectionRealTime(true);
                            return null;
                        });
                        return null;
                    });
                } catch (Exception e) {

                }
            }
        } else if (id == R.id.sw_notificaiton_toggle) {
            if (ServiceManager.getInstance() != null) {
                if (!PreferenceUtils.isShowHideNotificationManager()) {
                    ServiceManager.getInstance().setRestartService(false);
                    ServiceManager.getInstance().onDestroy();
                    Intent mIntent = new Intent(this, ServiceManager.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ContextCompat.startForegroundService(this, mIntent);
                    } else {
                        startService(new Intent(this, ServiceManager.class));
                    }
                    PreferenceUtils.setShowHideNotificationManager(true);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.note))
                            .setMessage(getString(R.string.note_turn_off_notifi, getString(R.string.app_name)))
                            .setPositiveButton(getString(R.string.cancel), (dialog, which) -> {
                                dialog.dismiss();
                                swNotificationToggle.setChecked(true);
                            })
                            .setNegativeButton(getString(R.string.turn_off), (dialog, which) -> {
                                ServiceManager.getInstance().deleteViewNotifi();
                                PreferenceUtils.setShowHideNotificationManager(false);
                            })
                            .show();
                }
            }
        } else if (id == R.id.sw_phone_boost) {
            if (swPhoneBoost.isChecked()) {
                long time = new Random().nextInt(30) * 60 * 1000;
                PreferenceUtils.setTimeAlarmPhoneBoost(time);
                AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_BOOOST, time);
            } else {
                PreferenceUtils.setTimeAlarmPhoneBoost(0);
                AlarmUtils.cancel(this, AlarmUtils.ALARM_PHONE_BOOOST);
            }
        } else if (id == R.id.sw_cpu_cooler) {
            if (swCpuCooler.isChecked()) {
                long time = new Random().nextInt(30) * 60 * 1000;
                PreferenceUtils.setTimeAlarmCpuCooler(time);
                AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_CPU_COOLER, time);
            } else {
                PreferenceUtils.setTimeAlarmCpuCooler(0);
                AlarmUtils.cancel(this, AlarmUtils.ALARM_PHONE_CPU_COOLER);
            }
        } else if (id == R.id.sw_battery_save) {
            if (swBatterySave.isChecked()) {
                long time = new Random().nextInt(30) * 60 * 1000;
                PreferenceUtils.setTimeAlarmBatterySave(time);
                AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_BATTERY_SAVE, time);
            } else {
                PreferenceUtils.setTimeAlarmBatterySave(0);
                AlarmUtils.cancel(this, AlarmUtils.ALARM_PHONE_BATTERY_SAVE);
            }
        } else if (id == R.id.ll_junk_reminder) {
            selectTimeJunkFile();
        } else if (id == R.id.ll_dissturb) {
            startActivity(new Intent(this, NotDissturbActivity.class));
        }
    }

    public void selectTimeJunkFile() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(getString(R.string.junk_reminder_frequency));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        arrayAdapter.add(getString(R.string.every_day));
        arrayAdapter.add(getString(R.string.every_3days));
        arrayAdapter.add(getString(R.string.every_7days));
        arrayAdapter.add(getString(R.string.never_reminder));

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            tvTimeRemind.setText(strName);
            if (which == 0) {
                PreferenceUtils.setTimeRemindJunkFile(1);
            } else if (which == 1) {
                PreferenceUtils.setTimeRemindJunkFile(3);
            } else if (which == 2) {
                PreferenceUtils.setTimeRemindJunkFile(7);
            } else if (which == 3) {
                PreferenceUtils.setTimeRemindJunkFile(0);
            }
            if (PreferenceUtils.getTimeRemindJunkFile() != 0) {
                AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_JUNK_FILE, Toolbox.getTimeAlarmJunkFile(true));
            } else {
                AlarmUtils.cancel(this, AlarmUtils.ALARM_PHONE_JUNK_FILE);
            }
        });
        builderSingle.show();
    }
}
