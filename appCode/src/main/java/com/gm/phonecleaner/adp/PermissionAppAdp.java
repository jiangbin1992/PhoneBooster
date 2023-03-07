package com.gm.phonecleaner.adp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gm.phonecleaner.R;import com.gm.phonecleaner.R2;
import com.gm.phonecleaner.utils.Config;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PermissionAppAdp extends RecyclerView.Adapter<PermissionAppAdp.ViewHolder> {

    private List<Config.PERMISSION_DANGEROUS> lstData;

    public PermissionAppAdp(List<Config.PERMISSION_DANGEROUS> lstData) {
        this.lstData = lstData;
    }

    @NonNull
    @Override
    public PermissionAppAdp.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_permission_app, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionAppAdp.ViewHolder holder, int position) {
        if (lstData.get(position) != null)
            holder.binData(lstData.get(position));
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.tv_content)
        TextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void binData(Config.PERMISSION_DANGEROUS mPermissionDangerous) {
            tvTitle.setText(mPermissionDangerous.title);
            tvContent.setText(mPermissionDangerous.description);
        }
    }
}
