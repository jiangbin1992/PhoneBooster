package com.gm.phonecleaner.password.widget;

import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.password.adp.MainAdp;
import com.gm.phonecleaner.password.model.CommLockInfo;
import com.gm.phonecleaner.password.mvp.contract.LockMainContract;
import com.gm.phonecleaner.password.mvp.p.LockMainPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xian on 2017/3/4.
 */

public class DialogSearch extends BaseDialog implements LockMainContract.View {

    private Context mContext;
    private EditText mEditSearch;
    private RecyclerView mRecyclerView;
    private ImageView mBtnBack;
    private MainAdp mMainAdp;
    private LockMainPresenter mLockMainPresenter;

    public DialogSearch(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().gravity = Gravity.TOP;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    protected float setWidthScale() {
        return 1;
    }

    @Nullable
    @Override
    protected AnimatorSet setEnterAnim() {
        return null;
    }

    @Nullable
    @Override
    protected AnimatorSet setExitAnim() {
        return null;
    }

    @Override
    protected void init() {
        mLockMainPresenter = new LockMainPresenter(this, mContext);
        mRecyclerView = findViewById(R.id.recycler_view);
        mEditSearch = findViewById(R.id.edit_search);
        mBtnBack = findViewById(R.id.btn_back);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mMainAdp = new MainAdp(mContext);
        mRecyclerView.setAdapter(mMainAdp);

        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(@NonNull Editable editable) {
                if (editable.length() == 0) {
                    mMainAdp.setLockInfos(new ArrayList<>());
                } else {
                    mLockMainPresenter.searchAppInfo(editable.toString(), commLockInfos -> mMainAdp.setLockInfos(commLockInfos));
                }
            }
        });
        mBtnBack.setOnClickListener(v -> dismiss());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.dialog_lock_search;
    }


    @Override
    public void loadAppInfoSuccess(List<CommLockInfo> list) {

    }

    @Override
    public void showProgressbar(boolean isShow) {

    }
}
