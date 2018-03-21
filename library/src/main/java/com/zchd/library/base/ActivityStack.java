package com.zchd.library.base;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Stack;


/**
 * Created by chenboling on 16/8/5.
 */
public class ActivityStack {

//    private Stack<Activity> activityStack;
    private Stack<WeakReference>stackWeak;
    private static final ActivityStack instance = new ActivityStack();

    private ActivityStack() {
        stackWeak = new Stack<>();
    }

    public static ActivityStack create() {
        return instance;
    }

    /**
     * 获取当前Activity栈中元素个数
     */
    public int getCount() {
        return stackWeak.size();
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        Log.e("tag",activity.getLocalClassName());
        stackWeak.add(new WeakReference<Activity>(activity));
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public WeakReference topActivity() {
        if (stackWeak == null) {
            throw new NullPointerException("Activity stack is Null,your Activity must extend BaseActivity");
        }
        if (stackWeak.isEmpty()) {
            return null;
        }
        WeakReference activity = stackWeak.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        WeakReference activity = stackWeak.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(WeakReference activity) {
        if (activity != null) {
            stackWeak.remove(activity);
            ((Activity)activity.get()).finish();
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        for (WeakReference activity : stackWeak) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        for (WeakReference activity : stackWeak) {
            if (!(activity.getClass().equals(cls))) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = stackWeak.size(); i < size; i++) {
            if (null != stackWeak.get(i).get()) {
                ((Activity)(stackWeak.get(i)).get()).finish();
            }
        }
        stackWeak.clear();
    }

    @Deprecated
    public void AppExit(Context cxt) {
        appExit(cxt);
    }

    /**
     * 应用程序退出
     */
    public void appExit(Context context) {
        if (context==null)
        {
            Handler handler=null;
            for (int i = 0, size = stackWeak.size(); i < size; i++) {
                if (null != stackWeak.get(i).get()&&handler==null) {
                    handler=new Handler(((Activity)stackWeak.get(i).get()).getMainLooper());

                    break;
                }
            }
            if (handler!=null)
            {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            finishAllActivity();
                            Runtime.getRuntime().exit(0);
                        } catch (Exception e) {
                            Runtime.getRuntime().exit(-1);
                        }
                    }
                },3000);
            }
        }
        else {
            try {
                finishAllActivity();
                Runtime.getRuntime().exit(0);
            } catch (Exception e) {
                Runtime.getRuntime().exit(-1);
            }
        }
    }
}
