package com.zchd.library.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zchd.library.R;
import com.zchd.library.adapter.IcssAdapter;


/**
 * Created by GJ on 2016/12/9.
 */
public abstract class SharePopupWindow extends ShareBasePopupWindow {
    private PopupWindow mPopupWindow=null;
    private View view=null;
    int[] locations = new  int[2] ;//为解决魅族手机底部虚拟按键显示问题
    private Activity activity;
    boolean enB;
    private int image=0;
    public SharePopupWindow(boolean language) {
        super(1);
        enB=language;
    }
    public SharePopupWindow(int x,boolean language) {
        super(x);
        enB=language;
    }
    public void show(Activity activity, View views,String sharestring,String dimmstext){
        views.getLocationOnScreen(locations);
        this.activity=activity;
        view =  LayoutInflater.from(activity).inflate(getLayout(), null, false);
        init();
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        ImageView shareapp_image= (ImageView) view.findViewById(R.id.shareapp_image);
        if (image==0)
            shareapp_image.setVisibility(View.INVISIBLE);
        else {
            shareapp_image.setVisibility(View.VISIBLE);
            Glide.with(activity).load(image).into(shareapp_image);
        }
        TextView share_text= (TextView) view.findViewById(R.id.share_text);
        share_text.setText(sharestring);
        TextView dimms= (TextView) view.findViewById(R.id.dimms);
        dimms.setText(dimmstext);
        dimms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dimms();
            }
        });
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
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setAnimationStyle(R.style.dialogWindowAnim);
        int location[] = getScreenWH(activity);
        mPopupWindow.showAsDropDown(views,0,-(location[1]));

    }
    public int[] getScreenWH(Context poCotext) {
        WindowManager wm = (WindowManager) poCotext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return new int[] { width, height };
    }
    private void init() {
        GridView gridView= (GridView) view.findViewById(getGridViewID());
        gridView.setAdapter(new IcssAdapter<ItemBin>(activity,list,getItemLayout()) {
            @Override
            public void getview(int position) {
                if (enB)
                    viewholder.setText(getGridView_textId(),list.get(position).getTexten())
                            .setImageResource(getGridView_imageID(),list.get(position).getImage());
                else
                    viewholder.setText(getGridView_textId(),list.get(position).getText())
                            .setImageResource(getGridView_imageID(),list.get(position).getImage());
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemclick(parent,view,position,id);
                dimms();
            }
        });

    }

    public void dimms()
    {
        if (mPopupWindow!=null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }

    public abstract void onItemclick(AdapterView<?> parent, View view, int position, long id);
    public void setlayout(int x){
        image=x;
    }
}
