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

import classes.Graph;
import classes.Line;

public class GraphView extends View {

    Graph gp;

    Path GraphPath;
    Paint GraphPaint;
    Paint AxisPaint;
    Paint HelpLinePaint;

    float x1, x2, y1, y2;
    private VelocityTracker mVelocityTracker = null;


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
        gp = new Graph();

        GraphPaint = new Paint();

        GraphPaint.setAntiAlias(true);
        GraphPaint.setColor(Color.RED);
        GraphPaint.setStyle(Paint.Style.STROKE);
        GraphPaint.setStrokeJoin(Paint.Join.ROUND);
        GraphPaint.setStrokeWidth(5f);

        AxisPaint = new Paint();

        AxisPaint.setAntiAlias(true);
        AxisPaint.setColor(Color.DKGRAY);
        AxisPaint.setStyle(Paint.Style.STROKE);
        AxisPaint.setStrokeJoin(Paint.Join.ROUND);
        AxisPaint.setStrokeWidth(4f);

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

        // Set the according canvas.
        gp.canvas = canvas;

        Line<Float> xAxis, yAxis;
        xAxis = gp.getXAxis();
        yAxis = gp.getYAxis();

        // Draw Axis Lines
        canvas.drawLine(
                xAxis.x1,
                xAxis.y1,
                xAxis.x2,
                xAxis.y2,
                AxisPaint
        ); // X Axis
        canvas.drawLine(
                yAxis.x1,
                yAxis.y1,
                yAxis.x2,
                yAxis.y2,
                AxisPaint
        ); // Y Axis

        // Render Graph.
        GraphPath = gp.render();
        canvas.drawPath(GraphPath, GraphPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(gp.canvas == null)
            return false;

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
                    //mVelocityTracker.clear();
                }
                //mVelocityTracker.addMovement(event);
                break;
            case (MotionEvent.ACTION_MOVE): {
                //mVelocityTracker.addMovement(event);
                //mVelocityTracker.computeCurrentVelocity(1000);


                // Use this for after processing event.getDownTime()
                x2 = event.getX();
                y2 = event.getY();

                // Move values in graph.
                gp.touchMove(x1,y1,x2,y2);

                // Save the current.
                x1 = x2;
                y1 = y2;

                break;
            }
            case (MotionEvent.ACTION_UP): {
                // Continue motion
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                break;
            }
        }

        refresh();
        return true;
    }


    public void refresh()
    {
       //GraphPath = new Path();
        invalidate();
    }
}
