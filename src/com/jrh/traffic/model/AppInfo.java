package com.jrh.traffic.model;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private Drawable img = null;
	private String uid = "uid";
	private String pkgname = "pkgname";
	private String label = "label";
	private long alltraffic;
	private long gprs;
	private long wifi;
	private long temp;
	private String frequency = "frequency";
	private String tag = "tag";

	public AppInfo() {
	}

	public AppInfo(String pkgName, String label, long wifi, long gprs,
	        long alltraffic, long temp) {
		this.pkgname = pkgName;
		this.label = label;
		this.wifi = wifi;
		this.gprs = gprs;
		this.alltraffic = alltraffic;
		this.temp = temp;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public long getAlltraffic() {
		return alltraffic;
	}

	public void setAlltraffic(long alltraffic) {
		this.alltraffic = alltraffic;
	}

	public long getGprs() {
		return gprs;
	}

	public void setGprs(long gprs) {
		this.gprs = gprs;
	}

	public long getWifi() {
		return wifi;
	}

	public void setWifi(long wifi) {
		this.wifi = wifi;
	}

	public long getTemp() {
		return temp;
	}

	public void setTemp(long temp) {
		this.temp = temp;
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