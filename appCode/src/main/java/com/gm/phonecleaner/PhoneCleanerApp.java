package com.gm.phonecleaner;

import com.ads.control.AdmobHelp;
import com.gm.phonecleaner.ui.BaseActivity;
import com.gm.phonecleaner.ui.main.MainActivity;
import com.gm.phonecleaner.utils.PreferenceUtils;
import com.gm.phonecleaner.password.act.lock.GestureUnlockLockActivity;
import com.gm.phonecleaner.password.utils.SpUtil;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

public class PhoneCleanerApp extends LitePalApplication {
    private static String mAuth= "436F70797269676874206279204C7562755465616D2E636F6D5F2B3834393731393737373937";

    private static List<BaseActivity> activityList;

    private static PhoneCleanerApp instance;

    public static PhoneCleanerApp getInstance() {
        return instance;
    }

    private synchronized static void setInstance(PhoneCleanerApp instance) {
        PhoneCleanerApp.instance = instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null)
            setInstance(PhoneCleanerApp.this);
        PreferenceUtils.init(this);
        if (PreferenceUtils.getTimeInstallApp() == 0)
            PreferenceUtils.setTimeInstallApp(System.currentTimeMillis());
        SpUtil.getInstance().init(instance);
        activityList = new ArrayList<>();
        AdmobHelp.getInstance().init(this);
    }

    public void doForCreate(BaseActivity activity) {
        activityList.add(activity);
    }

    public void doForFinish(BaseActivity activity) {
        activityList.remove(activity);
    }

    public BaseActivity getTopActivity() {
        if (activityList.isEmpty())
            return null;
        return activityList.get(activityList.size() - 1);
    }

    public void clearAllActivity() {
        for (BaseActivity activity : activityList) {
            if (activity != null && !(activity instanceof GestureUnlockLockActivity))
                activity.clear();
        }
        activityList.clear();
    }

    public List<BaseActivity> getActivityList() {
        return activityList;
    }

    public void clearAllActivityUnlessMain() {
        for (BaseActivity activity : activityList) {
            if (activity != null && !(activity instanceof MainActivity))
                activity.clear();
        }
        activityList.clear();
    }

}
