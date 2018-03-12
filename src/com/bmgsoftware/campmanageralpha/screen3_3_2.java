package com.bmgsoftware.campmanageralpha;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Select group menu. Students can select their group number.
 * 
 */
public class screen3_3_2 extends Activity {

	Button button2;
	int count = 0;
	Spinner groupList;
	String groupnumber;
	List<String> group = new ArrayList<String>();
	String username;
	final Context context = this;

	/**
	 * On create grab layout screen3_3_2 and Select group screen.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen3_3_2);
		group.add("Select");

		try {
			addItemsOnNameList();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, group);

		dataAdapter
		 .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		groupList.setAdapter(dataAdapter);

		addListenerOnButton();

	}

	/**
	 * Add listener on button.
	 */
	public void addItemsOnNameList() throws InterruptedException {

		final String username = "admin";
		groupList = (Spinner) findViewById(R.id.spinner1);

		// Start a thread and connect to server.
		Thread t1 = new Thread() {
			@Override
			public void run() {
				String path = "http://www.bmgsoftware.com/groupnumberreceive.php";
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams
						.setConnectionTimeout(client.getParams(), 10000);
				HttpResponse response;
				JSONObject json = new JSONObject();

				try {

					HttpPost post = new HttpPost(path);
					json.put("username", username);
					post.setHeader("json", json.toString());
					StringEntity se = new StringEntity(json.toString());
					se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					 "application/json"));
					post.setEntity(se);
					response = client.execute(post);
					InputStream in = response.getEntity().getContent();
					groupnumber = convertStreamToString(in);
					for (int i = 1; i <= Integer
							.parseInt("" + groupnumber.charAt(0)); i++)
					{
						group.add("" + i);
					}

				}

				catch (Exception e) {
					e.printStackTrace();
				}

			}

		};
		t1.start();
		try {
			t1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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

	public void addListenerOnButton() {

		button2 = (Button) findViewById(R.id.buttonlog);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Scanner sc = new Scanner(groupList.getSelectedItem().toString());
				groupnumber = sc.next();
				if (groupnumber.contains("Select")) {
					// don't do anything
				} else {
					Thread t = new Thread() {
						@Override
						public void run() {
							GlobalObject gobj = (GlobalObject) context
									.getApplicationContext();
							String path = 
							 "http://www.bmgsoftware.com/studentselectsgroup.php";
							HttpClient client = new DefaultHttpClient();
							HttpConnectionParams.setConnectionTimeout(
							 client.getParams(), 10000);
							HttpResponse response;
							JSONObject json = new JSONObject();

							try {

								HttpPost post = new HttpPost(path);
								username = gobj.getUser().getmUserName();
								json.put("username", username);
								json.put("groupnumber", groupnumber);
								gobj.getUser().setmGroupNumber(groupnumber);
								post.setHeader("json", json.toString());
								StringEntity se = new StringEntity(json.toString());
								se.setContentEncoding(new BasicHeader(
							  	HTTP.CONTENT_TYPE, "application/json"));
								post.setEntity(se);
								response = client.execute(post);

							}

							catch (Exception e) {
								e.printStackTrace();
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

					Intent intent = new Intent(context, screen3_3_2_1.class);
					startActivity(intent);
					finish();
				}
			}

		});

	}

}