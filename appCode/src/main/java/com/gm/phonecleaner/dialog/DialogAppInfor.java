package com.gm.phonecleaner.dialog;

import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.adp.PermissionAppAdp;
import com.gm.phonecleaner.model.TaskInfo;
import com.gm.phonecleaner.utils.Toolbox;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogAppInfor extends DialogFragment {

    @BindView(R2.id.im_iconApp)
    RoundedImageView imIconApp;
    @BindView(R2.id.tv_appname)
    TextView tvAppName;
    @BindView(R2.id.tv_version)
    TextView tvVersion;
    @BindView(R2.id.tv_date)
    TextView tvDate;
    @BindView(R2.id.tv_size)
    TextView tvSize;
    @BindView(R2.id.rcv_permission)
    RecyclerView rcvPermisson;

    private TaskInfo mTaskInfo;
    private PermissionAppAdp mPermissionAppAdp;
    private ClickButtonListner mClickButtonListner;

    public static DialogAppInfor getInstance(TaskInfo mTaskInfo, ClickButtonListner mClickButtonListner) {
        DialogAppInfor mDialogAppInfor = new DialogAppInfor();
        mDialogAppInfor.mTaskInfo = mTaskInfo;
        mDialogAppInfor.mClickButtonListner = mClickButtonListner;
        return mDialogAppInfor;
    }

    public interface ClickButtonListner {
        void onClickUninstallListener(TaskInfo mTaskInfo);
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        String t = getClass().getSimpleName();
        if (manager.findFragmentByTag(t) == null) {
            super.show(manager, t);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.7f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
//            windowParams.windowAnimations = R.style.DialogAnimation;
            window.setAttributes(windowParams);
//            dialog.setCanceledOnTouchOutside(false);
//            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_app_infor, null);
        ButterKnife.bind(this, mView);
        dialogBuilder.setView(mView);
        try {
            initData();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return dialogBuilder.create();
    }

    private void initData() throws PackageManager.NameNotFoundException {
        tvVersion.setSelected(true);
        PackageManager packageManager = getContext().getPackageManager();
        imIconApp.setImageDrawable(mTaskInfo.getAppinfo().loadIcon(packageManager));
        if (!TextUtils.isEmpty(mTaskInfo.getTitle()))
            tvAppName.setText(mTaskInfo.getTitle());
        PackageInfo pinfo = packageManager.getPackageInfo(mTaskInfo.getPackageName(), 0);
        if (!TextUtils.isEmpty(pinfo.versionName))
            tvVersion.setText(pinfo.versionName);
        tvDate.setText(getString(R.string.date, Toolbox.longToDate(pinfo.firstInstallTime)));
        tvSize.setText(getString(R.string.size, Toolbox.getApkSize(getActivity(), mTaskInfo.getPackageName())));

        mPermissionAppAdp = new PermissionAppAdp(mTaskInfo.getLstPermissonDangerous());
        rcvPermisson.setAdapter(mPermissionAppAdp);
    }

    @OnClick({R2.id.tv_cancel, R2.id.tv_uninstall})
    void click(View mView) {
        int id = mView.getId();
        if (id == R.id.tv_cancel) {
        } else if (id == R.id.tv_uninstall) {
            if (mClickButtonListner != null)
                mClickButtonListner.onClickUninstallListener(mTaskInfo);
        }
        dismiss();
    }
}
