package com.gm.phonecleaner.adp;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.model.NotifiModel;
import com.gm.phonecleaner.utils.Toolbox;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationCleanAdp extends RecyclerView.Adapter<NotificationCleanAdp.ViewHolder> {

    private List<NotifiModel> lstNotifi;
    private Context mContext;
    private ItemClickListener mItemClickListener;

    public void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface ItemClickListener {
        void ItemClickSeleted(NotifiModel mNotifiModel);
    }
    public NotificationCleanAdp(List<NotifiModel> lstNotifi) {
        this.lstNotifi = lstNotifi;
    }

    @NonNull
    @Override
    public NotificationCleanAdp.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_notification_clean, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationCleanAdp.ViewHolder holder, int position) {
        holder.binData(lstNotifi.get(position));
    }

    @Override
    public int getItemCount() {
        return lstNotifi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.im_iconApp)
        RoundedImageView imIconApp;
        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.tv_content)
        TextView tvContent;
        @BindView(R2.id.tv_time)
        TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void binData(NotifiModel mNotifiModel) {
            if (mNotifiModel != null) {
                Glide.with(mContext).load(mNotifiModel.iconApp).into(imIconApp);
                Notification notification = mNotifiModel.barNotification.getNotification();
                Bundle bundle = notification.extras;
                if (bundle.get(Notification.EXTRA_TITLE) != null)
                    tvTitle.setText(bundle.get(Notification.EXTRA_TITLE).toString());
                else
                    tvTitle.setText(mNotifiModel.appName);
                if (bundle.get(Notification.EXTRA_TEXT) != null)
                    tvContent.setText(bundle.get(Notification.EXTRA_TEXT).toString());
                else
                    tvContent.setText("");
                tvTime.setText(Toolbox.getDistanceTime(mContext, mNotifiModel.barNotification.getPostTime()));

                itemView.setOnClickListener(v -> {
                    if (mItemClickListener!=null)
                        mItemClickListener.ItemClickSeleted(mNotifiModel);
                });
            }
        }
    }
}
