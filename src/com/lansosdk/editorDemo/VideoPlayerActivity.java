package com.lansosdk.editorDemo;

import java.io.IOException;

import com.lansoeditor.demo.R;
import com.lansosdk.videoeditor.player.IMediaPlayer;
import com.lansosdk.videoeditor.player.VPlayer;
import com.lansosdk.videoeditor.player.IMediaPlayer.OnPreparedListener;
import com.lansosdk.videoeditor.utils.TextureRenderView;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;



public class VideoPlayerActivity extends Activity {
	   

	private TextureRenderView textureView;
	private TextureRenderView textureView2;
    private MediaPlayer mediaPlayer=null;  
    String videoPath=null;
  
    private static final boolean VERBOSE = false; 
    private static final String TAG = "VideoPlayerActivity";
    
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.player_layout);  
        
    	
        videoPath=getIntent().getStringExtra("videopath");

        textureView=(TextureRenderView)findViewById(R.id.surface1);
        textureView.setSurfaceTextureListener(new SurfaceTextureListener() {
			
			@Override
			public void onSurfaceTextureUpdated(SurfaceTexture surface) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
					int height) {
				// TODO Auto-generated method stub
				play(new Surface(surface));
//				startPlayVideo(new Surface(surface));
			}
		});
        
//        textureView2=(TextureRenderView)findViewById(R.id.surface2);
//        textureView2.setSurfaceTextureListener(new SurfaceTextureListener() {
//			
//			@Override
//			public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
//					int height) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
//					int height) {
//				// TODO Auto-generated method stub
//				startPlayVideo2(new Surface(surface));
//			}
//		});
    }  
    public void play(Surface surface)  {  

    	if(videoPath==null)
    		return ;
        mediaPlayer = new MediaPlayer();  
        mediaPlayer.reset();  
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);  
        try {
			mediaPlayer.setDataSource(videoPath);
			  mediaPlayer.setSurface(surface);  
		        mediaPlayer.prepare();  
		        
		        textureView.setVideoSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
		        textureView.requestLayout();
		        
		        mediaPlayer.start();  
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
  

    private VPlayer  mVPlayer=null;
    private VPlayer  mVPlayer2=null;
    private void startPlayVideo(final Surface surface)
    {
          if (videoPath != null){
        	  mVPlayer=new VPlayer(this);
        	  mVPlayer.setVideoPath(videoPath);
              mVPlayer.setOnPreparedListener(new OnPreparedListener() {
    			
    			@Override
    			public void onPrepared(IMediaPlayer mp) {
    				// TODO Auto-generated method stub
    						mVPlayer.setSurface(surface);
    					    textureView.setVideoSize(mp.getVideoWidth(), mp.getVideoHeight());
    				        textureView.requestLayout();
    				        mVPlayer.start();
    					}
    			});
        	  mVPlayer.prepareAsync();
          }else {
              Log.e("sno", "Null Data Source\n");
              finish();
              return;
          }
    }
    private void startPlayVideo2(final Surface surface)
    {
        	  mVPlayer2=new VPlayer(this);
        	  mVPlayer2.setVideoPath(videoPath);
              mVPlayer2.setOnPreparedListener(new OnPreparedListener() {
    			
    			@Override
    			public void onPrepared(IMediaPlayer mp) {
    				// TODO Auto-generated method stub
    						mVPlayer2.setSurface(surface);
    						
    						Log.i("sno","=====>width"+mp.getVideoWidth()+" height"+mp.getVideoHeight());
    						
    					    textureView2.setVideoSize(mp.getVideoWidth(), mp.getVideoHeight());
    				        textureView2.requestLayout();
    				        mVPlayer2.start();
    					}
    			});
        	  mVPlayer2.prepareAsync();
    }
    @Override  
    protected void onPause() {  
        if (mediaPlayer!=null) {  
        	mediaPlayer.stop();
        	mediaPlayer.release();
        	mediaPlayer=null;  
        }
        if (mVPlayer!=null) {  
        	mVPlayer.stop();
        	mVPlayer.release();
        	mVPlayer=null;  
        }
        
        if (mVPlayer2!=null) {  
        	mVPlayer2.stop();
        	mVPlayer2.release();
        	mVPlayer2=null;  
        }
        super.onPause();  
    }  
  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
    }  
}
