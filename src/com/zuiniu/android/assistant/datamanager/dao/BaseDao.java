/**
 * 
 */
package com.zuiniu.android.assistant.datamanager.dao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Administrator
 *
 */
public abstract class BaseDao<T> {
	protected Context mContext;
	protected ExecutorService mExecutorService = Executors.newCachedThreadPool();
	protected SQLiteDatabase db;
	
	private static final String DATABASE_NAME = "database.db";

	public BaseDao(Context mContext) {
		this.mContext = mContext;
		
		db = mContext.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
	}
	
	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
	
	public void execSQL(String sql) throws Exception {
		db.execSQL(sql);
	}
	
	public Cursor rawQuery(String sql, String[] selectionArgs) throws Exception {
		return db.rawQuery(sql, selectionArgs);
	}
	
	public Cursor query(boolean distinct, String table, String[] columns, String where, String[] whereArgs, String groupBy, String having, String orderBy, String limit) {
		return db.query(distinct, table, columns, where, whereArgs, groupBy, having, orderBy, limit);
	}
	
	public Cursor query(String table, String[] columns, String where, String[] whereArgs, String groupBy, String having, String orderBy, String limit) {
		return db.query(table, columns, where, whereArgs, groupBy, having, orderBy, limit);
	}
	
	public Cursor query(String table, String[] columns, String where, String[] whereArgs, String groupBy, String having, String orderBy) {
		return db.query(table, columns, where, whereArgs, groupBy, having, orderBy);
	}
	
	public Cursor query(String table, String[] columns, String where, String[] whereArgs) {
		return query(table, columns, where, whereArgs, null, null, null);
	}
	
	// 先不使用orm实现，以后有时间我会完善实现一个小型的orm，完全没有必要使用到额外的jar包类库。
	// 事实上就是语句的拼接和元数据等的读取判断。
	public abstract void insert(T t);
	public abstract void update(T t);
	public abstract void delete(int key);
	public abstract Cursor query(T t);
}
