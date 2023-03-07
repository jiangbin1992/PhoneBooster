package com.gm.phonecleaner.password.act.main;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.password.adp.MainAdp;
import com.gm.phonecleaner.password.base.BaseFragment;
import com.gm.phonecleaner.password.model.CommLockInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xian on 2017/3/1.
 */

public class SysAppFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    @Nullable
    private List<CommLockInfo> data, list;
    @Nullable
    private MainAdp mMainAdp;

    @NonNull
    public static SysAppFragment newInstance(List<CommLockInfo> list) {
        SysAppFragment sysAppFragment = new SysAppFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) list);
        sysAppFragment.setArguments(bundle);
        return sysAppFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_lock_app_list;
    }

    @Override
    protected void init(View rootView) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        data = getArguments().getParcelableArrayList("data");
        mMainAdp = new MainAdp(getContext());
        mRecyclerView.setAdapter(mMainAdp);
        list = new ArrayList<>();
        for (CommLockInfo info : data) {
            if (info.isSysApp()) {
                list.add(info);
            }
        }
        mMainAdp.setLockInfos(list);
    }
}
