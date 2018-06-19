package com.zchd.hdsd.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zchd.hdsd.R;
import com.zchd.library.Anim.AnimUtil;



/**
 * Created by GJ on 2017/2/9.
 */
public class LearningShiziPop {
    private PopupWindow mPopupWindow;
    public void showAlertDialog(Activity activity,View views,String text,String imageurl){
        View view = activity.getLayoutInflater().inflate(R.layout.learningpop, null, false);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.showAtLocation(views, Gravity.CENTER, 0, 0);
        AnimUtil animUtil=new AnimUtil();
        view.findViewById(R.id.learning_relativeLayout).setAnimation(animUtil.getSetAnimations(700,null));
        view.findViewById(R.id.learning_relativeLayout).setOnClickListener(view1 -> {

        });
        view.findViewById(R.id.learning_linear).setOnClickListener(view1 -> dimms());
        TextView tx=(TextView) view.findViewById(R.id.learningpop_text);
        tx.setText(text);
        GlideRound(imageurl, (ImageView) view.findViewById(R.id.learningpop_image),R.drawable.head_pic,activity);
    }
    public void dimms(){
        if (mPopupWindow!=null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }

    /*
        * 加载圆形图片
        *
        * */
    public void GlideRound(String path, ImageView imageView,int err, Activity context)
    {
        Glide.with(context).load((path==""||path==null)?err:path)
                .apply(new RequestOptions().circleCrop())
                .into(imageView);
    }
}
