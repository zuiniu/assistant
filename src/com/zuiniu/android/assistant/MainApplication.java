/**
 * 
 */
package com.zuiniu.android.assistant;

import com.zuiniu.android.assistant.broadcastreceiver.ConnectionChangeReceiver;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.view.WindowManager;

/**
 * @author Administrator
 * 
 */
public class MainApplication extends Application {
	public static Context context;
	private static MainApplication singleton;
	private ConnectionChangeReceiver receiver;

	public static WindowManager getWindowManager() {
		return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}
	
	public static MainApplication getInstance() {
		return singleton;
	}

	@Override
	public void onConfigurationChanged(Configuration paramConfiguration) {
		super.onConfigurationChanged(paramConfiguration);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		singleton = this;
		
		// 尝试注册使用广播
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new ConnectionChangeReceiver();
		registerReceiver(receiver, filter);
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		
		super.onTerminate();
	}
}
