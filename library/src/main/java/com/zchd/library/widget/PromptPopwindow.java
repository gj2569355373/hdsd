package com.zchd.library.widget;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zchd.library.R;


/**
 * Created by GJ on 2016/9/22.
 */
public class PromptPopwindow {
    private PopupWindow mPopupWindow;
    View view;
    public void showAlertDialog(Activity activity, View views, String text, String yes,String no,final IPopupWindow iPopupWindow){
        if (activity==null)
            return;
         view = activity.getLayoutInflater().inflate(R.layout.confirm_dialog, null, false);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.showAtLocation(views, Gravity.CENTER, 0, 0);
        TextView tx=(TextView) view.findViewById(R.id.dialog_textview);
        tx.setText(text);
        TextView v1= (TextView) view.findViewById(R.id.diglog_cancel);
        v1.setText(no);
        TextView v2= (TextView) view.findViewById(R.id.dialog_ok);
        v2.setText(yes);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dimms();
                    return true;
                }
                return false;
            }
        });
        view.findViewById(R.id.diglog_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dimms();
                iPopupWindow.cancel();

            }
        });
        view.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //	http_delect(x);
                dimms();
                iPopupWindow.Oks();

            }
        });
    }
    public void dimms(){
        if (mPopupWindow!=null)
            mPopupWindow.dismiss();
        mPopupWindow=null;
    }

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }
}
