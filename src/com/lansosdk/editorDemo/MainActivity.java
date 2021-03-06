package com.lansosdk.editorDemo;

import java.io.File;


import com.lansoeditor.demo.R;
import com.lansosdk.editorDemo.VideoEditDemoActivity.SubAsyncTask;
import com.lansosdk.videoeditor.LanSoEditor;
import com.lansosdk.videoeditor.MediaInfo;
import com.lansosdk.videoeditor.utils.snoCrashHandler;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {


	 private static final String TAG="MainActivity";
	 
	 
	EditText etVideoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		 Thread.setDefaultUncaughtExceptionHandler(new snoCrashHandler());
        setContentView(R.layout.activity_main);
        
        LanSoEditor.initSo(getApplicationContext());
        
        etVideoPath=(EditText)findViewById(R.id.id_main_etvideo);
        etVideoPath.setText("/sdcard/2x.mp4");
       
//        MediaInfo info=new MediaInfo("/sdcard/x3_m7crash.mp4");
//        info.prepare();
//        Log.i(TAG,"info:"+info.toString());
        
//         info=new MediaInfo("/sdcard/x3.mp4");
//        info.prepare();
//        Log.i(TAG,"info:"+info.toString());
//        
//         info=new MediaInfo("/sdcard/2x.mp4");
//        info.prepare();
//        Log.i(TAG,"info:"+info.toString());
//        
//         info=new MediaInfo("/sdcard/test_720p.mp4");
//        info.prepare();
//        Log.i(TAG,"info:"+info.toString());
//        
//         info=new MediaInfo("/sdcard/x2.mp4");
//        info.prepare();
//        Log.i(TAG,"info:"+info.toString());
        
        
        findViewById(R.id.id_main_demoplay).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkPath())
					startVideoPlayDemo();
			}
		});
        findViewById(R.id.id_main_demoedit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkPath())
				//	startVideoPlayDemo();
				
					startVideoEditDemo();
			}
		});
        
        if(LanSoEditor.selfPermissionGranted(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE")==false){
        	showHintDialog("当前没有读写权限");
        }else{
        	Log.i("sno","当前有读写权限");
        }
    }
    private boolean isstarted=false;
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
//    	if(isstarted)
//    		return;
//    	
//    	new Handler().postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				isstarted=true;
//				
//				startVideoEditDemo();
//			//	startVideoPlayDemo();
//			}
//		}, 1000);
      //  showHintDialog();

    }
    private void showHintDialog(String hint){
    	new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage(hint)
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
        .show();
    }
    		
    private void showHintDialog()
	{
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("SDK版本号是V1.2 [商用版本]\n\n,SDK底层做了授权限制,仅可在此demo中运行,并有效时间到2016年9月30号,请注意.)")
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
        .show();
	}
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	LanSoEditor.unInitSo();
    }
    
    
    private boolean checkPath(){
    	if(etVideoPath.getText()!=null && etVideoPath.getText().toString().isEmpty()){
    		Toast.makeText(MainActivity.this, "请输入视频地址", Toast.LENGTH_SHORT).show();
    		return false;
    	}	
    	else{
    		String path=etVideoPath.getText().toString();
    		if((new File(path)).exists()==false){
    			Toast.makeText(MainActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
    			return false;
    		}else{
    			MediaInfo info=new MediaInfo(path);
    	        info.prepare();
    	        Log.i(TAG,"info:"+info.toString());
    			return true;
    		}
    	}
    }
    private void startVideoPlayDemo()
    {
    	String path=etVideoPath.getText().toString();
    	Intent intent=new Intent(MainActivity.this,VideoPlayerActivity.class);
    	intent.putExtra("videopath", path);
    	startActivity(intent);
    }
    private void startVideoEditDemo()
    {
    	String path=etVideoPath.getText().toString();
    	Intent intent=new Intent(MainActivity.this,VideoEditDemoActivity.class);
    	intent.putExtra("videopath", path);
    	startActivity(intent);
    }
}
