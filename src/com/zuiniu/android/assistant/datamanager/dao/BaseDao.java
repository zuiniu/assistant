/**
 * 
 */
package com.zuiniu.android.assistant.datamanager.dao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

/**
 * @author Administrator
 *
 */
public abstract class BaseDao {
	protected Context mContext;
	protected ExecutorService mExecutorService = Executors.newCachedThreadPool();

	public BaseDao(Context mContext) {
		this.mContext = mContext;
	}
}
