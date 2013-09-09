/**
 * 
 */
package com.zuiniu.android.assistant;

import com.zuiniu.android.assistant.activity.panel.view.WhiteFloatView;
import com.zuiniu.android.assistant.broadcastreceiver.ConnectionChangeReceiver;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * @author Administrator
 * 
 */
public class MainApplication extends Application {
	public static Context context;
	private static MainApplication singleton;
	private ConnectionChangeReceiver receiver;
	
	public static final String KEY_SHOW="key_show";//是否显示桌面浮动图标
	
	/**application只要不多进程，只有一个实例，所以不需要搞什么单例，直接静态对象就可以了*/
	private static WhiteFloatView mWhiteFloatView;//浮动图标

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
		
		mWhiteFloatView=new WhiteFloatView(context);
		mWhiteFloatView.create();
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		
		super.onTerminate();
	}
	
	/**
	 * 根据设置来设置是否显示图标
	 */
	public static void refreshFloatView(){
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean isShow=prefs.getBoolean(KEY_SHOW, false);
		if(isShow){
			mWhiteFloatView.show();
		}else{
			mWhiteFloatView.remove();
		}
	}
}
