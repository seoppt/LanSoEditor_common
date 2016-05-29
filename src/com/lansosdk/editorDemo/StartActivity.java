package com.lansosdk.editorDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import com.lansoeditor.demo.R;
import com.lansosdk.videoeditor.utils.snoCrashHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

public class StartActivity extends Activity{

    private Handler mHandler;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 Thread.setDefaultUncaughtExceptionHandler(new snoCrashHandler());
		final View view = View.inflate(this, R.layout.start_layout3, null);
		setContentView(view);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
		alphaAnimation.setDuration(5000);
		view.setAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {
					
			}

			public void onAnimationEnd(Animation animation) {
				startMainActivity();
				StartActivity.this.finish();
			}
		});
	}
	private void startMainActivity()
	{
		Intent intent=new Intent(StartActivity.this,SdkHintActivity.class);
		startActivity(intent);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

}
