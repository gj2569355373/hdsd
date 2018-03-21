package com.zchd.library.widget;

import android.app.Activity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zchd.library.R;


/**
 * Created by GJ on 2016/12/28.
 */
public class InputPopwindow {
    private PopupWindow mPopupWindow;
    View view;
    public void showAlertDialog(final Activity activity, View views, String text, String titletext , String yes, String no, final String toasttewxt , final IInputPopwindow iPopupWindow){
        mPopupWindow=null;
        view = activity.getLayoutInflater().inflate(R.layout.input_popwindow, null, false);
//        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setFocusable(true);
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//        mPopupWindow.setOutsideTouchable(true);
//        mPopupWindow.setTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.showAtLocation(views, Gravity.CENTER, 0, 0);
        TextView tx=(TextView) view.findViewById(R.id.dialog_textview);
        tx.setText(text);
        TextView v1= (TextView) view.findViewById(R.id.diglog_quxiao);
        v1.setText(no);
        TextView v2= (TextView) view.findViewById(R.id.dialog_yanzheng);
        v2.setText(yes);
        final EditText ed= (EditText) view.findViewById(R.id.license_code);
        ed.setText(titletext);
        ed.requestFocus();
//        InputMethodManager imm = (InputMethodManager) ed.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//        ed.setFocusable(true);
        v1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dimms();
                iPopupWindow.cancel();

            }
        });
        v2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //	http_delect(x);
                if (ed.getText().toString().trim().equals("")) {
                    Toast.makeText(activity,toasttewxt,Toast.LENGTH_SHORT).show();
                    return;
                }
                dimms();
                iPopupWindow.Oks(ed.getText().toString());

            }
        });
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    iPopupWindow.cancel();
                    dimms();
                    return true;
                }
                return false;
            }
        });
    }
    public void showAlertDialog(final Activity activity, View views, String text, String titletext ,String hintText, String yes, String no, final String toasttewxt , final IInputPopwindow iPopupWindow){
        mPopupWindow=null;
        view = activity.getLayoutInflater().inflate(R.layout.input_popwindow, null, false);
//        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setFocusable(true);
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//        mPopupWindow.setOutsideTouchable(true);
//        mPopupWindow.setTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.showAtLocation(views, Gravity.CENTER, 0, 0);
        TextView tx=(TextView) view.findViewById(R.id.dialog_textview);
        tx.setText(text);
        TextView v1= (TextView) view.findViewById(R.id.diglog_quxiao);
        v1.setText(no);
        TextView v2= (TextView) view.findViewById(R.id.dialog_yanzheng);
        v2.setText(yes);
        final EditText ed= (EditText) view.findViewById(R.id.license_code);
        ed.setText(titletext);
        ed.setHint(hintText);
        ed.requestFocus();
//        InputMethodManager imm = (InputMethodManager) ed.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//        ed.setFocusable(true);
        v1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dimms();
                iPopupWindow.cancel();

            }
        });
        v2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //	http_delect(x);
                if (ed.getText().toString().trim().equals("")) {
                    Toast.makeText(activity,toasttewxt,Toast.LENGTH_SHORT).show();
                    return;
                }
                dimms();
                iPopupWindow.Oks(ed.getText().toString());

            }
        });
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    iPopupWindow.cancel();
                    dimms();
                    return true;
                }
                return false;
            }
        });
    }
    public interface IInputPopwindow{
        public void cancel();
        public void Oks(String title);
    }
    public void dimms(){
        if (mPopupWindow!=null)
            mPopupWindow.dismiss();
        mPopupWindow=null;
    }
}
