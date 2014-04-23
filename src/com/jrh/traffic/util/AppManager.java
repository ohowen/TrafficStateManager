package com.jrh.traffic.util;

import java.util.ArrayList;
import java.util.List;
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
	private List<AppInfo> appList;

	/**
	 * Get all app which have network access permission from a device
	 * 
	 * @param context
	 * @return
	 */
	public List<AppInfo> getNetworkAppList(Context context) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> pinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
						| PackageManager.GET_PERMISSIONS);
		List<AppInfo> appList = new ArrayList<AppInfo>();
		for (PackageInfo info : pinfos) {
			String[] premissions = info.requestedPermissions;
			if (premissions != null && premissions.length > 0) {
				for (String premission : premissions) {
					if ("android.permission.INTERNET".equals(premission)) {
						int uId = info.applicationInfo.uid;
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
	 * Save appInfo in a cache
	 * @param appList
	 */
	public void saveData(List<AppInfo> appList) {
		this.appList = appList; 
	}

	/**
	 * Save a specific app's traffic data to database
	 */
	public void saveDataToDB() {
		db = dbOpenHelper.getWritableDatabase();
		String sql = "update appInfo...";
		for (int i = 0; i < appList.size(); i++) {
			appInfo = appList.get(i);
			String pkgName = appInfo.getPkgname();
			String label = appInfo.getLabel();
			String wifi = appInfo.getWifi();
			String gprs = appInfo.getGprs();
			db.execSQL(sql, new Object[] { pkgName, label, wifi, gprs });
		}
		// Çå³ý»º´æÊý¾Ý
		this.appList = null;
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
