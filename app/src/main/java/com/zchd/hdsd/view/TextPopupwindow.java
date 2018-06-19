package com.zchd.hdsd.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zchd.hdsd.R;


/**
 * Created by GJ on 2017/3/15.
 */
public class TextPopupwindow {
    PopupWindow mPopupWindow=null;
    View view;
    public void show(Activity activity,View views,String text){
        view = activity.getLayoutInflater().inflate(R.layout.textpopupwindow, null, false);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.showAtLocation(views, Gravity.CENTER, 0, 0);
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dimms();
                return true;
            }
            return false;
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dimms();
            }
        });
//        view.findViewById(R.id.textpop_view2).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dimms();
//            }
//        });
//        view.findViewById(R.id.textpop_view1).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dimms();
//            }
//        });
        TextView textView= (TextView) view.findViewById(R.id.textpop_textview);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        textView.setText(text);
    }
    private void dimms(){
        if (mPopupWindow!=null)
        {
            mPopupWindow.dismiss();
            mPopupWindow=null;
        }
    }
}
