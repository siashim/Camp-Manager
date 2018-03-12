package com.bmgsoftware.campmanageralpha;

import com.bmgsoftware.campmanageralpha.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * This class creates the message screen for when the user tries to open an
 * account using an existing email address.
 */
public class screen2_1_1 extends Activity {

	Button button2;

	/**
	 * Sets up the the layout of the screen.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen2_1_1);

		addListenerOnButton();

	}

	/**
	 * Determines what happens when the OK button is pressed.
	 */
	public void addListenerOnButton() {

		final Context context = this;
		button2 = (Button) findViewById(R.id.Button1);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				finish();
			}

		});

	}

}