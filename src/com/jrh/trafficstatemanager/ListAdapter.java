package com.jrh.trafficstatemanager;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater; 
	public List<AppInfo> mList; 
	
	public ListAdapter(Context context, List<AppInfo> list)
	  {
	    this.mContext = context;
	    this.mList = list;
	    this.mInflater = LayoutInflater.from(this.mContext);
	  }
	
	public void changed(List<AppInfo> list) {
		if (list != null) {
			mList = list;
		}
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		Object app = null;
		if(mList != null){
			app = mList.get(position);
		}
		return app;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.sub_item, null);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.label = (TextView) convertView.findViewById(R.id.label);
			viewHolder.wifi = (TextView) convertView.findViewById(R.id.wifi);
			viewHolder.gprs = (TextView) convertView.findViewById(R.id.gprs);
			viewHolder.alltraffic = (TextView) convertView.findViewById(R.id.total);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		AppInfo appInfo = mList.get(position);
		viewHolder.img.setImageDrawable(appInfo.getImg());
		viewHolder.label.setText(appInfo.getLabel());
		viewHolder.wifi.setText(appInfo.getWifi());
		viewHolder.gprs.setText(appInfo.getGprs());
		viewHolder.alltraffic.setText(appInfo.getAlltraffic());
		return convertView;
	}
	
	private static class ViewHolder{
		ImageView img;
		TextView label;
		TextView wifi;
		TextView gprs;
		TextView alltraffic;
	}
}
