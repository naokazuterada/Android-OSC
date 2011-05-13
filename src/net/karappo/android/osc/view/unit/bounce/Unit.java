package net.karappo.android.osc.view.unit.bounce;

import net.karappo.android.osc.Main;
import net.karappo.android.osc.R;
import net.karappo.android.osc.view.unit.AnimLayout;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Unit extends net.karappo.android.osc.view.unit.Unit
{
	private final String TAG = "OSC";
	final private boolean D = true;
	
	protected TextView idTV;
	
	private SeekBar gravitySeek;
	private Button gravityBtnMin;
	private Button gravityBtnMax;
	
	private SeekBar reflectionSeek;
	private Button reflectionBtnMin;
	private Button reflectionBtnMax;
	
	private SeekBar frictionSeek;
	private Button frictionBtnMin;
	private Button frictionBtnMax;
	
	public Unit(Context context) 
	{
		super(context);
		setting(context);
	}
	
	public Unit(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		setting(context);
	}
	
	protected void setting(Context context)
	{
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View v = inflater.inflate(R.layout.unit_bounce, null);
		
		animLayout = (AnimLayout) v.findViewById(R.id.anim_layout);
		
		idTV = (TextView) v.findViewById(R.id.idTV);
		if(!isInEditMode()) // Eclipse上でエラーになるのを防止
		{
			Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/DS-DIGI.TTF");
			idTV.setTypeface(face);
		}
		
		enableBtn = (ToggleButton) v.findViewById(R.id.enableBtn);
		
		settingPanel = (LinearLayout) v.findViewById(R.id.settingPanel);
		settingPanel.setVisibility(View.GONE);
		settingBtn = (Button) v.findViewById(R.id.settingBtn);
		settingBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(settingPanel.getVisibility()==View.GONE)	settingPanel.setVisibility(View.VISIBLE);
				else 										settingPanel.setVisibility(View.GONE);
			}
		});
		
		gravitySeek = (SeekBar) v.findViewById(R.id.gravitySeek);
		gravityBtnMin = (Button) v.findViewById(R.id.gravityBtnMin);
		gravityBtnMax = (Button) v.findViewById(R.id.gravityBtnMax);
		
		reflectionSeek = (SeekBar) v.findViewById(R.id.reflectionSeek);
		reflectionBtnMin = (Button) v.findViewById(R.id.reflectionBtnMin);
		reflectionBtnMax = (Button) v.findViewById(R.id.reflectionBtnMax);
		
		frictionSeek = (SeekBar) v.findViewById(R.id.frictionSeek);
		frictionBtnMin = (Button) v.findViewById(R.id.frictionBtnMin);
		frictionBtnMax = (Button) v.findViewById(R.id.frictionBtnMax);
		
		
		// ユニットを有効に
		enableBtn.setChecked(true);
		animLayout.setEnebaled(true);
		enableBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				animLayout.setEnebaled(enableBtn.isChecked());
			}
		});
		
		
		gravitySeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
			{
				cast(animLayout).setGravity(progress);
				if(progress==0) 	gravityBtnMin.setEnabled(false);
				else 				gravityBtnMin.setEnabled(true);
				if(progress==100) 	gravityBtnMax.setEnabled(false);
				else 				gravityBtnMax.setEnabled(true);
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar){}
			@Override public void onStopTrackingTouch(SeekBar seekBar){}
		});
		gravityBtnMin.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				gravitySeek.setProgress(0);
			}
		});
		gravityBtnMax.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				gravitySeek.setProgress(100);
			}
		});
		
		reflectionSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
			{
				cast(animLayout).setReflection(progress);
				if(progress==0) 	reflectionBtnMin.setEnabled(false);
				else 				reflectionBtnMin.setEnabled(true);
				if(progress==100) 	reflectionBtnMax.setEnabled(false);
				else 				reflectionBtnMax.setEnabled(true);
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar){}
			@Override public void onStopTrackingTouch(SeekBar seekBar){}
		});
		reflectionBtnMin.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				reflectionSeek.setProgress(0);
			}
		});
		reflectionBtnMax.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				reflectionSeek.setProgress(100);
			}
		});
		
		frictionSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
			{
				cast(animLayout).setFriction(progress);
				if(progress==0) 	frictionBtnMin.setEnabled(false);
				else 				frictionBtnMin.setEnabled(true);
				if(progress==100) 	frictionBtnMax.setEnabled(false);
				else 				frictionBtnMax.setEnabled(true);
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar){}
			@Override public void onStopTrackingTouch(SeekBar seekBar){}
		});
		frictionBtnMin.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				frictionSeek.setProgress(0);
			}
		});
		frictionBtnMax.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				frictionSeek.setProgress(100);
			}
		});
		
		gravityBtnMin.setEnabled(false);
		reflectionBtnMin.setEnabled(false);
		frictionBtnMin.setEnabled(false);
		
		addView(v);
		
		//  値を買える必要があるので一旦Maxにする
		setGravitySeek(100);
		setReflectionSeek(0);
	    setFrictionSeek(100);
	    
		setGravitySeek(0);
		setReflectionSeek(100);
	    setFrictionSeek(0);
	}
	
	public net.karappo.android.osc.view.unit.AnimLayout init(Main main, int id)
	{
		this.id = id;
		idTV.setText(id+"");
		animLayout.setOnChangedListener(main);
		animLayout.setId(id);
		animLayout.start();
		return animLayout;
	}
	
	private net.karappo.android.osc.view.unit.bounce.AnimLayout cast(AnimLayout view)
	{
		return (net.karappo.android.osc.view.unit.bounce.AnimLayout) view;
	}
	
	// val = 0 ~ 100
	private void setGravitySeek(int val)		{	gravitySeek.setProgress(val);	}
	private void setReflectionSeek(int val) 	{ 	reflectionSeek.setProgress(val);	}
	private void setFrictionSeek(int val)   	{ 	frictionSeek.setProgress(val);	}
}
