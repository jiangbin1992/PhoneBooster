package com.gm.phonecleaner.ui.main.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.control.AdmobHelp;
import com.ads.control.funtion.UtilsApp;
import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.ui.BaseFragment;
import com.gm.phonecleaner.ui.setting.SettingActivity;
import com.gm.phonecleaner.utils.PreferenceUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentPersional extends BaseFragment {



    public static FragmentPersional getInstance() {
        FragmentPersional mFragmentPersional = new FragmentPersional();
        Bundle mBundle = new Bundle();
        mFragmentPersional.setArguments(mBundle);
        return mFragmentPersional;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_persional_new, container, false);
        ButterKnife.bind(this, mView);
        initData();
        return mView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AdmobHelp.getInstance().loadBannerFragment(getActivity(), view);
    }
    private void initData() {
        int day = (int) ((System.currentTimeMillis() - PreferenceUtils.getTimeInstallApp()) / (24 * 60 * 60 * 1000));
        //tvPersionalHeader.setText(getString(R.string.has_protected_your_phone, getString(R.string.app_name), String.valueOf(day == 0 ? 1 : day)));
    }

    @OnClick({R2.id.ll_settings, R2.id.ll_feedback, R2.id.ll_share})
    void click(View mView) {
        int id = mView.getId();
        if (id == R.id.ll_settings) {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        } else if (id == R.id.ll_feedback) {
            UtilsApp.SendFeedBack(getActivity(), getString(R.string.email_feedback), getString(R.string.Title_email));
        } else if (id == R.id.ll_share) {
            UtilsApp.shareApp(getActivity());
        }
    }

}
