package com.zchd.library.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.zchd.library.entity.AppInfo;

import java.util.List;



/**
 * 获取手机应用程序
 * Created by GJ on 2017/3/20.
 */
public class AppInfoService {
    private Context context;
    private PackageManager pm;
    public AppInfoService(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        pm = context.getPackageManager();
    }

    /**
     * 得到手机中所有的应用程序信息
     * @return
     */
    public void getAppInfos(final List<AppInfo> appInfos , final ListAppInfo listAppInfo){

        new Thread(new Runnable() {
            @Override
            public void run() {

                //获取手机中所有安装的应用集合
                List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
                //遍历所有的应用集合
                for(ApplicationInfo info : applicationInfos){

                    AppInfo appInfo = new AppInfo();

                    //获取应用程序的图标
                    Drawable app_icon = info.loadIcon(pm);
                    appInfo.setApp_icon(app_icon);

                    //获取应用的名称
                    String app_name = info.loadLabel(pm).toString();
                    appInfo.setApp_name(app_name);

                    //获取应用的包名
                    String packageName = info.packageName;
                    appInfo.setPackagename(packageName);
                    try {
                        //获取应用的版本号
                        PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                        String app_version = packageInfo.versionName;
                        appInfo.setApp_version(app_version);
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    //判断应用程序是否是用户程序
                    boolean isUserApp = filterApp(info);
                    appInfo.setUserApp(isUserApp);
                    appInfos.add(appInfo);
                }
                if (context!=null) {
                    if (context.getMainLooper()!=null) {
                        Handler handler = new Handler(context.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listAppInfo.getListAppInfo(appInfos);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    //判断应用程序是否是用户程序
    public boolean filterApp(ApplicationInfo info) {
        //原来是系统应用，用户手动升级
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //用户自己安装的应用程序
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
    public interface ListAppInfo{
        public void getListAppInfo(List<AppInfo> listAppInfo);
    }
}
