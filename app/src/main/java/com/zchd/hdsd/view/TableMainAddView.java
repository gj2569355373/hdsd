package com.zchd.hdsd.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;


/**
 * Created by GJ on 2016/11/30.
 */
public class TableMainAddView {
    View view;
    String XZ="#1F1F1F";
    String YESXZ="#B20000";
    IonclickListener ionclickListener=null;
    public int index=1;
    private int max=4;
    SparseArray<ImageView> sparseArray=new SparseArray<ImageView>();
    SparseArray<TextView> textviewArray=new SparseArray<TextView>();

    public TableMainAddView(int index) {
        this.index = index;
    }

    public View addView(Activity activity){
        view = LayoutInflater.from(activity).inflate(R.layout.tablemainaddview, null, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout tuijian= (RelativeLayout) view.findViewById(R.id.main_tuijian);
        RelativeLayout paihang=(RelativeLayout) view.findViewById(R.id.main_paihang);
        RelativeLayout shoucang=(RelativeLayout) view.findViewById(R.id.main_shoucang);
        RelativeLayout wode=(RelativeLayout) view.findViewById(R.id.main_wode);
//        RelativeLayout shouye= (RelativeLayout) view.findViewById(R.id.main_shouye);


        sparseArray.put(1,(ImageView)view.findViewById(R.id.main_tuijian_Image));
        sparseArray.put(2,(ImageView)view.findViewById(R.id.main_shoucang_Image));
        sparseArray.put(3,(ImageView)view.findViewById(R.id.main_paihang_Image));
        sparseArray.put(4,(ImageView)view.findViewById(R.id.main_wode_image));
//        sparseArray.put(5,(ImageView)view.findViewById(R.id.main_shouye_Image));
        setImageAll();
        final TextView tuijian_textview= (TextView) view.findViewById(R.id.main_tuijian_textview);
        final TextView paihang_textview= (TextView) view.findViewById(R.id.main_paihang_textview);
        final TextView shoucang_textview= (TextView) view.findViewById(R.id.main_shoucang_textview);
        final TextView wode_textview= (TextView) view.findViewById(R.id.main_wode_textview);
//        final TextView shouye_textview= (TextView) view.findViewById(R.id.main_shouye_textview);

        textviewArray.put(1,tuijian_textview);
        textviewArray.put(2,shoucang_textview);
        textviewArray.put(3,paihang_textview);
        textviewArray.put(4,wode_textview);
//        textviewArray.put(5,shouye_textview);
        setTextView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tuijian.setBackgroundResource(R.drawable.withmaskripple);
            paihang.setBackgroundResource(R.drawable.withmaskripple);
            shoucang.setBackgroundResource(R.drawable.withmaskripple);
            wode.setBackgroundResource(R.drawable.withmaskripple);
//            shouye.setBackgroundResource(R.drawable.withmaskripple);
        }
        paihang.setOnClickListener(v -> {
            if (HdsdApplication.login){
                if (index==3)
                    return;
                setImageviews(3,index);
                index=3;
                if (ionclickListener!=null)
                    ionclickListener.gouwuche(v);
            }
            else
                ionclickListener.gouwuche(v);
        });
        tuijian.setOnClickListener(v -> {
            if (index==1)
                return;
            setImageviews(1,index);
            index=1;
            if (ionclickListener!=null)
                ionclickListener.shouye(v);
        });
        wode.setOnClickListener(v -> {
            if (index==4)
                return;
            setImageviews(4,index);
            index=4;
            if (ionclickListener!=null)
                ionclickListener.wode(v);
        });
        shoucang.setOnClickListener(v -> {
            if (HdsdApplication.login) {
                if (index == 2)
                    return;
                setImageviews(2, index);
                index = 2;
                if (ionclickListener != null)
                    ionclickListener.paihang(v);
            }
            else
                ionclickListener.paihang(v);
        });
//        shouye.setOnClickListener(v -> {
////                setImageviews(5,index);
//            if (ionclickListener!=null)
//                ionclickListener.share(v);
//        });
        return view;
    }
    public void setIndexs(int x){
        if (index==x)
            return;
        setImageviews(x,index);
        index=x;
    }

    public void setTextcolor(int now,int out){
        textviewArray.get(out).setTextColor(Color.parseColor(XZ));
        textviewArray.get(now).setTextColor(Color.parseColor(YESXZ));
    }

    private void setTextView() {
        for (int i=1;i<max+1;i++) {
            if (i==index)
                textviewArray.get(i).setTextColor(Color.parseColor(YESXZ));
            else
                textviewArray.get(i).setTextColor(Color.parseColor(XZ));
        }
    }
    public void setBottom(int x){
        setImageviews(x,index);
        index=x;
    }

    public void setIonclickListener(IonclickListener ionclickListener) {
        this.ionclickListener = ionclickListener;
    }

    public interface IonclickListener{
        public void shouye(View v);//首页
        public void paihang(View v);
        public void wode(View v);//我的
        public void gouwuche(View v);//购物车
//        public void share(View v);
    }
    public void setImageviews(int now,int out){
        setTextcolor(now,out);
        sparseArray.get(out).setColorFilter(Color.parseColor(XZ));
        sparseArray.get(now).setColorFilter(Color.parseColor(YESXZ));
    }
    void setImageAll(){
        for (int i=1;i<max+1;i++) {
            if (i==index)
            {
                sparseArray.get(i).setColorFilter(Color.parseColor(YESXZ));
            }
            else
                sparseArray.get(i).setColorFilter(Color.parseColor(XZ));
        }
    }
}
