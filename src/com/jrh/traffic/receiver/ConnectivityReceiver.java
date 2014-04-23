package com.jrh.traffic.receiver;

import com.jrh.traffic.util.AppManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * 在网络连接断开时，把统计的应用流量使用情况保存到数据库
 * 
 * @author Rio
 * 
 */
public class ConnectivityReceiver extends BroadcastReceiver {
	private AppManager mAppManager;
	private State mWifi = null;
	private State mMobile = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		mMobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		mAppManager = new AppManager();

		// 表示手机此时没有任何网络连接
		if (mWifi != null && mMobile != null && State.CONNECTED != mWifi
				&& State.CONNECTED != mMobile) {
			// 数据保存操作
			mAppManager.saveDataToDB();
		}
	}
}
