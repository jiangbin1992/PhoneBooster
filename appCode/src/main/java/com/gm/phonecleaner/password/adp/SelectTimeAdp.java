package com.gm.phonecleaner.password.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.password.model.LockAutoTime;

import java.util.List;

/**
 * Created by xian on 2017/2/17.
 */

public class SelectTimeAdp extends RecyclerView.Adapter<SelectTimeAdp.ViewHolder> {

    private List<LockAutoTime> mTimeList;
    private Context mContext;
    private String title = "";
    private OnItemClickListener listener;

    public SelectTimeAdp(List<LockAutoTime> timeList, Context context) {
        mTimeList = timeList;
        mContext = context;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectTimeAdp.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectTimeAdp.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lock_select_time, null));
    }

    @Override
    public void onBindViewHolder(SelectTimeAdp.ViewHolder holder, final int position) {
        final LockAutoTime info = mTimeList.get(position);
        holder.mItemTime.setText(info.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (position == mTimeList.size() - 1)
                        listener.onItemClick(info, true);
                    else
                        listener.onItemClick(info, false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTimeList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(LockAutoTime info, boolean isLast);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mItemTime;
        public View mLine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemTime = itemView.findViewById(R.id.item_time);
            mLine = itemView.findViewById(R.id.line);
        }
    }
}
