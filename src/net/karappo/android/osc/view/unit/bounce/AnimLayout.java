package net.karappo.android.osc.view.unit.bounce;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class AnimLayout extends net.karappo.android.osc.view.unit.AnimLayout
{
	final private static String TAG = "OSC";
	final private boolean D = true;
	
	private float _gravity = 0.98f;
	private float _reflection = 0.7f;
	private double _friction = 0.99;
	
	public AnimLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
//		setGravity(0);
//		setReflection(100);
//	    setFriction(0);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
	{
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = MeasureSpec.getSize(heightMeasureSpec);
	    ball.init(new Point(width-ball.getDiameter()/2,(int)height/2));
//	    ball.init(new Point(ball.getDiameter()/2,(int)height/2));
	}
    
	// val : 0 ~ 100
	public void setGravity(float val)    { 	_gravity 	= 0.98f*(val-50)/50;	}
	public void setReflection(float val) { 	_reflection = 0.7f+0.3f*val/100;	}
	public void setFriction(float val)   { 	_friction 	= 1.0f-0.2f*(val/100);	}
	
	float last_edge_v;
	int count = 0;
	
	private void calculate()
	{
//		Log.d(TAG,"G:R:F "+_gravity+":"+_reflection+":"+_friction);
		int right_edge = getWidth()-ball.getDiameter()/2;
		int left_edge = ball.getDiameter()/2;
		Point current_center = ball.getPosition();
		Point new_center = new Point(0,ball.start_point.y);	// yは固定
		
		if(0<=_gravity)
		{
			ball.vx = (ball.vx+_gravity)*_friction;
			new_center.x = (int) (current_center.x  + ball.vx);
			
			if (right_edge<new_center.x) 
			{
				// Right Edge
				Log.d(TAG,"R :"+ball.vx);
				ball.vx *= -_reflection;
				if(Math.abs(ball.vx)<1)
				{
//					Log.d(TAG,"v=0");
					ball.vx = 0;
				}
				
				new_center.x = right_edge;
				Log.d(TAG,"R>:"+ball.vx);
			}
		}
		else
		{
			ball.vx = (ball.vx+_gravity)*_friction;
			new_center.x = (int) (current_center.x  + ball.vx);
			
			if (new_center.x<left_edge) 
			{
				// Left Edge
				Log.d(TAG,"L :"+ball.vx);
				ball.vx *= -_reflection;
				if(Math.abs(ball.vx)<1) 
				{
//					Log.d(TAG,"v=0");
					ball.vx = 0;
				}
				
				
				new_center.x = left_edge;
				Log.d(TAG,"L>:"+ball.vx);
			}
		}
		
		ball.setPosition(new_center);
	}
	/*
	private void calculate()
	{
//		Log.d(TAG,"G:R:F "+_gravity+":"+_reflection+":"+_friction);
		int right_edge = getWidth()-ball.getDiameter()/2;
		int left_edge = ball.getDiameter()/2;
		Point current_center = ball.getPosition();
		Point new_center = new Point(0,ball.start_point.y);	// yは固定
		
//		ball.vx = ball.vx*_friction+_gravity;
		ball.vx = (ball.vx+_gravity)*_friction;
		
		new_center.x = (int) (current_center.x  + ball.vx);
//		Log.d(TAG,"x:"+current_center.x+"+"+ball.vx+"="+new_center.x);
		
		
		if (new_center.x<left_edge) 
		{
			// Left Edge
			Log.d(TAG,"L :"+ball.vx);
			ball.vx *= -_reflection;
			if(Math.abs(ball.vx)<1) 
			{
				Log.d(TAG,"v=0");
				ball.vx = 0;
			}
			
			
			new_center.x = left_edge;
//			new_center.x = left_edge+(left_edge-new_center.x);	// TODO ほんとはこっちが正しいかも
			Log.d(TAG,"L>:"+ball.vx);
		}
		else if (right_edge<new_center.x) 
		{
			// Right Edge
			Log.d(TAG,"R :"+ball.vx);
			ball.vx *= -_reflection;
			if(Math.abs(ball.vx)<1)
			{
				Log.d(TAG,"v=0");
				ball.vx = 0;
			}
			
			new_center.x = right_edge;
//			new_center.x = right_edge-(new_center.x-right_edge);	// TODO ほんとはこっちが正しいかも
			Log.d(TAG,"R>:"+ball.vx);
		}
		else
		{
			if(ball.vx!=0) Log.d(TAG," :"+ball.vx);
		}
		
		ball.setPosition(new_center);
	}
	*/
	/*
	private void calculate()
	{
		Point current_center = ball.getPosition();
		Point new_center = new Point(0,ball.start_point.y);	// yは固定
		
		ball.vx *= _friction;
		ball.vx += _gravity;
		
		new_center.x = (int) (current_center.x  + ball.vx);
		
		
		int right_edge = getWidth()-ball.getDiameter()/2;
		int left_edge = ball.getDiameter()/2;
		if (right_edge<new_center.x) 
		{
			// Right Edge
			ball.vx *= -_reflection;
			float diff = Math.abs(last_edge_v-Math.abs(ball.vx));
//			Log.d(TAG,"diff:"+diff);
			if(Math.abs(ball.vx)<1 && diff<0.002)
			{
				if(ball.vx!=0)
				{
//					Log.d(TAG,"---------");
					ball.vx = 0;
				}
			}
			
			new_center.x = right_edge;
			Log.d(TAG,"R:"+ball.vx);
			last_edge_v = Math.abs(ball.vx);
		}
		else if (new_center.x<left_edge) 
		{
			// Left Edge
			ball.vx *= -_reflection;
			if(Math.abs(ball.vx)<1) ball.vx = 0;
			
			new_center.x = left_edge;
			Log.d(TAG,"L:"+ball.vx);
		}
		else
		{
			if(ball.vx!=0) Log.d(TAG," :"+ball.vx);
		}
		
		//　TODO 速度に一定以上変化がなかったら停止
		
		ball.setPosition(new_center);
	}
	*/
	@Override
    public boolean onTouchEvent(MotionEvent event) 
	{
		int action = event.getAction();
		
		switch (action & MotionEvent.ACTION_MASK) 
	    {
	    case MotionEvent.ACTION_DOWN:
	    case MotionEvent.ACTION_POINTER_DOWN:
	    	touching = true;
	    	ball.vx = 0;
	    	break;
	    case MotionEvent.ACTION_MOVE:
	    	break;
	    case MotionEvent.ACTION_UP:
	    case MotionEvent.ACTION_POINTER_UP:
	    	touching = false;
			break;
	    }
	    
	    float x = event.getX();
        float y = event.getY();
        
        // おもり
        ball.setPosition(new Point((int)x, (int)y));
        
        return true;
	}
	
	// animation
	protected void onEnterFrame()
	{
		if(!touching) calculate();
		
		ball.update(); // MEMO:calculateしないときはonTouchイベント中なので、そちらで位置修正処理をしている。なので、ここは確実に実行。
		
		// OSC SEND
		float val = ball.getPosition().x;	// 0 ～ getWidth()
		float val2 = val/getWidth()*2 -1;// -1 ~ 1 
		if(listener!=null && enabled) listener.onChanged(id, val2, (float)ball.vx);
	};
}

