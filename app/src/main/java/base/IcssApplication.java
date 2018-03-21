package base;

import android.app.Application;
import android.graphics.Typeface;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.sina.sinavideo.sdk.utils.VDApplication;
import com.sina.sinavideo.sdk.utils.VDResolutionManager;
import com.umeng.socialize.PlatformConfig;
import com.zchd.hdsd.Bin.Constants;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


/**
 * Created by GJ on 2016/9/18.
 */
public class IcssApplication extends Application {
    private Typeface kaiti;//设置字体
    private static IcssApplication context;
//    protected static YWIMKit mIMKit=null;
    private static String APP_KEY="";
    private static String userid="";
    public boolean login=false;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        initplayer();
        changeTypeface();
        noHttp();
    }

    public static IcssApplication getContext() {
        return context;
    }
    public void noHttp(){

    }
    private void initplayer() {
        // TODO Auto-generated method stub
        // 播放器初始化，要在app启动前进行初始化，才能解压出相应的解码器
        VDApplication.getInstance().initPlayer(this);
        VDResolutionManager.getInstance(this).init(VDResolutionManager.RESOLUTION_SOLUTION_NONE);
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/clpmn");
        // 如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            System.out.println("//不存在");
            file.mkdir();
        } else {
            System.out.println("//目录存在");
        }
    }
    private void initPlatforms() {
        // 微信 appid appsecret
        PlatformConfig.setWeixin(Constants.WEIXINAPPID, Constants.WEIXINAPPSECRET);
        // 新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo(Constants.WEIBOAPPKEY, Constants.WEBOAPPSECRET);
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone(Constants.QQAPPID, Constants.QQAPPSECRET);
    }

    public void changeTypeface() {
        kaiti = Typeface.createFromAsset(getAssets(), "fonts/kaiti.ttf");//路径
        try {
            Field field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null, kaiti);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
