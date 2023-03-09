package com.gm.phonecleaner.ui.main;

import static com.best.now.myad.utils.Constant.URL_PRIVACY_POLICY;
import static com.best.now.myad.utils.PublicHelperKt.loadAd;
import static com.gm.phonecleaner.password.utils.NotificationUtil.cancelNotification;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.ads.control.Rate;
import com.ads.control.funtion.UtilsApp;
import com.best.now.myad.WebActivity;
import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.adp.CustomFragmentPagerAdp;
import com.gm.phonecleaner.listener.ObserverPartener.ObserverInterface;
import com.gm.phonecleaner.listener.ObserverPartener.ObserverUtils;
import com.gm.phonecleaner.listener.ObserverPartener.eventModel.EvbCheckLoadAds;
import com.gm.phonecleaner.listener.ObserverPartener.eventModel.EvbOpenFunc;
import com.gm.phonecleaner.ui.BaseActivity;
import com.gm.phonecleaner.ui.ExitActivity;
import com.gm.phonecleaner.ui.main.home.FragmentHome;
import com.gm.phonecleaner.ui.main.personal.FragmentPersional;
import com.gm.phonecleaner.ui.main.tool.FragmentTool;
import com.gm.phonecleaner.ui.setting.SettingActivity;
import com.gm.phonecleaner.service.NotificationUtil;
import com.gm.phonecleaner.service.ServiceManager;
import com.gm.phonecleaner.utils.AlarmUtils;
import com.gm.phonecleaner.utils.Config;
import com.gm.phonecleaner.utils.PreferenceUtils;
import com.gm.phonecleaner.widget.CustomViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements ObserverInterface {

    @BindView(R2.id.tv_toolbar)
    TextView tvTitleToolbar;
    @BindView(R2.id.viewpager_home)
    CustomViewPager mViewPagerHome;

    private boolean doubleBackToExitPressedOnce = false;
    private CustomFragmentPagerAdp mCustomFragmentPagerAdp;
    private boolean loadAdsNative = true;
    private FragmentHome mFragmentHome;

    public boolean isLoadAdsNative() {
        return loadAdsNative;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ObserverUtils.getInstance().registerObserver(this);
        Intent mIntent = new Intent(this, ServiceManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, mIntent);
        } else {
            startService(mIntent);
        }
        initView();
        initData();
        initControl();
    }

    private void initView() {
        LinearLayout advBanner = findViewById(R.id.advBanner);
        loadAd(advBanner);
        mCustomFragmentPagerAdp = new CustomFragmentPagerAdp(getSupportFragmentManager());
        mFragmentHome = FragmentHome.getInstance();
        mCustomFragmentPagerAdp.addFragment(mFragmentHome, getString(R.string.title_home));
        mCustomFragmentPagerAdp.addFragment(FragmentTool.getInstance(), getString(R.string.title_tool));
        mCustomFragmentPagerAdp.addFragment(FragmentPersional.getInstance(), getString(R.string.title_persional));
        mViewPagerHome.setPagingEnabled(false);
        mViewPagerHome.setOffscreenPageLimit(mCustomFragmentPagerAdp.getCount());
        mViewPagerHome.setAdapter(mCustomFragmentPagerAdp);

        tvTitleToolbar.setTextSize(20);
    }

    private void initData() {

    }

    public void openSetting(View v){
        startActivity(new Intent(this, SettingActivity.class));
    }
    public void openFeedback(View v){
        UtilsApp.SendFeedBack(this,getString(R.string.email_feedback),getString(R.string.Title_email));
    }
    public void openUpgrade(View v){
        UtilsApp.RateApp(this);
    }
    public void openapps(View v){
        UtilsApp.OpenBrower(this,getResources().getString(R.string.link_store_more_app));
    }
    public void share(View v){
//        UtilsApp.shareApp(this);
        WebActivity.Companion.startActivity(MainActivity.this,"Privacy Policy",URL_PRIVACY_POLICY);
    }
    public void likeFacebook(View v){

        UtilsApp.OpenBrower(this,getResources().getString(R.string.link_fb));

    }


    private void initControl() {
        Config.FUNCTION mFunction = Config.getFunctionById(getIntent().getIntExtra(Config.DATA_OPEN_RESULT, 0));
        if (mFunction != null) {
            loadAdsNative = false;
            openScreenResult(mFunction);
        } else {
            Config.FUNCTION mFunctionService = Config.getFunctionById(getIntent().getIntExtra(Config.DATA_OPEN_FUNCTION, 0));
            if (mFunctionService != null) {
                loadAdsNative = false;
                openScreenFunction(mFunctionService);
            } else {
                int idFunc = getIntent().getIntExtra(Config.ALARM_OPEN_FUNTION, 0);
                Config.FUNCTION mFunctionAlarm = Config.getFunctionById(idFunc);
                if (mFunctionAlarm != null) {
                    loadAdsNative = false;
                    openScreenFunction(mFunctionAlarm);
                    if (mFunctionAlarm == Config.FUNCTION.PHONE_BOOST) {
                        NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFICATTION_PHONE_BOOST);
                        PreferenceUtils.setTimeAlarmPhoneBoost(AlarmUtils.TIME_PHONE_BOOST_CLICK);
                        AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_BOOOST, AlarmUtils.TIME_PHONE_BOOST_CLICK);
                    } else if (mFunctionAlarm == Config.FUNCTION.CPU_COOLER) {
                        NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFICATTION_CPU_COOLER);
                        PreferenceUtils.setTimeAlarmPhoneBoost(AlarmUtils.TIME_CPU_COOLER_CLICK);
                        AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_CPU_COOLER, AlarmUtils.TIME_CPU_COOLER_CLICK);
                    } else if (mFunctionAlarm == Config.FUNCTION.POWER_SAVING) {
                        NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFICATTION_BATTERY_SAVE);
                        PreferenceUtils.setTimeAlarmPhoneBoost(AlarmUtils.TIME_BATTERY_SAVE_CLICK);
                        AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_BATTERY_SAVE, AlarmUtils.TIME_BATTERY_SAVE_CLICK);
                    } else if (mFunctionAlarm == Config.FUNCTION.JUNK_FILES) {
                        NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFICATTION_JUNK_FILE);
                    }
                } else {
                    boolean isResultDeepClean = getIntent().getBooleanExtra(Config.REUSLT_DEEP_CLEAN_DATA, false);
                    if (isResultDeepClean) {
                        loadAdsNative = false;
                        openScreenResult(Config.FUNCTION.DEEP_CLEAN);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ObserverUtils.getInstance().removeObserver(this);
    }

    @Override
    public void notifyAction(Object action) {
        if (action instanceof EvbOpenFunc) {
            openScreenFunction(((EvbOpenFunc) action).mFunction);
        } else if (action instanceof EvbCheckLoadAds) {
            loadAdsNative = true;
            if (mFragmentHome != null)
                mFragmentHome.loadAds();
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPagerHome.getCurrentItem() != 0) {
            return;
        }
        Rate.Show(this, new Rate.OnResult() {
            @Override
            public void callActionAfter() {
                ExitActivity.exitApplicationAndRemoveFromRecent(MainActivity.this);
            }
        });
    }
}
