package com.gm.phonecleaner.ui.main.home;

import static com.best.now.myad.utils.PublicHelperKt.isRewarded;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.control.AdmobHelp;
import com.best.now.myad.utils.PublicHelperKt;
import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.adp.FunctionAdp;
import com.gm.phonecleaner.appdata.TotalMemoryStorageTask;
import com.gm.phonecleaner.appdata.TotalRamTask;
import com.gm.phonecleaner.listener.ObserverPartener.ObserverInterface;
import com.gm.phonecleaner.listener.ObserverPartener.ObserverUtils;
import com.gm.phonecleaner.listener.ObserverPartener.eventModel.EvbOnResumeAct;
import com.gm.phonecleaner.ui.BaseActivity;
import com.gm.phonecleaner.ui.BaseFragment;
import com.gm.phonecleaner.ui.main.MainActivity;
import com.gm.phonecleaner.utils.Config;
import com.gm.phonecleaner.widget.circularprogressindicator.CircularProgressIndicator;

import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentHome extends BaseFragment implements FunctionAdp.ClickItemListener, ObserverInterface {

    @BindView(R2.id.rcv_home_horizontal)
    RecyclerView rcvHorizontal;
    @BindView(R2.id.prg_storage_used)
    CircularProgressIndicator prgStorageUsed;
    @BindView(R2.id.prg_memory_used)
    CircularProgressIndicator prgMemoryUsed;
    @BindView(R2.id.tv_memory_used)
    TextView tvMemoryUsed;
    @BindView(R2.id.tv_storage_used)
    TextView tvStorageUsed;

    private FunctionAdp mFunctionAdpHorizontal;
    private FunctionAdp mFunctionAdpVertical;

    public static FragmentHome getInstance() {
        FragmentHome mFragmentHome = new FragmentHome();
        Bundle mBundle = new Bundle();
        mFragmentHome.setArguments(mBundle);
        return mFragmentHome;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_home_new, container, false);
        ButterKnife.bind(this, mView);
        ObserverUtils.getInstance().registerObserver(this);
        initView();
        initData();
        initControl();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadAds();
    }

    public void loadAds() {
        if (((MainActivity) requireActivity()).isLoadAdsNative()) {
            new Handler().postDelayed(() -> {
                AdmobHelp.getInstance().loadNativeFragment(getActivity(), getView());
            }, 1000);
        }
    }

    @OnClick(R2.id.im_main)
    void click() {
        openScreenFunction(Config.FUNCTION.PHONE_BOOST);
    }

    private void initView() {
        prgStorageUsed.setMaxProgress(100);
        prgMemoryUsed.setMaxProgress(100);

        int randomMemnory = new Random().nextInt(20) + 30;
        tvMemoryUsed.setText(randomMemnory + getString(R.string.percent));
        prgMemoryUsed.setCurrentProgress(randomMemnory);

        int randomStorage = new Random().nextInt(20);
        tvStorageUsed.setText(randomStorage + getString(R.string.percent));
        prgStorageUsed.setCurrentProgress(randomStorage);
    }

    private void initData() {
        mFunctionAdpHorizontal = new FunctionAdp(Config.LST_HOME_HORIZONTAL, Config.TYPE_DISPLAY_ADAPTER.HORIZOLTAL);
        rcvHorizontal.setAdapter(mFunctionAdpHorizontal);
        getDataRamMemory();
    }

    public void getDataRamMemory() {
        new TotalRamTask((useRam, totalRam) -> {
            prgMemoryUsed.setCurrentProgress(0);
            float progress = (float) useRam / (float) totalRam;
            if (tvMemoryUsed != null && prgMemoryUsed != null && getActivity() != null) {
                tvMemoryUsed.setText((int) (progress * 100) + getActivity().getString(R.string.percent));
                prgMemoryUsed.setCurrentProgress((int) (progress * 100));
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new TotalMemoryStorageTask((useMemory, totalMemory) -> {
            prgStorageUsed.setCurrentProgress(0);
            float progress = (float) useMemory / (float) totalMemory;
            if (tvStorageUsed != null && prgStorageUsed != null && getActivity() != null) {
                tvStorageUsed.setText((int) (progress * 100) + getActivity().getString(R.string.percent));
                prgStorageUsed.setCurrentProgress((int) (progress * 100));
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initControl() {
        mFunctionAdpHorizontal.setmClickItemListener(this);
    }

    @Override
    public void itemSelected(Config.FUNCTION mFunction) {
        openScreenFunction(mFunction);
    }

    @Override
    public void notifyAction(Object action) {
        if (action instanceof EvbOnResumeAct) {
            getDataRamMemory();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObserverUtils.getInstance().removeObserver(this::notifyAction);
    }
}
