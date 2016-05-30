package com.lansosdk.editorDemo;

import com.lansosdk.videoeditor.MediaInfo;

public class WaterMarkConfig {
	
	  public final String srcPath;
	  public final String logo1;
	  public final int  logo1X;
	  public final int logo1Y;

	  public final String logo2;
	  public final int logo2X;
	  public final int logo2Y;
	  public final float logo2StartTimeS;
	  public final float logo2EndTimeS;
	  public final String dstPath;
	  public final int  bitRate;// <============注意:这里的bitrate在设置的时候, 因为是设置编码器的恒定码率, 推荐设置为 预设值的1.5倍为准, 比如视频原有的码率是1M,则裁剪一半,预设值可能是500k, 
			   //这里推荐是为500k的1.5,因为原有的视频大部分是动态码率VBR,可以认为通过{@link MediaInfo} 得到的 {@link MediaInfo#vBitRate}是平均码率,这里要设置,推荐是1.5倍为好.
	  
	  public WaterMarkConfig(String src,String lo1,int lo1X,int lo1Y,String lo2,int lo2X,int lo2Y,
			  float lo2StartTimeS,float lo2EndTimeS,String dst,int bitrate){
		  
		  this.srcPath=src;
		  this.logo1=lo1;
		  this.logo1X=lo1X;
		  this.logo1Y=lo1Y;
		  this.logo2=lo2;
		  this.logo2X=lo2X;
		  this.logo2Y=lo2Y;
		  this.logo2StartTimeS=lo2StartTimeS;
		  this.logo2EndTimeS=lo2EndTimeS;
		  this.dstPath=dst;
		  this.bitRate=bitrate;
	  }
}
