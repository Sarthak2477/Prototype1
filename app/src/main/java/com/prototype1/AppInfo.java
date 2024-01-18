package com.prototype1;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String appName;
    private String usageStat;
    private Drawable appIcon;

    public AppInfo(String appName, String usageStat, Drawable appIcon) {
        this.appName = appName;
        this.usageStat = usageStat;
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUsageStat() {
        return usageStat;
    }

    public void setUsageStat(String usageStat) {
        this.usageStat = usageStat;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", usageStat='" + usageStat + '\'' +
                ", appIcon='" + appIcon + '\'' +
                '}';
    }
}
