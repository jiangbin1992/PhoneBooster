package com.gm.phonecleaner.password.act.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.password.act.lock.GestureCreateLockActivity;
import com.gm.phonecleaner.password.base.AppConstants;
import com.gm.phonecleaner.password.base.BaseLockActivity;
import com.gm.phonecleaner.password.model.LockAutoTime;
import com.gm.phonecleaner.password.services.BackgroundManager;
import com.gm.phonecleaner.password.services.LockService;
import com.gm.phonecleaner.password.utils.SpUtil;
import com.gm.phonecleaner.password.utils.ToastUtil;
import com.gm.phonecleaner.password.widget.SelectLockTimeDialog;


/**
 * Created by xian on 2017/2/17.
 */

public class LockSettingLockActivity extends BaseLockActivity implements View.OnClickListener
        , DialogInterface.OnDismissListener, CompoundButton.OnCheckedChangeListener {

    public static final String ON_ITEM_CLICK_ACTION = "on_item_click_action";
    public static final int REQUEST_CHANGE_PWD = 3;

    private SwitchCompat cbLockSwitch;
    private SwitchCompat cbLockScreen;
    private SwitchCompat cbIntruderSelfie;
    private SwitchCompat cbHidePattern;
    private SwitchCompat cbVibration;

    private TextView tvLockTime, tvChangePwd, tvSecurutySettings;
    private LockSettingReceiver mLockSettingReceiver;
    private SelectLockTimeDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lock_setting;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        cbLockSwitch = findViewById(R.id.checkbox_app_lock_on_off);
        cbLockScreen = findViewById(R.id.checkbox_lock_screen_switch_on_phone_lock);
        cbIntruderSelfie = findViewById(R.id.checkbox_intruder_selfie);
        cbHidePattern = findViewById(R.id.checkbox_show_hide_pattern);
        cbVibration = findViewById(R.id.checkbox_vibrate);

        tvSecurutySettings = findViewById(R.id.security_settings);
        tvChangePwd = findViewById(R.id.btn_change_pwd);
        tvLockTime = findViewById(R.id.lock_time);
    }

    @Override
    protected void initData() {
        mLockSettingReceiver = new LockSettingReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ON_ITEM_CLICK_ACTION);
        registerReceiver(mLockSettingReceiver, filter);
        dialog = new SelectLockTimeDialog(this, "");
        dialog.setOnDismissListener(this);
        boolean isLockOpen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        cbLockSwitch.setChecked(isLockOpen);

        boolean isLockAutoScreen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN, false);
        cbLockScreen.setChecked(isLockAutoScreen);

        boolean isTakePic = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, false);
        cbIntruderSelfie.setChecked(isTakePic);
        tvLockTime.setText(SpUtil.getInstance().getString(AppConstants.LOCK_APART_TITLE, "immediately"));
    }

    @Override
    protected void initAction() {

        cbLockSwitch.setOnCheckedChangeListener(this);
        cbLockScreen.setOnCheckedChangeListener(this);
        cbIntruderSelfie.setOnCheckedChangeListener(this);
        cbHidePattern.setOnCheckedChangeListener(this);
        cbVibration.setOnCheckedChangeListener(this);
        tvLockTime.setOnClickListener(this);
        tvChangePwd.setOnClickListener(this);
        tvSecurutySettings.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == R.id.btn_change_pwd) {
            Intent intent = new Intent(LockSettingLockActivity.this, GestureCreateLockActivity.class);
            startActivityForResult(intent, REQUEST_CHANGE_PWD);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (id == R.id.lock_when) {
            String title = SpUtil.getInstance().getString(AppConstants.LOCK_APART_TITLE, "");
            dialog.setTitle(title);
            dialog.show();
        } else if (id == R.id.security_settings) {
            SecuritySettingActivity.openSettingSecurytiScreen(this, SecuritySettingActivity.TYPE_OPEN.SET_PASS);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean b) {
        int id = buttonView.getId();
        if (id == R.id.checkbox_app_lock_on_off) {
            SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, b);
            if (b) {
                BackgroundManager.getInstance().init(LockSettingLockActivity.this).stopService(LockService.class);
                BackgroundManager.getInstance().init(LockSettingLockActivity.this).startService(LockService.class);
                BackgroundManager.getInstance().init(LockSettingLockActivity.this).startAlarmManager();
            } else {
                BackgroundManager.getInstance().init(LockSettingLockActivity.this).stopService(LockService.class);
                BackgroundManager.getInstance().init(LockSettingLockActivity.this).stopAlarmManager();
            }
        } else if (id == R.id.checkbox_lock_screen_switch_on_phone_lock) {
            SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_SCREEN, b);
        } else if (id == R.id.checkbox_intruder_selfie) {
            SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, b);
            Toast.makeText(LockSettingLockActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.checkbox_show_hide_pattern) {
            SpUtil.getInstance().putBoolean(AppConstants.LOCK_IS_HIDE_LINE, b);
        } else if (id == R.id.checkbox_vibrate) {
            SpUtil.getInstance().putBoolean(AppConstants.PATTERN_VIBRATION, b);
            Toast.makeText(LockSettingLockActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHANGE_PWD:
                    ToastUtil.showToast("Password reset succeeded");
                    break;
            }
        }
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mLockSettingReceiver);
    }

    private class LockSettingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            String action = intent.getAction();
            if (action.equals(ON_ITEM_CLICK_ACTION)) {
                LockAutoTime info = intent.getParcelableExtra("info");
                boolean isLast = intent.getBooleanExtra("isLast", true);
                if (isLast) {
                    tvLockTime.setText(info.getTitle());
                    SpUtil.getInstance().putString(AppConstants.LOCK_APART_TITLE, info.getTitle());
                    SpUtil.getInstance().putLong(AppConstants.LOCK_APART_MILLISECONDS, 0L);
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_SCREEN_TIME, false);
                } else {
                    tvLockTime.setText(info.getTitle());
                    SpUtil.getInstance().putString(AppConstants.LOCK_APART_TITLE, info.getTitle());
                    SpUtil.getInstance().putLong(AppConstants.LOCK_APART_MILLISECONDS, info.getTime());
                    SpUtil.getInstance().putBoolean(AppConstants.LOCK_AUTO_SCREEN_TIME, true);
                }
                dialog.dismiss();
            }
        }
    }

}
