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
		// ��ȡ���еİ�װ���ֻ��ϵ�Ӧ���������Ϣ�����һ�ȡ��Щ��������Ȩ����Ϣ
		PackageManager pm = getPackageManager();// ��ȡϵͳӦ�ð�����
		// ��ȡÿ�����ڵ�androidmanifest.xml��Ϣ������Ȩ�޵ȵ�
		List<PackageInfo> pinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
						| PackageManager.GET_PERMISSIONS);
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
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
						// �������-1������֧��ʹ�ø÷�����ע�������2.2���ϵ�
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
	 * �ڹػ�ǰ ��ͳ�Ƶ�Ӧ������ʹ��������浽���ݿ�
	 * @author Rio
	 *
	 */
	public static class ShutDownReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// ���ݱ������
			
		}
	}
	
	/**
	 * ���������ӶϿ�ʱ����ͳ�Ƶ�Ӧ������ʹ��������浽���ݿ�
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
			//��ʾ�ֻ���ʱû���κ���������
			if(wifi != null && conn != null
					&& State.CONNECTED != wifi
					&& State.CONNECTED != conn){
				//���ݱ������
			}
		}
	}
	
	/**
	 * ����wifi�����͹رյ����
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
					// ���౾��wifi������ uidӦ�õ� ����
					
				} else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
					// ��¼��ǰuidӦ�õ�����
					
				}
			}
		}
	}
}
