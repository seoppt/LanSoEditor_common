package com.lansosdk.editorDemo;

import java.io.IOException;

import com.lansoeditor.demo.R;
import com.lansosdk.videoeditor.utils.TextureRenderView;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;



public class VideoPlayerActivity extends Activity {
	   

	private TextureRenderView textureView;
    private MediaPlayer mediaPlayer=null;  
    String videoPath=null;
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.player_layout);  
        textureView=(TextureRenderView)findViewById(R.id.surface1);
        
        videoPath=getIntent().getStringExtra("videopath");
        
        
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
			}
		});
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
  
  
    @Override  
    protected void onPause() {  
        if (mediaPlayer!=null) {  
        	mediaPlayer.stop();
        	mediaPlayer.release();
        	mediaPlayer=null;  
        }  
        super.onPause();  
    }  
  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
    }  
}
