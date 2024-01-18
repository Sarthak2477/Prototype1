package com.prototype1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the toolbar as the action bar
        setSupportActionBar(toolbar);

        // Enable the home button for navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());

        if(mode == AppOpsManager.MODE_ALLOWED){
            List<AppInfo> appInfoList = getInstalledApps();

            for(AppInfo app : appInfoList){
                System.out.println(app);
            }
            RecyclerView recyclerView = findViewById(R.id.recycler_view); // Replace with the actual ID
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            CustomAdapter adapter = new CustomAdapter(appInfoList);
            recyclerView.setAdapter(adapter);

        }else{
            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            System.out.println("Permission Granted");
                        }
                    });

            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            activityResultLauncher.launch(intent);
        }

    }

    private List<AppInfo> getInstalledApps() {
        List<AppInfo> installedAppsList = new ArrayList<>();

        // Get the PackageManager
        PackageManager packageManager = getPackageManager();

        // Create an Intent for main activities labeled as launchers
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // Query for main activities
        List<ResolveInfo> resolvedInfos = packageManager.queryIntentActivities(mainIntent, 0);

        // Get the package names of user-facing apps
        Set<String> userAppsSet = new HashSet<>();
        for (ResolveInfo resolveInfo : resolvedInfos) {
            userAppsSet.add(resolveInfo.activityInfo.packageName);
        }

        // Get a list of all installed applications
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : installedApplications) {
            // Check if the app is a user-facing app
            if (userAppsSet.contains(appInfo.packageName)) {
                // Get the app name using the method introduced before
                String appName = getAppNameFromPackageInfo(packageManager, appInfo);
                Drawable appIcon = packageManager.getApplicationIcon(appInfo);
                String stats = getForegroundTimeForPackage(appInfo.packageName);

                // Add the package name to the list
                installedAppsList.add(new AppInfo(appName, stats, appIcon));
            }
        }

        return installedAppsList;
    }


    private String getAppNameFromPackageInfo(PackageManager packageManager, ApplicationInfo appInfo) {
        CharSequence appName = packageManager.getApplicationLabel(appInfo);
        return appName != null ? appName.toString() : "";
    }

    private String getForegroundTimeForPackage(String packageName) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);

        // Get the current time in milliseconds
        Calendar calendar = Calendar.getInstance();
        long endMillis = calendar.getTimeInMillis();

        // Set the time to the beginning of the day (midnight)
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startMillis = calendar.getTimeInMillis();

        Map<String, UsageStats> lUsageStatsMap = usageStatsManager.queryAndAggregateUsageStats(startMillis, endMillis);

        UsageStats usageStats = lUsageStatsMap.get(packageName);
        if (usageStats != null) {
            long totalTimeUsageInMillis = usageStats.getTotalTimeInForeground();
            // Convert milliseconds to hours, minutes, and seconds
            long seconds = totalTimeUsageInMillis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;

            // Calculate remaining minutes and seconds
            minutes %= 60;
            seconds %= 60;

            // Format the result
            return String.format("%02d hrs %02d mins %02d secs", hours, minutes, seconds);
        } else {
            // Handle the case when the package name is not found in the map
            return "Not available";
        }
    }
}