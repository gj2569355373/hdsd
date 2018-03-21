package com.zchd.library.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by GJ on 2017/1/1.
 */
public abstract class MThread extends Thread{
    Handler handler=null;

    public MThread(Runnable runnable, Looper looper) {
        super(runnable);
        if (looper!=null)
            handler=new Handler(looper);
    }

    @Override
    public void run() {
        super.run();
        if (handler!=null)
            handler.post(new Runnable() {
                @Override
                public void run() {
                    runno();
                }
            });
    }
    public abstract void runno();
}
