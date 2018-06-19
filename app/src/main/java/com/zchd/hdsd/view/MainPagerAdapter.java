package com.zchd.hdsd.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class MainPagerAdapter extends FragmentPagerAdapter {
	 ArrayList<Fragment> list;
	String[] titlelist=null;
	public MainPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);
		this.list=list;
		// TODO Auto-generated constructor stub
	}
	public MainPagerAdapter(FragmentManager fm, ArrayList<Fragment> list, String[] titlelist) {
		super(fm);
		this.list=list;
		this.titlelist=titlelist;
		// TODO Auto-generated constructor stub
	}
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (titlelist==null)
			return super.getPageTitle(position);
		else
			return titlelist[position];
	}
}
