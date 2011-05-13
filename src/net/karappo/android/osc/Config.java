package net.karappo.android.osc;

import net.karappo.android.numpicker.NumberPicker;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class Config extends Activity implements OnClickListener
{
	private final String TAG = "OSC";
	final private boolean D = true;
	 	
	// views
 	private NumberPicker NP_hostip_1;
 	private NumberPicker NP_hostip_2;
 	private NumberPicker NP_hostip_3;
 	private NumberPicker NP_hostip_4;
	private NumberPicker NP_hostport;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.config);
        
		Intent i = getIntent();
		Host host = (Host) i.getSerializableExtra(Main.HOST_DATA);

		NP_hostip_1 = (NumberPicker) findViewById(R.id.host_ip_1);
		NP_hostip_2 = (NumberPicker) findViewById(R.id.host_ip_2);
		NP_hostip_3 = (NumberPicker) findViewById(R.id.host_ip_3);
		NP_hostip_4 = (NumberPicker) findViewById(R.id.host_ip_4);
		NP_hostport = (NumberPicker) findViewById(R.id.host_port);
		
		NP_hostip_1.setCurrent(host.ip_1);
		NP_hostip_2.setCurrent(host.ip_2);
		NP_hostip_3.setCurrent(host.ip_3);
		NP_hostip_4.setCurrent(host.ip_4);
		NP_hostport.setCurrent(host.port);
	
		// add button listeners
		((Button) findViewById(R.id.connect_btn)).setOnClickListener(this);
		((Button) findViewById(R.id.cancel_btn)).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) {
		case R.id.connect_btn:
			int host_ip_1 = NP_hostip_1.getCurrent();
			int host_ip_2 = NP_hostip_2.getCurrent();
			int host_ip_3 = NP_hostip_3.getCurrent();
			int host_ip_4 = NP_hostip_4.getCurrent();
			int host_port = NP_hostport.getCurrent();
			Host host = new Host(host_ip_1, host_ip_2, host_ip_3, host_ip_4, host_port);
			Intent i = new Intent();
			i.putExtra(Main.HOST_DATA, host);
			setResult(RESULT_OK, i);
			finish();
			break;
		case R.id.cancel_btn:
			setResult(RESULT_CANCELED, new Intent());
			finish();
			break;
		}
	}
}
