package com.zchd.library.adapter;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

abstract public class IcssBaseAdapter<T> extends BaseAdapter{
	protected List<T> list;
	protected ViewHolder viewholder;
	protected Context context ;
	protected int layoutId;
	public IcssBaseAdapter(Context context ,List<T> list,int layoutId) {
		// TODO Auto-generated constructor stub
		if (list==null) {
			list=new ArrayList<T>();
		}
		this.list=list;
		this.context=context;
		this.layoutId=layoutId;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	public void updataView(int posi, ListView listView,Iupdata updata) {
		int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {  
            View view = listView.getChildAt(posi - visibleFirstPosi);
            updata.updateOne(view);
        }
	}
	public interface Iupdata {
		void updateOne(View view);
	}
}
