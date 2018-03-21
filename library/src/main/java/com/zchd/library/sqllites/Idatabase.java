package com.zchd.library.sqllites;

import java.util.List;
import java.util.Map;

public interface Idatabase {
	/*
	 * data插入的数据;
	 * */
	public boolean add(String table, Map<String, String> data);
	/*
	 * 修改
	 * */
	public boolean update(String table, String condition, String[] args, Map<String, String> data);
	/*
	 * arg1条件内容
	 * arg2查询的内容
	 * */
	public List<Map<String, String>> query(String table, String condition, String[] arg1, String[] arg2);
	public boolean delete(String table, String condition, String[] arg1);
	public boolean deletetable(String table);
	public boolean creattable(String sql, String table);
}
