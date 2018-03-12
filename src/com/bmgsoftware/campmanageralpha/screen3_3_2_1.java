package com.bmgsoftware.campmanageralpha;

import com.bmgsoftware.campmanageralpha.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
 
/**
 * Group selected notification screen.
 * 
 */
public class screen3_3_2_1 extends Activity {
 
	Button button2;
 
	/**
	 * On create grab layout screen3_3_2_1 and prepare the screen.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen3_3_2_1);
 
		addListenerOnButton();
 
	}
	

	/**
	 * Add listener on button.
	 */
	public void addListenerOnButton() {
		 
		final Context context = this;
		button2 = (Button) findViewById(R.id.Button1);
		button2.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
				finish();
				
				Intent intent = new Intent(context, screen3_3.class);
				startActivity(intent); 
			}
 
		});
 
	}
 
}