package com.lansosdk.videoeditor;


import com.lansosdk.videoeditor.utils.FileUtils;

import android.content.Context;
import android.os.Message;
import android.util.Log;

/**
 *  暂时只支持一个音频和一个视频组成的多媒体文件,如MP4等,如果有多个音频,则音频数据是最后一个音频的info.
 *
 */
public class MediaInfo {
//

	 private static final String TAG="MediaInfo";
	 
	 /***************video track info(total 12)*************** */
	 /**
	  * 
	  */
	 public  int vHeight;
     public  int vWidth;
     public  int vCodecHeight;
     public  int vCodecWidth;    
     
     /**
      * 视频的码率,注意,一般的视频编码时采用的是动态码率VBR,故这里得到的是平均值, 建议在使用时,乘以1.5后,使用.
      * 
      */
     public int vBitRate; 
     /**
      * 视频文件中的视频流总帧数.
      */
     public int vTotalFrames;  
     
     public float vDuration;  //单位秒.
     public float vFrameRate;
     public float vRotateAngle;
     
     public boolean vHasBFrame;
     public String vCodecName;
     public String vPixelFmt;
     
     /********************audio track info (total 12)**************************/
     
     public int aSampleRate;
     public int aChannels;
     /**
      * 视频文件中的音频流 总帧数.
      */
     public int aTotalFrames;
     public int aBitRate;
     public int aMaxBitRate;   
     public float aDuration;
     public String  aCodecName;
    
     
     private String mfilePath;
     private boolean getSuccess=false;
     
     public MediaInfo(String filePath)
     {
    	 mfilePath=filePath;
     }
     
     public int prepare()
     {
    	int ret=0;
    	 if(FileUtils.fileExist(mfilePath)){ //这里检测下mfilePath是否是多媒体后缀.
    		 ret= nativePrepare(mfilePath);	 
    		 if(ret>=0)
    			 getSuccess=true;
    	 }else{
    		 Log.e(TAG,"mediainfo file is may be not exist!");
    		 ret=-1;
    	 } 
    	 return ret;
     }
     
     public void release()
     {
    	 //TODO nothing 
    	 getSuccess=false;
     }
     @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	 String info="file name:"+mfilePath+"\n";
    	 info+= "vHeight:"+vHeight+"\n";
    	 info+= "vWidth:"+vWidth+"\n";
    	 info+= "vCodecHeight:"+vCodecHeight+"\n";
    	 info+= "vCodecWidth:"+vCodecWidth+"\n";
    	 info+= "vBitRate:"+vBitRate+"\n";
    	 info+= "vTotalFrames:"+vTotalFrames+"\n";
    	 info+= "vDuration:"+vDuration+"\n";
    	 info+= "vFrameRate:"+vFrameRate+"\n";
    	 info+= "vRotateAngle:"+vRotateAngle+"\n";
    	 info+= "vHasBFrame:"+vHasBFrame+"\n";
    	 info+= "vCodecName:"+vCodecName+"\n";
    	 info+= "vPixelFmt:"+vPixelFmt+"\n";
    	 
    	 info+= "aSampleRate:"+aSampleRate+"\n";
    	 info+= "aChannels:"+aChannels+"\n";
    	 info+= "aTotalFrames:"+aTotalFrames+"\n";
    	 info+= "aBitRate:"+aBitRate+"\n";
    	 info+= "aMaxBitRate:"+aMaxBitRate+"\n";
    	 info+= "aDuration:"+aDuration+"\n";
    	 info+= "aCodecName:"+aCodecName+"\n";
    	 
    	if(getSuccess)
    		return info;
    	else
    	 return "MediaInfo is not ready.or call failed";
    }
     private  native int nativePrepare(String filepath);
     
     //used by JNI
     private void setVideoCodecName(String name)
     {
    	 this.vCodecName=name;
     }
     //used by JNI
     private void setVideoPixelFormat(String pxlfmt)
     {
    	 this.vPixelFmt=pxlfmt;
     }
     //used by JNI
     private void setAudioCodecName(String name)
     {
    	 this.aCodecName=name;
     }
     
     /*
      * 测试
 //        new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				MediaInfo mif=new MediaInfo("/sdcard/2x.mp4");
//				mif.prepare();
//				Log.i(TAG,"mif is:"+ mif.toString());
//				mif.release();
//			}
//		},"testMediaInfo#1").start();
//  new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				MediaInfo mif=new MediaInfo("/sdcard/2x.mp4");
//				mif.prepare();
//				Log.i(TAG,"mif is:"+ mif.toString());
//				mif.release();
//			}
//		},"testMediaInfo#2").start();
      */
}
