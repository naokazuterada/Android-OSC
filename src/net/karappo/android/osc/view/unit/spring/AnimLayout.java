package net.karappo.android.osc.view.unit.spring;

import net.karappo.android.osc.view.Ball;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class AnimLayout extends net.karappo.android.osc.view.unit.AnimLayout
{
	public static final String type = "spring";
	
	private boolean stop = false;
	
	private float _spring = 0.2f;
	private float _friction = 1.0f;
	
	public AnimLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
	    setSpring(0);
	    setFriction(0);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
	{
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = MeasureSpec.getSize(heightMeasureSpec);
	    ball.init(new Point((int)width/2,(int)height/2));
	}
    
	public void setSpring(float val){ _spring = 0.01f+0.4f*val/100; }
	public void setFriction(float val){ _friction = 1.0f-0.2f*(val/100); }
	
	private int old_x = 10000000;
	private void calculate()
	{
		float targetX = getWidth()/2;
		
		Point new_center = new Point(0,getHeight()/2);
		
		Point ball_center = ball.getPosition();
		ball.vx += (targetX - ball_center.x) * _spring;
		ball.vx *= _friction;
		new_center.x = (int) (ball_center.x  + ball.vx);

		if(old_x!=10000000 && new_center.x == old_x) 	stop = true;
		else 						stop = false;
		if(stop) Log.d(TAG,"?"+new_center.x);
		ball.setPosition(new_center);
		
		old_x = new_center.x;
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) 
	{
		int action = event.getAction();
		
		switch (action & MotionEvent.ACTION_MASK) 
	    {
	    case MotionEvent.ACTION_DOWN:
	    case MotionEvent.ACTION_POINTER_DOWN:
	    	Log.d(TAG,"onTouchEvent"+this+" "+action);
	    	touching = true;
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
        
        // Ç®Ç‡ÇË
        ball.vx = ball.vy = 0;
        ball.setPosition(new Point((int)x, (int)y));
        
        return true;
	}
	
	// animation
	protected void onEnterFrame()
	{
		if(!touching) calculate();
		ball.update();
		
		// OSC SEND
		float val = ball.getPosition().x;	// 0 Å` getWidth()
		float position = val/getWidth()*2 -1;// -1 ~ 1 
		float speed = ball.vx;
		Object[] oscArgs = {position, speed};
		if(listener!=null && enabled && !stop) listener.onChanged(id, "spring", oscArgs);
	};
}

