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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * This class creates the conversation menu where the user is 
 * able to choose one of three conversations
 */
public class conversation_menu extends Activity {

	Button con1, con2, con3;
	String[] receiver;
	String[] conversation;
	String con;

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
	 * Sets up the the layout of the screen. In this case three buttons
	 * with the names of the people who are having conversation with 
	 * the user is shown. If one of the conversations are empty,
	 * then "Empty Conversation" is shown.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		GlobalObject gobj = (GlobalObject) context.getApplicationContext();
		gobj.setalarmsound(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation_menu);
		addListenerOnButton();
		receiver = new String[3];
		conversation = new String[3];
		con1 = (Button) findViewById(R.id.con1_button);
		con2 = (Button) findViewById(R.id.con2_button);
		con3 = (Button) findViewById(R.id.con3_button);
		for (int i = 0; i < 3; i++) {
			receiver[i] = gobj.getUser().getmConversationUser(i);
		}
		con1.setText(receiver[0]);
		con2.setText(receiver[1]);
		con3.setText(receiver[2]);
		if (receiver[0].equals("na")) {
			con1.setText("Empty Conversation");
		}
		if (receiver[1].equals("na")) {
			con2.setText("Empty Conversation");
		}
		if (receiver[2].equals("na")) {
			con3.setText("Empty Conversation");
		}

	}

	final Context context = this;

	/**
	 * Determines what happens when any of the text fields or the button is
	 * pressed by the user.
	 */
	public void addListenerOnButton() {
		final Context context = this;
		con1 = (Button) findViewById(R.id.con1_button);
		con1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				GlobalObject gobj = (GlobalObject)
				 context.getApplicationContext();
				gobj.getUser().setmActiveConversation(0);
				if (gobj.getUser().getmConversationUser(0).equals("na")) {
					Intent intent = new Intent(context, messaging_contacts.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(context, conversation.class);
					startActivity(intent);

				}

			}

		});

		/*
		 * Defines what happens when conversation button 1 is held. In this case
		 * it deletes the conversation
		 */
		con1.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				GlobalObject gobj = (GlobalObject)
				 context.getApplicationContext();
				gobj.getUser().setmConversation(0, "");
				gobj.getUser().setmConversationUser(0, "na");
				con1.setText("Empty Conversation");
				gobj.getUser().setmConversation(0, "na");
				gobj.getUser().setmConversationUser(0, "na");

				// Deletes the conversation from the server
				Thread t = new Thread() {
					@Override
					public void run() {
						String path =
						 "http://www.bmgsoftware.com/conversation_delete.php";
						HttpClient client = new DefaultHttpClient();
						HttpConnectionParams.setConnectionTimeout(client.getParams(),
						 10000);
						HttpResponse response;
						JSONObject json = new JSONObject();

						try {
							GlobalObject gobj = (GlobalObject) context
							 .getApplicationContext();
							HttpPost post = new HttpPost(path);
							json.put("username", gobj.getUser().getmUserName());
							json.put("conID", "0");
							post.setHeader("json", json.toString());
							StringEntity se = new StringEntity(json.toString());
							se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
							 "application/json"));
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

				return true;
			}
		});

		con2 = (Button) findViewById(R.id.con2_button);
		con2.setOnClickListener(new OnClickListener() {

			// Determines what happens when the conversation button is pressed
			@Override
			public void onClick(View arg0) {
				GlobalObject gobj = (GlobalObject) context.getApplicationContext();
				gobj.getUser().setmActiveConversation(1);
				if (gobj.getUser().getmConversationUser(1).equals("na")) {
					Intent intent = new Intent(context, messaging_contacts.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(context, conversation.class);
					startActivity(intent);

				}

			}

		});

		/*
		 * Defines what happens when conversation button 2 is held. In this case
		 * it deletes the conversation
		 */
		con2.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				GlobalObject gobj = (GlobalObject) context.getApplicationContext();
				gobj.getUser().setmConversation(1, "");
				gobj.getUser().setmConversationUser(1, "na");
				con2.setText("Empty Conversation");
				gobj.getUser().setmConversation(1, "na");
				gobj.getUser().setmConversationUser(1, "na");

				// Deletes the conversation from the server
				Thread t = new Thread() {
					@Override
					public void run() {
						String path =
						 "http://www.bmgsoftware.com/conversation_delete.php";
						HttpClient client = new DefaultHttpClient();
						HttpConnectionParams.setConnectionTimeout(client.getParams(),
						 10000);
						HttpResponse response;
						JSONObject json = new JSONObject();

						try {
							GlobalObject gobj = (GlobalObject) context
							 .getApplicationContext();
							HttpPost post = new HttpPost(path);
							json.put("username", gobj.getUser().getmUserName());
							json.put("conID", "1");
							post.setHeader("json", json.toString());
							StringEntity se = new StringEntity(json.toString());
							se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
							 "application/json"));
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

				return true;
			}
		});

		con3 = (Button) findViewById(R.id.con3_button);
		con3.setOnClickListener(new OnClickListener() {

			// Determines what happens when the conversation button is pressed
			@Override
			public void onClick(View arg0) {
				GlobalObject gobj = (GlobalObject) context.getApplicationContext();
				gobj.getUser().setmActiveConversation(2);
				// Put this for every click in message menu
				if (gobj.getUser().getmConversationUser(2).equals("na")) {
					Intent intent = new Intent(context, messaging_contacts.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(context, conversation.class);
					startActivity(intent);

				}

			}

		});

		/*
		 * Defines what happens when conversation button 3 is held. In this case
		 * it deletes the conversation
		 */
		con3.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {

				GlobalObject gobj = (GlobalObject) context.getApplicationContext();
				gobj.getUser().setmConversation(2, "");
				gobj.getUser().setmConversationUser(2, "na");
				con3.setText("Empty Conversation");
				gobj.getUser().setmConversation(2, "na");
				gobj.getUser().setmConversationUser(2, "na");

				// Deletes the conversation from the server
				Thread t = new Thread() {
					@Override
					public void run() {
						String path = 
						 "http://www.bmgsoftware.com/conversation_delete.php";
						HttpClient client = new DefaultHttpClient();
						HttpConnectionParams.setConnectionTimeout(client.getParams(),
						 10000);
						HttpResponse response;
						JSONObject json = new JSONObject();

						try {
							GlobalObject gobj = (GlobalObject) context
							 .getApplicationContext();
							HttpPost post = new HttpPost(path);
							json.put("username", gobj.getUser().getmUserName());
							json.put("conID", "2");
							post.setHeader("json", json.toString());
							StringEntity se = new StringEntity(json.toString());
							se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
							 "application/json"));
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

				return true;
			}
		});

	}

}