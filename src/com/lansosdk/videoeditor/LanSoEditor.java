package com.lansosdk.videoeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;
import android.util.Log;


public class LanSoEditor {

	private static boolean isLoaded=false;
	  
		static synchronized void loadLibraries() {
	        if (isLoaded)
	            return;
	        Log.d("lansoeditor","load libraries......");
	    	System.loadLibrary("ffmpegeditor");
    	    isLoaded=true;
	  }
		
		  public static void initSo(Context context)
		  {
			  		loadLibraries();
		    	    nativeInit(context);
		  }
	    public static void unInitSo()
	    {
	    	nativeUninit();
	    }
	    
	    @SuppressLint("NewApi") 
			  public static boolean selfPermissionGranted(Context context,String permission) {
			        // For Android < Android M, self permissions are always granted.
			        boolean result = true;
			        int targetSdkVersion = 0;
			        try {
			            final PackageInfo info = context.getPackageManager().getPackageInfo(
			                    context.getPackageName(), 0);
			            targetSdkVersion = info.applicationInfo.targetSdkVersion;
			        } catch (PackageManager.NameNotFoundException e) {
			            e.printStackTrace();
			        }

			        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

			            if (targetSdkVersion >= Build.VERSION_CODES.M) {
			                // targetSdkVersion >= Android M, we can
			                // use Context#checkSelfPermission
			                result = context.checkSelfPermission(permission)
			                        == PackageManager.PERMISSION_GRANTED;
			            } else {
			                // targetSdkVersion < Android M, we have to use PermissionChecker
			                result = PermissionChecker.checkSelfPermission(context, permission)
			                        == PermissionChecker.PERMISSION_GRANTED;
			            }
			        }
			        return result;
			    }
	    
	    public static native void nativeInit(Context ctx);
	    public static native void nativeUninit();
	    
}
