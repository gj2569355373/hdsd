package base;


import android.graphics.Typeface;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;
import com.sina.sinavideo.sdk.utils.VDApplication;
import com.sina.sinavideo.sdk.utils.VDResolutionManager;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.zchd.hdsd.Bin.Constants;


import java.io.File;

import java.lang.reflect.Field;



/**
 * Created by GJ on 2016/9/18.
 */
public class IcssApplication extends MultiDexApplication {
    private Typeface kaiti;//设置字体
    protected static IcssApplication context;
//    protected static YWIMKit mIMKit=null;
    private static String APP_KEY="";
    private static String userid="";
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        initplayer();
//        changeTypeface();
        noHttp();
        initPlatforms();
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
//        UMConfigure.init(this,"5a12384aa40fa3551f0001d1"
//                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
        UMConfigure.init(this,Constants.UMENGAPPKEY,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        // 微信 appid appsecret
        PlatformConfig.setWeixin(Constants.WEIXINAPPID, Constants.WEIXINAPPSECRET);
        // 新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo(Constants.WEIBOAPPKEY, Constants.WEBOAPPSECRET,"http://www.hongdedu.cn");
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
