package com.prototype1;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<AppViewHolder>{

    private List<AppInfo> appInfoList;

    public CustomAdapter(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        String appName = appInfoList.get(position).getAppName();
        String appStat = appInfoList.get(position).getUsageStat();
        Drawable icon = appInfoList.get(position).getAppIcon();

        holder.appIcon.setImageDrawable(icon);
        holder.appName.setText(appName);
        holder.stat.setText(appStat);

    }

    @Override
    public int getItemCount() {
        return appInfoList.size();
    }
}
