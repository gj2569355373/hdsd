package com.zchd.library.sqllites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DatabaseOperation implements Idatabase{//DbDao
	public static DatabaseOperation databaseOperation=null;
	private Context context;
	private static SQLiteDatabase dbs=null;
	private ContentValues cv ;//游标
	/*x=1,create table if not exists work
	 * x=2,create table if not exists phone
	 * */
	public DatabaseOperation(Context context,int x) {
		super();
		this.context=context;
		// TODO Auto-generated constructor stub
		if(dbs==null)
		{
			StorageOpenhelp storages=new StorageOpenhelp(context,x);
			dbs=storages.getWritableDatabase();
		}
		if(databaseOperation==null)
			databaseOperation=this;
	}
	public SQLiteDatabase getSQLiteDatabase(){
		return dbs;
	}
	/*
	 * 删除
	 * */
	@Override
	public boolean delete(String table,String condition,String[] arg1){
		query(table, condition, arg1);
		if(dbs.delete(table, condition, arg1)!=0)
			return true;
		else
			return false;	
	}
	
	public boolean add(String table,Map data ,String condition,String[]arg0) {	//存在则不添加添加
		// TODO Auto-generated method stub
		boolean isd=false;
		if(!iftable(table))
			return false;	
		if(query(table, condition, arg0))
			return isd;		
		return add(table, data);
	}
	//修改
	@Override
	public boolean update(String table,String condition,String[] args,Map<String, String> data) {//data�?
		// TODO Auto-generated method stub
		boolean marker=false;
		if(!iftable(table))
			return false;
		if(query(table, condition, args))
			if(getMapEntry(data))
			{
				dbs.update(table,cv , condition, args);
				marker=true;
				cv.clear();
			}
		return marker;
	}
	public int getworkint(){
		Cursor cursor=dbs.rawQuery("Select count(*) from work;", null);
		return cursor.getCount();
	}
	public boolean query(String table,String condition,String[] arg1) {
		// TODO Auto-generated method stub
		boolean isd=false;
		if(condition==null)
			return isd;
		Cursor cursor = dbs.query (table,null,condition,arg1,null,null,null);
		while(cursor.moveToNext()){
			cursor.close();
			return true;
		}
		cursor.close();
		return isd;
	}
	@Override
	public List<Map<String, String>> query(String table,String condition,String[] arg1,String[] arg2) {
		// TODO Auto-generated method stub
		boolean isd=false;
		List<Map<String, String>> list=null;
		if(arg2!=null){
			list=new ArrayList<Map<String,String>>();
		}
		Cursor cursor = dbs.query (table,null,condition,arg1,null,null,null);
		while(cursor.moveToNext()){
			if(arg2!=null){
//				t1.setText(cursor.getString(cursor.getColumnIndex("phone")));
				Map<String , String>map=new HashMap<String, String>();
				for(int i=0;i<arg2.length;i++)
					map.put(arg2[i], cursor.getString(cursor.getColumnIndex(arg2[i])));
				list.add(map);
			}
			isd=true;
		}
		cursor.close();
		if(isd)
			return list;
		else
			return null;
	}
	@Override
	public boolean add(String table, Map<String, String> data) {
		// TODO Auto-generated method stub
		if(!iftable(table))
			return false;	
		if(getMapEntry(data))
			if(dbs.insert(table, null, cv)!=-1)//插入表中
			{	
				cv.clear();
				return true;			
			}
		return false;
	}
	public boolean getMapEntry(Map<String, String> map){
		boolean is=false;
		cv = new ContentValues();//游标
		Set entries = map.entrySet( );
		if(entries != null) {
			Iterator iterator = entries.iterator( );
			while(iterator.hasNext( )) {
				Entry entry =(Entry) iterator.next( );
				cv.put((String)entry.getKey( ), (String)entry.getValue());
				is=true;
			}
		}
		return is;
	}
	@Override
	public boolean deletetable(String table) {
		// TODO Auto-generated method stub
		try
        {
            String sql="drop table "+table;         
            dbs.execSQL(sql);  
            return !iftable(table);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
		return false;
	}
	@Override
	public boolean creattable(String sql,String table) {
		// TODO Auto-generated method stub
		try
        {
			dbs.execSQL(sql);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
		return iftable(table);
	}
	/*
	 * 判断表是否存在
	 * */
	public boolean iftable(String table){
		boolean marker=false;
		Cursor cursor = null;
		String sqls = "select count(*) as c from Sqlite_master  where type ='table' and name ='"+table.trim()+"' ";
        cursor = dbs.rawQuery(sqls, null);
        if(cursor!=null)
		{	
			cursor.close();
			marker=true;
		}
		return marker;	
	}
}
