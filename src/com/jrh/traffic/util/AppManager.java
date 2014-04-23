package com.jrh.traffic.util;

import java.util.ArrayList;
import java.util.HashMap;
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

	/**
	 * Save a specific app's traffic data to database
	 */
	public void saveData(HashMap<String, AppInfo> appMap) {

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

	
	public HashMap<String, AppInfo> getNetworkAppMap(Context context) {
		// ��ȡ���еİ�װ���ֻ��ϵ�Ӧ����������Ϣ�����һ�ȡ��Щ���������Ȩ����Ϣ
		PackageManager pm = context.getPackageManager();// ��ȡϵͳӦ�ð�����
		// ��ȡÿ�����ڵ�androidmanifest.xml��Ϣ������Ȩ�޵ȵ�
		List<PackageInfo> pinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
						| PackageManager.GET_PERMISSIONS);
		HashMap<String, AppInfo> appMap = new HashMap<String, AppInfo>();
		// ����ÿ��Ӧ�ð���Ϣ
		for (PackageInfo info : pinfos) {
			// ����ÿ���������Ӧ��androidManifest.xml�����Ȩ��
			String[] premissions = info.requestedPermissions;
			if (premissions != null && premissions.length > 0) {
				// �ҳ���Ҫ��������Ӧ�ó���
				for (String premission : premissions) {
					if ("android.permission.INTERNET".equals(premission)) {
						// ��ȡÿ��Ӧ�ó����ڲ���ϵͳ�ڵĽ���id
						int uId = info.applicationInfo.uid;
						// �������-1��������֧��ʹ�ø÷�����ע�������2.2���ϵ�
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
							appMap.put(pkgName, appInfo);
						}
					}
				}
			}
		}
		return appMap;
	}
}