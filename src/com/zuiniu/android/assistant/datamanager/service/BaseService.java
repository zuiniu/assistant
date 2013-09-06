/**
 * 
 */
package com.zuiniu.android.assistant.datamanager.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

/**
 * @author Administrator
 *
 */
public abstract class BaseService {
	protected Context mContext;
	protected ExecutorService executorService = Executors.newCachedThreadPool();

	public BaseService(Context mContext) {
		this.mContext = mContext;
	}
}
