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
	
	float last_edge_v;
	int count = 0;
	private void calculate()
	{
		int right_edge = getWidth()-ball.getDiameter()/2;
		int left_edge = ball.getDiameter()/2;
		Point current_center = ball.getPosition();
		Point new_center = new Point(0,ball.start_point.y);	// y‚ÍŒÅ’è
		
		ball.vx = (ball.vx+_gravity)*_friction;
		
		new_center.x = (int) (current_center.x  + ball.vx);
		if(0<=_gravity) new_center.x++;	// MEMO y’ˆÓz‚±‚ê‚ª‚È‚¢‚Æ‰E‚Éd—Í‚Ì‚Æ‚«‚É”½”­ŒW”‚ª‚PˆÈã‚Á‚Û‚¢“®‚«‚É‚È‚Á‚Ä‚µ‚Ü‚¤I
		
		if (new_center.x<left_edge) 
		{
			// Left Edge
			ball.vx *= -_reflection;
			if(Math.abs(ball.vx)<1) ball.vx = 0;
			new_center.x = left_edge;
		}
		else if (right_edge<new_center.x) 
		{
			// Right Edge
			ball.vx *= -_reflection;
			if(Math.abs(ball.vx)<1) ball.vx = 0;
			new_center.x = right_edge;
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
        
        // ‚¨‚à‚è
        ball.setPosition(new Point((int)x, (int)y));
        
        return true;
	}
	
	// animation
	protected void onEnterFrame()
	{
		if(!touching) calculate();
		
		ball.update(); // MEMO:calculate‚µ‚È‚¢‚Æ‚«‚ÍonTouchƒCƒxƒ“ƒg’†‚È‚Ì‚ÅA‚»‚¿‚ç‚ÅˆÊ’uC³ˆ—‚ð‚µ‚Ä‚¢‚éB‚È‚Ì‚ÅA‚±‚±‚ÍŠmŽÀ‚ÉŽÀsB
		
		// OSC SEND
		float val = ball.getPosition().x;	// 0 ` getWidth()
		float val2 = val/getWidth()*2 -1;// -1 ~ 1 
		if(listener!=null && enabled) listener.onChanged(id, val2, (float)ball.vx);
	};
}

