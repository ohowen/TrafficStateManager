package com.jrh.trafficstatemanager;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private Drawable img;
	private String pkgname;
	private String label;
	private String alltraffic;
	private String gprs;
	private String wifi;
	private String frequency;
	private String tag;
	
	public AppInfo(){}

	public AppInfo(String label, String wifi, String gprs, String alltraffic) {
		this.label = label;
		this.wifi = wifi;
		this.gprs = gprs;
		this.alltraffic = alltraffic;
	}

	public Drawable getImg() {
		return img;
	}

	public void setImg(Drawable img) {
		this.img = img;
	}

	public String getPkgname() {
		return pkgname;
	}

	public void setPkgname(String pkgname) {
		this.pkgname = pkgname;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAlltraffic() {
		return alltraffic;
	}

	public void setAlltraffic(String alltraffic) {
		this.alltraffic = alltraffic;
	}

	public String getGprs() {
		return gprs;
	}

	public void setGprs(String gprs) {
		this.gprs = gprs;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}