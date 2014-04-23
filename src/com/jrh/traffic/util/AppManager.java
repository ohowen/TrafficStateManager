package com.jrh.traffic.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.TrafficStats;
import android.text.format.Formatter;

import com.jrh.traffic.model.AppInfo;

public class AppManager {
	private static DBOpenHelper dbOpenHelper;
	private SQLiteDatabase db;
	private AppInfo appInfo;

	/**
	 * Get all app which have network access permission from a device
	 * 
	 * @param context
	 * @return
	 */
	public List<AppInfo> getNetworkAppMap(Context context) {
		// public List<AppInfo> getNetworkAppMap(Context context) {
		// 获取所有的安装在手机上的应用软件的信息，并且获取这些软件里面的权限信息
		PackageManager pm = context.getPackageManager();// 获取系统应用包管理
		// 获取每个包内的androidmanifest.xml信息，它的权限等等
		List<PackageInfo> pinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
						| PackageManager.GET_PERMISSIONS);
		List<AppInfo> appList = new ArrayList<AppInfo>();

		// List<AppInfo> appList = new ArrayList<AppInfo>();
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
							String pkgName = info.packageName;
							AppInfo appInfo = new AppInfo();
							appInfo.setUid(String.valueOf(uId));
							appInfo.setLabel(info.applicationInfo.loadLabel(pm)
									.toString());
							appInfo.setPkgname(pkgName);
							appInfo.setAlltraffic(Formatter.formatFileSize(
									context, all));
							appList.add(appInfo);
						}
					}
				}
			}
		}
		return appList;
	}

	/**
	 * Save a specific app's traffic data to database
	 */
	public void saveData(List<AppInfo> appList) {
		db = dbOpenHelper.getWritableDatabase();
		String sql = "";
		for (int i = 0; i < appList.size(); i++) {
			appInfo = appList.get(i);
			String pkgName = appInfo.getPkgname();
			String label = appInfo.getLabel();
			String wifi = appInfo.getWifi();
			String gprs = appInfo.getGprs();
			db.execSQL(sql, new Object[] { pkgName, label, wifi, gprs });
		}

	}

	/**
	 * Get all appInfo from database
	 * 
	 * @return appList
	 */
	public List<AppInfo> getAllApps() {
		List<AppInfo> appList = new ArrayList<AppInfo>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from appInfo order by gprs desc",
				null);
		while (cursor.moveToNext()) {
			String label = cursor.getString(cursor.getColumnIndex("label"));
			String wifi = cursor.getString(cursor.getColumnIndex("wifi"));
			String gprs = cursor.getString(cursor.getColumnIndex("gprs"));
			String alltraffic = cursor.getString(cursor
					.getColumnIndex("alltraffic"));
			appList.add(new AppInfo(label, wifi, gprs, alltraffic));
		}
		cursor.close();
		return appList;
	}
}
