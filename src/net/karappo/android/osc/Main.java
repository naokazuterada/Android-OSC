package net.karappo.android.osc;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

import net.karappo.android.osc.view.unit.AnimLayout;
import net.karappo.android.osc.view.unit.Unit;
import net.karappo.android.osc.view.unit.AnimLayout.OnChangedListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class Main extends Activity implements OnChangedListener
{
	private final String TAG = "OSC";
	final private boolean D = true;
	
	static final int REQUEST_HOST_SETTING = 0;
	
	public static final String HOST_DATA = "host_data";

	public static final String PREFS_NAME = "KarappoOSC";
	public static final String PKEY_HOST_IP_1 = "hostIP1";
	public static final String PKEY_HOST_IP_2 = "hostIP2";
	public static final String PKEY_HOST_IP_3 = "hostIP3";
	public static final String PKEY_HOST_IP_4 = "hostIP4";
	public static final String PKEY_HOST_PORT = "hostPort";
	
	private OSCPortOut oscPortOut;
 	private String deviceIPAddress = " ";
 	private Host host = new Host(192,168,1,2, 7400);
 	private String root = "/karappoosc";
	
 	// views
 	private TextView hostTV;
 	private TextView springPlusTV;
 	private TextView collisionPlusTV;
 	private TextView minusTV;
 	private ArrayList<AnimLayout> animLayouts = new ArrayList<AnimLayout>();
 	private LinearLayout units;
 	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);	// 3.0のみ
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        // retrieve preferences
        host = getHost();
        
        hostTV = (TextView) findViewById(R.id.hostTV);
        springPlusTV = (TextView) findViewById(R.id.springPlusTV);
        minusTV = (TextView) findViewById(R.id.minusTV);
        collisionPlusTV = (TextView) findViewById(R.id.collisionPlusTV);
        units = (LinearLayout) findViewById(R.id.units);

		Typeface face = Typeface.createFromAsset(getAssets(), "fonts/DS-DIGI.TTF");
		hostTV.setTypeface(face);
		springPlusTV.setTypeface(face);
		collisionPlusTV.setTypeface(face);
		minusTV.setTypeface(face);
		
		hostTV.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0)
			{
				Intent i = new Intent(getApplicationContext(),Config.class);
				i.putExtra(HOST_DATA, host);
				startActivityForResult(i,REQUEST_HOST_SETTING);
			}
		});
		springPlusTV.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0)
			{
				addUnit(new net.karappo.android.osc.view.unit.spring.Unit(getBaseContext()));
			}
		});
		collisionPlusTV.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0)
			{
				addUnit(new net.karappo.android.osc.view.unit.bounce.Unit(getBaseContext()));
			}
		});
		minusTV.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0)
			{
				removeUnit();
			}
		});
		
		setConfigDisp();
		pingSetUpData();
    }
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
        //返り値の取得
        if (requestCode == REQUEST_HOST_SETTING)
        {
            if (resultCode == RESULT_OK)
            {
            	// HOSTが変更された
        		host = (Host) intent.getSerializableExtra(Main.HOST_DATA);
        		saveHost();
        		setConfigDisp();
            	pingSetUpData();
            }
        }
    }
	
	private void addUnit(Unit unit)
	{
		int nextId = units.getChildCount()+1;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, 0, 0, 5);
		unit.setLayoutParams(layoutParams);
		units.addView(unit);
		
		animLayouts.add(unit.init(this, nextId));
		
	}
	private void removeUnit()
	{
		if(units.getChildCount()<=0) return;
		
		Unit lastUnit = (Unit) units.getChildAt(units.getChildCount()-1);
		((AnimLayout)lastUnit.getAnimLayout()).stop();
		animLayouts.remove(animLayouts.size()-1);
		units.removeView(lastUnit);
	}
	
	private void setConfigDisp()
	{
		hostTV.setText(host.ip+" : "+host.port);
	}
	
	
	private void pingSetUpData() 
	{
		oscPortOut = getOSCPort();
		Object[] oscArgs = {deviceIPAddress};
		OSCMessage oscMsgIP = new OSCMessage(root+"/setup", oscArgs);
		sendOSCMessage(oscMsgIP);
	}
	private void pingTestData() 
	{
		oscPortOut = getOSCPort();
		Object[] oscArgs = {deviceIPAddress};
		OSCMessage oscMsg = new OSCMessage(root+"/test", oscArgs);
		sendOSCMessage(oscMsg);
	}
	private OSCPortOut getOSCPort()
	{
		try {
			return new OSCPortOut(InetAddress.getByName(host.ip),host.port);   
		} catch (Exception e) {
			Log.e(TAG, "Connection Error:Couldn't set address" + e);
			Log.e(TAG, "IP:"+host.ip+", PORT:"+host.port);
			return null;
		}
	}
	private void sendOSCMessage(OSCMessage msg)
	{
		try {
			if(oscPortOut!=null) oscPortOut.send(msg);
		} catch (IOException e) {
			Log.e("IOException", e.toString());
		}
	}
	
	// LOAD AND SAVE PREF ////////////////////////////////////////////////////////////
	
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
	
	@Override
	protected void onDestroy() 
	{
		for(int i=0; i<animLayouts.size(); i++)
		{
			((AnimLayout)animLayouts.get(i)).stop();
		}
		super.onDestroy();
	}

	// バネの値が変わった時
	@Override
	public void onChanged(int id, String unitType, Object[] oscArgs) 
	{
		OSCMessage oscMsg = new OSCMessage(root+"/"+unitType+"/"+id, oscArgs);
		sendOSCMessage(oscMsg);
	}
	
	protected void showToast(String anErrorMessage) 
	{
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast.makeText(context, anErrorMessage, duration).show();
	}
}

class Host implements Serializable
{
	private static final long serialVersionUID = -8821489750333513435L;
	
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