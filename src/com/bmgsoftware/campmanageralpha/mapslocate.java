package com.bmgsoftware.campmanageralpha;

import com.bmgsoftware.campmanageralpha.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
 
public class mapslocate extends Activity {
 
	Button button2;
 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen3_2_2_1);
  
		addListenerOnButton();
 
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        //LatLng sydney = new LatLng(-34.867, 151.206);

        map.setMyLocationEnabled(true);
        LatLng usa = new LatLng(37.0000, -120.0000);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(usa, 2));
        
//        moveTaskToBack(true);
//        Intent intent = new Intent(getBaseContext(), screen2.class);
//        startActivity(intent);   
        
        
	}

	public void addListenerOnButton() {
		 
		final Context context = this;
		button2 = (Button) findViewById(R.id.buttonlog);
		button2.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, screen2.class);
                            startActivity(intent);   
 
			}
 
		});
 
	}
 
}