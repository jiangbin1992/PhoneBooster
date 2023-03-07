package com.gm.phonecleaner.password.act.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.utils.Toolbox;
import com.google.android.material.tabs.TabLayout;
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.gm.phonecleaner.password.act.setting.LockSettingLockActivity;
import com.gm.phonecleaner.password.base.BaseLockActivity;
import com.gm.phonecleaner.password.model.CommLockInfo;
import com.gm.phonecleaner.password.mvp.contract.LockMainContract;
import com.gm.phonecleaner.password.mvp.p.LockMainPresenter;
import com.gm.phonecleaner.password.services.BackgroundManager;
import com.gm.phonecleaner.password.services.LockService;
import com.gm.phonecleaner.password.widget.DialogSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xian on 2017/3/1.
 */

public class MainLockActivity extends BaseLockActivity implements LockMainContract.View, View.OnClickListener {

    private static final int RESULT_ACTION_IGNORE_BATTERY_OPTIMIZATION = 351;
    private static final String TAG = "MainActivity";
    private ImageView mBtnSetting;
    private TextView mEditSearch;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private CommentPagerAdapter mPagerAdapter;
    private GoogleProgressBar mGoogleProgressBar;
    private RelativeLayout viewProgress;
    private LockMainPresenter mLockMainPresenter;
    private DialogSearch mDialogSearch;
    private List<String> titles;
    private List<Fragment> fragmentList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lock_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mBtnSetting = findViewById(R.id.btn_setting);
        mEditSearch = findViewById(R.id.edit_search);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mGoogleProgressBar = findViewById(R.id.google_progress);
        viewProgress = findViewById(R.id.view_progress);

        Drawable drawable = new ChromeFloatingCirclesDrawable.Builder(this)
                .colors(Toolbox.getProgressDrawableColors(this))
                .build();
        Rect bounds = mGoogleProgressBar.getIndeterminateDrawable().getBounds();
        mGoogleProgressBar.setIndeterminateDrawable(drawable);
        mGoogleProgressBar.getIndeterminateDrawable().setBounds(bounds);

        mLockMainPresenter = new LockMainPresenter(this, this);
        mLockMainPresenter.loadAppInfo(this);
        //
    }

    @Override
    protected void initData() {
        mDialogSearch = new DialogSearch(this);

        PowerManager powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (powerManager != null && !powerManager.isIgnoringBatteryOptimizations(AppConstants.APP_PACKAGE_NAME)) {
//                @SuppressLint("BatteryLife")
//                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                intent.setData(Uri.parse("package:" + AppConstants.APP_PACKAGE_NAME));
//                startActivity(intent);
//            }
//        }
        if (!BackgroundManager.getInstance().init(this).isServiceRunning(LockService.class)) {
            BackgroundManager.getInstance().init(this).startService(LockService.class);
        }
        BackgroundManager.getInstance().init(this).startAlarmManager();
    }


    @Override
    protected void initAction() {
        mBtnSetting.setOnClickListener(this);
        mEditSearch.setOnClickListener(this);
        mDialogSearch.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mLockMainPresenter.loadAppInfo(MainLockActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTION_IGNORE_BATTERY_OPTIMIZATION) {
            // do nothing::  make this required
        }
    }

    @Override
    public void loadAppInfoSuccess(@NonNull List<CommLockInfo> list) {
        int sysNum = 0;
        int userNum = 0;
        for (CommLockInfo info : list) {
            if (info.isSysApp()) {
                sysNum++;
            } else {
                userNum++;
            }
        }
        titles = new ArrayList<>();
        titles.add("System Apps" + " (" + sysNum + ")");
        titles.add("User Apps" + " (" + userNum + ")");

        SysAppFragment sysAppFragment = SysAppFragment.newInstance(list);
        UserAppFragment userAppFragment = UserAppFragment.newInstance(list);

        fragmentList = new ArrayList<>();
        fragmentList.add(sysAppFragment);
        fragmentList.add(userAppFragment);
        mPagerAdapter = new CommentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void showProgressbar(boolean isShow) {
        viewProgress.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == R.id.btn_setting) {
            startActivity(new Intent(this, LockSettingLockActivity.class));
        } else if (id == R.id.edit_search) {
            mDialogSearch.show();
        }
    }

    public class CommentPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList;
        private List<String> titles;


        public CommentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titles) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }
    }

}
