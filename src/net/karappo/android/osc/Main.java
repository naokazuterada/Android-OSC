package net.karappo.android.osc;

import java.io.IOException;
import java.net.InetAddress;

import net.karappo.android.osc.view.AnimLayout;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class Main extends Activity implements OnClickListener
{
	private final String TAG = "OSC";
	
	public static final String PREFS_NAME = "KarappoOSC";
	public static final String PKEY_HOST_IP = "lastHostIP";
	public static final String PKEY_HOST_PORT = "lastHostPort";
	
	private EditText ET_hostip;
	private EditText ET_hostport;
	private AnimLayout spring;
	
	private OSCPortOut oscPortOut;
 	private String deviceIPAddress = " ";
 	private Host host = new Host("10.0.1.3", 7400);
 	private String root = "/karappoosc";
	
 	
 	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // retrieve preferences
		Host lastHost = getHost();
		
        // textbox for setting ip address of host
		ET_hostip = (EditText) findViewById(R.id.host_ip);
		ET_hostip.setText(lastHost.ip);
		ET_hostip.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

					// update the prefix
					hostChange();
					// and hide the keyboard for now
					//InputMethodManager inputMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					//inputMM.hideSoftInputFromWindow(prefixTextBox.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});
		
		ET_hostport = (EditText) findViewById(R.id.host_port);
		Log.d(TAG,"ET_hostport"+ET_hostport);
		ET_hostport.setText(lastHost.port+"");
		ET_hostport.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

					// update the prefix
					hostChange();
					// and hide the keyboard for now
					//InputMethodManager inputMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					//inputMM.hideSoftInputFromWindow(prefixTextBox.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});
		
		// add button listeners
		((Button) findViewById(R.id.connect_btn)).setOnClickListener(this);
		
		((Button) findViewById(R.id.send_btn)).setOnClickListener(this);
		
		
		spring = (AnimLayout) findViewById(R.id.spring);
		spring.start();
		
		((SeekBar) findViewById(R.id.seekSpring)).setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
			{
				spring.setSpring(progress);
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar){}
			@Override public void onStopTrackingTouch(SeekBar seekBar){}
		});
		((SeekBar) findViewById(R.id.seekFriction)).setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
			{
				spring.setFriction(progress);
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar){}
			@Override public void onStopTrackingTouch(SeekBar seekBar){}
		});
    }
	
	private Host getHost()
	{
		SharedPreferences mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		String ip = mPrefs.getString(PKEY_HOST_IP, "192.168.1.1");
		int port = mPrefs.getInt(PKEY_HOST_PORT, 0);
		return new Host(ip,port);
	}
	private void saveHost()
	{
		SharedPreferences mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		Editor editor = mPrefs.edit();
		editor.putString(PKEY_HOST_IP, host.ip);
		editor.putInt(PKEY_HOST_PORT, host.port);
		editor.commit();
	}
	
	// sets the host machine address
	private void hostChange() {

		// get host machine address from EditText
		String host_ip = ET_hostip.getText().toString();
		int host_port = Integer.parseInt(ET_hostport.getText().toString());
		// tell user/log
		showToast("Host set to: " + host_ip+"(port:"+host_port+")");
		
		// update the monome grid object, 
		// and check with Max
		host = new Host(host_ip, host_port);
		pingMaxWithSetupData();
		
		saveHost();
	}
	
	
	// create a method for the addressChanged action
	public void pingMaxWithSetupData() {
		// the variable OSCPortOut tries to get an instance of OSCPortOut at the address
		try {
			oscPortOut = new OSCPortOut(InetAddress.getByName(host.ip),host.port);   
			// if the oscPort variable fails to be instantiated then sent the error message
		} catch (Exception e) {
			Log.e(TAG, "Connection Error:Couldn't set address" + e);
		}

		Object[] oscArgs = {deviceIPAddress};
		OSCMessage oscMsgIP = new OSCMessage(root+"/setup", oscArgs);

		sendOSCMessage(oscMsgIP);
	}
	
	// create a Toast to display info/errors etc
	protected void showToast(String anErrorMessage) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast.makeText(context, anErrorMessage, duration).show();
	}
	
	// send a OSC message over the network
	private void sendOSCMessage(OSCMessage msg)
	{
		try {
			if(oscPortOut!=null) oscPortOut.send(msg);
		} catch (IOException e) {
			Log.e("IOException", e.toString());
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.connect_btn:
			hostChange();
			pingMaxWithSetupData();
			break;

		case R.id.send_btn:
			Log.d(TAG,"Send Message:");
			Object[] oscArgs = {1, 0};
			OSCMessage oscMsg = new OSCMessage(root+"/tilt", oscArgs);
			sendOSCMessage(oscMsg);
			break;
		}
	}
	
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		spring.stop();
	}
}

class Host
{
	public String ip;
	public int port;
	
	Host(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
	}
}