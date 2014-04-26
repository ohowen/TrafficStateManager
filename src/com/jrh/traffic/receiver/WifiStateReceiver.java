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
	private long mOld, mNew, mFlag;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
			int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
			        WifiManager.WIFI_STATE_DISABLED);
			mAppManager = new AppManager(context);
			if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
				// 1.wifi关闭，读取数据库和系统文件，结余本次wifi过程中 uid应用的流量，两者的差即为wifi开启期间的wifi流量
				// 2.把当前读到的系统文件数据暂时存到temp字段，这样是为了在wifi开启时与从系统文件中读到的数据对比，这样他们的差即为关闭期间的3g流量
				mAppListWifiOn = mAppManager.getAllApps();// 数据库的数据，为了得到temp->old
				mAppListWifiOff = mAppManager.getNetworkAppList(context);// 此时系统中的数据,为了得到alltraffic->new
				for (int i = 0; i < mAppListWifiOff.size(); i++) {
					mAppListWifiOff.get(i).setTemp(
					        mAppListWifiOff.get(i).getAlltraffic());//  把当前的总流量赋给temp，对应操作2
					for (int j = 0; j < mAppListWifiOn.size(); j++) {
						// 要考虑到有应用被安装和卸载的情况
						if (mAppListWifiOff.get(i).getPkgname()
						        .equals(mAppListWifiOn.get(j).getPkgname())) {
							mNew = mAppListWifiOff.get(i).getAlltraffic();// 获取当前的总流量
							mOld = mAppListWifiOn.get(j).getTemp();// 获取旧数据
							mFlag = mNew - mOld;
							mAppListWifiOff.get(i).setWifi(mFlag);
						} else {
							continue;
						}
					}
				}
				mAppManager.setAppList(mAppListWifiOff);
				mAppManager.saveDataToDB();
			} else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
				// 1.wifi开启，首先读取数据库上次保存的数据，然后读取此时的系统文件，获取当前uid应用的流量，两个数据的差即为wifi关闭期间的3g流量
				// 2.把当前读到的系统文件数据暂时存到temp字段，这样是为了在wifi关闭时与从系统文件中读到的数据对比，这样他们的差即为wifi开启期间的wifi流量
				mAppListWifiOff = mAppManager.getAllApps();// 数据库里的数据，为了得到temp->old
				mAppListWifiOn = mAppManager.getNetworkAppList(context);// 此时系统中的数据，为了得到alltraffic->new
				for (int i = 0; i < mAppListWifiOn.size(); i++) {
					mAppListWifiOn.get(i).setTemp(
					        mAppListWifiOn.get(i).getAlltraffic());// 把当前的总流量赋给temp，对应操作2
					for (int j = 0; j < mAppListWifiOff.size(); j++) {
						if (mAppListWifiOn.get(i).getPkgname()
						        .equals(mAppListWifiOff.get(j).getPkgname())) {
							mNew = mAppListWifiOn.get(i).getAlltraffic();// 获取当前的总流量alltraffic
							mOld = mAppListWifiOff.get(j).getTemp();// 获取temp数据
							mFlag = mNew - mOld;// 对应操作1
							mAppListWifiOn.get(i).setGprs(mFlag);
						} else {
							continue;
						}
					}
				}
				mAppManager.setAppList(mAppListWifiOn);
				mAppManager.saveDataToDB();
			}
		}
	}
}
