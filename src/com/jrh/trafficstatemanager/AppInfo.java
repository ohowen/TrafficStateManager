package com.jrh.trafficstatemanager;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private Drawable img;
	private String name;
	private String total;
	private String gprs;
	private String wifi;
	
	public AppInfo(){
		
	}

	public AppInfo(String name, String wifi, String gprs, String total) {
		this.name = name;
		this.wifi = wifi;
		this.gprs = gprs;
		this.total = total;
	}

	public String getGprs() {
		return this.gprs;
	}

	public Drawable getImg() {
		return this.img;
	}

	public String getName() {
		return this.name;
	}

	public String getTotal() {
		return this.total;
	}

	public String getWifi() {
		return this.wifi;
	}

	public void setGprs(String paramString) {
		this.gprs = paramString;
	}

	public void setImg(Drawable paramDrawable) {
		this.img = paramDrawable;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setTotal(String paramString) {
		this.total = paramString;
	}

	public void setWifi(String paramString) {
		this.wifi = paramString;
	}
}