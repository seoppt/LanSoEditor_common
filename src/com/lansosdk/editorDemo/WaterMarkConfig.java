package com.lansosdk.editorDemo;

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
	  public final int  bitRate;
	  
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
