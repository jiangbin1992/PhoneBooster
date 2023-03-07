package com.gm.phonecleaner.password.act.lock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;

import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.password.act.main.MainLockActivity;
import com.gm.phonecleaner.password.act.setting.LockSettingLockActivity;
import com.gm.phonecleaner.password.act.setting.SecuritySettingActivity;
import com.gm.phonecleaner.password.base.AppConstants;
import com.gm.phonecleaner.password.base.BaseLockActivity;
import com.gm.phonecleaner.password.db.CommLockInfoManager;
import com.gm.phonecleaner.password.utils.LockPatternUtils;
import com.gm.phonecleaner.password.utils.SpUtil;
import com.gm.phonecleaner.password.widget.LockPatternView;
import com.gm.phonecleaner.password.widget.LockPatternViewPattern;

import java.util.List;

/**
 * Created by xian on 2017/2/17.
 */

public class GestureSelfUnlockLockActivity extends BaseLockActivity {

    private LockPatternView mLockPatternView;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private String actionFrom;
    private String pkgName;
    private CommLockInfoManager mManager;
    private ImageView imMore;

    @NonNull
    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_lock_gesture_self_unlock;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mLockPatternView = findViewById(R.id.unlock_lock_view);
        imMore = findViewById(R.id.btn_more);
    }

    @Override
    protected void initData() {
        mManager = new CommLockInfoManager(this);
        pkgName = getIntent().getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
        actionFrom = getIntent().getStringExtra(AppConstants.LOCK_FROM);
        imMore.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, imMore);
            popupMenu.getMenuInflater().inflate(R.menu.unlock_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                SecuritySettingActivity.openSettingSecurytiScreen(this, SecuritySettingActivity.TYPE_OPEN.FORGOT_PASS);
                return true;
            });
            popupMenu.show();
        });
        initLockPatternView();
    }

    private void initLockPatternView() {
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(@NonNull List<LockPatternView.Cell> pattern) {
                if (mLockPatternUtils.checkPattern(pattern)) {
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    if (actionFrom.equals(AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY)) {
                        Intent intent = new Intent(GestureSelfUnlockLockActivity.this, MainLockActivity.class);
                        startActivity(intent);
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } else if (actionFrom.equals(AppConstants.LOCK_FROM_FINISH)) {
                        mManager.unlockCommApplication(pkgName);
                        finish();
                    } else if (actionFrom.equals(AppConstants.LOCK_FROM_SETTING)) {
                        startActivity(new Intent(GestureSelfUnlockLockActivity.this, LockSettingLockActivity.class));
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } else if (actionFrom.equals(AppConstants.LOCK_FROM_UNLOCK)) {
                        mManager.setIsUnLockThisApp(pkgName, true);
                        mManager.unlockCommApplication(pkgName);
                        sendBroadcast(new Intent(GestureUnlockLockActivity.FINISH_UNLOCK_THIS_APP));
                        finish();
                    }
                } else {
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                        mFailedPatternAttemptsSinceLastTimeout++;
                        int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
                        if (retry >= 0) {
                        }
                    } else {
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= 3) {
                        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_RECORD_PIC, false)) {

                        }
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) { //The number of failures is greater than the maximum number of incorrect attempts before blocking the use

                    } else {
                        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
                    }
                }
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    @Override
    protected void initAction() {

    }


}
