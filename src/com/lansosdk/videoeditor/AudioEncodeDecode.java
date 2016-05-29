package com.lansosdk.videoeditor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;


public class AudioEncodeDecode {
		  
		    /**
		     * 把原始音频流,包括MP3 或aac解码成裸码流保存起来, 小端模式
		     * 中间可以读取各种音频信息,但没有读取.
		     * 一般播放采用ffplay -f s16le -ar 44.1k -ac 2 hong222.pcm
		     * @param srcAudioPath
		     * @param dstAudioPcmPath  
		     * @return
		     */
		    public static int decodeAudio(String srcAudioPath,String dstAudioPcmPath)
			{
//			  		int mChannels;
//			  		int mNumSamples; 
//			  	     int mSampleRate;
//			  	    int mNumFrames;
		    	
		    	 File tmpFile=new File(srcAudioPath);
		    	 if(tmpFile.exists()==false)
					return -1;

		    		FileOutputStream fos = null;
		    		BufferedOutputStream bos = null;
			        try {
			            fos = new FileOutputStream(dstAudioPcmPath);
			            bos = new BufferedOutputStream(fos);
			        } catch (IOException ioe) {
			           Log.e("lansongeditor",ioe.toString());
			           return -1;
			        }
		        
			        MediaExtractor extractor = new MediaExtractor();
			        MediaFormat format = null;
			        int i;
			        try {
						extractor.setDataSource(srcAudioPath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        int numTracks = extractor.getTrackCount();
			        // find and select the first audio track present in the file.
			        
			        for (i=0; i<numTracks; i++) {
			            format = extractor.getTrackFormat(i);
			            if (format.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
			                extractor.selectTrack(i);
			                break;
			            }
			        }
			        if (i == numTracks) {
			        	extractor.release();
			            Log.e("lansongedit","No audio track found in " + srcAudioPath);
			       	 	try {
							bos.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							Log.e("lansongeditor",e1.toString());
							return -1;
						}
			            return -1;
			        }
			        
			        
			        MediaCodec codec;
					try {
						codec = MediaCodec.createDecoderByType(format.getString(MediaFormat.KEY_MIME));
						 codec.configure(format, null, null, 0);
					        codec.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
//						e.printStackTrace();
						 Log.e("lansongedit","No audio track found in " + srcAudioPath);
						 if(extractor!=null)
							 extractor.release();
						 
						 try {
							bos.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							Log.e("lansongeditor",e1.toString());
							return -1;
						}
						 return -1;
					}
			       
			        int decodedSamplesSize = 0;  // size of the output buffer containing decoded samples.
			        byte[] decodedSamples = null;
			        ByteBuffer[] inputBuffers = codec.getInputBuffers();
			        ByteBuffer[] outputBuffers = codec.getOutputBuffers();
			        int sample_size;
			        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
			        long presentation_time;
			        boolean done_reading = false;

			        // Set the size of the decoded samples buffer to 1MB (~6sec of a stereo stream at 44.1kHz).
			        // For longer streams, the buffer size will be increased later on, calculating a rough
			        // estimate of the total size needed to store all the samples in order to resize the buffer
			        // only once.
			        Boolean firstSampleData = true;
				    

				    
			        while (true) {
			            // read data from file and feed it to the decoder input buffers.
			            int inputBufferIndex = codec.dequeueInputBuffer(100);
			            if (!done_reading && inputBufferIndex >= 0) {
			                sample_size = extractor.readSampleData(inputBuffers[inputBufferIndex], 0);
			                if (firstSampleData
			                        && format.getString(MediaFormat.KEY_MIME).equals("audio/mp4a-latm")
			                        && sample_size == 2) {
			                    // For some reasons on some devices (e.g. the Samsung S3) you should not
			                    // provide the first two bytes of an AAC stream, otherwise the MediaCodec will
			                    // crash. These two bytes do not contain music data but basic info on the
			                    // stream (e.g. channel configuration and sampling frequency), and skipping them
			                    // seems OK with other devices (MediaCodec has already been configured and
			                    // already knows these parameters).
			                    extractor.advance();
			                } else if (sample_size < 0) {
			                    // All samples have been read.
			                    codec.queueInputBuffer(
			                            inputBufferIndex, 0, 0, -1, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
			                    done_reading = true;
			                } else {
			                    presentation_time = extractor.getSampleTime();
			                    codec.queueInputBuffer(inputBufferIndex, 0, sample_size, presentation_time, 0);
			                    extractor.advance();
			                }
			                firstSampleData = false;
			            }

			            // Get decoded stream from the decoder output buffers.
			            int outputBufferIndex = codec.dequeueOutputBuffer(info, 100);
			            if (outputBufferIndex >= 0 && info.size > 0) {
			                if (decodedSamplesSize < info.size) {
			                    decodedSamplesSize = info.size;
			                    decodedSamples = new byte[decodedSamplesSize];
			                }
			                outputBuffers[outputBufferIndex].get(decodedSamples, 0, info.size);
			                outputBuffers[outputBufferIndex].clear();
			                
			                try {
			    				bos.write(decodedSamples);
			    			} catch (IOException e) {
			    				// TODO Auto-generated catch block
			    				e.printStackTrace();
			    			}
			                codec.releaseOutputBuffer(outputBufferIndex, false);
			            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
			                outputBuffers = codec.getOutputBuffers();
			            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
			                // Subsequent data will conform to new format.
			                // We could check that codec.getOutputFormat(), which is the new output format,
			                // is what we expect.
			            }
			            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
			                // We got all the decoded data from the decoder. Stop here.
			                // Theoretically dequeueOutputBuffer(info, ...) should have set info.flags to
			                // MediaCodec.BUFFER_FLAG_END_OF_STREAM. However some phones (e.g. Samsung S3)
			                // won't do that for some files (e.g. with mono AAC files), in which case subsequent
			                // calls to dequeueOutputBuffer may result in the application crashing, without
			                // even an exception being thrown... Hence the second check.
			                // (for mono AAC files, the S3 will actually double each sample, as if the stream
			                // was stereo. The resulting stream is half what it's supposed to be and with a much
			                // lower pitch.)
			                break;
			            }
			        }
			      
			        extractor.release();
			        extractor = null;
			        codec.stop();
			        codec.release();
			        codec = null;
			        try {
			             if (bos != null) {
			                 bos.close();
			             }
			             if (fos != null) {
			                 fos.close();
			             }
			         } catch (IOException ioe) {
			        	Log.e("lansongeditor",ioe.toString());
			        	 return -1;
			         }
			        return 0;
		}
		/**
		 * 把原始的音频pcm数据,压缩成aac格式的数据,一直阻塞,只到处理完毕.
		 * 注意:如果时长过大,则可能出问题,因为内部分配一个大buffer来接收所有压缩好的字节数
		 *  
		 * @param srcPath
		 * @param dstPath
		 * @param startTime  单位秒
		 * @param endTime
		 * @param sampleRate  测试用44100,根据pcm之前的参数数据而定
		 * @param channels   测试用2, 根据pcm之前的参数而定.
		 * @param bitRate  测试用的是 64000
		 * @return 成功返回1,失败则返回-1;
		 */
		public static int encodePcmData(String srcPath,String dstPath, float startTime, float endTime,int sampleRate,int channels,int bitRate)
	    {
			 File tmpFile=new File(srcPath);
			if(tmpFile.exists()==false)
				return -1;
			
	    	//开始的偏移字节  等于     开始时间*采样率*2 * 通道数. 
	        int startOffset = (int)(startTime * sampleRate) * 2 * channels;
	        
	        //总共多少个采样点,时间单位是秒. 这个时间段的采样点等于 时间段*采样率 
	        int numSamples = (int)((endTime - startTime) * sampleRate);
	        
	        // Some devices have problems reading mono AAC files (e.g. Samsung S3). Making it stereo.
	        int numChannels = (channels == 1) ? 2 : channels;  //一定要用立体声,因为一些不支持单声道.

	        String mimeType = "audio/mp4a-latm";
	        
	        //bitrate等于 每个通道的bitrate*总通道数
	        int bitrate = bitRate * numChannels;  // rule of thumb for a good quality: 64kbps per channel.  /<---每个通道是64000的比特率
	       
	        // Get an estimation of the encoded data based on the bitrate. Add 10% to it.
	        int estimatedEncodedSize = (int)((endTime - startTime) * (bitrate / 8) * 1.1);  
	        ByteBuffer encodedBytes = ByteBuffer.allocate(estimatedEncodedSize);//一次性分配所有编码后的字节需要的大小,暂时不清楚,如果分配失败,会怎样.????
	        
	        MediaCodec codec=null;
			try {
				codec = MediaCodec.createEncoderByType(mimeType);
				  MediaFormat format = MediaFormat.createAudioFormat(mimeType, sampleRate, numChannels);
			        format.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);  //设置bitrate.
			        
			        codec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
			        codec.start();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				Log.e("lansoeidtor",e1.toString());
				return -1;
			}
			
	       
	        ByteBuffer[] inputBuffers = codec.getInputBuffers();//<----这里可能调整.
	        ByteBuffer[] outputBuffers = codec.getOutputBuffers();
	        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
	        
	        boolean done_reading = false;
	        long presentation_time = 0;

	        //每次取1024个采样点.
	        int frame_size = 1024;  // number of samples per frame per channel for an mp4 (AAC) stream.
	        byte buffer[] = new byte[frame_size * numChannels * 2];  // a sample is coded with a short.因为单位是short.
	        
	        //总的采样点是 增加两帧
	        numSamples += (2 * frame_size);  // Adding 2 frames, Cf. priming frames for AAC.
	        
	        //总帧数, 
	        int tot_num_frames = 1 + (numSamples / frame_size);  // first AAC frame = 2 bytes
	        
	        if (numSamples % frame_size != 0) {
	            tot_num_frames++;
	        }
	        
	        int[] frame_sizes = new int[tot_num_frames];
	        
	        int num_out_frames = 0;
	        int num_frames=0;
	        int num_samples_left = numSamples;
	        int encodedSamplesSize = 0;  // size of the output buffer containing the encoded samples.
	        byte[] encodedSamples = null;
	        
	        InputStream is = null;
			// TODO Auto-generated method stub
			   try {
	            	 is = new FileInputStream(srcPath);
				} catch (FileNotFoundException e) {
				//	e.printStackTrace();
					Log.e("lansongeditor",e.toString());
					return -1;
				}
			   try {
				is.skip(startOffset);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
//				e1.printStackTrace();
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("lansongeditor",e.toString());
				}
				Log.e("lansongeditor",e1.toString());
				return -1;
			}
	        
			   while (true) {  
	        	
	        	//step1://把采样点填入到编码器中.
	            // Feed the samples to the encoder.
	            int inputBufferIndex = codec.dequeueInputBuffer(100);
	            if (!done_reading && inputBufferIndex >= 0) {
	                if (num_samples_left <= 0) {
	                    // All samples have been read.
	                    codec.queueInputBuffer(
	                            inputBufferIndex, 0, 0, -1, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
	                    done_reading = true;
	                } else {
	                    inputBuffers[inputBufferIndex].clear();
	                    if (buffer.length > inputBuffers[inputBufferIndex].remaining()) {
	                        // Input buffer is smaller than one frame. This should never happen.
	                        continue;
	                    }
	                    // bufferSize is a hack to create a stereo file from a mono stream.
	                    int bufferSize = (channels == 1) ? (buffer.length / 2) : buffer.length;
	                    
	                    
	                    	//获取数据.
	                	int getsize=0;
	                    try {
	                    	getsize=is.read(buffer, 0, bufferSize);
	                    	//如果最后的数据不足一帧,则最后的部分填0
	                    	if(getsize<bufferSize){
	                    		for (int i=getsize; i < bufferSize; i++) {
	                                buffer[i] = 0;  // pad with extra 0s to make a full frame.
	                            }
	                    	}
	            		} catch (IOException e) {
	            			// TODO Auto-generated catch block
	            			//e.printStackTrace();
	            			Log.e("lansongeditor",e.toString());
	            		}
	                        
	                    if (channels == 1) {
	                        for (int i=bufferSize - 1; i >= 1; i -= 2) {
	                            buffer[2*i + 1] = buffer[i];
	                            buffer[2*i] = buffer[i-1];
	                            buffer[2*i - 1] = buffer[2*i + 1];
	                            buffer[2*i - 2] = buffer[2*i];
	                        }
	                    }
	                    num_samples_left -= frame_size;
	                    inputBuffers[inputBufferIndex].put(buffer);  //<-------------放入到inputbuffer中.
	                    
	                    presentation_time = (long) (((num_frames++) * frame_size * 1e6) / sampleRate);
	                    codec.queueInputBuffer(
	                            inputBufferIndex, 0, buffer.length, presentation_time, 0);
	                }
	            }
	            //step2:从编码器中获取数据.	
	            // Get the encoded samples from the encoder.
	            int outputBufferIndex = codec.dequeueOutputBuffer(info, 100);
	            if (outputBufferIndex >= 0 && info.size > 0 && info.presentationTimeUs >=0) {
	            	
	                if (num_out_frames < frame_sizes.length) {
	                    frame_sizes[num_out_frames++] = info.size;
	                }
	                
	                if (encodedSamplesSize < info.size) {
	                    encodedSamplesSize = info.size;
	                    encodedSamples = new byte[encodedSamplesSize];
	                }
	                outputBuffers[outputBufferIndex].get(encodedSamples, 0, info.size);
	                outputBuffers[outputBufferIndex].clear();
	                
	                //release
	                codec.releaseOutputBuffer(outputBufferIndex, false);
	                
	                if (encodedBytes.remaining() < info.size) {  // Hopefully this should not happen.
	                    estimatedEncodedSize = (int)(estimatedEncodedSize * 1.2);  // Add 20%.
	                    ByteBuffer newEncodedBytes = ByteBuffer.allocate(estimatedEncodedSize);
	                    int position = encodedBytes.position();
	                    encodedBytes.rewind();
	                    newEncodedBytes.put(encodedBytes);
	                    encodedBytes = newEncodedBytes;
	                    encodedBytes.position(position);
	                }
	                encodedBytes.put(encodedSamples, 0, info.size);
	            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
	                outputBuffers = codec.getOutputBuffers();
	            } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
	                // Subsequent data will conform to new format.
	                // We could check that codec.getOutputFormat(), which is the new output format,
	                // is what we expect.
	            }
	            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
	                // We got all the encoded data from the encoder.
	                break;
	            }
	        } //end of  encoder
	        if(is!=null){
	    		try {
	    			is.close();
	    			is=null;
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    	}
	        int encoded_size = encodedBytes.position();
	        encodedBytes.rewind();
	        codec.stop();
	        codec.release();
	        codec = null;
	        

	        // Write the encoded stream to the file, 4kB at a time.
	        buffer = new byte[4096];
	        try {
	        	//先写头
	            FileOutputStream outputStream = new FileOutputStream(dstPath);
	            outputStream.write(
	                    MP4Header.getMP4Header(sampleRate, numChannels, frame_sizes, bitrate));
	            
	            //再写数据.
	            while (encoded_size - encodedBytes.position() > buffer.length) {
	                encodedBytes.get(buffer);
	                outputStream.write(buffer);
	            }
	            int remaining = encoded_size - encodedBytes.position();
	            if (remaining > 0) {
	                encodedBytes.get(buffer, 0, remaining);
	                outputStream.write(buffer, 0, remaining);
	            }
	            //完成 close
	            outputStream.close();
	            
	        } catch (IOException e) {
	            Log.e("Ringdroid", "Failed to create the .m4a file.");
	            
	            StringWriter writer = new StringWriter();
		        e.printStackTrace(new PrintWriter(writer));
	            Log.e("Ringdroid",  writer.toString());
	            return -1;
	        }
	    	return 0;
	    }
}
