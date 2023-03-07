package com.gm.phonecleaner.ui.antivirus.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.adp.AppSelectAdp;
import com.gm.phonecleaner.dialog.DialogAppInfor;
import com.gm.phonecleaner.model.TaskInfo;
import com.gm.phonecleaner.ui.BaseFragment;
import com.gm.phonecleaner.ui.antivirus.AntivirusActivity;
import com.gm.phonecleaner.utils.Config;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class ListAppDangerousFragment extends BaseFragment {
    @BindView(R2.id.im_back_toolbar)
    ImageView imBackToolbar;
    @BindView(R2.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R2.id.rcv_app)
    RecyclerView rcvApp;
    @BindView(R2.id.tv_title)
    TextView tvTitle;

    private AppSelectAdp mAppSelectAdp;
    private List<TaskInfo> lstApp = new ArrayList<>();
    private int positionSelect = -1;
    private ClickButtonListener mClickButtonListener;

    public static ListAppDangerousFragment getInstance(ClickButtonListener mClickButtonListener) {
        ListAppDangerousFragment mListAppDangerousFragment = new ListAppDangerousFragment();
        mListAppDangerousFragment.mClickButtonListener = mClickButtonListener;
        return mListAppDangerousFragment;
    }

    public interface ClickButtonListener {
        void onClickSkipAllListerner();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list_app_dangerous, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        imBackToolbar.setVisibility(View.VISIBLE);
        tvToolbar.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        lstApp.clear();
        lstApp.addAll(((AntivirusActivity) getActivity()).getLstAppDangerous());
        tvTitle.setText(getString(R.string.app_dangerous, String.valueOf(lstApp.size())));
        mAppSelectAdp = new AppSelectAdp(getActivity(), AppSelectAdp.TYPE_SELECT.ONLY_VIEW, lstApp);
        mAppSelectAdp.setmItemClickListener(position -> {
            positionSelect = position;
            DialogAppInfor.getInstance(lstApp.get(position), mTaskInfo -> {
                Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                intent.setData(Uri.parse("package:" + mTaskInfo.getPackageName()));
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                startActivityForResult(intent, Config.UNINSTALL_REQUEST_CODE);
            }).show(getFragmentManager(), "");
        });
        rcvApp.setAdapter(mAppSelectAdp);
    }

    @OnClick({R2.id.im_back_toolbar, R2.id.tv_skip_all})
    void click(View mView) {
        int id = mView.getId();
        if (id == R.id.im_back_toolbar) {
            getFragmentManager().popBackStack();
        } else if (id == R.id.tv_skip_all) {
            if (mClickButtonListener != null)
                mClickButtonListener.onClickSkipAllListerner();
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.UNINSTALL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ((AntivirusActivity) getActivity()).getLstAppDangerous().remove(positionSelect);
                lstApp.remove(positionSelect);
                ((AntivirusActivity) getActivity()).updateData();
                mAppSelectAdp.notifyItemRemoved(positionSelect);
                tvTitle.setText(getString(R.string.app_dangerous, String.valueOf(lstApp.size())));
            }
        }
    }
}
