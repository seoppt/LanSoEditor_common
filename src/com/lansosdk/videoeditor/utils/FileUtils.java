package com.lansosdk.videoeditor.utils;

import java.io.File;

import android.text.TextUtils;

public class FileUtils {

	 public static String getFileNameFromPath(String path){
	        if (path == null)
	            return "";
	        int index = path.lastIndexOf('/');
	        if (index> -1)
	            return path.substring(index+1);
	        else
	            return path;
	    }

	    public static String getParent(String path){
	        if (TextUtils.equals("/", path))
	            return path;
	        String parentPath = path;
	        if (parentPath.endsWith("/"))
	            parentPath = parentPath.substring(0, parentPath.length()-1);
	        int index = parentPath.lastIndexOf('/');
	        if (index > 0){
	            parentPath = parentPath.substring(0, index);
	        } else if (index == 0)
	            parentPath = "/";
	        return parentPath;
	    }
	     public static boolean fileExist(String absolutePath)
		 {
			 if(absolutePath==null)
				 return false;
			 else 
				 return (new File(absolutePath)).exists();
		 }
	     public static boolean filesExist(String[] fileArray)
		 {
			 
			 for(String file: fileArray)
			 {
				 if(fileExist(file)==false)
					 return false;
			 }
			 return true;
		 }
}
