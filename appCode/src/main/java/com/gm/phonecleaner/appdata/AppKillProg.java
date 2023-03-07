package com.gm.phonecleaner.appdata;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;

import com.gm.phonecleaner.model.TaskInfo;
import com.gm.phonecleaner.utils.Toolbox;

import java.util.List;

public class AppKillProg extends AsyncTask<Void, Void, Void> {

    private ActivityManager mActivityManager;
    private List<TaskInfo> lstAppKill;

    public AppKillProg(Context mContext, List<TaskInfo> lstAppKill) {
        this.lstAppKill = lstAppKill;
        mActivityManager = (ActivityManager) mContext.getSystemService(
                Context.ACTIVITY_SERVICE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<TaskInfo> lstKill = Toolbox.getLstTaskFillterIgnore(lstAppKill);
        for (TaskInfo mTaskInfo : lstKill) {
            if (mTaskInfo.isChceked())
                mActivityManager.killBackgroundProcesses(mTaskInfo.getAppinfo().packageName);
        }
        return null;
    }
}
