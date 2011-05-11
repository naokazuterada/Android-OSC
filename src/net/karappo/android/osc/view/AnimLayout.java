package net.karappo.android.osc.view;

import net.karappo.android.osc.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class AnimLayout extends FrameLayout implements Runnable
{
	final private static String TAG = "OSC";
	final private boolean D = true;
	
	protected int id;
	protected boolean enabled = false;	// OSCÇëóêMÇ∑ÇÈÇ©Ç«Ç§Ç©
	
	// Event
	protected OnChangedListener listener;
	public interface OnChangedListener{ void onChanged(int unitId, float position, float speed); }
	public void setOnChangedListener(OnChangedListener listener){ this.listener = listener; }
	
	// anitmation
	protected volatile boolean done = false;
 	protected Thread thread = null;
 	protected int FPS = 30;
	
	protected Ball ball;
	
	public AnimLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// styleable Ç©ÇÁ TypedArray ÇÃéÊìæ
	    TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.AnimLayout);
	    int ballWidth = tArray.getDimensionPixelSize(R.styleable.AnimLayout_ballWidth, 30);
	    int ballLineWidth = tArray.getColor(R.styleable.AnimLayout_ballLineWidth, 0);
	    int ballLineColor = tArray.getColor(R.styleable.AnimLayout_ballLineColor, 0xffffffff);
	    int ballFillColor = tArray.getColor(R.styleable.AnimLayout_ballFillColor, 0xffffffff);
	    
	    // É{Å[Éã
	    ball = new Ball(context, ballWidth, ballLineWidth, ballLineColor, ballFillColor);
	    addView(ball, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	public void setId(int unitId){ id = unitId; }
	public void setEnebaled(boolean val) { enabled = val; }
	
	// animation
	protected boolean touching = false;
	protected final Handler hdlr = new Handler();
	protected final Runnable displayUpdate = new Runnable() { public void run() { onEnterFrame(); } };
	@Override
	public void run() {
		while (!done) {
			try { Thread.sleep(1000/FPS); }
			catch (InterruptedException e) {}
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
	
	
	
	// for override ///////////////////////////////////////////////////////
	
	protected void onEnterFrame(){};
	
	/*
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
	{
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = MeasureSpec.getSize(heightMeasureSpec);
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) 
	{
		int action = event.getAction();
		
		switch (action & MotionEvent.ACTION_MASK) 
	    {
	    case MotionEvent.ACTION_DOWN:
	    case MotionEvent.ACTION_POINTER_DOWN:
	    case MotionEvent.ACTION_MOVE:
	    case MotionEvent.ACTION_UP:
	    case MotionEvent.ACTION_POINTER_UP:
	    }
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
	*/
}

