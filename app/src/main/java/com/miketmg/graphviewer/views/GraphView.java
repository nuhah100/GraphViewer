package com.miketmg.graphviewer.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import androidx.annotation.Nullable;

public class GraphView extends View {
    Path GraphPath;
    Paint GraphPaint;
    Paint AxixPaint;
    Paint HelpLinePaint;
    float MinX, MaxX;
    float MinY, MaxY;

    // DUBUG VARSSSSSSSSSSSS
    int k = 0;

    // Dirs
    float x1, x2, y1, y2, dx, dy;
    private VelocityTracker mVelocityTracker = null;
    public int i = 1;
    double r;
    public GraphView(Context context) {
        super(context);

        init(null);
    }

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        MinX = -10;
        MaxX = 10;

        MinY = Function(0) - 20;
        MaxY = Function(MaxX) + 20;

        GraphPaint = new Paint();

        GraphPaint.setAntiAlias(true);
        GraphPaint.setColor(Color.RED);
        GraphPaint.setStyle(Paint.Style.STROKE);
        GraphPaint.setStrokeJoin(Paint.Join.ROUND);
        GraphPaint.setStrokeWidth(5f);

        AxixPaint = new Paint();

        AxixPaint.setAntiAlias(true);
        AxixPaint.setColor(Color.DKGRAY);
        AxixPaint.setStyle(Paint.Style.STROKE);
        AxixPaint.setStrokeJoin(Paint.Join.ROUND);
        AxixPaint.setStrokeWidth(4f);

        HelpLinePaint = new Paint();

        HelpLinePaint.setAntiAlias(true);
        HelpLinePaint.setColor(Color.GRAY);
        HelpLinePaint.setStyle(Paint.Style.STROKE);
        HelpLinePaint.setStrokeJoin(Paint.Join.ROUND);
        HelpLinePaint.setStrokeWidth(4f);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // !!!!!!!!!!!!!!!!!!!!
        // MUST DO!!!!!!!!!!!!!!!!!!!!!!
        // OPTIMIZE RUNTIME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

/*
        double spaceX, spaceY;
        spaceX = Math.abs(MaxX - MinX)/10;
        spaceY = Math.abs(MaxY - MinY)/10;
        for (int i = 0; i <= 11; i++)
        {
            canvas.drawLine(
                    0,
                    getHeight()/12 * i,
                    getWidth(),
                    getHeight()/12 * i,
                    HelpLinePaint
            ); // X Line
            canvas.drawLine(
                    getWidth()/12 * i,
                    0,
                    getWidth()/12 * i,
                    getHeight(),
                    HelpLinePaint
            ); // Y Line
        }

*/

        // Draw Axix Lines
        canvas.drawLine(
                0,
                Remap(0, MinY,MaxY, getHeight(),0),
                getWidth(),
                Remap(0, MinY,MaxY, getHeight(),0),
                AxixPaint
        ); // X Axix
        canvas.drawLine(
                Remap(0, MinX, MaxX, 0, getWidth()),
                0,
                Remap(0, MinX, MaxX, 0, getWidth()),
                getHeight(),
                AxixPaint
        ); // Y Axix

        GraphPath = new Path();

        for(int i = 0; i <= canvas.getWidth(); i++)
        {
            float x = Remap(i,0,canvas.getWidth(), MinX, MaxX);
            float y = Function(x);
            float j = Remap(y,MinY,MaxY, canvas.getHeight() ,0);
            if(i == 0)
                GraphPath.moveTo(i,j);
            else
                GraphPath.lineTo(i,j);
            //GraphPath.addCircle(i,j,2.5f, Path.Direction.CW);
        }
        canvas.drawPath(GraphPath, GraphPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float amount = .2f;
        int pointerId = event.getPointerId(event.getActionIndex());
        switch(event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                x1 = event.getX();
                y1 = event.getY();
                if (mVelocityTracker == null) {

                    // Retrieve a new VelocityTracker object to watch the velocity
                    // of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                } else {

                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(event);
                break;
            case (MotionEvent.ACTION_MOVE): {
                float velX, velY;
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                //velX = getXVelocity(mVelocityTracker,
                //     pointerId);
                //velY = getYVelocity(mVelocityTracker,
                //       pointerId);

                if(k  == 1)
                    break;

                // Use this for after processing event.getDownTime()
                x2 = event.getX();
                y2 = event.getY();
                dx = x2 - x1;
                dy = y2 - y1;


                velX = Remap(x2, 0 , getWidth(), MinX, MaxX) - Remap(x1, 0 , getWidth(),MinX, MaxX);
                velY = Remap(y2, getHeight(), 0, MinY, MaxY) - Remap(y1, getHeight(), 0, MinY, MaxY);



                if(dx != 0 )//&& MaxX + velX  < 60 && MinX + velX > -60)
                {
                    MaxX -= velX ;
                    MinX -= velX;
                }
                if(dy != 0)// && MaxY + velY  < 60 && MinY + velY > -60)
                {
                    MaxY -= velY;
                    MinY -= velY;
                }
                System.out.println("X: " +MaxX +" " + MinX);
                System.out.println("Y: " + MaxY +" " + MinY);
                if (dx > 0) {

                    //MaxX -= amount;
                    //MinX -= amount;
                    System.out.println("Right");
                } // Right
                else {
                    //MaxX += amount;
                    //MinX += amount;
                    System.out.println("Left");
                } // Left
                if (dy > 0) {
                   // MaxY += amount;
                    //MinY += amount;
                    System.out.println("Down");
                } // Down
                else {
                    //MaxY -= amount;
                    //MinY -= amount;
                    System.out.println("Up");
                } // Up*/

                x1 = x2;
                y1 = y2;

                break;
            }
            case (MotionEvent.ACTION_UP): {
                // Continue motion
                k  = 0;
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                break;
            }
        }


        invalidate();
        return true;
    }




    private float Function(float x)
    {
        return (float) Math.pow(x, i);//2);
    }

    private float FunctionN(float x)
    {
        return (float)x*2;
    }

    public static float Remap (float value, float from1, float to1, float from2, float to2) {
        return (value - from1) / (to1 - from1) * (to2 - from2) + from2;
    }

    public void refresh()
    {
        //GraphPath = new Path();
        invalidate();
    }
}
