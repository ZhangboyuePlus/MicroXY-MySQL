package com.project.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.Util.Constant;

public class DBVersionOperator 
{
		private SQLiteDatabase db  =null;
		
		public DBVersionOperator(Context context)
		{
			DBHelper helper = new DBHelper(context, Constant.NAME_DB, Constant.BDVersion);
			db = helper.getWritableDatabase();
		}
		
		public void insertVersion(String ver, String ownerOfVersion)
		{
			String SQLString = "INSERT INTO " + Constant.TABLE_NAME_VERSION + 
							   " (ver, ownerOfVersion) VALUES " + 
							   "('" + ver + "', '" + ownerOfVersion + "')";
			db.execSQL(SQLString);
			db.close();
	 	}
		
		public void updateVersion(String ver, String ownerOfVersion)
		{
			String SQLString = "UPDATE " + Constant.TABLE_NAME_VERSION + " SET ver = " + ver 
								+ " WHERE ownerOfVersion = " + "\'" + ownerOfVersion + "\'";
			db.execSQL(SQLString);
			db.close();
		}
		
		public boolean isVersionExist(String ownerOfVersion)
		{
			int count = 0;
			String SQLString = "SELECT COUNT(*) FROM " + Constant.TABLE_NAME_VERSION + 
									" WHERE ownerOfVersion = " + "\'" + ownerOfVersion + "\'";
			Cursor cursor = db.rawQuery(SQLString, null);
			cursor.moveToFirst();
			if(cursor.isAfterLast() == false)
				count = cursor.getInt(0);
			
			return count <= 0 ? false : true;
		}
		
		public String getVersion(String ownerOfVersion)
		{
			String ver = "";
			String SQLString = "SELECT ver FROM " + Constant.TABLE_NAME_VERSION + 
						" WHERE ownerOfVersion = " + "\'" + ownerOfVersion + "\'";
			
			Cursor cursor = db.rawQuery(SQLString, null);
			for(cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext())
				ver = cursor.getString(0);

			db.close();
			
			return ver;
		}
}
