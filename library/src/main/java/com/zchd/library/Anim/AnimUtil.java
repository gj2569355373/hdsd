package com.zchd.library.Anim;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by GJ on 2016/12/2.
 */
public class AnimUtil {
    public static Animation getAnimation(){
        Animation anim=null;

        return anim;
    }
    /**
	 * 动态设置渐变动画,改变透明度
     * animationListener监听
	 */
    private AlphaAnimation getAlphaAnimation(Animation.AnimationListener animationListener,long time){
        //改变透明度
        AlphaAnimation anim=new AlphaAnimation(0, 1);//透明到完全不透明
        anim.setDuration(time);////动画执行时间
        anim.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        if (animationListener!=null)
            anim.setAnimationListener(animationListener);
        return anim;
    }
    /**
     * 移动动画
     * animationListener监听
     * X>0向右移动，Y>0向下移动
     * 以自身view为坐标原点；
     */
    private TranslateAnimation getTranslateAnimation(float x,float y,Animation.AnimationListener animationListener){
        TranslateAnimation anim=new TranslateAnimation(0, x, 0, y);
        anim.setDuration(1500);
        anim.setFillAfter(true);
        if (animationListener!=null)
            anim.setAnimationListener(animationListener);
        return anim;
    }
    /**
     * 缩放动画
     * animationListener监听
     */
    public ScaleAnimation getScaleAnimation(Animation.AnimationListener animationListener ,long time){
		/*ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue)
		*起始大小（fromX，fromY）
		*结束大小（toX，toY）
		*（pivotXValue，pivotYValue）缩放的定点
		*pivotXType——X轴相对位置
		*pivotYType——Y轴相对位置
		*ScaleAnimation.RELATIVE_TO_SELF相对于自己
		*/
        ScaleAnimation anim=new ScaleAnimation(0.01f, 1f, 0.01f, 1f, 	ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(time);
        anim.setFillAfter(true);
        if (animationListener!=null)
            anim.setAnimationListener(animationListener);
        return anim;
    }

    /**
     * 旋转动画
     * animationListener监听
     */
    public RotateAnimation getRotateAnimation(Animation.AnimationListener animationListener,long time){
        RotateAnimation anim= new RotateAnimation(0f, 180f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(time);
        anim.setRepeatCount(0);//重复的次数，默认为0；Animation.INFINITE可以使其一直旋转
//        anim.setRepeatMode(Animation.REVERSE);//指定的是该次动画达到终点后返回起点的方式;REVERSE指的是控件会按着之前的路径反转，而RESTART比较单一，达到终点后，迅速回到起点。
        anim.setFillAfter(true);
        if (animationListener!=null)
            anim.setAnimationListener(animationListener);
        return anim;
    }
    /**
     * 综合动画
     * animationListener监听
     */
    private AnimationSet getSetAnimation(float x,float y,Animation.AnimationListener animationListener){
        //参数AnimationSet(shareInterpolator)是否共享加速器
        //shareInterpolator定义了动画变化速率
        AnimationSet anim=new AnimationSet(true);
        anim.setDuration(1500);
        anim.setRepeatCount(0);
        anim.setFillAfter(true);
        anim.setInterpolator(new AccelerateInterpolator());//（也可以单独设置）
        AlphaAnimation anim1= getAlphaAnimation(null,1500);//渐变动画
        RotateAnimation anim2= getRotateAnimation(null,1500);//旋转动画
        ScaleAnimation anim3=getScaleAnimation(null,1500);//改变大小
        TranslateAnimation anim4=getTranslateAnimation(x,0,null);//移动
        TranslateAnimation anim5=getTranslateAnimation(0,y,null);//移动
        //等待2000毫秒开始旋转
        //	anim2.setStartOffset(2000);
        if (animationListener!=null)
            anim.setAnimationListener(animationListener);
        anim.addAnimation(anim1);
        anim.addAnimation(anim2);
        anim.addAnimation(anim3);
        anim.addAnimation(anim4);
        return anim;
    }
    /**
     * 渐隐渐现动画
     * animationListener监听
     */
    public AnimationSet getSetAnimations(long time,Animation.AnimationListener animationListener){
        //参数AnimationSet(shareInterpolator)是否共享加速器
        //shareInterpolator定义了动画变化速率
        AnimationSet anim=new AnimationSet(true);
        anim.setDuration(time);
        anim.setRepeatCount(0);
        anim.setFillAfter(true);
        anim.setInterpolator(new AccelerateInterpolator());//（也可以单独设置）
        AlphaAnimation anim1= getAlphaAnimation(null,time);//渐变动画RotateAnimation anim2= getRotateAnimation(null);//旋转动画
        ScaleAnimation anim3=getScaleAnimation(null,time);//改变大小
        //等待2000毫秒开始旋转
        //	anim2.setStartOffset(2000);
        if (animationListener!=null)
            anim.setAnimationListener(animationListener);
        anim.addAnimation(anim1);
        anim.addAnimation(anim3);
        return anim;
    }
}
