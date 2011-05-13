package net.karappo.android.osc.view.unit.bounce;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class AnimLayout extends net.karappo.android.osc.view.unit.AnimLayout
{
	public static final String type = "bounce";
	
	private float _gravity;
	private float _reflection;
	private float _friction;
	
	private boolean stop = false;
	private boolean collision = false;	// 衝突
	
	public AnimLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
	{
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = MeasureSpec.getSize(heightMeasureSpec);
//	    ball.init(new Point(width-ball.getDiameter()/2,(int)height/2));
	    ball.init(new Point(ball.getDiameter()/2,(int)height/2));
	}
    
	// val : 0 ~ 100
	public void setGravity(float val)    { 	_gravity 	= 0.98f*(val-50)/50;	}
	public void setReflection(float val) { 	_reflection = 0.7f+0.3f*val/100;	}
	public void setFriction(float val)   { 	_friction 	= 1.0f-0.2f*(val/100);	}
	
	private void calculate()
	{
		stop = false;
		collision = false;
		
		int right_edge = getWidth()-ball.getDiameter()/2;
		int left_edge = ball.getDiameter()/2;
		Point current_center = ball.getPosition();
		Point new_center = new Point(0,ball.start_point.y);	// yは固定
		
		ball.vx = (ball.vx+_gravity)*_friction;
		
		new_center.x = (int) (current_center.x  + ball.vx);
		if(0<=_gravity) new_center.x++;	// MEMO 【注意】これがないと右に重力のときに反発係数が１以上っぽい動きになってしまう！
		
		if (new_center.x<left_edge) 
		{
			// Left Edge
			ball.vx *= -_reflection;
			if(Math.abs(ball.vx)<1)
			{
				ball.vx = 0;
				stop = true;
			}
			new_center.x = left_edge;
			collision = true;
		}
		else if (right_edge<new_center.x) 
		{
			// Right Edge
			ball.vx *= -_reflection;
			if(Math.abs(ball.vx)<1)
			{
				ball.vx = 0;
				stop = true;
			}
			new_center.x = right_edge;
			collision = true;
		}
		
		ball.setPosition(new_center);
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) 
	{
		
		int action = event.getAction();
		
		switch (action & MotionEvent.ACTION_MASK) 
	    {
	    case MotionEvent.ACTION_DOWN:
	    	Log.d(TAG,"ACTION_DOWN");
	    case MotionEvent.ACTION_POINTER_DOWN:
	    	touching = true;
	    	ball.vx = 0;
	    	Log.d(TAG,"ACTION_POINTER_DOWN");
	    	break;
	    case MotionEvent.ACTION_MOVE:
	    	Log.d(TAG,"ACTION_MOVE");
	    	break;
	    case MotionEvent.ACTION_UP:
	    	Log.d(TAG,"ACTION_UP");
	    case MotionEvent.ACTION_POINTER_UP:
	    	Log.d(TAG,"ACTION_POINTER_UP");
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
		float position = val/getWidth()*2 -1;// -1 ~ 1 
		float speed = ball.vx;
		Object[] oscArgs = {position, speed, collision};
		if(listener!=null && enabled && !stop) listener.onChanged(id, type, oscArgs);
	};
}

