package com.gm.phonecleaner.ui;

import static com.best.now.myad.utils.PublicHelperKt.isRewarded;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.best.now.myad.utils.PublicHelperKt;
import com.gm.phonecleaner.PhoneCleanerApp;
import com.gm.phonecleaner.R;
import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.dialog.DialogAskPermission;
import com.gm.phonecleaner.ui.antivirus.AntivirusActivity;
import com.gm.phonecleaner.ui.appManager.AppManagerActivity;
import com.gm.phonecleaner.ui.cleanNotification.NotificationCleanActivity;
import com.gm.phonecleaner.ui.cleanNotification.NotificationCleanGuildActivity;
import com.gm.phonecleaner.ui.cleanNotification.NotificationCleanSettingActivity;
import com.gm.phonecleaner.ui.gameboost.GameBoostActivity;
import com.gm.phonecleaner.ui.listAppSelect.AppSelectActivity;
import com.gm.phonecleaner.ui.junkfile.JunkFileActivity;
import com.gm.phonecleaner.ui.phoneboost.PhoneBoostActivity;
import com.gm.phonecleaner.ui.result.ResultAcitvity;
import com.gm.phonecleaner.ui.guildPermission.GuildPermissionActivity;
import com.gm.phonecleaner.ui.smartCharger.SmartChargerActivity;
import com.gm.phonecleaner.service.NotificationListener;
import com.gm.phonecleaner.utils.Config;
import com.gm.phonecleaner.utils.PreferenceUtils;
import com.gm.phonecleaner.utils.SystemUtil;
import com.gm.phonecleaner.utils.Toolbox;
import com.gm.phonecleaner.password.act.main.SplashLockActivity;
import com.testapp.duplicatefileremover.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class BaseActivity extends AppCompatActivity {

    private ImageView imBackToolbar;
    private ImageView menuView;
    private ImageView notification;
    private View loPanel;
    DrawerLayout mDrawerLayout;
    private List<Callable<Void>> callables = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneCleanerApp.getInstance().doForCreate(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhoneCleanerApp.getInstance().doForFinish(this);
    }

    public final void clear() {
        super.finish();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        imBackToolbar = findViewById(R.id.im_back_toolbar);
        menuView = findViewById(R.id.menu);
        notification = findViewById(R.id.notification);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        loPanel = findViewById(R.id.layout_padding);
        if (loPanel != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Toolbox.getHeightStatusBar(this) > 0) {
                loPanel.setPadding(0, Toolbox.getHeightStatusBar(this), 0, 0);
            }
            Toolbox.setStatusBarHomeTransparent(this);
        }
        initControl();
    }

    private void initControl() {
        if (imBackToolbar != null)
            imBackToolbar.setOnClickListener(v -> onBackPressed());

        if (menuView != null)
            menuView.setOnClickListener(view -> {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            });

        if (notification != null)
            notification.setOnClickListener(view -> {
                if (isRewarded(this)) {
                    PublicHelperKt.showInterstitialAd(BaseActivity.this, new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            try {
                                startActivity(new Intent(BaseActivity.this, NotificationCleanGuildActivity.class));
                            } catch (Exception er) {
                                try {
                                    askPermissionNotificaitonSetting(() -> {
                                        if (NotificationListener.getInstance() != null) {
                                            if (NotificationListener.getInstance().getLstSave().isEmpty()) {
                                                startActivity(new Intent(BaseActivity.this, NotificationCleanSettingActivity.class));
                                            } else {
                                                startActivity(new Intent(BaseActivity.this, NotificationCleanActivity.class));
                                            }
                                        } else {
                                            startActivity(new Intent(BaseActivity.this, NotificationCleanSettingActivity.class));
                                        }
                                        return null;
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            return null;
                        }
                    });

                }
            });
    }


    public void openScreenFunction(Config.FUNCTION mFunction) {
        if (isRewarded(this)) {
            PublicHelperKt.showInterstitialAd(BaseActivity.this, new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    switch (mFunction) {
                        case JUNK_FILES:
                            try {
                                askPermissionUsageSetting(() -> {
                                    askPermissionStorage(() -> {
                                        JunkFileActivity.startActivityWithData(BaseActivity.this);
                                        return null;
                                    });
                                    return null;
                                });
                            } catch (Exception e) {

                            }

                            break;
                        case CPU_COOLER:
                        case PHONE_BOOST:
                        case POWER_SAVING:

                            try {
                                askPermissionUsageSetting(() -> {
                                    if (PreferenceUtils.checkLastTimeUseFunction(mFunction))
                                        PhoneBoostActivity.startActivityWithData(BaseActivity.this, mFunction);
                                    else
                                        openScreenResult(mFunction);
                                    return null;
                                });
                            } catch (Exception e) {

                            }

                            break;
                        case ANTIVIRUS:

                            try {
                                askPermissionStorage(() -> {
                                    startActivity(new Intent(BaseActivity.this, AntivirusActivity.class));
                                    return null;
                                });
                            } catch (Exception e) {

                            }

                            break;


                        case APP_LOCK:

                            try {
                                askPermissionUsageSetting(() -> {
                                    startActivity(new Intent(BaseActivity.this, SplashLockActivity.class));
                                    return null;
                                });
                            } catch (Exception e) {

                            }

                            break;
                        case SMART_CHARGE:

                            if (!SystemUtil.checkCanWriteSettings(BaseActivity.this)) {
                                try {
                                    askPermissionUsageSetting(() -> {
                                        askPermissionWriteSetting(() -> {
                                            startActivity(new Intent(BaseActivity.this, SmartChargerActivity.class));
                                            return null;
                                        });
                                        return null;
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                startActivity(new Intent(BaseActivity.this, SmartChargerActivity.class));
                            }
                            break;
//            case MESSAGE_SECURITY:
//                break;
//            case SMART_CLEANUP:
//                break;
                        case DEEP_CLEAN:

                            startActivity(new Intent(BaseActivity.this, MainActivity.class));

                            break;
                        case APP_UNINSTALL:

                            startActivity(new Intent(BaseActivity.this, AppManagerActivity.class));

                            break;
                        case GAME_BOOSTER_MAIN:
                        case GAME_BOOSTER:

                            startActivity(new Intent(BaseActivity.this, GameBoostActivity.class));

                            break;
                        case NOTIFICATION_MANAGER:

                            if (PreferenceUtils.isFirstUsedFunction(mFunction)) {
                                startActivity(new Intent(BaseActivity.this, NotificationCleanGuildActivity.class));
                            } else {
                                try {
                                    askPermissionNotificaitonSetting(() -> {
                                        if (NotificationListener.getInstance() != null) {
                                            if (NotificationListener.getInstance().getLstSave().isEmpty()) {
                                                startActivity(new Intent(BaseActivity.this, NotificationCleanSettingActivity.class));
                                            } else {
                                                startActivity(new Intent(BaseActivity.this, NotificationCleanActivity.class));
                                            }
                                        } else {
                                            startActivity(new Intent(BaseActivity.this, NotificationCleanSettingActivity.class));
                                        }
                                        return null;
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
//            case HARASSMENT_FILLTER:
//                break;
//            case SPEEND_PROTECTOR:
//                break;
//            case PAYMENT_SAFE:
//                break;
//            case FILE_MOVE:
//                break;
                    }
                    return null;
                }
            });

        }

    }

    public void openIgnoreScreen() {
        AppSelectActivity.openSelectAppScreen(this, AppSelectActivity.TYPE_SCREEN.IGNORE);
    }

    public void openWhileListVirusSceen() {
        AppSelectActivity.openSelectAppScreen(this, AppSelectActivity.TYPE_SCREEN.WHILE_LIST_VIRUS);
    }

    public void openScreenResult(Config.FUNCTION mFunction) {
        Intent mIntent = new Intent(this, ResultAcitvity.class);
        if (mFunction != null)
            mIntent.putExtra(Config.DATA_OPEN_RESULT, mFunction.id);
        startActivity(mIntent);
    }

    public void askPermissionStorage(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                DialogAskPermission.getInstance(Manifest.permission.READ_EXTERNAL_STORAGE, () -> {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                    , Manifest.permission.WRITE_EXTERNAL_STORAGE}, Config.MY_PERMISSIONS_REQUEST_STORAGE);
                }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
            } else {
                callable.call();
            }
        } else {
            callable.call();
        }
    }

    public void askPermissionUsageSetting(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) && !SystemUtil.isUsageAccessAllowed(this)) {
            DialogAskPermission.getInstance(Settings.ACTION_USAGE_ACCESS_SETTINGS, () -> {
                startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), Config.PERMISSIONS_USAGE);
                GuildPermissionActivity.openActivityGuildPermission(this, Settings.ACTION_USAGE_ACCESS_SETTINGS);
            }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
        } else {
            callable.call();
        }
    }

    public void askPermissionWriteSetting(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(this)) {
            DialogAskPermission.getInstance(Settings.ACTION_MANAGE_WRITE_SETTINGS, () -> {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, Config.PERMISSIONS_WRITE_SETTINGS);
                GuildPermissionActivity.openActivityGuildPermission(this, Settings.ACTION_MANAGE_WRITE_SETTINGS);
            }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
        } else {
            callable.call();
        }
    }

    public void askPermissionNotificaitonSetting(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) && !SystemUtil.isNotificationListenerEnabled(this)) {
            DialogAskPermission.getInstance(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS, () -> {
                startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), Config.PERMISSIONS_NOTIFICATION_LISTENER);
                GuildPermissionActivity.openActivityGuildPermission(this, Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
        } else {
            callable.call();
        }
    }

    public void checkdrawPermission(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            DialogAskPermission.getInstance(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, () -> {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, Config.PERMISSIONS_DRAW_APPICATION);
                GuildPermissionActivity.openActivityGuildPermission(this, Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
        } else {
            callable.call();
        }
    }

    public void requestDetectHome() {
        DialogAskPermission.getInstance(Settings.ACTION_ACCESSIBILITY_SETTINGS, () -> {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            GuildPermissionActivity.openActivityGuildPermission(this, Settings.ACTION_ACCESSIBILITY_SETTINGS);
        }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
    }

    public void openSettingApplication(String packageName) {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.PERMISSIONS_DRAW_APPICATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this))
                    callListener();
                break;
            case Config.PERMISSIONS_USAGE:
                if (SystemUtil.isUsageAccessAllowed(this)) {
                    callListener();
                }
                break;
            case Config.PERMISSIONS_NOTIFICATION_LISTENER:
                if (SystemUtil.isNotificationListenerEnabled(this))
                    callListener();
                break;
            case Config.PERMISSIONS_WRITE_SETTINGS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.System.canWrite(this))
                    callListener();
                break;
        }
    }

    public void callListener() {
        for (Callable callable : callables) {
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Config.MY_PERMISSIONS_REQUEST_CLEAN_CACHE:
            case Config.MY_PERMISSIONS_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (Callable callable : callables) {
                        try {
                            callable.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    callables.clear();
                }
                break;
        }
    }

    public void addFragmentWithTag(Fragment targetFragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.pull_in_right, R.anim.push_out_left, R.anim.pull_in_left, R.anim.push_out_right);
        if (targetFragment != null) {
            ft.addToBackStack(null);
            ft.add(R.id.layout_fragment, targetFragment, tag).commitAllowingStateLoss();
        }
    }

}
