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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Chaperone locate students maps view.
 * 
 */
public class screen3_2_2_1 extends Activity {

	Button button2;
	int count = 0;
	List<String> user = new ArrayList<String>();
	List<String> onmap = new ArrayList<String>();

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Full screen page.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.screen3_2_2_1);
		// Start Google maps module service.
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(
		 R.id.map)).getMap();
		// Enable current location detection.
		map.setMyLocationEnabled(true);
		LatLng usa = new LatLng(37.0000, -120.0000);
		// Get name for location selected.
		final Context context = this;
		GlobalObject gobj = (GlobalObject) context.getApplicationContext();
		gobj.getlocateName();

		/*
		 * Connect to server and retrieve latitude and longitude.
		 */
		Thread t = new Thread() {
			@Override
			public void run() {
				String path = "http://www.bmgsoftware.com/locationall.php";
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams
				 .setConnectionTimeout(client.getParams(), 10000);
				HttpResponse response;
				JSONObject json = new JSONObject();

				try {
					HttpPost post = new HttpPost(path);
					json.put("HELLO", "HELLO");
					GlobalObject gobj = (GlobalObject) context
					 .getApplicationContext();
					json.put("locateName", gobj.getlocateName());
					post.setHeader("json", json.toString());
					StringEntity se = new StringEntity(json.toString());
					se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					 "application/json"));
					post.setEntity(se);
					response = client.execute(post);
					InputStream in = response.getEntity().getContent();
					String names = convertStreamToString(in);
					String nameCount = names;
					count = nameCount.length() - nameCount.replace("#", "").length();

					for (int i = 0; i < count; i++) {
						String[] hash = names.split("#");
						user.add(hash[i]);
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

		LatLng userr = usa;
		int i = 0;
		String[] hash;
		for (String a : user) {
			Scanner ss = new Scanner(a);
			String nm = "";
			String ln = "";
			String al = "";
			String lg = "";
			nm = ss.next();
			ln = ss.next();
			al = ss.next();
			lg = ss.next();

			float all = Float.parseFloat(al);
			float lgg = Float.parseFloat(lg);

			userr = new LatLng(all, lgg);
										
			map.addMarker(new MarkerOptions().title(nm + " " + ln).snippet("")
					.position(userr));

			i++;
		}

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(userr, 18));
	}



}