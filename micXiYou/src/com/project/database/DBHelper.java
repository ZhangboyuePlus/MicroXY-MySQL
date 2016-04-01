package com.project.database;

import com.project.Util.Constant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
	public DBHelper(Context context, String name, int version) 
	{
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		String SQLString = "CREATE TABLE " + Constant.TABLE_NAME_VERSION + "(id INTEGER PRIMARY KEY, ver VARCHAR(5) NOT NULL, "
															   + " ownerOfVersion VARCHAR(30) NOT NULL)";
		db.execSQL(SQLString);

		SQLString = "CREATE TABLE " + Constant.TABLE_NAME_STUDY_ROOM + "(id INTEGER PRIMARY KEY, isDoubleWeek VARCHAR(1) NOT NULL, "
				   + " building VARCHAR(1) NOT NULL, classRoom VARCHAR(3) NOT NULL, dayOfWeek VARCHAR(1) NOT NULL, "
				   + "classOfDay VARCHAR(1) NOT NULL )" ;
		db.execSQL(SQLString);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		String SQLString = "DROP TABLE IF EXIST " + Constant.TABLE_NAME_VERSION;
		db.execSQL(SQLString);
		
		SQLString = "DROP TABLE IF EXIST " + Constant.TABLE_NAME_STUDY_ROOM;
		db.execSQL(SQLString);
		
		this.onCreate(db);
	}
}
