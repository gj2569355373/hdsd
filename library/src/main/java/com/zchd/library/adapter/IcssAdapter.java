package com.zchd.library.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class IcssAdapter<T> extends IcssBaseAdapter<T>{
	
	public IcssAdapter(Context context ,List<T> list,int layoutId) {
		super(context,list,layoutId);
	}



	@Override
	public View getView(int position, View view, ViewGroup convertView) {
		// TODO Auto-generated method stub
		viewholder=ViewHolder.get(context, view, convertView, layoutId, position);
		if (list.size()!=0)
			getview(position);
		return viewholder.getmConvertView();
	}
	public abstract void getview(int position);
}
