package com.gm.phonecleaner.password.mvp.contract;

import android.content.Context;

import com.gm.phonecleaner.password.base.BasePresenter;
import com.gm.phonecleaner.password.base.BaseView;
import com.gm.phonecleaner.password.model.CommLockInfo;
import com.gm.phonecleaner.password.mvp.p.LockMainPresenter;

import java.util.List;

/**
 * Created by xian on 2017/2/17.
 */

public interface LockMainContract {
    interface View extends BaseView<Presenter> {

        void loadAppInfoSuccess(List<CommLockInfo> list);

        void showProgressbar(boolean isShow);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context);

        void searchAppInfo(String search, LockMainPresenter.ISearchResultListener listener);

        void onDestroy();
    }
}
