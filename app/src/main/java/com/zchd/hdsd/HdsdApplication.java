package com.zchd.hdsd;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.component.AppComponent;
import com.zchd.hdsd.business.component.DaggerAppComponent;
import com.zchd.hdsd.business.model.ActivityModule;
import com.zchd.library.sqllites.SharedPreferences_operate;
import com.zchd.library.util.AppConfig;

import base.IcssApplication;

/**
 * Created by GJ on 2018/3/21.
 */
public class HdsdApplication extends IcssApplication {
    private static int AvailableHight=0;
    private String wechat_braodcast_type;
    public static  String mobile;
    public static String hasUnRead = "0";
    public static  String token="";//锟街伙拷锟斤拷锟斤拷
    public static  String password;//锟街伙拷锟斤拷锟斤拷
    public static  String TelephonyMgr="";
    public static  String qqName="";
    public static  String weixinName="";
    public static  String courseId="";
    public static  String weiboName="";
    public static  String avatar="";//头像路径
    public static  String nickname="";//name
    public static  String id="";//id
    private SharedPreferences_operate operate;
    //activity组件
    private ActivityComponent activityComponent;
    //application组件
    private AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        inits();
        Bugtags.start(AppConfig.BUGTAGS_KEY, this, Bugtags.BTGInvocationEventNone,bugtags()); //bugtags key
    }
    public static HdsdApplication get(Context context) {
        return (HdsdApplication) context.getApplicationContext();
    }
    private void inits() {
        // TODO Auto-generated method stub
        operate = new SharedPreferences_operate("login", this);
        token = operate.getString("token" );
        mobile = operate.getString("username");
        password = operate.getString("password");
        TelephonyManager TelephonyMgrs = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        TelephonyMgr = TelephonyMgrs.getDeviceId();
    }
    private BugtagsOptions bugtags(){
        BugtagsOptions options = new BugtagsOptions.Builder().
                trackingLocation(false).       //是否获取位置，默认 true
                trackingCrashLog(true).       //是否收集闪退，默认 true
                trackingConsoleLog(true).     //是否收集控制台日志，默认 true
                trackingUserSteps(false).      //是否跟踪用户操作步骤，默认 true
                crashWithScreenshot(false).    //收集闪退是否附带截图，默认 true
                trackingAnr(true).              //收集 ANR，默认 false
                trackingBackgroundCrash(true).  //收集 独立进程 crash，默认 false
                versionName(BuildConfig.VERSION_NAME).         //自定义版本名称，默认 app versionName
                versionCode(BuildConfig.VERSION_CODE).              //自定义版本号，默认 app versionCode
                trackingNetworkURLFilter("(.*)").//自定义网络请求跟踪的 url 规则，默认 null
                enableUserSignIn(true).            //是否允许显示用户登录按钮，默认 true
                startAsync(false).    //设置 为 true 则 SDK 会在异步线程初始化，节省主线程时间，默认 false
                startCallback(null).            //初始化成功回调，默认 null
                remoteConfigDataMode(Bugtags.BTGDataModeProduction).//设置远程配置数据模式，默认Bugtags.BTGDataModeProduction 参见[文档](https://docs.bugtags.com/zh/remoteconfig/android/index.html)
                remoteConfigCallback(null).//设置远程配置的回调函数，详见[文档](https://docs.bugtags.com/zh/remoteconfig/android/index.html)
                enableCapturePlus(false).        //是否开启手动截屏监控，默认 false，参见[文档](https://docs.bugtags.com/zh/faq/android/capture-plus.html)
                //设置 log 记录的行数
                        extraOptions(Bugtags.BTGConsoleLogCapacityKey, 1000).//设置控制台日志行数的控制 value > 0，默认500
                extraOptions(Bugtags.BTGBugtagsLogCapacityKey, 500).//控制 Bugtags log 行数的控制 value > 0，默认1000
                extraOptions(Bugtags.BTGUserStepLogCapacityKey, 1000).//控制用户操作步骤行数的控制 value > 0，默认1000
                extraOptions(Bugtags.BTGNetworkLogCapacityKey, 20).//控制网络请求数据行数的控制 value > 0，默认20
                build();
        return options;
    }
    public void setWechat_braodcast_type(String wechat_braodcast_type) {
        this.wechat_braodcast_type = wechat_braodcast_type;
    }
    public String getWechat_braodcast_type() {
        return wechat_braodcast_type;
    }
    public static int getAvailableHight() {
        return AvailableHight;
    }

    public static void setAvailableHight(int availableHight) {
        AvailableHight = availableHight;
    }
    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
    public void initActivityComponent() {
        this.activityComponent = getAppComponent().plus(new ActivityModule());
    }
    public AppComponent getAppComponent() {
        return appComponent;
    }
    private void initAppComponent() {
        this.appComponent = DaggerAppComponent.create();
    }
}
