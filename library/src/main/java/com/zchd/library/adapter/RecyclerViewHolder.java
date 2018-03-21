package com.zchd.library.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by GJ on 2016/8/25.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder{
    private SparseArray<View> mViews;
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mViews= new SparseArray<View>();
    }

    //通过viewId获取控件
    public <T extends View> T getView(int viewId) {

        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }
    /**
     * 设置TextView的值
     */
    public RecyclerViewHolder  setText(int viewId,String text){
        TextView tv= getView(viewId);
        tv.setText(text);
        return this;
    }
    public RecyclerViewHolder setImageResource(int viewId,int resId){
        ImageView view= getView(viewId);
        view.setImageResource(resId);
        return this;
    }
    public RecyclerViewHolder setImageBitamp(int viewId,Bitmap bitmap){
        ImageView view= getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }
    public RecyclerViewHolder setImageDrawable(int viewId,Drawable drawable){
        if (drawable==null)
            return this;
        ImageView view= getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }
}
