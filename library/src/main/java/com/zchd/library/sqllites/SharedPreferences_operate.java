package com.zchd.library.sqllites;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferences_operate {
	private SharedPreferences operate;
	private SharedPreferences.Editor editor;
	private Boolean is=true;

	public SharedPreferences_operate(String str, Context context) {
		// TODO Auto-generated constructor stub
		operate=context.getSharedPreferences(str, 
				Activity.MODE_PRIVATE); 
		
	}

	public void addString(String key,String vlale){
		if(is)
			edit();
		editor.putString(key, vlale);
		editor.commit();
	}
	public void addbool(String key, boolean b){
		if(is)
			edit();
		editor.putBoolean(key,b);
		editor.commit();
	}
	public void addint(String key,int vlale){
		if(is)
			edit();
		editor.putInt(key, vlale);
		editor.commit();
	}
	public String getString(String key){
		return operate.getString(key, "");
	}
	public int getint(String key){
		return operate.getInt(key, 0);
	}
	private void edit(){
		editor=operate.edit();
		is=false;
	}
	public boolean getBoolean(String key){
		return operate.getBoolean(key,false);
	}
	public boolean getBoolean(String key,boolean b){
		return operate.getBoolean(key,b);
	}
}
