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
import com.bmgsoftware.campmanageralpha.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
 
/**
 * Student menu. View schedule, select group, send messages within the group.
 * 
 */
public class screen3_3 extends Activity {
 
	Button schedule;
	Button selectgroup;
	Button messaging;
	String username;
	String message;
	String[] conversation;
	String[][] con;
	int i;
	String contemp;
 
	/**
	 * On create grab layout screen3_3 and prepare student menu.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen3_3);
		conversation = new String[3];
		con = new String[3][2];

		GlobalObject gobj = (GlobalObject)context.getApplicationContext();
		getActionBar().setTitle("Welcome " + gobj.getUser().getmName()); 
		username = gobj.getUser().getmUserName();
		
		addListenerOnButton();
 
		
		
		
	}

	/**
	 * To be used with response from server. Process server streamed data to
	 * String data.
	 * 
	 * @param is
	 * @return
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
	final Context context = this;
	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
 
			alertDialogBuilder.setTitle("Log out?");
			
			
			alertDialogBuilder
			.setMessage("Click yes to logout")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					GlobalObject gobj = (GlobalObject)context.getApplicationContext();
					

					Thread t = new Thread() {
						@Override
						public void run() {
					String path = "http://www.bmgsoftware.com/conversation_final.php"; 
				    HttpClient client = new DefaultHttpClient();
				    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);                                                         // Limit
				    HttpResponse response;
				    JSONObject json = new JSONObject();
				    try {
				    	GlobalObject gobj = (GlobalObject)context.getApplicationContext();
				        HttpPost post = new HttpPost(path);
				        json.put("username", gobj.getUser().getmUserName());
				        json.put("convfinal", gobj.getUser().getmConversation(gobj.getUser().getmActiveConversation()));
				        post.setHeader("json", json.toString());
				        StringEntity se = new StringEntity(json.toString());
				        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
				        post.setEntity(se);
				        response = client.execute(post);
				        if (response != null) { 
				            InputStream in = response.getEntity().getContent();
				            String a = convertStreamToString(in);
				            
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
					
					
					screen3_3.this.finish();
					Intent intent = new Intent(context, screen2.class);
                    startActivity(intent);
				}
			  })
			.setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
	
	public void addListenerOnButton() {
		 
		final Context context = this;
		schedule = (Button) findViewById(R.id.schedule_button);
		schedule.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, screen3_3_1.class);
                            startActivity(intent);   
 
			}
 
		});
		
		selectgroup = (Button) findViewById(R.id.select_group_button);
		selectgroup.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, screen3_3_2.class);
                            startActivity(intent);   
 
			}
 
		});
		
		messaging = (Button) findViewById(R.id.message_button);
		messaging.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
					
					Thread t = new Thread() {
						@Override
						public void run()
						{
					String path = "http://www.bmgsoftware.com/conversation_receive_all.php"; 
				    HttpClient client = new DefaultHttpClient();
				    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
				    HttpResponse response;
				    JSONObject json = new JSONObject();
				    
				    try {
				    	
				        HttpPost post = new HttpPost(path);
				        json.put("username", username);
				        post.setHeader("json", json.toString());
				        StringEntity se = new StringEntity(json.toString());
				        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
				        post.setEntity(se);
				        response = client.execute(post);
				        InputStream in = response.getEntity().getContent();
				        message = convertStreamToString(in);
				        GlobalObject gobj = (GlobalObject)context.getApplicationContext();
				        
				        for(int i=0; i<3; i++){
				    		conversation = message.split("%%");
				        }
					    for(int l=0; l<3; l++){    
				        	for(int i=0; i<2; i++){
					    		con[l] = conversation[l].split("&&");
					        }
					    }
					    for(int l=0; l<3; l++){   
					    	gobj.getUser().setmConversationUser(l, con[l][0]);
				        	gobj.getUser().setmConversation(l, con[l][1]);
					    }

				  
				    }
				    
				    
				    catch (Exception e)
				    {
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
				Intent intent = new Intent(context, conversation_menu.class);
	                            startActivity(intent); 
			}
			 
		});
				
			    
	}
 
}