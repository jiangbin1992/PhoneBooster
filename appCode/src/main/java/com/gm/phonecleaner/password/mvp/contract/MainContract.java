package com.gm.phonecleaner.password.mvp.contract;

import android.content.Context;

import com.gm.phonecleaner.password.base.BasePresenter;
import com.gm.phonecleaner.password.base.BaseView;
import com.gm.phonecleaner.password.model.CommLockInfo;

import java.util.List;

/**
 * Created by xian on 2017/2/17.
 */

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context, boolean isSort);

        void loadLockAppInfo(Context context);

        void onDestroy();
    }
}
