package net.karappo.android.osc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

public class Ball extends View implements AnimView
{
	final private static String TAG = "OSC";
	final private boolean D = true;
	
	private final boolean flagX = true;
	private final boolean flagY = false;
	
	// デフォルト値
	private int lineWidth = 0;
	private int fillColor = 0xff000000;
	private int lineColor = 0xffffffff;
	private int diameter = 100;
	
	public Point start_point = new Point((int)diameter/2,(int)diameter/2);
	private Point center = start_point;
	
	public float vx = 0;
	public float vy = 0;
	
	public Ball(Context context)
	{
		super(context);
	}
	public Ball(Context context, int diameter)
	{
		super(context);
		this.diameter = diameter;
	}
	
	public Ball(Context context, int diameter, int lineWidth, int lineColor, int fillColor)
	{
		super(context);
		this.diameter = diameter;
		this.lineWidth = lineWidth;
		this.lineColor = lineColor;
		this.fillColor = fillColor;
		start_point = new Point((int)diameter/2,(int)diameter/2);
	}
	
	protected void onDraw(Canvas canvas)
	{
        super.onDraw(canvas);
        
        float radius = diameter/2;
        float inner_radius = radius-lineWidth;
        
        Paint paint = new Paint();
        paint.setColor(lineColor);
        paint.setAntiAlias(true);
        if(0<lineWidth) canvas.drawCircle(radius, radius, radius, paint);
        
        paint.setColor(fillColor);
        canvas.translate(lineWidth, lineWidth);
        canvas.drawCircle(inner_radius, inner_radius, inner_radius, paint);
    }
	
	public void setPosition(Point point)
	{
		center = point;
	}
	public void init(Point center)
	{
		start_point = center;
		vx = vy = 0;
		setPosition(start_point);
	}
	public Point getPosition()
	{
		return center;
	}
	public int getDiameter()
	{
		return diameter;
	}
	
	@Override
	public void update() 
	{
		if(!flagX) center.x = start_point.x;
		if(!flagY) center.y = start_point.y;
		layout((int)(center.x-diameter/2), (int)(center.y-diameter/2), (int)(center.x+diameter/2), (int)(center.y+diameter/2));
		invalidate();
	}
}
