package com.jrh.traffic.activity;

import com.jrh.traffic.adapter.ListAdapter;
import com.jrh.traffic.util.AppManager;
import com.jrh.trafficstatemanager.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListAdapter mListAdapter;
	private ListView mListView;
	private Context mContext;
	private AppManager mAppManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;

		mListView = (ListView) findViewById(R.id.applist);
		mListAdapter = new ListAdapter(mContext, mAppManager.getAllApps());
		mListView.setAdapter(mListAdapter);
	}
}