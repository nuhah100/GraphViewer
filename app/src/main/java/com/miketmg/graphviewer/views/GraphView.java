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

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import classes.Graph;
import classes.Line;

public class GraphView extends View  {

    Graph gp,div;

    Path GraphPath, DivPath;
    Paint GraphPaint, DivPaint;
    Paint AxisPaint;
    Paint AsimPaint;
    Paint HelpLinePaint;


    float x1, x2, y1, y2;
    private VelocityTracker mVelocityTracker = null;

    // Multithreading
    ExecutorService service;

    public GraphView(Context context) {
        super(context);
        setFocusable(true);
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

        gp.cache();

        div = new Graph();

        div.cache();

        GraphPaint = new Paint();

        GraphPaint.setAntiAlias(true);
        GraphPaint.setColor(Color.RED);
        GraphPaint.setStyle(Paint.Style.STROKE);
        GraphPaint.setStrokeJoin(Paint.Join.ROUND);
        GraphPaint.setStrokeWidth(5f);

        DivPaint = new Paint();

        DivPaint.setAntiAlias(true);
        DivPaint.setColor(Color.BLUE);
        DivPaint.setStyle(Paint.Style.STROKE);
        DivPaint.setStrokeJoin(Paint.Join.ROUND);
        DivPaint.setStrokeWidth(3f);

        AxisPaint = new Paint();

        AxisPaint.setAntiAlias(true);
        AxisPaint.setColor(Color.BLACK);
        AxisPaint.setStyle(Paint.Style.STROKE);
        AxisPaint.setStrokeJoin(Paint.Join.ROUND);
        AxisPaint.setStrokeWidth(6.4f);

        AsimPaint = new Paint();

        AsimPaint.setAntiAlias(true);
        AsimPaint.setColor(Color.BLUE);
        AsimPaint.setStyle(Paint.Style.STROKE);
        AsimPaint.setStrokeJoin(Paint.Join.ROUND);
        AsimPaint.setStrokeWidth(8f);

        HelpLinePaint = new Paint();

        HelpLinePaint.setAntiAlias(true);
        HelpLinePaint.setColor(Color.GRAY);
        HelpLinePaint.setStyle(Paint.Style.STROKE);
        HelpLinePaint.setStrokeJoin(Paint.Join.ROUND);
        HelpLinePaint.setStrokeWidth(3.4f);


        service = Executors.newCachedThreadPool();

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // !!!!!!!!!!!!!!!!!!!!
        // MUST DO!!!!!!!!!!!!!!!!!!!!!!
        // OPTIMIZE RUNTIME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // Set the according canvas.
        gp.canvas = canvas;
        div.canvas = canvas;
        Line<Double> xAxis, yAxis;
        xAxis = gp.getXAxis();
        yAxis = gp.getYAxis();

        // Draw Axis Lines
        canvas.drawLine(
                xAxis.x1.floatValue(),
                xAxis.y1.floatValue(),
                xAxis.x2.floatValue(),
                xAxis.y2.floatValue(),
                AxisPaint
        ); // X Axis
        canvas.drawLine(
                yAxis.x1.floatValue(),
                yAxis.y1.floatValue(),
                yAxis.x2.floatValue(),
                yAxis.y2.floatValue(),
                AxisPaint
        ); // Y Axis

/*
        ArrayList<Line<Double>> ar = gp.getHelperLines();
        for(int i = 0; i  < ar.size(); i++) {
            Line<Double> l = ar.get(i);
            canvas.drawLine(
                    l.x1.floatValue(),
                    l.y1.floatValue(),
                    l.x2.floatValue(),
                    l.y2.floatValue(),
                    HelpLinePaint
            );
        }
*/// Enable after

        // Render Graph.


        // For test purpose.
        long start = System.currentTimeMillis();
        // Multithreading
/*        class MultiRender extends Thread
        {
            ArrayList<Path> p;
            int i = 1;
            public MultiRender()
            {
                p = new ArrayList<Path>();
            }
            @Override
            public void run()
            {
                p.add(gp.renderFuncPart(i));
                i++;
            }
        }
        // Multithreading
        MultiRender mr = new MultiRender();
        for (int i = 1; i <= 5; ++i) {
            service.submit(mr);
        }
        service.shutdown();
        try {
            service.awaitTermination(1, TimeUnit.DAYS);
        } catch(Exception e) {
            System.out.println(e);
        }
        GraphPath = new Path();
        for (int i = 0; i < 5; i++)
            GraphPath.op(mr.p.get(i), Path.Op.UNION);
*/// Try to do it after basic is done.

        GraphPath = gp.renderFunc()[0];
        DivPath  = div.renderFunc()[0];
        // -------
        long elapsedTime = System.currentTimeMillis() - start;
        //System.out.println("Time that takes: " +(elapsedTime/1000F));
        canvas.drawPath(GraphPath, GraphPaint);
        canvas.drawPath(DivPath, DivPaint);

        GraphPath = null;
        System.gc();

        for (Line<Double> asim:
                gp.Asim) {
            canvas.drawLine(
                    asim.x1.floatValue(),
                    asim.y1.floatValue(),
                    asim.x2.floatValue(),
                    asim.y2.floatValue(),
                    AsimPaint
            );

        }



    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        if(gp.canvas == null)
            return false;


        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();
        //System.out.println(event.getPointerCount());
        float amount = .2f;

        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case (MotionEvent.ACTION_DOWN):
                x1 = event.getX();
                y1 = event.getY();
                if (mVelocityTracker == null) {

                    // Retrieve a new VelocityTracker object to watch the velocity
                    // of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                } //else {

                    // Reset the velocity tracker back to its initial state.
                    //mVelocityTracker.clear();
                //}
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
                div.touchMove(x1,y1,x2,y2);
                // Save the current.
                x1 = x2;
                y1 = y2;


                // ------

                break;
            }
            case (MotionEvent.ACTION_UP): {
                // Continue motion
                // ---

                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                //System.out.println("TOUCH TWO: " + event.getPointerId(0)getX() + ", " + getY());
                break;
            }
        }

        refresh();
        return true;
    }


    public void refresh()
    {
        GraphPath = new Path();
        DivPath = new Path();
        invalidate();
    }

    public void updateFunc(String f) {
        f = f.toLowerCase();
        gp.updateFunc(f);
        String divT = String.format(
                "(%s-(%s))/%f",f.replaceAll("x", String.format("(x+%f)", Graph.DivA))
                ,f
                ,Graph.DivA
        );
        System.out.println(divT);
        div.updateFunc(divT);
        refresh();
    }


}
