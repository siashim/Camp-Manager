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
import com.bmgsoftware.campmanageralpha.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Sets up the the layout of the screen.
 */
public class screen3_1 extends Activity {
	Button schedule;
	Button assigngroup;
	Button sendmessage;
	Button selectgroup;
	Button messaging;
	String username;
	String message;
	String[] conversation;
	String[][] con;
	int i;
	String contemp;

	/**
	 * Determines what happens when the back button is pressed. In this case the
	 * user is asked if he or she wants to log out.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen3_1);
		GlobalObject gobj = (GlobalObject) context.getApplicationContext();
		getActionBar().setTitle("Welcome " + gobj.getUser().getmName());
		conversation = new String[3];
		con = new String[3][2];
		username = gobj.getUser().getmUserName();
		addListenerOnButton();

	}

	final Context context = this;

	/**
	 * Determines what happens when pressing back button. In this case it asks
	 * the user whether or not to log out.
	 */
	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialogBuilder = 
		 new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("Log out?");
		alertDialogBuilder.setMessage("Click yes to logout").setCancelable(false)
		 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						screen3_1.this.finish();
						Intent intent = new Intent(context, screen2.class);
						startActivity(intent);
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	/**
	 * Takes the info that is given as stream and converts them to String.
	 * 
	 * @param is
	 *           the stream required to be converted to string.
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

		schedule = (Button) findViewById(R.id.schedule_button);
		schedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, screen3_1_1.class);
				startActivity(intent);
			}
		});

		assigngroup = (Button) findViewById(R.id.assign_groups_button);
		assigngroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, screen3_1_2.class);
				startActivity(intent);
			}

		});

		sendmessage = (Button) findViewById(R.id.send_message_button);
		sendmessage.setOnClickListener(new OnClickListener() {

			/*
			 * Gets the conversations from the server, and separates both the
			 * conversations and the users related to those conversations. Then it
			 * assigns those conversations to the related users
			 */
			@Override
			public void onClick(View arg0) {

				Thread t = new Thread() {
					@Override
					public void run() {
						String path = 
						 "http://www.bmgsoftware.com/conversation_receive_all.php";
						HttpClient client = new DefaultHttpClient();
						HttpConnectionParams.setConnectionTimeout(client.getParams(),
						 10000);
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
							message = convertStreamToString(in);
							GlobalObject gobj = (GlobalObject) context
							 .getApplicationContext();

							for (int i = 0; i < 3; i++) {
								conversation = message.split("%%");
							}
							for (int l = 0; l < 3; l++) {
								for (int i = 0; i < 2; i++) {
									con[l] = conversation[l].split("&&");
								}
							}
							for (int l = 0; l < 3; l++) {
								gobj.getUser().setmConversationUser(l, con[l][0]);
								gobj.getUser().setmConversation(l, con[l][1]);
							}

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
					e.printStackTrace();
				}

				Intent intent = new Intent(context, conversation_menu.class);
				startActivity(intent);
			}

		});

	}

}