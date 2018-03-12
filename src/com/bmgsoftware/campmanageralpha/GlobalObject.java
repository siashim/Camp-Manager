package com.bmgsoftware.campmanageralpha;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.EditText;

public class GlobalObject extends Application{

	private String someVariable;
	private User usr;
	EditText messageHistoryText;
	
	private String locateName = "";

	private mapslocate ml;
	
	private boolean alarmsound;
	
	public boolean getalarmsound()
	{
		return alarmsound;
	}
	public void setalarmsound(boolean alarm)
	{
		alarmsound = alarm;
	}
	public void chooseConversationTextBox() {
	}

	public String getlocateName() {
		return locateName;
	}

	public void setlocateName(String s) {
		this.locateName = s;
	}
    
	public mapslocate getMl() {
		return ml;
	}

	public void setMl(mapslocate ml) {
		this.ml = ml;
	}
    
    

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }
    
    public User getUser() {
        return usr;
    }

    public void setUser(User usrr) {
        this.usr = usrr;
    }

	
}
