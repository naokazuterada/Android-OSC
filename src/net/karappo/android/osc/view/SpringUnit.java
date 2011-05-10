package net.karappo.android.osc.view;

import net.karappo.android.osc.Main;
import net.karappo.android.osc.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SpringUnit extends LinearLayout
{
	private AnimLayout animLayout;
	
	private int id;
	
	private ToggleButton enableBtn;
	
	private SeekBar springSeek;
	private TextView springTV;
	private Button springBtn1;
	private Button springBtn2;
	
	private SeekBar frictionSeek;
	private TextView frictionTV;
	private Button frictionBtn1;
	private Button frictionBtn2;
	
	public SpringUnit(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View v = inflater.inflate(R.layout.spring_unit, null);
		
		animLayout = (AnimLayout) v.findViewById(R.id.anim_layout);
		
		enableBtn = (ToggleButton) v.findViewById(R.id.enableBtn);
		
		springSeek = (SeekBar) v.findViewById(R.id.springSeek);
		springTV = (TextView) v.findViewById(R.id.springTV);
		springBtn1 = (Button) v.findViewById(R.id.springBtn1);
		springBtn2 = (Button) v.findViewById(R.id.springBtn2);
		
		frictionSeek = (SeekBar) v.findViewById(R.id.frictionSeek);
		frictionTV = (TextView) v.findViewById(R.id.frictionTV);
		frictionBtn1 = (Button) v.findViewById(R.id.frictionBtn1);
		frictionBtn2 = (Button) v.findViewById(R.id.frictionBtn2);
		
		
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
				animLayout.setSpring(progress);
				if(progress==0) 	springBtn1.setEnabled(false);
				else 				springBtn1.setEnabled(true);
				if(progress==100) 	springBtn2.setEnabled(false);
				else 				springBtn2.setEnabled(true);
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar){}
			@Override public void onStopTrackingTouch(SeekBar seekBar){}
		});
		springBtn1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				springSeek.setProgress(0);
			}
		});
		springBtn2.setOnClickListener(new OnClickListener()
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
				animLayout.setFriction(progress);
				if(progress==0) 	frictionBtn1.setEnabled(false);
				else 				frictionBtn1.setEnabled(true);
				if(progress==100) 	frictionBtn2.setEnabled(false);
				else 				frictionBtn2.setEnabled(true);
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar){}
			@Override public void onStopTrackingTouch(SeekBar seekBar){}
		});
		frictionBtn1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				frictionSeek.setProgress(0);
			}
		});
		frictionBtn2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				frictionSeek.setProgress(100);
			}
		});
		
		addView(v);
		
	}
	
	
	public AnimLayout init(Main main, int unitId)
	{
		id = unitId;
		animLayout.setOnSpringProgressChangedListener(main);
		animLayout.setId(unitId);
		animLayout.start();
		return animLayout;
	}
}
