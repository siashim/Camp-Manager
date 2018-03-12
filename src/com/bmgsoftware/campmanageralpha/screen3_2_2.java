package com.bmgsoftware.campmanageralpha;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Chaperone locate students menu.
 * 
 */
public class screen3_2_2 extends Activity {
	Button button1;
	Button button2;
	EditText notification;
	int count = 0;
	Spinner nameList;
	List<String> user = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen3_2_2);
		user.add("All");
		addItemsOnNameList();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
		 android.R.layout.simple_spinner_item, user);
		dataAdapter
		 .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		nameList.setAdapter(dataAdapter);

		addListenerOnButton();

	}

	final Context context = this;

	public void addItemsOnNameList() {
		nameList = (Spinner) findViewById(R.id.nameSpinner1);
		final String id = "3";

		// Gets the location data from the server
		new Thread() {
			@Override
			public void run() {
				String path = "http://www.bmgsoftware.com/adminreceive.php";
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams
				 .setConnectionTimeout(client.getParams(), 10000);
				HttpResponse response;
				JSONObject json = new JSONObject();

				try {
					HttpPost post = new HttpPost(path);
					json.put("userid", id);

					post.setHeader("json", json.toString());
					StringEntity se = new StringEntity(json.toString());
					se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);
					response = client.execute(post);
					InputStream in = response.getEntity().getContent();
					String names = convertStreamToString(in);
					// names.replaceFirst(" ", "");
					String nameCount = names;
					count = nameCount.length() - nameCount.replace("#", "")
					 .length();

					for (int i = 0; i < count; i++) {
						String[] hash = names.split("#");
						user.add(hash[i]);
					}

				}

				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * Takes the info that is given as stream and converts them to String.
	 * @param is the stream required to be converted to string
	 * @return a string version of the stream given to the method.
	 */
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * Determines what happens when any of the text fields or the button is
	 * pressed by the user.
	 */
	public void addListenerOnButton() {

		final Context context = this;
		button2 = (Button) findViewById(R.id.locate_button);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// nameList.getSelectedItem().toString();

				GlobalObject gobj = (GlobalObject) context.getApplicationContext();
				if (!(nameList.getSelectedItem() == null)) {
					String names = nameList.getSelectedItem().toString();
					String[] hash = names.split(" ");
					gobj.setlocateName(hash[0]);
				} else {
					gobj.setlocateName("all");
				}
				Intent intent = new Intent(context, screen3_2_2_1.class);
				startActivity(intent);

			}

		});

	}

}