package net.karappo.android.osc.view.unit;

import net.karappo.android.osc.Main;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public abstract class Unit extends LinearLayout
{
	protected AnimLayout animLayout;
	
	protected int id;
	
	protected TextView idTV;
	protected ToggleButton enableBtn;
	protected Button settingBtn;
	protected LinearLayout settingPanel;
	
	
	public Unit(Context context) 
	{
		super(context);
	}
	public Unit(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}
	public AnimLayout init(Main main, int unitId)
	{
		return animLayout;
	}
	public AnimLayout getAnimLayout()
	{
		return animLayout;
	}
}
