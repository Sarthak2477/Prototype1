package com.prototype1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class AppViewHolder extends RecyclerView.ViewHolder {
    ImageView appIcon;
    TextView appName;
    TextView stat;

    public AppViewHolder(@NonNull View itemView) {
        super(itemView);
        appIcon = itemView.findViewById(R.id.appIcon);
        appName = itemView.findViewById(R.id.appName);
        stat = itemView.findViewById(R.id.stat);
    }
}
