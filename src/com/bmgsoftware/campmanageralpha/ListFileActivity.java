/**
 * ScheduleSelection.java
 */
package com.bmgsoftware.campmanageralpha;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.ListActivity;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 
 */
public class ListFileActivity extends ListActivity {
	int serverResponseCode = 0;
	ProgressDialog dialog = null;
	String upLoadServerUri = null;
	// File path
	String uploadFileNameTest = "";
	private String path;
	String ownName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_files);

		// Use the current directory as title
		path = "/storage/emulated/0/DCIM/Camera";// Default directory
		if (getIntent().hasExtra("path")) {
			path = getIntent().getStringExtra("path");
		}

		setTitle(path);

		// Read all files sorted into the values-array
		List<String> values = new ArrayList<String>();
		File dir = new File(path);
		ownName = dir.getName();
		if (!dir.canRead()) {
			setTitle(getTitle() + " (inaccessible)");
			values.add("..");
		}

		// Put string array to arraylist
		String[] list = dir.list();
		if (list != null) {
			if (!dir.getName().equals("storage")) {
				values.add("..");// + dir.getParentFile().getPath());
			}
			for (String file : list) {
				values.add(file);
			}
		}
		Collections.sort(values);

		// Put the data into the list
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		 android.R.layout.simple_list_item_2, android.R.id.text1, values);
		setListAdapter(adapter);
	}

	// Execute when list item selected
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String filename = (String) getListAdapter().getItem(position);
		if (path.endsWith(File.separator)) {
			filename = path + filename;
		} else if (filename.contains("..")) {
			filename = path.replace("/" + ownName, "");
		} else {
			filename = path + File.separator + filename;
		}

		Log.i("newpath", filename);

		uploadFileNameTest = filename;

		if (new File(filename).isDirectory()) {// Change to new directory
			Intent intent = new Intent(this, ListFileActivity.class);
			intent.putExtra("path", filename);
			startActivity(intent);
		} else {// Start uploading schedule
			setTitle("Uploading File...");
			upLoadServerUri = "http://bmgsoftware.com/uploadfile.php";
			Thread t = new Thread(new Runnable() {
				public void run() {
					uploadFile(uploadFileNameTest);
				}
			});
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Go back to schedule viewing screen
			Intent intent = new Intent(this, screen3_1_1.class);
			startActivity(intent);
		}

		// finish schedule selection page
		finish();
	}

	public int uploadFile(String sourceFileUri) {
		String fileName = sourceFileUri;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);
		if (!sourceFile.isFile()) {
			dialog.dismiss();
			// Log.e("uploadFile", "Source File not exist :" + uploadFilePath + ""
			// + uploadFileName);

			runOnUiThread(new Runnable() {
				public void run() {
					// messageText.setText("Source File not exist :"
					// +uploadFilePath + "" + uploadFileName);
				}
			});
			return 0;
		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(sourceFile);
				URL url = new URL(upLoadServerUri);
				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
				 "multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);
				dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name='uploaded_file';filename='"
				 + fileName + "'" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage
						+ ": " + serverResponseCode);
				if (serverResponseCode == 200) {
					runOnUiThread(new Runnable() {
						public void run() {
						}
					});
				}
				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();
			} catch (MalformedURLException ex) {
				// dialog.dismiss();
				ex.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
					}
				});
				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {
				// dialog.dismiss();
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
					}
				});
				Log.e("Upload file to server Exception",
				 "Exception : " + e.getMessage(), e);
			}
			return serverResponseCode;
		} // End else block
	}
}
