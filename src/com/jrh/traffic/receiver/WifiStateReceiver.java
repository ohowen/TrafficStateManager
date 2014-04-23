package com.jrh.traffic.receiver;

import java.util.List;

import com.jrh.traffic.model.AppInfo;
import com.jrh.traffic.util.AppManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

/**
 * ����wifi�����͹رյ����
 * @author Rio
 *
 */
public class WifiStateReceiver extends BroadcastReceiver{
	private long mFlag;
	private List<AppInfo> mAppList;
	private AppManager mAppManager;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		mAppManager = new AppManager();
		mAppList = mAppManager.getNetworkAppMap(context);
		
		// TODO Auto-generated method stub
		if (intent.getAction()
				.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
			int wifistate = intent.getIntExtra(
					WifiManager.EXTRA_WIFI_STATE,
					WifiManager.WIFI_STATE_DISABLED);
			if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
				// ���౾��wifi������ uidӦ�õ� ����
				
			} else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
				// ��¼��ǰuidӦ�õ�����

			}
		}
	}
}
