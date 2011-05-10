package net.karappo.android.osc;

import java.io.IOException;
import java.net.InetAddress;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class Config extends Activity implements OnClickListener
{
	private final String TAG = "OSC";
	final private boolean D = true;
	
	public static final String PREFS_NAME = "KarappoOSC";
	public static final String PKEY_HOST_IP_1 = "hostIP1";
	public static final String PKEY_HOST_IP_2 = "hostIP2";
	public static final String PKEY_HOST_IP_3 = "hostIP3";
	public static final String PKEY_HOST_IP_4 = "hostIP4";
	public static final String PKEY_HOST_PORT = "hostPort";
	
	private OSCPortOut oscPortOut;
 	private String deviceIPAddress = " ";
 	private Host host;
 	private String root = "/karappoosc";
 	
	// views
 	private EditText ET_hostip_1;
 	private EditText ET_hostip_2;
 	private EditText ET_hostip_3;
 	private EditText ET_hostip_4;
	private EditText ET_hostport;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.config);
        
        // retrieve preferences
		Host lastHost = getHost();
		Log.d(TAG,"host"+lastHost.ip+":"+lastHost.port);
        // textbox for setting ip address of host
		ET_hostip_1 = (EditText) findViewById(R.id.host_ip_1);
		ET_hostip_2 = (EditText) findViewById(R.id.host_ip_2);
		ET_hostip_3 = (EditText) findViewById(R.id.host_ip_3);
		ET_hostip_4 = (EditText) findViewById(R.id.host_ip_4);
		ET_hostport = (EditText) findViewById(R.id.host_port);
		
		ET_hostip_1.setText(lastHost.ip_1+"");
		ET_hostip_2.setText(lastHost.ip_2+"");
		ET_hostip_3.setText(lastHost.ip_3+"");
		ET_hostip_4.setText(lastHost.ip_4+"");
		ET_hostport.setText(lastHost.port+"");
		
		// add button listeners
		((Button) findViewById(R.id.connect_btn)).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) {
		case R.id.connect_btn:
			hostChange();
			pingSetUpData();
			break;

		case R.id.send_btn:
			if(D) Log.d(TAG,"Send Message:");
			Object[] oscArgs = {1, 0};
			OSCMessage oscMsg = new OSCMessage(root+"/test", oscArgs);
			sendOSCMessage(oscMsg);
			break;
		}
	}
	
	private Host getHost()
	{
		SharedPreferences mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		int ip_1 = mPrefs.getInt(PKEY_HOST_IP_1, 192);
		int ip_2 = mPrefs.getInt(PKEY_HOST_IP_2, 168);
		int ip_3 = mPrefs.getInt(PKEY_HOST_IP_3, 1);
		int ip_4 = mPrefs.getInt(PKEY_HOST_IP_4, 2);
		int port = mPrefs.getInt(PKEY_HOST_PORT, 7400);
		return new Host(ip_1, ip_2, ip_3, ip_4,port);
	}
	
	private void saveHost()
	{
		SharedPreferences mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		Editor editor = mPrefs.edit();
		editor.putInt(PKEY_HOST_IP_1, host.ip_1);
		editor.putInt(PKEY_HOST_IP_2, host.ip_2);
		editor.putInt(PKEY_HOST_IP_3, host.ip_3);
		editor.putInt(PKEY_HOST_IP_4, host.ip_4);
		editor.putInt(PKEY_HOST_PORT, host.port);
		editor.commit();
	}
	
	// sets the host machine address
	private void hostChange() {

		// get host machine address from EditText
		int host_ip_1 = Integer.parseInt(ET_hostip_1.getText().toString());
		int host_ip_2 = Integer.parseInt(ET_hostip_2.getText().toString());
		int host_ip_3 = Integer.parseInt(ET_hostip_3.getText().toString());
		int host_ip_4 = Integer.parseInt(ET_hostip_4.getText().toString());
		int host_port = Integer.parseInt(ET_hostport.getText().toString());
		
		// update the monome grid object, 
		host = new Host(host_ip_1, host_ip_2, host_ip_3, host_ip_4, host_port);
		pingSetUpData();
		
		saveHost();
		
		// tell user/log
		showToast("Host set to: " + host.ip+"(port:"+host.port+")");
	}
	
	public void pingSetUpData() 
	{
		try {
			oscPortOut = new OSCPortOut(InetAddress.getByName(host.ip),host.port);   
		} catch (Exception e) {
			Log.e(TAG, "Connection Error:Couldn't set address" + e);
			Log.e(TAG, "IP:"+host.ip+", PORT:"+host.port);
		}

		Object[] oscArgs = {deviceIPAddress};
		OSCMessage oscMsgIP = new OSCMessage(root+"/setup", oscArgs);

		sendOSCMessage(oscMsgIP);
	}
	private void sendOSCMessage(OSCMessage msg)
	{
		try {
			if(oscPortOut!=null) oscPortOut.send(msg);
		} catch (IOException e) {
			Log.e("IOException", e.toString());
		}
	}
	

	// create a Toast to display info/errors etc
	protected void showToast(String anErrorMessage) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast.makeText(context, anErrorMessage, duration).show();
	}
}
