package net.karappo.android.osc.view;

import java.util.Hashtable;

import net.karappo.android.osc.Main;
import net.karappo.android.osc.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;

public class AnimLayout extends FrameLayout implements Runnable
{
	final private static String TAG = "OSC";
	final private boolean D = true;
	
	private int id;
	private boolean enabled = false;
	
	// Event
	protected OnSpringProgressChangedListener listener;
	public interface OnSpringProgressChangedListener{ void onChanged(int unitId, float position, float speed); }
	public void setOnSpringProgressChangedListener(OnSpringProgressChangedListener listener){ this.listener = listener; }
	
	
	// anitmation
 	private volatile boolean done = false;
	private Thread thread = null;
	private int FPS = 30;
	
	private float _spring = 0.2f;
	private float _friction = 1.0f;
	
	private Ball ball;
	
	public AnimLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// styleable ‚©‚ç TypedArray ‚ÌŽæ“¾
	    TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.AnimLayout);
	    int ballWidth = tArray.getDimensionPixelSize(R.styleable.AnimLayout_ballWidth, 30);
	    int ballLineWidth = tArray.getColor(R.styleable.AnimLayout_ballLineWidth, 0);
	    int ballLineColor = tArray.getColor(R.styleable.AnimLayout_ballLineColor, 0xffffffff);
	    int ballFillColor = tArray.getColor(R.styleable.AnimLayout_ballFillColor, 0xffffffff);
	    
	    
	    // ‚¨‚à‚è
	    ball = new Ball(context, ballWidth, ballLineWidth, ballLineColor, ballFillColor);
	    addView(ball, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    
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
    
	public void setId(int unitId){ id = unitId; }
	
	public void setSpring(float val){ _spring = 0.01f+0.4f*val/100; }
	public void setFriction(float val){ _friction = 1.0f-0.2f*(val/100); }
	
	
	private void springTo(Ball ball, float targetX, float targetY)
	{
		Point ball_center = ball.getPosition();
		ball.vx += (targetX - ball_center.x) * _spring;
		ball.vy += (targetY - ball_center.y) * _spring;

		ball.vx *= _friction;
		ball.vy *= _friction;
		
		Point new_center = new Point();
		new_center.x = (int) (ball_center.x  + ball.vx);
		new_center.y = (int) (ball_center.y  + ball.vy);
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
        
        // ‚¨‚à‚è
        // ˆÊ’u
        ball.setPosition(new Point((int)x, (int)y));
        
        return true;
	}
	
	Hashtable<Integer, TouchPoint> points = new Hashtable<Integer, TouchPoint>();
	class TouchPoint
	{
		public float x;
		public float y;
		public float p;
	}
	void put_points(MotionEvent ev) 
	{
	    int count = ev.getPointerCount();
	    for (int i=0; i < count; i++) 
	    {
			int id = ev.getPointerId(i);
			TouchPoint p = new TouchPoint();
			p.x = ev.getX(i);
			p.y = ev.getY(i);
			p.p = ev.getPressure(i);
			points.put(id, p);
	    }
	}
	
	// animation
	private boolean touching = false;
	private final Handler hdlr = new Handler();
	private final Runnable displayUpdate = new Runnable() {
		public void run() 
		{
			if(!touching) springTo(ball, getWidth()/2, ball.start_point.y);
			ball.update();
			
			// OSC SEND
			float val = ball.getPosition().x;	// 0 ` getWidth()
			float val2 = val/getWidth()*2 -1;// -1 ~ 1 
			if(listener!=null && enabled) listener.onChanged(id, val2, ball.vx);
		}
	};
	@Override
	public void run() {
		while (!done) {
			try { Thread.sleep(1000/FPS); }
			catch (InterruptedException e) { }
			hdlr.post(displayUpdate);
		}
	}
	public void start()
	{
		thread = new Thread(this);
		thread.start();
	}
	public void stop()
	{
		done = true;
		try { thread.stop(); }
		catch (SecurityException e) {}
	}
	public void setEnebaled(boolean val)
	{
		enabled = val;
	}
}

