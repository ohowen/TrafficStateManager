package com.jrh.traffic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * ���������ӶϿ�ʱ����ͳ�Ƶ�Ӧ������ʹ��������浽���ݿ�
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
		// ��ʾ�ֻ���ʱû���κ���������
		if (wifi != null && conn != null && State.CONNECTED != wifi
				&& State.CONNECTED != conn) {
			// ���ݱ������
			
			
		}
	}
}
