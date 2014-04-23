package com.jrh.traffic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * 在网络连接断开时，把统计的应用流量使用情况保存到数据库
 * @author Rio
 *
 */
public class ConnectivityReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		State wifi = null;
		State conn = null;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		conn = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		// 表示手机此时没有任何网络连接
		if (wifi != null && conn != null && State.CONNECTED != wifi
				&& State.CONNECTED != conn) {
			// 数据保存操作
			
			
		}
	}
}
