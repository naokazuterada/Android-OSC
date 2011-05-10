package net.karappo.android.osc;

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
 	private EditText ET_hostip_1;
 	private EditText ET_hostip_2;
 	private EditText ET_hostip_3;
 	private EditText ET_hostip_4;
	private EditText ET_hostport;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.config);
        
		Intent i = getIntent();
		Host host = (Host) i.getSerializableExtra(Main.HOST_DATA);

		ET_hostip_1 = (EditText) findViewById(R.id.host_ip_1);
		ET_hostip_2 = (EditText) findViewById(R.id.host_ip_2);
		ET_hostip_3 = (EditText) findViewById(R.id.host_ip_3);
		ET_hostip_4 = (EditText) findViewById(R.id.host_ip_4);
		ET_hostport = (EditText) findViewById(R.id.host_port);
		
		ET_hostip_1.setText(host.ip_1+"");
		ET_hostip_2.setText(host.ip_2+"");
		ET_hostip_3.setText(host.ip_3+"");
		ET_hostip_4.setText(host.ip_4+"");
		ET_hostport.setText(host.port+"");
		
		// add button listeners
		((Button) findViewById(R.id.connect_btn)).setOnClickListener(this);
		((Button) findViewById(R.id.cancel_btn)).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) {
		case R.id.connect_btn:
			int host_ip_1 = Integer.parseInt(ET_hostip_1.getText().toString());
			int host_ip_2 = Integer.parseInt(ET_hostip_2.getText().toString());
			int host_ip_3 = Integer.parseInt(ET_hostip_3.getText().toString());
			int host_ip_4 = Integer.parseInt(ET_hostip_4.getText().toString());
			int host_port = Integer.parseInt(ET_hostport.getText().toString());
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
