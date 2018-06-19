package com.zchd.hdsd.Bin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PingLunSerialize implements Serializable{
	String hasMoreEvaluation="";
	List<Pinglun> listPinglun;
	int page=1;
	public PingLunSerialize(List<Pinglun> listPinglun) {
		super();
		this.listPinglun = listPinglun;
	}
	public PingLunSerialize(PingLunSerialize PL)
	{
		hasMoreEvaluation= PL.getHasMoreEvaluation();
		listPinglun=new ArrayList<Pinglun>();
		for (int i=0;i<PL.getListPinglun().size();i++)
		{
			listPinglun.add(new Pinglun(PL.getListPinglun().get(i).getImgurl(),PL.getListPinglun().get(i).getContext(),PL.getListPinglun().get(i).getName()
					,PL.getListPinglun().get(i).getTime(),PL.getListPinglun().get(i).getXingji()));
		}
	}
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getHasMoreEvaluation() {
		return hasMoreEvaluation;
	}

	public void setHasMoreEvaluation(String hasMoreEvaluation) {
		this.hasMoreEvaluation = hasMoreEvaluation;
	}

	public List<Pinglun> getListPinglun() {
		return listPinglun;
	}

	public void setListPinglun(List<Pinglun> listPinglun) {
		this.listPinglun = listPinglun;
	}
	
}
