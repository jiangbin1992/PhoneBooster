package com.gm.phonecleaner.ui.cleanNotification;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.adp.NotificationCleanAdp;
import com.gm.phonecleaner.listener.ObserverPartener.ObserverInterface;
import com.gm.phonecleaner.listener.ObserverPartener.ObserverUtils;
import com.gm.phonecleaner.listener.ObserverPartener.eventModel.EvbAddListNoti;
import com.gm.phonecleaner.model.NotifiModel;
import com.gm.phonecleaner.ui.BaseActivity;
import com.gm.phonecleaner.service.NotificationListener;
import com.gm.phonecleaner.service.NotificationUtil;
import com.gm.phonecleaner.utils.Config;
import com.gm.phonecleaner.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationCleanActivity extends BaseActivity implements ObserverInterface {

    @BindView(R2.id.im_back_toolbar)
    ImageView imBackToolbar;
    @BindView(R2.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R2.id.id_menu_toolbar)
    ImageView imActionToolbar;
    @BindView(R2.id.rcv_notification)
    RecyclerView rcvNotificaiton;
    @BindView(R2.id.ll_empty)
    View llEmpty;

    private NotificationCleanAdp mNotificationCleanAdp;
    private List<NotifiModel> lstNotifi = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_notification);
        ButterKnife.bind(this);
        ObserverUtils.getInstance().registerObserver(this);
        initView();
        initData();
    }

    private void initView() {
        imBackToolbar.setVisibility(View.VISIBLE);
        tvToolbar.setText(getString(R.string.notification_manager));
        imActionToolbar.setVisibility(View.VISIBLE);
        imActionToolbar.setImageResource(R.drawable.ic_settings);
        imActionToolbar.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        /**/
    }

    private void initData() {
        mNotificationCleanAdp = new NotificationCleanAdp(lstNotifi);
        rcvNotificaiton.setAdapter(mNotificationCleanAdp);
        mNotificationCleanAdp.setmItemClickListener(mNotifiModel -> {
            if (mNotifiModel != null) {
                PendingIntent mPendingIntent = mNotifiModel.barNotification.getNotification().contentIntent;
                Intent intent = new Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    if (mPendingIntent != null) {
                        mPendingIntent.send(this, 0, intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Remove item from backing list here
                int position = viewHolder.getAdapterPosition();
                lstNotifi.remove(position);
                mNotificationCleanAdp.notifyItemRemoved(position);
                if (NotificationListener.getInstance() != null)
                    NotificationListener.getInstance().removeItemLst(position);
                if (lstNotifi.size() == 0)
                    llEmpty.setVisibility(View.VISIBLE);
            }
        });

        itemTouchHelper.attachToRecyclerView(rcvNotificaiton);
        if (NotificationListener.getInstance() != null) {
            lstNotifi.addAll(NotificationListener.getInstance().getLstSave());
            mNotificationCleanAdp.notifyDataSetChanged();
            new Handler().postDelayed(() -> {
                if (SystemUtil.checkDNDDoing())
                    NotificationUtil.getInstance().postNotificationSpam(lstNotifi.get(0).barNotification, lstNotifi.size());
            }, 500);
        }
    }

    @OnClick({R2.id.id_menu_toolbar, R2.id.tv_clean})
    void click(View mView) {
        int id = mView.getId();
        if (id == R.id.id_menu_toolbar) {
            startActivity(new Intent(this, NotificationCleanSettingActivity.class));
        } else if (id == R.id.tv_clean) {
            cleanAll();
        }
    }

    public void cleanAll() {
        lstNotifi.clear();
        mNotificationCleanAdp.notifyDataSetChanged();
        llEmpty.setVisibility(View.VISIBLE);
        if (NotificationListener.getInstance() != null)
            NotificationListener.getInstance().removeAllItem();
        openScreenResult(Config.FUNCTION.NOTIFICATION_MANAGER);
        finish();
    }

    @Override
    public void notifyAction(Object action) {
        if (action instanceof EvbAddListNoti) {
            List<NotifiModel> notification = ((EvbAddListNoti) action).lst;
//            if (notification.size() == 1) {
//                lstNotifi.add(0, notification.get(0));
//            } else {
//                lstNotifi.clear();
//                lstNotifi.addAll(notification);
//            }
            lstNotifi.clear();
            lstNotifi.addAll(notification);
            mNotificationCleanAdp.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ObserverUtils.getInstance().removeObserver(this::notifyAction);
    }

}
