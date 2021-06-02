package classes;

import android.graphics.Canvas;
import android.graphics.Path;


public class Graph{

    // Range of function
    private static double MinX, MaxX;
    private static double MinY, MaxY;

    // Canvas
    public static Canvas canvas;

    // How precise is the function
    public double percision = 0.07;

    // Function
    public Function Fx;


    /*
       public native double CalculateExp(String func);
     public native int Alloc();
       public native int Free();
    static {
        System.loadLibrary("native-lib");
    }
    */

    public Graph(FunctionType type) {

        switch(type)
        {
            case DERIVATIVE:
            {
                Fx = new Derivative("x");
                break;
            }

            case INTEGRATION:
            {
                Fx = new Integration("x");
                percision = 1;
                break;
            }

            case FUNCTION:
            {
                Fx = new Function("x");
                break;
            }
        }


        MinX = -10;
        MaxX = 10;


        try {
            MinY = Fx.calculate(0) - 20;
            MaxY = Fx.calculate(MaxX) + 20;
        } catch (Exception e) {
            MinY = -10;
            MaxY = 60;
        }

        System.out.println(MinY + " " + MaxY);
        //function
        //in ;
    }



    // Render function
    public Path[] render(double MinX, double MaxX, double MinY, double MaxY)
    {

        Path graph = new Path();
        double x = 0,y = 0,j = 0, i = 0;
        boolean isAsim = false;

        for (x = MinX; x <= MaxX; x += percision) {
            try {
                //x = remap(i, 0, canvas.getWidth(), MinX, MaxX);
                y = Fx.calculate(x);
                i = remap(x,MinX,MaxX,0,canvas.getWidth());
                j = remap((float) y, MinY, MaxY, canvas.getHeight(), 0);
                if (graph.isEmpty()) {
                    graph.moveTo((float) i, (float) j);
                    System.out.println(i);
                    isAsim = false;
                }
                else
                    graph.lineTo((float) i, (float) j);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }


        return new Path[]{graph};
    }


    // Move function vertically
    public static void moveVertically(double vel)
    {
        MinY += vel;
        MaxY += vel;

        //System.out.println("Y: " + MinY + " " + MaxY);
    }

    // Move function horizontally
    public static void moveHorizontally(double vel)
    {
        MinX += vel;
        MaxX += vel;

       // System.out.println("X: " + MinX + " " + MaxX);
    }

    // Get X Axis
    public static Line<Double> getXAxis()
    {
        Line<Double> l = new Line<Double>();
        l.x1 = Double.valueOf(0);
        l.y1 = remap(0, MinY,MaxY, canvas.getHeight(),0);
        l.x2 = Double.valueOf(canvas.getWidth());
        l.y2 = remap(0, MinY,MaxY, canvas.getHeight(),0);
        return l;
    }

    // Get Y Axis
    public static Line<Double> getYAxis()
    {
        Line<Double> l = new Line<Double>();
        l.x1 = remap(0, MinX, MaxX, 0, canvas.getWidth());
        l.y1 = 0d;
        l.x2 = remap(0, MinX, MaxX, 0, canvas.getWidth());
        l.y2 = Double.valueOf(canvas.getHeight());
        return l;
    }




    // Function that transform value from group to another group.
    public static double remap (double value, double fromOrg, double toOrg, double fromDes, double toDes) {
        return (value - fromOrg) / (toOrg - fromOrg) * (toDes - fromDes) + fromDes;
    }

    // Get vector of transformation
    public static void touchMove(double x1, double y1, double x2, double y2) {
        double velX = remap(x2, 0 , canvas.getWidth(), MinX, MaxX) - remap(x1, 0 , canvas.getWidth(),MinX, MaxX);
        double velY = remap(y2, canvas.getHeight(), 0, MinY, MaxY) - remap(y1, canvas.getHeight(), 0, MinY, MaxY);

        double dx = x2 - x1, dy = y2 - y1;

        if(dx != 0 )//&& MaxX + velX  < 60 && MinX + velX > -60)
            moveHorizontally(-velX);
        if(dy != 0)// && MaxY + velY  < 60 && MinY + velY > -60)
            moveVertically(-velY);
    }

    // Update function
    public void updateFunc(String f)
    {
        Fx.updateFunction(f);
    }


    // Public rendering function
    public Path[] renderFunc() {
        return render(MinX,MaxX,MinY,MaxY);
    }

}

