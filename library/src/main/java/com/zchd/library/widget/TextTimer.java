package com.zchd.library.widget;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by GJ on 2017/3/22.
 */
public class TextTimer{
    CountDownTimer countDownTimer;
    int timer,timers=60;
    GradientDrawable myGrad;
    public TextTimer(int timers) {
        this.timers = timers;
    }

    public void play(final TextView tv, final String textok,final String textTimeOut){
        tv.setClickable(false);
        this.timer=this.timers;
        if (myGrad==null)
             myGrad = (GradientDrawable)tv.getBackground();
        if (countDownTimer==null) {
            countDownTimer = new CountDownTimer(timer * 1000, 1000) {
                @Override
                public void onFinish() {
                    // done
                    tv.setText(textok);
                    tv.setClickable(true);
                    myGrad.setColor(Color.parseColor("#1AAD19"));
                    countDownTimer=null;
                }

                @Override
                public void onTick(long arg0) {
                    // 每1000毫秒回调的方法
                    tv.setText(textTimeOut + "(" + timer + ")");
                    tv.setTextColor(Color.WHITE);
                    myGrad.setColor(0xFFCACACA);
                    tv.setClickable(false);
                    timer--;
                }
            };
            countDownTimer.start();
        }
    }
    public void playtoo(final TextView tv, final String textok,final String textTimeOut){
        tv.setClickable(false);
        this.timer=this.timers;
        if (myGrad==null)
            myGrad = (GradientDrawable)tv.getBackground();
        if (countDownTimer==null) {
            countDownTimer = new CountDownTimer(timer * 1000, 1000) {
                @Override
                public void onFinish() {
                    // done
                    tv.setText(textok);
                    tv.setClickable(true);
                    tv.setTextColor(Color.parseColor("#000000"));
                    countDownTimer=null;
                }

                @Override
                public void onTick(long arg0) {
                    // 每1000毫秒回调的方法
                    tv.setText(textTimeOut + "(" + timer + ")");
                    tv.setTextColor(Color.GRAY);
                    tv.setClickable(false);
                    timer--;
                }
            };
            countDownTimer.start();
        }
    }
    public void finish(){
        if(countDownTimer!=null) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
    }

}
