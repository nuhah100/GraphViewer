package classes;

import android.graphics.Canvas;
import android.graphics.Path;

import java.util.ArrayList;

public class Graph {

    private float MinX, MaxX;
    private float MinY, MaxY;

    public Canvas canvas;

    private String function;

    public ArrayList<Line<Float>> Asim;

    private Interpeter in = new Interpeter();
    /*
       public native double CalculateExp(String func);
     public native int Alloc();
       public native int Free();
    static {
        System.loadLibrary("native-lib");
    }
    */

    public Graph()
    {
        function = "1/x";

        MinX = -10;
        MaxX = 10;

        Asim = new ArrayList<Line<Float> >();
try {
    MinY = function(0) - 20;
    MaxY = function(MaxX) + 20;
}
catch (Exception e)
{
    MinY = -20;
    MaxY = 120;
}

        //function
        //in ;
    }



    public Path render()
    {
        //Alloc();
        Path graph = new Path();
        double x = 0,y = 0,j = 0;
        for(int i = 0; i <= canvas.getWidth(); i+=4)
        {
            try {
                x = remap(i, 0, canvas.getWidth(), MinX, MaxX);
                y = function((float) x);
                j = remap((float) y, MinY, MaxY, canvas.getHeight(), 0);
                if (i == 0)
                    graph.moveTo(i, (float) j);
                else
                    graph.lineTo(i, (float) j);
            }
            catch (ArithmeticException e)
            {
                Line<Float> l = new Line<Float>();
                l.x1 = remap((float) x,MinX,MaxX,0,canvas.getWidth());
                l.x2 = remap((float) x,MinX,MaxX,0,canvas.getWidth());
                l.y1 = 0f;
                l.y2 =(float) canvas.getHeight();
                Asim.add(l);
                System.out.println(i);
            }
            catch (Exception e)
            {
                System.out.println(i);
            }
            //GraphPath.addCircle(i,j,2.5f, Path.Direction.CW);
        }
        //Free();
        return graph;
    }

    public void moveVertically(double vel)
    {
        MinY += vel;
        MaxY += vel;

        //System.out.println("Y: " + MinY + " " + MaxY);
    }

    public void moveHorizontally(double vel)
    {
        MinX += vel;
        MaxX += vel;

       // System.out.println("X: " + MinX + " " + MaxX);
    }

    public Line<Float> getXAxis()
    {
        Line<Float> l = new Line<Float>();
        l.x1 = Float.valueOf(0);
        l.y1 = remap(0, MinY,MaxY, canvas.getHeight(),0);
        l.x2 = Float.valueOf(canvas.getWidth());
        l.y2 = remap(0, MinY,MaxY, canvas.getHeight(),0);
        return l;
    }

    public Line<Float> getYAxis()
    {
        Line<Float> l = new Line<Float>();
        l.x1 = remap(0, MinX, MaxX, 0, canvas.getWidth());
        l.y1 = Float.valueOf(0);
        l.x2 = remap(0, MinX, MaxX, 0, canvas.getWidth());
        l.y2 = Float.valueOf(canvas.getHeight());
        return l;
    }

    private float function(float x)
    {
        // Need to do function that user input.
        //return (float) Math.pow(x, 2);
        String exp = function.replace("x",String.format("%.3f",x));
       // System.out.println(exp);
        return (float) in.calculate(exp);
        //return (float) CalculateExp(exp);
    }


    // Function that transform value from group to another group.
    public static float remap (float value, float fromOrg, float toOrg, float fromDes, float toDes) {
        return (value - fromOrg) / (toOrg - fromOrg) * (toDes - fromDes) + fromDes;
    }



    public void touchMove(float x1, float y1, float x2, float y2) {
        double velX = remap(x2, 0 , canvas.getWidth(), MinX, MaxX) - remap(x1, 0 , canvas.getWidth(),MinX, MaxX);
        double velY = remap(y2, canvas.getHeight(), 0, MinY, MaxY) - remap(y1, canvas.getHeight(), 0, MinY, MaxY);

        double dx = x2 - x1, dy = y2 - y1;

        if(dx != 0 )//&& MaxX + velX  < 60 && MinX + velX > -60)
            moveHorizontally(-velX);
        if(dy != 0)// && MaxY + velY  < 60 && MinY + velY > -60)
            moveVertically(-velY);
    }

    public void updateFunc(String f)
    {
        function = f.toLowerCase().trim();
        Asim = new ArrayList<Line<Float>>();
    }
}

