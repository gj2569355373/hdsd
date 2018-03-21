package com.zchd.library.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.os.Build;
import android.os.Handler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by GJ on 2017/3/21.
 */
public class DetectCalendarLaunchRunnable implements Runnable {
    Handler mHandler;
    ActivityManager mActivityManager;

    public DetectCalendarLaunchRunnable(Handler mHandler, ActivityManager mActivityManager) {
        this.mHandler = mHandler;
        this.mActivityManager = mActivityManager;
    }

    @Override
    public void run() {
        String[] activePackages;
        ComponentName cn = mActivityManager.getRunningTasks(1).get(0).topActivity;
//        ListappList = am.getRunningAppProcesses();
        String packageName = cn.getPackageName();

        if (packageName != null && packageName.equals("包名")) {//......如果要在这里停止含有定时执行的服务,则在停止之前需要先取消该定时器}
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            activePackages = getActivePackages();
        } else {
            activePackages = getActivePackagesCompat();
        }
        if (activePackages != null) {
            for (String activePackage : activePackages) {
                if (activePackage.equals("com.google.android.calendar")) {
                    //Calendar app is launched, do something
                }
            }
        }
        mHandler.postDelayed(this, 1000);
    }

    String[] getActivePackagesCompat() {
        final List<ActivityManager.RunningTaskInfo> taskInfo = mActivityManager.getRunningTasks(1);
        final ComponentName componentName = taskInfo.get(0).topActivity;
        final String[] activePackages = new String[1];
        activePackages[0] = componentName.getPackageName();
        return activePackages;
    }

    String[] getActivePackages() {
        final Set<String> activePackages = new HashSet<String>();
        final List<ActivityManager.RunningAppProcessInfo> processInfos = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                activePackages.addAll(Arrays.asList(processInfo.pkgList));
            }
        }
        return activePackages.toArray(new String[activePackages.size()]);
    }
}