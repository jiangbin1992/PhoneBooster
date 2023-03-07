package com.gm.phonecleaner.ui.main.tool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.control.AdmobHelp;
import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.adp.FunctionAdp;
import com.gm.phonecleaner.ui.BaseFragment;
import com.gm.phonecleaner.utils.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTool extends BaseFragment implements FunctionAdp.ClickItemListener {

    @BindView(R2.id.rcv_clean_boost)
    RecyclerView rcvCleanBoost;
    @BindView(R2.id.rcv_security)
    RecyclerView rcvSecurity;

    private FunctionAdp mFunctionCleanBoost;
    private FunctionAdp mFunctionSecurity;


    public static FragmentTool getInstance() {
        FragmentTool mFragmentTool = new FragmentTool();
        Bundle mBundle = new Bundle();
        mFragmentTool.setArguments(mBundle);
        return mFragmentTool;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AdmobHelp.getInstance().loadBannerFragment(getActivity(), view);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_tool_new, container, false);
        ButterKnife.bind(this, mView);
        initView();
        initData();
        initControl();
        return mView;
    }

    private void initView() {

    }

    private void initData() {
        mFunctionCleanBoost = new FunctionAdp(Config.LST_TOOL_CLEAN_BOOST, Config.TYPE_DISPLAY_ADAPTER.VERTICAL);
        rcvCleanBoost.setAdapter(mFunctionCleanBoost);

        mFunctionSecurity = new FunctionAdp(Config.LST_TOOL_SECURITY, Config.TYPE_DISPLAY_ADAPTER.VERTICAL);
        rcvSecurity.setAdapter(mFunctionSecurity);
    }

    private void initControl() {
        mFunctionCleanBoost.setmClickItemListener(this);
        mFunctionSecurity.setmClickItemListener(this);
    }

    @Override
    public void itemSelected(Config.FUNCTION mFunction) {
        openScreenFunction(mFunction);
    }
}
