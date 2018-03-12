package com.bmgsoftware.campmanageralpha;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

public class TestIntent extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {
	
		private static final String TAG = null;
		private AlarmManager alarmMgr;
		private PendingIntent alarmIntent;
		private Alarm alarm = new Alarm();
	
	    
	    private String defaultUploadWebsite;
	
	    private boolean currentlyProcessingLocation = false;
	    private LocationRequest locationRequest;
	    private LocationClient locationClient;
	
	    @Override
	    public void onCreate() {
        super.onCreate();

        //defaultUploadWebsite = getString(R.string.default_upload_website);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // if we are currently trying to get a location and the alarm manager has called this again,
        // no need to start processing a new location.
    	alarm.SetAlarm(this);
    	
        if (!currentlyProcessingLocation) {
            currentlyProcessingLocation = true;
            startTracking();
            
            
        }

        
        return START_NOT_STICKY;
    }

    public void onStart(Context context,Intent intent, int startId)
    {
        alarm.SetAlarm(context);
        //Log.i("jwoijgiowe","oiwrhbjowijgiowejiwejfiewji");
    }
    private void startTracking() {
        Log.d(TAG, "startTracking");

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            locationClient = new LocationClient(this,this,this);

            if (!locationClient.isConnected() || !locationClient.isConnecting()) {
                locationClient.connect();
            }
        } else {
            Log.e(TAG, "unable to connect to google play services.");
        }
        
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    double lat;
	double lon;
	double alt;
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e(TAG, "position: " + location.getLatitude() + ", " + location.getLongitude() + " accuracy: " + location.getAccuracy());
            //Log.i("sht","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            // we have our desired accuracy of 500 meters so lets quit this service,
            // onDestroy will be called and stop our location uodates
            if (location.getAccuracy() < 500.0f){//4.0f){//500.0f) {
               // stopLocationUpdates();//disable this line for default and enable above comment for default
                //sendLocationDataToWebsite(location);
                
                
      	
                
                
                
                final Context context = this;
            	 lat = location.getLatitude();
            	 lon = location.getLongitude();
            	 alt = location.getAltitude();
          	        //    Log.i(TAG, "Latitude: "+lat+"\nLongitude: "+lon+"\nAltitude: "+alt);
  
          	         //}
          	        //else {
          	          //  Log.i(TAG, "Error!");
          	            //}
          	    //}
          	            Thread tt = new Thread() {
          	    			@Override
          	    			public void run()
          	    			{
          	    				
          	    				
          	    	        	
          	    		     	   
          	    				GlobalObject gobj = (GlobalObject)context.getApplicationContext();
          	    				String path = "http://www.bmgsoftware.com/updatelocation.php"; 
          	    				HttpClient client = new DefaultHttpClient();
          	    				HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
          	    				HttpResponse response;
          	    				JSONObject json = new JSONObject();	    
          	    				try {
          	    					HttpPost post = new HttpPost(path);
          	    					json.put("user", gobj.getUser().getmUserName());//"chap");
          	    					json.put("lat", lat);//-0.0170703  );
          	    					json.put("lon", lon);//+0.0027463  );
          	    					json.put("alt", alt);
          	    					//Log.i("jason Object", json.toString());
          	    					post.setHeader("json", json.toString());
          	    					StringEntity se = new StringEntity(json.toString());
          	    					se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
          	    					post.setEntity(se);
          	    					response = client.execute(post);
          	    					InputStream in = response.getEntity().getContent();
          	    					
          	    				//	Log.i("sht","cccccccccccccccccccccccccccccccccccccccccccccccccc");
          	    					//notification = convertStreamToString(in);
          	    					//names.replaceFirst(" ", "");
          	    					//Log.i("Not", notification);
          	    				}   
          	    				catch (Exception e)
          	    				{
          	    					e.printStackTrace();
          	    				}
          	    			}
          	    		};
          	            tt.start();
          	            try {
							tt.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
          	          //stopLocationUpdates();
          	         // Log.i("sht","bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            }
           // Log.i("sht","qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        }
    }

    private void stopLocationUpdates() {
        if (locationClient != null && locationClient.isConnected()) {
            locationClient.removeLocationUpdates(this);
            locationClient.disconnect();
        }
    }

    /**
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); // milliseconds
        locationRequest.setFastestInterval(1000); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationClient.requestLocationUpdates(locationRequest, this);
    }

    /**
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        Log.e(TAG, "onDisconnected");

        stopLocationUpdates();
        stopSelf();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");

        stopLocationUpdates();
        stopSelf();
    }
}
