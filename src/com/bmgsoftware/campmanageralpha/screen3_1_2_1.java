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
 * This class creates that notifies the user the groups have been set.
 */
public class screen3_1_2_1 extends Activity {

	Button button2;

	/**
	 * Sets up the the layout of the screen.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen3_1_2_1);

		addListenerOnButton();
	}

	/**
	 * Determines what happens when any of the button is pressed by the user.
	 */
	public void addListenerOnButton() {

		final Context context = this;
		button2 = (Button) findViewById(R.id.button1);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, screen3_1.class);
				startActivity(intent);
				finish();
			}

		});

	}

}