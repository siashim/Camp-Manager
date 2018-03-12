package com.bmgsoftware.campmanageralpha;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.bmgsoftware.campmanageralpha.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
 
public class screen3_2_1move extends Activity {
 
	Button button2;
    int downloadedSize = 0;
    int totalSize = 0;
    String download_file_path = "http://bmgsoftware.com/uploads/distribute.jpg";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen3_2_1);
		
		addListenerOnButton();
		
		ImageView img = (ImageView) findViewById(R.id.imageView1);
		Bitmap bMap = BitmapFactory.decodeFile("/storage/emulated/0/Download/downloaded_file.jpg");
        img.setImageBitmap(bMap);
	}
	
	public void addListenerOnButton() {
		 
		final Context context = this;
		button2 = (Button) findViewById(R.id.buttonlog);
		button2.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
			    Intent intent = new Intent(context, ListFileActivity.class);
                            startActivity(intent);  
			}
		});
		
		Thread t = new Thread()
		{
			@Override
			public void run()
			{
        
		        try {
		            URL url = new URL(download_file_path);
		            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		 
		            urlConnection.setRequestMethod("GET");
		            urlConnection.setDoOutput(true);
		 
		            //connect
		            urlConnection.connect();
		 
		            //set the path where we want to save the file           
		            File SDCardRoot = new File("/storage/emulated/0/Download/");//Environment.getExternalStorageDirectory(); 
		            
		            //create a new file, to save the downloaded file 
		            File file = new File(SDCardRoot,"downloaded_file.jpg");
		  
		            FileOutputStream fileOutput = new FileOutputStream(file);
		 
		            //Stream used for reading the data from the internet
		            InputStream inputStream = urlConnection.getInputStream();
		 
		            //this is the total size of the file which we are downloading
		            totalSize = urlConnection.getContentLength();
		 
		            runOnUiThread(new Runnable() {
		                public void run() {
		         //           pb.setMax(totalSize);
		                }               
		            });
		             
		            //create a buffer...
		            byte[] buffer = new byte[1024];
		            int bufferLength = 0;
		 
		            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
		            	Log.i("aaa","down");
		                fileOutput.write(buffer, 0, bufferLength);
		                downloadedSize += bufferLength;
		                // update the progressbar //
		                runOnUiThread(new Runnable() {
		                    public void run() {
		                       // pb.setProgress(downloadedSize);
		                        float per = ((float)downloadedSize/totalSize) * 100;
		                //        cur_val.setText("Downloaded " + downloadedSize + "KB / " + totalSize + "KB (" + (int)per + "%)" );
		                        Log.i("aaa","down");
		                    }
		                });
		            }
		            //close the output stream when complete //
		            fileOutput.close();
		            runOnUiThread(new Runnable() {
		                public void run() {
		                    // pb.dismiss(); // if you want close it..
		                }
		            });         
		         
		        } catch (final MalformedURLException e) {
		            //showError("Error : MalformedURLException " + e);        
		            e.printStackTrace();
		        } catch (final IOException e) {
		            //showError("Error : IOException " + e);          
		            e.printStackTrace();
		        }
		        catch (final Exception e) {
		            //showError("Error : Please check your internet connection " + e);
		        }     
					}
				};
				
				t.start();
				try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
}