package classes;

import android.graphics.Canvas;
import android.graphics.Path;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph{

    private double MinX, MaxX;
    private double MinY, MaxY;

    public Canvas canvas;

    private String function;

    public ArrayList<Line<Double>> Asim;

    private Interpeter in = new Interpeter();

    private HashMap<Double, Double> Cache;

    public static double DivA = 0.00001;

    public static double Percision = 0.07;

    Expression Calc;
    double ratio;
    /*
       public native double CalculateExp(String func);
     public native int Alloc();
       public native int Free();
    static {
        System.loadLibrary("native-lib");
    }
    */

    public Graph() {
        function = "1/(x)";

        MinX = -10;
        MaxX = 10;

        Asim = new ArrayList<>();
        try {
            MinY = function(0) - 20;
            MaxY = function(MaxX) + 20;
        } catch (Exception e) {
            MinY = -10;
            MaxY = 60;
        }

        Cache = new HashMap<>();


        Calc = new ExpressionBuilder(function)
                .variable("x")
                .build();
        //function
        //in ;
    }

    public void cache()
    {
        double y;
        for(double x = MinX;  x <= MaxX; x += Percision)
        {
            try {
                //x = remap(i, 0, canvas.getWidth(), MinX, MaxX);
                if(!Cache.containsKey(x)) {
                    y = function(x);
                    Cache.put(x,y);
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public void clearCache()
    {
        Cache.clear();
    }

    public Path[] render(double MinX, double MaxX, double MinY, double MaxY)
    {
        Asim.clear();
        Path graph = new Path();
        double x = 0,y = 0,j = 0, i = 0;
        boolean isAsim = false;

        for (x = MinX; x <= MaxX; x += Percision) {
            try {
                //x = remap(i, 0, canvas.getWidth(), MinX, MaxX);
                if (Cache.containsKey(x))
                    y = Cache.get(x);
                else {
                    y = function(x);
                    Cache.put(x, y);
                }
                i = remap(x,MinX,MaxX,0,canvas.getWidth());
                j = remap((float) y, MinY, MaxY, canvas.getHeight(), 0);
                if (graph.isEmpty() || isAsim) {
                    graph.moveTo((float) i, (float) j);
                    System.out.println(i);
                    isAsim = false;
                }
                else
                    graph.lineTo((float) i, (float) j);
            }
            catch (ArithmeticException e)
            {
                isAsim = true;
                Line<Double> l = new Line<Double>();
                l.x1 = remap(x,MinX,MaxX,0,canvas.getWidth());
                l.x2 = remap(x,MinX,MaxX,0,canvas.getWidth());
                l.y1 = 0d;
                l.y2 =(double) canvas.getHeight();
                Asim.add(l);
                // System.out.println(i);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            //GraphPath.addCircle(i,j,2.5f, Path.Direction.CW);
        }


        return new Path[]{graph};
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

    public Line<Double> getXAxis()
    {
        Line<Double> l = new Line<Double>();
        l.x1 = Double.valueOf(0);
        l.y1 = remap(0, MinY,MaxY, canvas.getHeight(),0);
        l.x2 = Double.valueOf(canvas.getWidth());
        l.y2 = remap(0, MinY,MaxY, canvas.getHeight(),0);
        return l;
    }

    public Line<Double> getYAxis()
    {
        Line<Double> l = new Line<Double>();
        l.x1 = remap(0, MinX, MaxX, 0, canvas.getWidth());
        l.y1 = 0d;
        l.x2 = remap(0, MinX, MaxX, 0, canvas.getWidth());
        l.y2 = Double.valueOf(canvas.getHeight());
        return l;
    }

    public ArrayList<Line<Double>> getHelperLines() {
        ArrayList<Line<Double>> arr = new ArrayList<Line<Double>>();

        double valX = 3d, valY = .3 * valX;

        Line<Double> l;


        for (double i = 0; i <= MaxX; i += valX)
        {
            l = new Line<Double>();
            l.x1 = remap(i,MinX,MaxX,0,canvas.getWidth());
            l.y1 = 0d;
            l.x2 = remap(i,MinX,MaxX,0,canvas.getWidth());
            l.y2 = (double)canvas.getHeight();
            arr.add(l);
        }
        for (double i = 0; i >= MinX; i -= valX)
        {
            l = new Line<Double>();
            l.x1 = remap(i,MinX,MaxX,0,canvas.getWidth());
            l.y1 = 0d;
            l.x2 = remap(i,MinX,MaxX,0,canvas.getWidth());
            l.y2 = (double)canvas.getHeight();
            arr.add(l);
        }


        for (double i = 0; i <= MaxY; i += valY) {
            l = new Line<Double>();
            l.x1 = 0d;
            l.y1 = remap(i, MinY,MaxY, canvas.getHeight(),0);
            l.x2 = Double.valueOf(canvas.getWidth());
            l.y2 = remap(i, MinY,MaxY, canvas.getHeight(),0);
            arr.add(l);
        }

        for (double i = 0; i >= MinY; i -= valY) {
            l = new Line<Double>();
            l.x1 = 0d;
            l.y1 = remap(i, MinY,MaxY, canvas.getHeight(),0);
            l.x2 = Double.valueOf(canvas.getWidth());
            l.y2 = remap(i, MinY,MaxY, canvas.getHeight(),0);
            arr.add(l);
        }


        return arr;
    }


    private double function(double x)
    {

        // Need to do function that user input.


        //String exp = function.replaceAll("x",String.format("%.3f",x));
       // System.out.println(exp);
        clearCache();
        Calc.setVariable("x", x);
        return Calc.evaluate();
        //return (double) in.calculate(exp);
        //return (double) CalculateExp(exp);
    }


    // Function that transform value from group to another group.
    public static double remap (double value, double fromOrg, double toOrg, double fromDes, double toDes) {
        return (value - fromOrg) / (toOrg - fromOrg) * (toDes - fromDes) + fromDes;
    }



    public void touchMove(double x1, double y1, double x2, double y2) {
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
        clearCache();
        function = f.toLowerCase().trim();
        Calc = new ExpressionBuilder(function)
                .variable("x")
                .build();
        Asim.clear();
    }

    public Path[] renderFunc() {
        return render(MinX,MaxX,MinY,MaxY);
    }

    public Path renderFuncPart(int i) {
        double dis = MaxX - MinX;
        double disPart = dis/5;
        return new Path();//render((MinX + ),,MinY,MaxY);
    }
}

