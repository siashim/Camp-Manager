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
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * This class creates the page where the actual conversation takes place.
 */
public class conversation extends Activity {

	EditText messageHistoryText;
	Button sendMessageButton;
	Button refresh_button;
	String conversation;
	int count = 0;
	int i;
	String[] hash;
	String sender;
	String receiver;
	Editable freshText;

	EditText messageType;

	/**
	 * Sets up the the layout of the screen. In this case a text field for typing
	 * the message that is to be sent, and another text field for showing the
	 * conversation will be created. send and refresh button are also shown on
	 * the page.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);
		GlobalObject gobj = (GlobalObject) context.getApplicationContext();
		i = gobj.getUser().getmActiveConversation();
		sender = gobj.getUser().getmUserName();
		receiver = gobj.getUser().getmConversationUser(i);
		conversation = gobj.getUser().getmConversation(i);
		conversation.replace("\n", "");
		if (conversation.equals("na")) {
			conversation = "";
		}

		addListenerOnButton();
		messageHistoryText = (EditText) findViewById(R.id.messageHistory);
		messageType = (EditText) findViewById(R.id.message);

		String conCount = conversation;

		count = conCount.length() - conCount.replace("##", "").length();

		for (int i = 1; i < count / 2 + 1; i++) {
			hash = conversation.split("##");
			if (i == 1) {
				messageHistoryText.append(hash[i]);
			} else {
				messageHistoryText.append("\n" + hash[i]);
			}
		}
		messageHistoryText.append("\n");
		gobj.getUser().setisLogged(true);

	}

	final Context context = this;

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

	@Override
	public void onBackPressed() {
		this.finish();
	}

	/**
	 * Determines what happens when any of the button is pressed by the user.
	 */
	public void addListenerOnButton() {

		final Context context = this;
		sendMessageButton = (Button) findViewById(R.id.send_button);

		sendMessageButton.setOnClickListener(new OnClickListener() {

			// Determines what happens when the send button is pressed
			@Override
			public void onClick(View arg0) {
				GlobalObject gobj = (GlobalObject) context.getApplicationContext();
				freshText = messageType.getText();
				String Text = sender + ": " + freshText;
				Text.replace("'", "´");
				conversation = conversation + "##" + Text;
				gobj.getUser().setmConversation(i, conversation);
				messageHistoryText.append(Text + "\n");
				messageType.setText("");
				
				/*
				 * The message typed by the user gets added to the end of message
				 * available in the server. Then the message culomn for both users
				 * in updated in the server.
				 */
				Thread t = new Thread() {
					@Override
					public void run() {
						String path = 
						 "http://www.bmgsoftware.com/conversation_update.php";
						HttpClient client = new DefaultHttpClient();
						HttpConnectionParams.setConnectionTimeout(client.getParams(),
						 10000); // Limit
						HttpResponse response;
						JSONObject json = new JSONObject();
						try {
							GlobalObject gobj = (GlobalObject) context
							 .getApplicationContext();
							gobj.getUser().setmConversation(
							 gobj.getUser().getmActiveConversation(),
							 gobj.getUser().getmConversation
							 (gobj.getUser().getmActiveConversation())
							 .replace("'", "`"));
							HttpPost post = new HttpPost(path);
							json.put("username", sender);
							json.put("receiver", receiver);
							json.put("conID", i);
							json.put("con1temp", freshText);
							json.put("convfinal", gobj.getUser()
							 .getmConversation(
							 gobj.getUser().getmActiveConversation())
							 .replace("\n", ""));
							post.setHeader("json", json.toString());
							StringEntity se = new StringEntity(json.toString());
							se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
							 "application/json"));
							post.setEntity(se);
							response = client.execute(post);
							if (response != null) {
								InputStream in = response.getEntity().getContent();
								String a = convertStreamToString(in);
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
			}

		});

		refresh_button = (Button) findViewById(R.id.refresh_button);
		refresh_button.setOnClickListener(new OnClickListener() {

			// Determines what happens when the refresh button is pressed
			@Override
			public void onClick(View arg0) {
				GlobalObject gobj = (GlobalObject) context.getApplicationContext();
				gobj.setalarmsound(true);

				messageHistoryText.setText("");

				/*
				 * Gets whatever is available in the conversation column in the
				 * server and puts in the text field showing the conversation.
				 */
				Thread t = new Thread() {
					@Override
					public void run() {
						String path = "http://www.bmgsoftware.com/conversation_receive.php";
						HttpClient client = new DefaultHttpClient();
						HttpConnectionParams.setConnectionTimeout(client.getParams(),
						 10000);
						HttpResponse response;
						JSONObject json = new JSONObject();

						try {

							HttpPost post = new HttpPost(path);
							json.put("username", sender);
							json.put("conID", i);
							post.setHeader("json", json.toString());
							StringEntity se = new StringEntity(json.toString());
							se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
							 "application/json"));
							post.setEntity(se);
							response = client.execute(post);
							InputStream in = response.getEntity().getContent();
							conversation = convertStreamToString(in);
							conversation.replace("\n", "");
							GlobalObject gobj = (GlobalObject) context
							 .getApplicationContext();
							if (conversation.contains("na")) {
								conversation = "";
							} else {
								gobj.getUser().setmConversation(i, conversation);
								sleep(500);
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

				i = gobj.getUser().getmActiveConversation();
				sender = gobj.getUser().getmUserName();
				receiver = gobj.getUser().getmConversationUser(i);
				conversation = gobj.getUser().getmConversation(i);
				String conCount = conversation;
				conversation.replace("\n", "");
				count = conCount.length() - conCount.replace("##", "").length();
				for (int i = 0; i < count / 2 + 1; i++) {
					hash = conversation.split("##");
					if (i == count / 2) {
						messageHistoryText.append(hash[i]);
					} else {
						messageHistoryText.append(hash[i] + "\n");
					}
				}

			}

		});

	}

}