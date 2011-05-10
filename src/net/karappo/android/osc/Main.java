package net.karappo.android.osc;

import java.io.IOException;
import net.karappo.android.osc.view.AnimLayout;
import net.karappo.android.osc.view.AnimLayout.OnSpringProgressChangedListener;
import net.karappo.android.osc.view.SpringUnit;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class Main extends Activity implements OnSpringProgressChangedListener
{
	private final String TAG = "OSC";
	final private boolean D = true;
	
	private OSCPortOut oscPortOut;
 	private String deviceIPAddress = " ";
 	private Host host = new Host(192,168,1,2, 7400);
 	private String root = "/karappoosc";
	
 	// views
 	private AnimLayout unit1;
 	private AnimLayout unit2;
 	private AnimLayout unit3;
 	private AnimLayout unit4;
 	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        // retrieve preferences
        host = getHost();
				
		unit1 = ((SpringUnit) findViewById(R.id.unit1)).init(this,1);
		unit2 = ((SpringUnit) findViewById(R.id.unit2)).init(this,2);
		unit3 = ((SpringUnit) findViewById(R.id.unit3)).init(this,3);
		unit4 = ((SpringUnit) findViewById(R.id.unit4)).init(this,4);
    }
	public boolean onCreateOptionsMenu(Menu menu)
	{
		boolean ret = super.onCreateOptionsMenu(menu);
		menu.add(0 , Menu.FIRST , Menu.NONE , "Configuration");
		return ret;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case Menu.FIRST:
			Intent i = new Intent(getApplicationContext(),Config.class);
			startActivity(i);
		default:
			break;
		}
		return true;
	}
	
	private Host getHost()
	{
		SharedPreferences mPrefs = getSharedPreferences(Config.PREFS_NAME, MODE_PRIVATE);
		int ip_1 = mPrefs.getInt(Config.PKEY_HOST_IP_1, 192);
		int ip_2 = mPrefs.getInt(Config.PKEY_HOST_IP_2, 168);
		int ip_3 = mPrefs.getInt(Config.PKEY_HOST_IP_3, 1);
		int ip_4 = mPrefs.getInt(Config.PKEY_HOST_IP_4, 2);
		int port = mPrefs.getInt(Config.PKEY_HOST_PORT, 7400);
		return new Host(ip_1, ip_2, ip_3, ip_4, port);
	}
	
	private void sendOSCMessage(OSCMessage msg)
	{
//		Log.d(TAG,"Host set to: " + host.ip+"(port:"+host.port+")");
		try {
			if(oscPortOut!=null) oscPortOut.send(msg);
		} catch (IOException e) {
			Log.e("IOException", e.toString());
		}
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		unit1.stop();
		unit2.stop();
		unit3.stop();
		unit4.stop();
	}

	// ƒoƒl‚Ì’l‚ª•Ï‚í‚Á‚½Žž
	@Override
	public void onChanged(int id, float value) 
	{
		Object[] oscArgs = {value};
		OSCMessage oscMsg = new OSCMessage(root+"/spring/"+id, oscArgs);
		sendOSCMessage(oscMsg);
	}
	
	protected void showToast(String anErrorMessage) 
	{
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast.makeText(context, anErrorMessage, duration).show();
	}
}

class Host
{
	public int ip_1,ip_2,ip_3,ip_4,port;
	public String ip;
	
	Host(int ip_1, int ip_2, int ip_3, int ip_4, int port)
	{
		this.ip_1 = ip_1;
		this.ip_2 = ip_2;
		this.ip_3 = ip_3;
		this.ip_4 = ip_4;
		ip = ip_1+"."+ip_2+"."+ip_3+"."+ip_4;
		this.port = port;
	}
}