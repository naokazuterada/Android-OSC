package net.karappo.android.osc.view.unit.spring;

import net.karappo.android.osc.Main;
import net.karappo.android.osc.R;
import net.karappo.android.osc.view.unit.AnimLayout;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
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
	
	private SeekBar springSeek;
	private Button springBtnMin;
	private Button springBtnMax;
	
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
	
	private void setting(Context context)
	{
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View v = inflater.inflate(R.layout.unit_spring, null);
		
		animLayout = (AnimLayout) v.findViewById(R.id.anim_layout);
		
		idTV = (TextView) v.findViewById(R.id.idTV_spring);
		if(!isInEditMode()) // Eclipse上でエラーになるのを防止
		{
			Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/DS-DIGI.TTF");
			idTV.setTypeface(face);
		}
		
		enableBtn = (ToggleButton) v.findViewById(R.id.enableBtn);
		
		settingPanel = (TableLayout) v.findViewById(R.id.settingPanel);
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
		
		springSeek = (SeekBar) v.findViewById(R.id.springSeek);
		springBtnMin = (Button) v.findViewById(R.id.springBtnMin);
		springBtnMax = (Button) v.findViewById(R.id.springBtnMax);
		
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
		
		
		springSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
			{
				cast(animLayout).setSpring(progress);
				if(progress==0) 	springBtnMin.setEnabled(false);
				else 				springBtnMin.setEnabled(true);
				if(progress==100) 	springBtnMax.setEnabled(false);
				else 				springBtnMax.setEnabled(true);
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar){}
			@Override public void onStopTrackingTouch(SeekBar seekBar){}
		});
		springBtnMin.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				springSeek.setProgress(0);
			}
		});
		springBtnMax.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				springSeek.setProgress(100);
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
		
		springBtnMin.setEnabled(false);
		frictionBtnMin.setEnabled(false);
		
		addView(v);
	}
	
	public AnimLayout init(Main main, int unitId)
	{
		id = unitId;
		idTV.setText(id+"");
		animLayout.setOnChangedListener(main);
		animLayout.setId(unitId);
		animLayout.start();
		return animLayout;
	}
	public AnimLayout getAnimLayout()
	{
		return animLayout;
	}
	
	private net.karappo.android.osc.view.unit.spring.AnimLayout cast(AnimLayout view)
	{
		return (net.karappo.android.osc.view.unit.spring.AnimLayout) view;
	}
}
