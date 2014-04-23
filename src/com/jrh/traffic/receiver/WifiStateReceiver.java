package com.jrh.traffic.receiver;

import java.util.List;

import com.jrh.traffic.model.AppInfo;
import com.jrh.traffic.util.AppManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

/**
 * 监听wifi开启和关闭的情况
 * @author Rio
 *
 */
public class WifiStateReceiver extends BroadcastReceiver{
	private long mFlag;
	private List<AppInfo> mAppList;
	private AppManager mAppManager;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction()
				.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
			int wifistate = intent.getIntExtra(
					WifiManager.EXTRA_WIFI_STATE,
					WifiManager.WIFI_STATE_DISABLED);
			mAppManager = new AppManager();
			mAppList = mAppManager.getNetworkAppList(context);
			if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
				// wifi关闭，结余本次wifi过程中 uid应用的 流量
				
			} else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
				// wifi开启，记录当前uid应用的流量
				
			}
		}
	}
}
