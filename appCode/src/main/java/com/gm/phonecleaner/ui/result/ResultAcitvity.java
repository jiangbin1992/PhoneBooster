package com.gm.phonecleaner.ui.result;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.control.AdmobHelp;
import com.airbnb.lottie.LottieAnimationView;
import com.best.now.myad.utils.PublicHelperKt;
import com.gm.phonecleaner.PhoneCleanerApp;
import com.gm.phonecleaner.R;
import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.adp.FunctionAdp;
import com.gm.phonecleaner.listener.ObserverPartener.ObserverUtils;
import com.gm.phonecleaner.listener.ObserverPartener.eventModel.EvbCheckLoadAds;
import com.gm.phonecleaner.listener.ObserverPartener.eventModel.EvbOnResumeAct;
import com.gm.phonecleaner.listener.ObserverPartener.eventModel.EvbOpenFunc;
import com.gm.phonecleaner.ui.BaseActivity;
import com.gm.phonecleaner.ui.ExitActivity;
import com.gm.phonecleaner.ui.main.MainActivity;
import com.gm.phonecleaner.utils.Config;
import com.gm.phonecleaner.utils.PreferenceUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class ResultAcitvity extends BaseActivity {

    @BindView(R2.id.rcv_funtion_suggest)
    RecyclerView rcvFunctionSuggest;
    @BindView(R2.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R2.id.im_back_toolbar)
    ImageView imBack;
    @BindView(R2.id.imgDone)
    LottieAnimationView imgDone;
    @BindView(R2.id.img_congratulations)
    LottieAnimationView imCongratulation;
    @BindView(R2.id.ll_infor)
    NestedScrollView scvInfor;
    @BindView(R2.id.ll_done)
    View llDone;
    @BindView(R2.id.ll_main_result)
    View llMainResut;
    @BindView(R2.id.ll_background)
    View llBackground;
    @BindView(R2.id.layout_padding)
    View llToolbar;
    @BindView(R2.id.tv_title)
    TextView tvTitle;

    private Config.FUNCTION mFunction;
    private FunctionAdp mFunctionAdp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_result);
        ButterKnife.bind(this);
        initView();
        initData();
        AdmobHelp.getInstance().loadNative(this);
    }

    private void initView() {
        llToolbar.setAlpha(0.0f);
        scvInfor.setAlpha(0.0f);
        if (getIntent() != null)
            mFunction = Config.getFunctionById(getIntent().getIntExtra(Config.DATA_OPEN_RESULT, 0));
        if (mFunction != null) {
            tvToolbar.setText(getString(mFunction.title));
            tvTitle.setText(getString(mFunction.titleResult));
            PreferenceUtils.setLastTimeUseFunction(mFunction);
        }
        imBack.setVisibility(View.VISIBLE);
        imgDone.playAnimation();
        imgDone.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                PublicHelperKt.showInterstitialAd(ResultAcitvity.this, new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        imgDone.pauseAnimation();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                llDone.setVisibility(View.GONE);
                                imCongratulation.setAnimation(mFunction != null ? mFunction.jsonResult : "restult_like.json");
                                if (mFunction != null)
                                    imCongratulation.setColorFilter(getResources().getColor(mFunction.color));
                                imCongratulation.playAnimation();
                                YoYo.with(Techniques.SlideInUp).duration(1000).playOn(scvInfor);
                                YoYo.with(Techniques.FadeIn).duration(1000).playOn(llBackground);
                                YoYo.with(Techniques.FadeIn).duration(1000).playOn(llToolbar);
                            }
                        }, 500);
                        return null;
                    }
                });

//                AdmobHelp.getInstance().showInterstitialAd(this,() -> {
//                    imgDone.pauseAnimation();
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            llDone.setVisibility(View.GONE);
//                            imCongratulation.setAnimation(mFunction != null ? mFunction.jsonResult : "restult_like.json");
//                            if (mFunction != null)
//                                imCongratulation.setColorFilter(getResources().getColor(mFunction.color));
//                            imCongratulation.playAnimation();
//                            YoYo.with(Techniques.SlideInUp).duration(1000).playOn(scvInfor);
//                            YoYo.with(Techniques.FadeIn).duration(1000).playOn(llBackground);
//                            YoYo.with(Techniques.FadeIn).duration(1000).playOn(llToolbar);
//                        }
//                    }, 500);
//                });

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initData() {
        List<Config.FUNCTION> lstShow = new ArrayList<>();
        for (int i = 0; i < Config.LST_SUGGEST.length; i++) {
            if (PreferenceUtils.checkLastTimeUseFunction(Config.LST_SUGGEST[i]))
                if (mFunction != null) {
                    if (Config.LST_SUGGEST[i] != mFunction)
                        lstShow.add(Config.LST_SUGGEST[i]);
                } else {
                    lstShow.add(Config.LST_SUGGEST[i]);
                }
        }
        Config.FUNCTION[] lstAdapter = new Config.FUNCTION[lstShow.size()];
        for (int i = 0; i < lstShow.size(); i++) {
            lstAdapter[i] = lstShow.get(i);
        }
        mFunctionAdp = new FunctionAdp(lstAdapter, Config.TYPE_DISPLAY_ADAPTER.SUGGEST);
        mFunctionAdp.setmClickItemListener(mFunction -> {
            if (PhoneCleanerApp.getInstance().getActivityList().size() == 1) {
                Intent intentAct = new Intent(this, MainActivity.class);
                intentAct.putExtra(Config.DATA_OPEN_FUNCTION, mFunction.id);
                startActivity(intentAct);
            } else {
                ObserverUtils.getInstance().notifyObservers(new EvbOpenFunc(mFunction));
            }
            finish();
        });
        rcvFunctionSuggest.setAdapter(mFunctionAdp);
        ObserverUtils.getInstance().notifyObservers(new EvbOnResumeAct());
    }

    @Override
    public void onBackPressed() {
        if (mFunction == Config.FUNCTION.DEEP_CLEAN) {
            ObserverUtils.getInstance().notifyObservers(new EvbOpenFunc(mFunction));
            finish();
        } else if (mFunction == null) {
            ExitActivity.exitApplicationAndRemoveFromRecent(this);
        } else {
            ObserverUtils.getInstance().notifyObservers(new EvbCheckLoadAds());
            super.onBackPressed();
        }
    }
}
