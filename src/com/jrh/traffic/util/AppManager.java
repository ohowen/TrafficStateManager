package com.jrh.traffic.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.TrafficStats;

import com.jrh.traffic.model.AppInfo;

public class AppManager {
	private DBOpenHelper dbOpenHelper;
	private AppInfo appInfo;
	private List<AppInfo> appList;

	public AppManager(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	public List<AppInfo> getAppList() {
		return appList;
	}

	public void setAppList(List<AppInfo> appList) {
		this.appList = appList;
	}

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
		appList = new ArrayList<AppInfo>();
		for (PackageInfo info : pinfos) {
			String[] premissions = info.requestedPermissions;
			if (premissions != null && premissions.length > 0) {
				for (String premission : premissions) {
					if ("android.permission.INTERNET".equals(premission)) {
						int uId = info.applicationInfo.uid;
						long rx = TrafficStats.getUidRxBytes(uId);
						long tx = TrafficStats.getUidTxBytes(uId);
						long alltraffic = rx + tx;
						if (rx < 0 || tx < 0) {
							continue;
						} else {
							String pkgName = info.packageName;
							AppInfo appInfo = new AppInfo();
							appInfo.setUid(String.valueOf(uId));
							appInfo.setLabel(info.applicationInfo.loadLabel(pm)
							        .toString());
							appInfo.setPkgname(pkgName);
							/*
							 * appInfo.setAlltraffic(Formatter.formatFileSize(
							 * context, all));
							 */
							appInfo.setAlltraffic(alltraffic);
							appList.add(appInfo);
						}
					}
				}
			}
		}
		return appList;
	}

	/**
	 * Database initially
	 * 
	 * @param appList
	 */
	public void initDB() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		String sql = "insert into appInfo(uid,pkgName,label,wifi,gprs,alltraffic) values(?,?,?,?,?,?) ";
		for (int i = 0; i < appList.size(); i++) {
			appInfo = appList.get(i);
			String uid = appInfo.getUid();
			String pkgname = appInfo.getPkgname();
			String label = appInfo.getLabel();
			db.execSQL(sql, new Object[] { uid, pkgname, label, 0, 0, 0 });
		}
		appList = null;
	}

	/**
	 * Save a specific app's traffic data to database
	 */
	public void saveDataToDB() {
		SQLiteDatabase dbw = dbOpenHelper.getWritableDatabase();
		SQLiteDatabase dbr = dbOpenHelper.getWritableDatabase();
		String update = "update appInfo set wifi=wifi+?,alltraffic=? where pkgName=?";
		Cursor cursor = dbr.rawQuery("select * from appInfo", null);
		if (cursor.getCount() > 0) {
			for (int i = 0; i < appList.size(); i++) {
				appInfo = appList.get(i);
				String pkgname = appInfo.getPkgname();
				long wifi = appInfo.getWifi();
				long alltraffic = appInfo.getAlltraffic();
				dbw.execSQL(update, new Object[] { wifi, alltraffic, pkgname });
			}
			cursor.close();
			// Çå³ý»º´æÊý¾Ý
			this.appList = null;
		} else {
			initDB();
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
			long wifi = cursor.getLong(cursor.getColumnIndex("wifi"));
			long alltraffic = cursor.getLong(cursor
			        .getColumnIndex("alltraffic"));
			long gprs = alltraffic - wifi;
			appList.add(new AppInfo(label, wifi, gprs, alltraffic));
		}
		cursor.close();
		return appList;
	}
}
