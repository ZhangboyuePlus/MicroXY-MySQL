package com.project.Util;

import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

/*
 * 没有用过
 */
public class FileOperator 
{
	private Context context;
	
	public FileOperator(Context context)
	{
		this.context = context;
	}
	
	public void writeFile(String fileName, String fileContent) throws IOException
	{
		FileOutputStream out = context.openFileOutput(fileName, context.MODE_PRIVATE);
		out.write(fileContent.getBytes());
		out.close();
	}
}
