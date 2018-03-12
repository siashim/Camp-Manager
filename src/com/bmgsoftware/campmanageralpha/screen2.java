package com.bmgsoftware.campmanageralpha;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import recycle.ConnectionDetector;
import com.bmgsoftware.campmanageralpha.R;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.content.Context;

/**
 * This class creates the login screen where the user inputs the user name and
 * password.
 */
public class screen2 extends ActionBarActivity {
	EditText usernamefield;
	EditText passwordfield;
	TextView response1;

	Button button;
	Button cantlogin;
	Button createanewaccount;
	GlobalObject gobj = (GlobalObject) this.getApplication();
	User u = null;
	public static Context ctx;

	/**
	 * Sets up the the layout of the screen. Two text fields for username and
	 * password. There is also a log-in button.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.screen2);
		ctx = this;
		response1 = (TextView) findViewById(R.id.textView1);
		response1.setText("Welcome");
		addListenerOnButton();
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
	 * Determines what happens when any of the text fields or buttons are pressed
	 * by the user.
	 */
	public void addListenerOnButton() {
		final Context context = this;
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ConnectionDetector cd = new ConnectionDetector(
						getApplicationContext());
				Boolean isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {
					usernamefield = (EditText) findViewById(R.id.editText2);
					passwordfield = (EditText) findViewById(R.id.passtxt);
					/*
					 * The username and password are checked to see if they match any
					 * of the ones in the server. If not the user is notified
					 */
					Thread t = new Thread() {
						@Override
						public void run() {
							String path =
							 "http://www.bmgsoftware.com/pagetoreceive.php";
							HttpClient client = new DefaultHttpClient();
							HttpConnectionParams.setConnectionTimeout(
									client.getParams(), 10000);
							HttpResponse response;
							JSONObject json = new JSONObject();
							try {
								HttpPost post = new HttpPost(path);
								json.put("username", usernamefield
								 .getText().toString());
								json.put("password", passwordfield
								 .getText().toString());
								post.setHeader("json", json.toString());
								StringEntity se = new StringEntity(json.toString());
								se.setContentEncoding(new BasicHeader(
								 HTTP.CONTENT_TYPE, "application/json"));
								post.setEntity(se);
								response = client.execute(post);
								if (response != null) {
									InputStream in = response.getEntity().getContent();
									String a = convertStreamToString(in);
									if (a.charAt(0) == '1') {
										u = new User(a);
										Context ctxx = screen2.getLastSetContext();
										GlobalObject global = (GlobalObject) ctxx
										 .getApplicationContext();
										global.setUser(u);

										getFragmentManager()
										 .beginTransaction()
										 .add(android.R.id.content,
										new TestIntentFragment()).commit();
										Intent intent = new Intent(context,
										 screen3_1.class);
										startActivity(intent);
									} else if (a.charAt(0) == '2') {
										u = new User(a);
										Context ctxx = screen2.getLastSetContext();
										GlobalObject global = (GlobalObject) ctxx
										 .getApplicationContext();
										global.setUser(u);

										getFragmentManager()
										 .beginTransaction()
										 .add(android.R.id.content,
										new TestIntentFragment()).commit();
										Intent intent = new Intent(context,
										 screen3_2.class);
										startActivity(intent);
									} else if (a.charAt(0) == '3') {
										u = new User(a);
										Context ctxx = screen2.getLastSetContext();
										GlobalObject global = (GlobalObject) ctxx
										 .getApplicationContext();
										global.setUser(u);

										getFragmentManager()
										 .beginTransaction()
										 .add(android.R.id.content,
										new TestIntentFragment()).commit();
										Intent intent = new Intent(context,
										 screen3_3.class);
										startActivity(intent);
									} else {
										response1.setText("username or password" +
										 " does not match our records.");
									}
								}
							} catch (Exception e) {
								response1
										.setText("Username or password" +
										 "does not match our records.");
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
				} else {
					response1
							.setText("Internet is not connected." +
							 " Check your internet connection.");
				}
			}
		});

		cantlogin = (Button) findViewById(R.id.button2);
		cantlogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, screen2_2.class);
				startActivity(intent);
			}
		});

		createanewaccount = (Button) findViewById(R.id.button3);
		createanewaccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, screen2_1.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static Context getLastSetContext() {
		return ctx;
	}
}
