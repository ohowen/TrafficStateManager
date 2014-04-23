package com.jrh.traffic.receiver;

import com.jrh.traffic.util.AppManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * ���������ӶϿ�ʱ����ͳ�Ƶ�Ӧ������ʹ��������浽���ݿ�
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

		// ��ʾ�ֻ���ʱû���κ���������
		if (mWifi != null && mMobile != null && State.CONNECTED != mWifi
				&& State.CONNECTED != mMobile) {
			// ���ݱ������
			mAppManager.saveDataToDB();
		}
	}
}
