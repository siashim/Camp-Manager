package com.bmgsoftware.campmanageralpha;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.bmgsoftware.campmanageralpha.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Chaperone schedule view. View trip schedule.
 * 
 */
public class screen3_3_1 extends Activity {

	int downloadedSize = 0;
	int totalSize = 0;
	String download_file_path = "http://bmgsoftware.com/uploads/distribute.jpg";

	/**
	 * On create grab layout screen3_3_1 and prepare schedule screen.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen3_3_1);

		/*
		 * Start thread and connects to server to retrieves image file for
		 * schedule.
		 */
		Thread t = new Thread() {
			@Override
			public void run() {

				try {
					URL url = new URL(download_file_path);
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();

					urlConnection.setRequestMethod("GET");
					urlConnection.setDoOutput(true);

					// connect
					urlConnection.connect();

					// set the path where we want to save the file
					// Environment.getExternalStorageDirectory();
					File SDCardRoot = new File("/storage/emulated/0/Download/");

					// create a new file, to save the downloaded file
					File file = new File(SDCardRoot, "downloaded_file.jpg");

					FileOutputStream fileOutput = new FileOutputStream(file);

					// Stream used for reading the data from the internet
					InputStream inputStream = urlConnection.getInputStream();

					// this is the total size of the file which we are downloading
					totalSize = urlConnection.getContentLength();

					runOnUiThread(new Runnable() {
						public void run() {
						}
					});

					byte[] buffer = new byte[1024];
					int bufferLength = 0;

					while ((bufferLength = inputStream.read(buffer)) > 0) {
						Log.i("aaa", "down");
						fileOutput.write(buffer, 0, bufferLength);
						downloadedSize += bufferLength;
						runOnUiThread(new Runnable() {
							public void run() {
								float per = ((float) downloadedSize / totalSize) * 100;
								Log.i("aaa", "down");
							}
						});
					}
					// close the output stream when complete //
					fileOutput.close();
					runOnUiThread(new Runnable() {
						public void run() {
							// pb.dismiss(); // if you want close it..
						}
					});

				} catch (final MalformedURLException e) {
					// showError("Error : MalformedURLException " + e);
					e.printStackTrace();
				} catch (final IOException e) {
					// showError("Error : IOException " + e);
					e.printStackTrace();
				} catch (final Exception e) {
					// showError("Error : Please check your internet connection " +
					// e);
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
		ImageView img = (ImageView) findViewById(R.id.imageView1);
		Bitmap bMap = BitmapFactory
				.decodeFile("/storage/emulated/0/Download/downloaded_file.jpg");
		img.setImageBitmap(bMap);
	}

}