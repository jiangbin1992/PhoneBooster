package com.gm.phonecleaner.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.ads.control.AdmobHelp;
import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.ui.BaseActivity;
import com.gm.phonecleaner.ui.main.MainActivity;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

//    @BindView(R2.id.tv_content)
//    TextView tvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);
        AdmobHelp.getInstance().init(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //  YoYo.with(Techniques.SlideInUp).duration(2000).playOn(tvContent);
        new Handler().postDelayed(() -> {
            Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }, 4000);
    }
}
