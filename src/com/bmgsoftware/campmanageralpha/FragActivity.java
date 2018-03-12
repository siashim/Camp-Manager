package com.bmgsoftware.campmanageralpha;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;

public class FragActivity extends FragmentActivity implements
    GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener {
	
// Global variable to hold the current location
Location mCurrentLocation;

@Override
public void onConnectionFailed(ConnectionResult arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onConnected(Bundle arg0) {
	// TODO Auto-generated method stub
	Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
}

@Override
public void onDisconnected() {
	// TODO Auto-generated method stub
	
}


}
