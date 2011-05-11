package net.karappo.android.osc.view.bounce;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class AnimLayout extends net.karappo.android.osc.view.AnimLayout
{
	final private static String TAG = "OSC";
	final private boolean D = true;
	
	private float _gravity = 0.98f;
	private float _reflection = 0.7f;
	private float _friction = 0.99f;
	
	public AnimLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		setGravity(0);
		setReflection(0);
	    setFriction(0);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
	{
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = MeasureSpec.getSize(heightMeasureSpec);
	    ball.init(new Point(ball.getDiameter()/2,(int)height/2));
	}
    
	// val : 0 ~ 100
//	public void setGravity(float val){ _gravity = 0.98f+5.0f*val/100; }
	public void setGravity(float val){ _gravity = 0.98f*(val-50)/50;Log.d(TAG,""+_gravity); }
	public void setReflection(float val){ _reflection = 0.7f+0.3f*val/100; }
	public void setFriction(float val){ _friction = 1.0f-0.2f*(val/100); }
	
	private float previous_vx = 0;
	private float previous_vx_diff = 0;
	private float top_x;
	private boolean ballIsStatic = false;
	private void calculate()
	{
		Point ball_center = ball.getPosition();
		ball_center.x += _gravity;
		
		ball.vx *= _friction;
		ball.vx += _gravity;
		
		Point new_center = new Point();
		new_center.x = (int) (ball_center.x  + ball.vx);
		new_center.y = ball.start_point.y;
		
		float diff = Math.abs(previous_vx-ball.vx);
		
		if (new_center.x>getWidth()-ball.getDiameter()/2) 
		{
			ball.vx *= -_reflection;
			new_center.x = getWidth()-ball.getDiameter()/2;
//			float aa = previous_vx_diff-diff;
//			Log.d(TAG,"a:"+aa);
//			if(Math.abs(ball.vx)<=10 && Math.abs(top_x)<=1) ball.vx = 0;
		}
		if (new_center.x<0+ball.getDiameter()/2) 
		{
			ball.vx *= -_reflection;
			new_center.x = 0+ball.getDiameter()/2;
		}
		
		ball.setPosition(new_center);
		
		/*
		// ‚Ä‚Á‚Ø‚ñ
		if(previous_vx<0 && ball.vx>0)
		{
			float a = Math.abs(top_x-new_center.x);
			Log.d(TAG,"a "+a);
			if(a<1) ballIsStatic = true;
			top_x = new_center.x;
		}
		previous_vx = ball.vx;
		*/
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) 
	{
		int action = event.getAction();
		
		switch (action & MotionEvent.ACTION_MASK) 
	    {
	    case MotionEvent.ACTION_DOWN:
	    case MotionEvent.ACTION_POINTER_DOWN:
	    	touching = true;
	    	ballIsStatic = false;
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
        
        // ‚¨‚à‚è
        ball.setPosition(new Point((int)x, (int)y));
        
        return true;
	}
	
	// animation
	protected void onEnterFrame()
	{
		if(!touching) calculate();
		ball.update();
		
		
		// OSC SEND
		float val = ball.getPosition().x;	// 0 ` getWidth()
		float val2 = val/getWidth()*2 -1;// -1 ~ 1 
		if(listener!=null && enabled) listener.onChanged(id, val2, ball.vx);
	};
}

