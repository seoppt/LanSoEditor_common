package com.lansosdk.videoeditor.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import android.text.TextUtils;

public class FileUtils {

	 /**
     * 在指定的文件夹里创建一个文件名字, 名字是当前时间,指定后缀.
     * @param dir   "/sdcard/"
     * @param suffix  ".mp4"
     * @return
     */
	public static String createFile(String dir,String suffix){
    	Calendar c = Calendar.getInstance();
		int  hour = c.get(Calendar.HOUR_OF_DAY);
	    int minute = c.get(Calendar.MINUTE);
		int year=c.get(Calendar.YEAR);
		int month=c.get(Calendar.MONTH)+1;
		int day=c.get(Calendar.DAY_OF_MONTH);
		int second=c.get(Calendar.SECOND);
		int millisecond=c.get(Calendar.MILLISECOND);
		year=year-2000;
		String name=dir;
		name+=String.valueOf(year);
		name+=String.valueOf(month);
		name+=String.valueOf(day);
		name+=String.valueOf(hour);
		name+=String.valueOf(minute);
		name+=String.valueOf(second);
		name+=String.valueOf(millisecond);
		name+=suffix;
		
		try {
			Thread.sleep(1);  //保持文件名的唯一性.
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File file=new File(name);
		if(file.exists()==false)
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return name;
    }
	/**
	 * 删除指定的文件.
	 * @param path
	 */
    public static void deleteFile(String path)
    {
    	File file=new File(path);
		if(file.exists())
		{
			file.delete();
		}
    }
    
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
	     public static void copyFile(InputStream in, OutputStream out) throws IOException {
	         byte[] buffer = new byte[1024];
	         int read;
	         while((read = in.read(buffer)) != -1){
	           out.write(buffer, 0, read);
	         }
	     }

	     public static boolean copyFile(File src, File dst){
	         boolean ret = true;
	         if (src.isDirectory()) {
	             File[] filesList = src.listFiles();
	             dst.mkdirs();
	             for (File file : filesList)
	                 ret &= copyFile(file, new File(dst, file.getName()));
	         } else if (src.isFile()) {
	             InputStream in = null;
	             OutputStream out = null;
	             try {
	                 in = new BufferedInputStream(new FileInputStream(src));
	                 out = new BufferedOutputStream(new FileOutputStream(dst));

	                 // Transfer bytes from in to out
	                 byte[] buf = new byte[1024];
	                 int len;
	                 while ((len = in.read(buf)) > 0) {
	                     out.write(buf, 0, len);
	                 }
	                 return true;
	             } catch (FileNotFoundException e) {
	             } catch (IOException e) {
	             } finally {
	                 close(in);
	                 close(out);
	             }
	             return false;
	         }
	         return ret;
	     }
	     public static boolean close(Closeable closeable) {
	         if (closeable != null)
	             try {
	                 closeable.close();
	                 return true;
	             } catch (IOException e) {}
	         return false;
	     }
}
