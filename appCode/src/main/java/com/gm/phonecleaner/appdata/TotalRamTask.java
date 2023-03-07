package com.gm.phonecleaner.appdata;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;

import com.gm.phonecleaner.PhoneCleanerApp;
import com.gm.phonecleaner.utils.Toolbox;

public class TotalRamTask extends AsyncTask<Void, Integer, Boolean> {

    private long totalRam;
    private long useRam;
    private DataCallBack mDataCallBack;

    public interface DataCallBack {
        void getDataRam(long useRam, long totalRam);
    }

    public TotalRamTask( DataCallBack mDataCallBack) {
        this.mDataCallBack = mDataCallBack;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        totalRam = Toolbox.getTotalRAM();
        long availableRam = getAvaiableRam(PhoneCleanerApp.getInstance());
        useRam = totalRam - availableRam;
        int percentRam = (int) (((double) useRam / (double) totalRam) * 100);

        if (percentRam > 100) {
            percentRam = percentRam % 100;
        }

        for (int i = 0; i <= percentRam; i++) {
            publishProgress(i);
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (mDataCallBack != null)
            mDataCallBack.getDataRam(useRam, totalRam);
    }


    private long getAvaiableRam(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            activityManager.getMemoryInfo(mi);
        }
        return mi.availMem;
    }
}
