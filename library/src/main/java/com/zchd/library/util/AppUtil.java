package com.zchd.library.util;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by GJ on 2016/12/21.
 */
public class AppUtil {
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getVersion(Application application) {
        try {
            PackageManager manager = application.getPackageManager();
            PackageInfo info = manager.getPackageInfo(application.getPackageName(), 0);
            String version = info.versionName;
            return  "V"+version;
        } catch (Exception e) {
            e.printStackTrace();
            return "V1.0";
        }
    }
}
