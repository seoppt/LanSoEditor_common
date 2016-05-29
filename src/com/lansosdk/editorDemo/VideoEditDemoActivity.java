package com.lansosdk.editorDemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lansoeditor.demo.R;
import com.lansosdk.videoeditor.MediaInfo;
import com.lansosdk.videoeditor.VideoEditor;
import com.lansosdk.videoeditor.VideoEditor.onProgressListener;
import com.lansosdk.videoeditor.utils.FileUtils;
import com.lansosdk.videoeditor.utils.snoCrashHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class VideoEditDemoActivity extends Activity{

	String videoPath=null;
	VideoEditor mEditor = new VideoEditor();
	ProgressDialog  mProgressDialog;
	int videoDuration;
	boolean isRuned=false;
	MediaInfo   mMediaInfo;
	TextView tvProgressHint;
	private final static String TAG="videoEditDemoActivity";
	
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
			 Thread.setDefaultUncaughtExceptionHandler(new snoCrashHandler());
	        
			 setContentView(R.layout.video_edit_demo_layout);
	        
			 videoPath=getIntent().getStringExtra("videopath");
				
			 mMediaInfo=new MediaInfo(videoPath);
				
			
			 tvProgressHint=(TextView)findViewById(R.id.id_video_edit_progress_hint);
			 
	        findViewById(R.id.id_video_edit_btn).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(videoPath==null)
						return ;
					
					mMediaInfo.prepare();
					
					Log.i(TAG,mMediaInfo.toString());
					
					if(mMediaInfo.vDuration>60*1000){//大于60秒
						showHintDialog();
					}else{
						new SubAsyncTask().execute();
					}
				}
			});
	        
	        findViewById(R.id.id_video_play_btn).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(FileUtils.fileExist("/sdcard/video_demo_framecrop.mp4")){
						String path="/sdcard/video_demo_framecrop.mp4";
				    	Intent intent=new Intent(VideoEditDemoActivity.this,VideoPlayerActivity.class);
				    	intent.putExtra("videopath", path);
				    	startActivity(intent);
					}else{
						Toast.makeText(VideoEditDemoActivity.this, R.string.file_not_exist,Toast.LENGTH_SHORT).show();
					}
				}
			});
	        
	        
	        mEditor.setOnProgessListener(new onProgressListener() {
				
				@Override
				public void onProgress(VideoEditor v, int percent) {
					// TODO Auto-generated method stub
					
					Log.i(TAG,"current percent is:"+percent);
					tvProgressHint.setText(String.valueOf(percent)+"%");
				}
			});
	        
//
//	       if(isRuned==false){
//		       isRuned=true;
//	    	   new Handler().postDelayed(new Runnable() {
//	   			
//		   			@Override
//		   			public void run() {
//		   				// TODO Auto-generated method stub
//		   				mMediaInfo.prepare();
//						Log.i(TAG,mMediaInfo.toString());
//		   				new SubAsyncTask().execute();
//		   			}
//	   			}, 1000);
//	       }
	  } 
		private void showHintDialog()
		{
			new AlertDialog.Builder(this)
			.setTitle("提示")
			.setMessage("视频过大,可能会需要一段时间,您确定要处理吗?")
	        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new SubAsyncTask().execute();
				}
			})
			.setNegativeButton("取消", null)
	        .show();
		}
	  public class SubAsyncTask extends AsyncTask<Object, Object, Boolean>{
			  @Override
			protected void onPreExecute() {
			// TODO Auto-generated method stub
				  mProgressDialog = new ProgressDialog(VideoEditDemoActivity.this);
		          mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		          mProgressDialog.setMessage("正在处理中...");
		        
		          mProgressDialog.setCanceledOnTouchOutside(false);
		          mProgressDialog.show();
		          super.onPreExecute();
			}
      	    @Override
      	    protected synchronized Boolean doInBackground(Object... params) {
      	    	// TODO Auto-generated method stub
      	    	
//      	    	if(mMediaInfo!=null){
      	    	//这里是要做音频编码格式的检测,以方便快速的提取,直接拷贝,不用解码后再编码.
//      	    		if(mMediaInfo.aCodecName.equals("mp3")){
//      	    			mEditor.executeDeleteVideo("/sdcard/2x.mp4","/sdcard/2x_nov.mp3");
//      	    		}else if(mMediaInfo.aCodecName.equals("aac")){
//      	    			mEditor.executeDeleteVideo("/sdcard/2x.mp4","/sdcard/2x_nov.aac");
//      	    		}
//      	    	}
      	    //	mEditor.executeVideoCutOut("/sdcard/2x.mp4","/sdcard/2x_cut.mp4",5,5);
//      	    	mEditor.executeGetAllFrames("/sdcard/2x.mp4","/sdcard/","getpng");
      	    	mEditor.executeVideoFrameCrop(videoPath, 240, 240, 0, 0, "/sdcard/video_demo_framecrop.mp4",mMediaInfo.vCodecName,mMediaInfo.vBitRate);
      	    	
      	    	
//      	    	mEditor.executeConvertMp4toTs("/sdcard/2x.mp4","/sdcard/2x0.ts");
      	    	//因为静态码率
//      	    	mEditor.executeAddWaterMark("/sdcard/2x.mp4","/sdcard/watermark.png",0,0,"/sdcard/2xmark3.mp4",(int)(mMediaInfo.vBitRate*1.5f));
      	    	
      	    	
      	    	
      	    //	mEditor.pictureFadeInOut("/sdcard/testfade.png",3,0,40,50,75,"/sdcard/testfade.mp4");
      	    //	mEditor.pictureFadeIn("/sdcard/testfade.png",3,0,60,"/sdcard/testfade2.mp4");
      	//  	mEditor.pictureFadeOut("/sdcard/testfade.png",3,0,60,"/sdcard/testfade3.mp4");
      	    	
      	    	
      	    	//String srcVideoPath,String srcPngPath,int totalTime,int offsetTime,int fadeinStart,int fadeoutCnt,int x,int y,String dstPath);
      	    	
      	    //	mEditor.waterMarkFadeIn("/sdcard/2x.mp4","/sdcard/watermark.png",2,5,0,30,0,0,"/sdcard/2xmarkfade.mp4");
      	    //	mEditor.videoRotateAngle("/sdcard/2x.mp4", mMediaInfo.vCodecName, 60, "/sdcard/2x_angle.mp4");
      	    	
      	    	//这里检测mp3的时长,
//      	    	mEditor.audioAdjustVolumeMix("/sdcard/hongdou.mp3", "/sdcard/kaimendaji.mp3", 3.0f, 0.5f, "/sdcard/hongdouxxx.mp3");
      	    	
      	    //	mVideoEditor.audioAdjustVolumeMix("/sdcard/save_encodec4.aac","/sdcard/kaimen20s.mp3",3.0f,0.4f,"/sdcard/jni_amix.aac");
      	    	//mVideoEditor.avReverse(null, null, null);
      	    	//String srcPath,int totalTime,int fadeinstart,int fadeinCnt,int fadeoutstart,int fadeoutCnt,
//     		   	String dstPath
      	    	//demoVideoGray();
      	    	//demodeleteMisuc();
      	    	return null;
      	    }
    	@Override
    	protected void onPostExecute(Boolean result) { 
    		// TODO Auto-generated method stub
    		super.onPostExecute(result);
    		if( mProgressDialog!=null){
	       		 mProgressDialog.cancel();
	       		 mProgressDialog=null;
    		}
    		Log.i(TAG,"onpost-------------------end");
    	}
    }
}

