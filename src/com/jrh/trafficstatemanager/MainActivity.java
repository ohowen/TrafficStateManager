package com.jrh.trafficstatemanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.ListView;

public class MainActivity extends Activity {
	private DBOpenHelper dbOpenHelper;
	private ListAdapter mListAdapter;
	private ListView mListView;
	private static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;

		mListView = (ListView) findViewById(R.id.applist);
		mListAdapter = new ListAdapter(mContext, getAllApps());
		mListView.setAdapter(mListAdapter);
	}	

	/**
	 * Get all appInfo from database 
	 * @return appList
	 */
	public List<AppInfo> getAllApps() {
		List<AppInfo> appList = new ArrayList<AppInfo>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from appInfo order by gprs desc", null);
		while (cursor.moveToNext()) {
			String label = cursor.getString(cursor.getColumnIndex("label"));
			String wifi = cursor.getString(cursor.getColumnIndex("wifi"));
			String gprs = cursor.getString(cursor.getColumnIndex("gprs"));
			String alltraffic = cursor.getString(cursor.getColumnIndex("alltraffic"));
			appList.add(new AppInfo(label, wifi, gprs, alltraffic));
		}
		cursor.close();
		return appList;
	}
	
	/**
	 * Save a specific app's traffic data to database
	 */
	public void saveData(){
		
	}

	public List<AppInfo> getAppTrafficList() {
		// 获取所有的安装在手机上的应用软件的信息，并且获取这些软件里面的权限信息
		PackageManager pm = getPackageManager();// 获取系统应用包管理
		// 获取每个包内的androidmanifest.xml信息，它的权限等等
		List<PackageInfo> pinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
						| PackageManager.GET_PERMISSIONS);
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		// 遍历每个应用包信息
		for (PackageInfo info : pinfos) {
			// 请求每个程序包对应的androidManifest.xml里面的权限
			String[] premissions = info.requestedPermissions;
			if (premissions != null && premissions.length > 0) {
				// 找出需要网络服务的应用程序
				for (String premission : premissions) {
					if ("android.permission.INTERNET".equals(premission)) {
						// 获取每个应用程序在操作系统内的进程id
						int uId = info.applicationInfo.uid;
						// 如果返回-1，代表不支持使用该方法，注意必须是2.2以上的
						long rx = TrafficStats.getUidRxBytes(uId);
						long tx = TrafficStats.getUidTxBytes(uId);
						long all = rx + tx;
						if (rx < 0 || tx < 0) {
							continue;
						} else {
							AppInfo appInfo = new AppInfo();
							appInfo.setLabel(info.applicationInfo.loadLabel(pm).toString());
							appInfo.setAlltraffic(Formatter.formatFileSize(this, all));
							appInfos.add(appInfo);
						}
					}
				}
			}
		}
		return appInfos;
	}

	/**
	 * 在关机前 把统计的应用流量使用情况保存到数据库
	 * @author Rio
	 *
	 */
	public static class ShutDownReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 数据保存操作
			
		}
	}
	
	/**
	 * 在网络连接断开时，把统计的应用流量使用情况保存到数据库
	 * @author Rio
	 *
	 */
	public static class ConnectivityReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			State wifi = null;
			State conn = null;
			
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			conn = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
			//表示手机此时没有任何网络连接
			if(wifi != null && conn != null
					&& State.CONNECTED != wifi
					&& State.CONNECTED != conn){
				//数据保存操作
			}
		}
	}
	
	/**
	 * 监听wifi开启和关闭的情况
	 * @author Rio
	 *
	 */
	public static class WifiStateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction()
					.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				int wifistate = intent.getIntExtra(
						WifiManager.EXTRA_WIFI_STATE,
						WifiManager.WIFI_STATE_DISABLED);
				if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
					// 结余本次wifi过程中 uid应用的 流量
					
				} else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
					// 记录当前uid应用的流量
					
				}
			}
		}
	}
}
