package com.lansosdk.videoeditor;

public interface onVideoEditorProgressListener {
	/**
	 * 
	 * @param v
	 * @param currentTime  当前正在处理的视频帧的时间戳.即当前的位置.
	 */
    void onProgress(VideoEditor v,int percent);
}
