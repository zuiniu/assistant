/**
 * 
 */
package com.zuiniu.android.assistant.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Administrator
 * 
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {

	private static final String TAG = ConnectionChangeReceiver.class
			.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			// 如果不是由网络状态动作触发，就返回
			return;
		}
		
		Log.d(TAG, "网络状态改变");

		boolean success = false;

		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// State state = connManager.getActiveNetworkInfo().getState();
		// 获取WIFI网络连接状态
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// 判断是否正在使用WIFI网络
		if (State.CONNECTED == state) {
			success = true;
		}
		// 获取GPRS网络连接状态
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		// 判断是否正在使用GPRS网络
		if (State.CONNECTED != state) {
			success = true;
		}

		if (!success) {
			Toast.makeText(context, "你断网了", Toast.LENGTH_LONG).show();
		}
	}
}
