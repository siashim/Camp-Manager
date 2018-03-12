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
import com.bmgsoftware.campmanageralpha.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * This class creates the screen where the admin assigns the groups.
 */
public class screen3_1_2 extends Activity {
	Button button2;
	int count = 0;
	EditText numberofgroups;
	Spinner nl[] = new Spinner[10];
	EditText etl[] = new EditText[10];
	Button submit;
	List<String> user = new ArrayList<String>();
	Editable numofgrp;
	final Context context = this;

	/**
	 * Sets up the the layout of the screen. In this case a drop-down menu that
	 * will holds the name of students is created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen3_1_2);
		user.add("Select a Chaperon");

		addItemsOnNameList();

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
		 android.R.layout.simple_spinner_item, user);
		dataAdapter
		 .setDropDownViewResource
		 (android.R.layout.simple_spinner_dropdown_item);

		for (int i = 0; i < 10; i++) {
			nl[i].setAdapter(dataAdapter);
		}

		numberofgroups = (EditText) findViewById(R.id.numberofgroups);
		numberofgroups.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
			 int count) {
				numofgrp = numberofgroups.getText();
				for (int i = 0; i < 10; i++) {
					nl[i].setVisibility(View.INVISIBLE);
				}

				int iii = Integer.parseInt("0" + numofgrp);
				for (int i = 0; i < iii; i++) {
					nl[i].setVisibility(View.VISIBLE);
				}

				/*
				 * Gets the name of students from the server in order to put in the
				 * drow-down menu
				 */
				new Thread() {
					@Override
					public void run() {
						try {
							sleep(500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						String path = 
						 "http://www.bmgsoftware.com/changenumberofgroup.php";
						HttpClient client = new DefaultHttpClient();
						HttpConnectionParams.setConnectionTimeout(client.getParams(),
						 10000);
						HttpResponse response;
						JSONObject json = new JSONObject();

						try {
							HttpPost post = new HttpPost(path);
							json.put("numberofgroup", numberofgroups.getText());
							post.setHeader("json", json.toString());
							StringEntity se = new StringEntity(json.toString());
							se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
							 "application/json"));
							post.setEntity(se);
							response = client.execute(post);
							InputStream in = response.getEntity().getContent();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();

			}
		});

		addListenerOnButton();
	}

	/**
	 * Adds the name of students to the drop-down menu
	 */
	public void addItemsOnNameList() {
		for (int i = 0; i < 10; i++) {
			int ii = i + 1;
			String buttonID = "nameSpinner" + ii;
			int resID = getResources().getIdentifier(buttonID, "id",
			 "com.bmgsoftware.campmanageralpha");
			nl[i] = (Spinner) findViewById(resID);
		}

		for (int i = 0; i < 10; i++) {
			nl[i].setVisibility(View.INVISIBLE);
		}

		final String id = "3";

		/*
		 * Takes the name of students from the server and separates them based on
		 * defined parameters to be put on the drop-down menu
		 */
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

					try {
						sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					post.setHeader("json", json.toString());
					StringEntity se = new StringEntity(json.toString());
					se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					 "application/json"));
					post.setEntity(se);
					response = client.execute(post);
					InputStream in = response.getEntity().getContent();
					String names = convertStreamToString(in);
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
		button2 = (Button) findViewById(R.id.buttonlog);
		button2.setOnClickListener(new OnClickListener() {

			/*
			 * Gets the names assigned by the admin, and changes their group number
			 * to the appropriate number.
			 */
			@Override
			public void onClick(View arg0) {
				Thread t = new Thread() {
					@Override
					public void run() {
						String path =
						 "http://www.bmgsoftware.com/adminpromotetochap.php";
						HttpClient client = new DefaultHttpClient();
						HttpConnectionParams.setConnectionTimeout(client.getParams(),
						 10000);
						HttpResponse response;
						JSONObject json = new JSONObject();

						try {
							HttpPost post = new HttpPost(path);
							for (int i = 0; i < 10; i++) {
								Scanner sc = new Scanner(nl[i].getSelectedItem()
								 .toString());
								String unput = sc.next();
								json.put("username" + i, unput);
								json.put("groupnumber" + i, i);
								sc.close();
							}

							post.setHeader("json", json.toString());
							StringEntity se = new StringEntity(json.toString());
							se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
							 "application/json"));
							post.setEntity(se);
							response = client.execute(post);
							InputStream in = response.getEntity().getContent();
							String names = convertStreamToString(in);
							String nameCount = names;
							count = nameCount.length()
							 - nameCount.replace("#", "").length();
							for (int i = 0; i < count; i++) {
								String[] hash = names.split("#");
								user.add(hash[i]);
							}
							for (String a : user) {
							}
						} catch (Exception e) {
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

				Intent intent = new Intent(context, screen3_1_2_1.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
