package com.jrh.traffic.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	public DBOpenHelper(Context context) {
		super(context, "apptrafficstate.db", null, 1);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE appInfo(appid integer primary key autoincrement, pkgName varchar(40), label VARCHAR(12),wifi VARCHAR(12),gprs VARCHAR(12),alltraffic VARCHAR(12),tag VARCHAR(12))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}