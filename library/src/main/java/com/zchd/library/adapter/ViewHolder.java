package com.zchd.library.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	private View mConvertView;
	private int mposition;
	private  SparseArray<View> mViews;
	public ViewHolder(Context context ,ViewGroup parent,int layoutId,int position) {
		// TODO Auto-generated constructor stub
		this.mposition=position;
		this.mViews=new SparseArray<View>();
		mConvertView=LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConvertView.setTag(this);
	}
	
	public static ViewHolder get(Context context ,View convertView,ViewGroup parent,int layoutId,int position){
		if(convertView==null)
		{
			return new ViewHolder(context, parent, layoutId, position);
		}
		else
		{
			ViewHolder holder=(ViewHolder) convertView.getTag();
			holder.mposition=position;
			return holder;
		}
	}
	public static ViewHolder getViewHolder(View convertView){
		ViewHolder holder=(ViewHolder) convertView.getTag();
		//holder.mposition=position;
		return holder;
	}
	public <T extends View> T getView(int viewId){
		View view=mViews.get(viewId);
		if(view==null)
		{
			view=mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getmConvertView() {
		return mConvertView;
	}
	public ViewHolder setText(int viewId,String text){
		TextView textview= getView(viewId);
		textview.setText(text);
		return this;
	}
	public ViewHolder setText(int viewId,String text,View.OnClickListener listener){
		TextView textview= getView(viewId);
		textview.setText(text);
		textview.setOnClickListener(listener);
		return this;
	}
	public ViewHolder setButton(int viewId,String text){
		Button view=getView(viewId);
		view.setFocusable(false);
		view.setText(text);
		return this;
	}
	public ViewHolder setImageResource(int viewId,int resId)
	{
		ImageView view=getView(viewId);
		view.setImageResource(resId);
		return this;
	}
	public ViewHolder setCheckBox(int viewId,int resId,View.OnClickListener listener){
		CheckBox view=getView(viewId);
		view.setFocusable(false);
		view.setOnClickListener(listener);
		return this;	
	}
}
