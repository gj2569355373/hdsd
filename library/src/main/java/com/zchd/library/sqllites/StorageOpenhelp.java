package com.zchd.library.sqllites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StorageOpenhelp extends SQLiteOpenHelper {
	private static final String storage = "Icssdatas.db"; //数据库名
	private static int version = 1; //版本
	private int x;
	public StorageOpenhelp(Context context, int x) {
		super(context, storage, null, version);
		this.x=x;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}


	private void createWork(SQLiteDatabase db) {//s
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists work(_id integer primary key, name text,phone varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub版本更新会调用
	}
}
