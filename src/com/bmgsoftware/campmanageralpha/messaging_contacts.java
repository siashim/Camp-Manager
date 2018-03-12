package com.bmgsoftware.campmanageralpha;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
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
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * messaging contacts. View the users.
 * 
 */
public class messaging_contacts extends ListActivity {

	int count = 0;
	List<String> user = new ArrayList<String>();
	private String path;
	List<String> list;

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
	 * On create grab layout messaging_contacts and prepare the list.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messaging_contacts);
		user.clear();

		List values = new ArrayList();

		// Start a thread and connect to server.
		Thread t = new Thread() {
			@Override
			public void run() {
				String path = "http://www.bmgsoftware.com/contacts_all.php";
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams
				 .setConnectionTimeout(client.getParams(), 10000);
				HttpResponse response;
				JSONObject json = new JSONObject();

				try {
					GlobalObject gobj = (GlobalObject) context
				    .getApplicationContext();
					HttpPost post = new HttpPost(path);
					json.put("userid", gobj.getUser().getmUserid());
					json.put("groupnumber", gobj.getUser().getmGroupNumber());
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
					String un = gobj.getUser().getmUserName() + " - "
					 + gobj.getUser().getmName() + " "
					 + gobj.getUser().getmLastName();
					user.remove(un);
					list = new ArrayList<String>();
					for (String a : user) {
						list.add(a);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Use the current directory as title
		path = "Contacts";
		if (getIntent().hasExtra("path")) {
			path = getIntent().getStringExtra("path");
		}
		setTitle(path);
		for (String a : user) {
		}
		if (list != null) {
			for (String file : list) {
				values.add(file);
			}
		}
		Collections.sort(values);
		ArrayAdapter adapter = new ArrayAdapter(this,
		 android.R.layout.simple_list_item_2, android.R.id.text1, values);
		setListAdapter(adapter);
	}

	final Context context = this;

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String user = (String) getListAdapter().getItem(position);
		String[] conUser;
		conUser = user.split(" ");
		GlobalObject gobj = (GlobalObject) context.getApplicationContext();
		gobj.getUser().setmConversationUser(
		 gobj.getUser().getmActiveConversation(), conUser[0]);
		Intent intent = new Intent(this, conversation.class);
		startActivity(intent);
		this.finish();
	}
}