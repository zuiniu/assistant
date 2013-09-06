/**
 * 
 */
package com.zuiniu.android.assistant;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.view.WindowManager;

/**
 * @author Administrator
 * 
 */
public class MainApplication extends Application {
	public static Context context;

	public static WindowManager getWindowManager() {
		return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	@Override
	public void onConfigurationChanged(Configuration paramConfiguration) {
		super.onConfigurationChanged(paramConfiguration);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}
}
