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
 * 
 * @author Rio
 * 
 */
public class WifiStateReceiver extends BroadcastReceiver {
	private List<AppInfo> mAppListWifiOn, mAppListWifiOff;
	private AppManager mAppManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
			int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
			        WifiManager.WIFI_STATE_DISABLED);
			mAppManager = new AppManager(context);
			if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
				// wifi关闭，再次读取系统文件，结余本次wifi过程中 uid应用的 流量，要考虑到有应用被安装和卸载的情况
				mAppListWifiOff = mAppManager.getNetworkAppList(context);
				for (int i = 0; i < mAppListWifiOff.size(); i++) {
					for (int j = 0; j < mAppListWifiOn.size(); j++) {
						if (mAppListWifiOff.get(i).getPkgname()
						        .equals(mAppListWifiOn.get(j).getPkgname())) {
							mAppListWifiOff.get(i).setWifi(
							        mAppListWifiOff.get(i).getAlltraffic()
							                - mAppListWifiOn.get(j).getTemp());
						} else {
							continue;
						}
					}
				}
				mAppManager.setAppList(mAppListWifiOff);
				mAppManager.saveDataToDB();
			} else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
				// wifi开启，首先读取系统文件，获取当前uid应用的流量
				mAppListWifiOn = mAppManager.getNetworkAppList(context);
				mAppManager.setAppList(mAppListWifiOn);
				for (int i = 0; i < mAppListWifiOn.size(); i++) {
					mAppListWifiOn.get(i).setTemp(
					        mAppListWifiOn.get(i).getAlltraffic());
				}
			}
		}
	}
}
