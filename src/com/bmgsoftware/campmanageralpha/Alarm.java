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
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Alarm extends BroadcastReceiver {
	String notification;
	String[] conversation;
	String message;
	String[][] con;

	@Override
	public void onReceive(final Context context, Intent intent) {
		PowerManager pm = (PowerManager) context
		 .getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock
		 (PowerManager.PARTIAL_WAKE_LOCK, "");
		wl.acquire();

		Thread t = new Thread() {
			@Override
			public void run() {
				GlobalObject gobj = (GlobalObject)
				 context.getApplicationContext();

				String path = "http://www.bmgsoftware.com/new_message.php";
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams
				 .setConnectionTimeout(client.getParams(), 10000);
				HttpResponse response;
				JSONObject json = new JSONObject();

				try {

					HttpPost post = new HttpPost(path);
					json.put("username", "admin");
					post.setHeader("json", json.toString());
					StringEntity se = new StringEntity(json.toString());
					se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					 "application/json"));
					post.setEntity(se);
					response = client.execute(post);
					InputStream in = response.getEntity().getContent();
					message = convertStreamToString(in);
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

		if (message.contains("1")) {
			Log.d("ME", "Notification started");
			GlobalObject gobj = (GlobalObject) context.getApplicationContext();
			if (gobj.getalarmsound()) {
				NotificationCompat.Builder mBuilder =
				 new NotificationCompat.Builder(
				 context).setSmallIcon(R.drawable.ic_launcher)
				 .setContentTitle("You have a new message")
				 .setDefaults(Notification.DEFAULT_SOUND)
				 .setContentText(notification);
				gobj.setalarmsound(false);

				NotificationManager mNotificationManager = 
				 (NotificationManager) context
				 .getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(1, mBuilder.build());
			}
		}

		Location loca;
		wl.release();
	}

	public void SetAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
		 .getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, Alarm.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
		 1000, pi);
	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent sender = 
		 PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
		 .getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader =
		 new BufferedReader(new InputStreamReader(is));
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

}