package classes;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.HashMap;


public class Function {

    // Function itself
    String Fx;
    // Calculator for calculate
    Expression Calc;

    // How close is the derivative
    public static double DivA = 0.000001;
    // Caching
    protected HashMap<Double, Double> Cache;



    public Function(String fx)
    {
        Fx = fx;

        Cache = new HashMap<>();


        Calc = new ExpressionBuilder(Fx)
                .variable("x")
                .build();

        Expression Calc2 = new ExpressionBuilder("log(x)")
            .variable("x")
            .build();

    }

    // Calculate the function itself
    public double calculate(double x)
    {
        double y = 0;
        if (Cache.containsKey(x))
            y = Cache.get(x);
        else {
            Calc.setVariable("x", x);
            Fx = Fx;
            y =  Calc.evaluate();
            Cache.put(x, y);
        }
        return y;
    }


    // Update function
    public void updateFunction(String fx)
    {
        Cache.clear();
        Fx = fx.toLowerCase().trim();
        Calc = new ExpressionBuilder(Fx)
                .variable("x")
                .build();
        //Asim.clear();
    }

    // Validate function
    public boolean validateFunction()
    {
        try
        {
            Expression Cal = new ExpressionBuilder(Fx)
                    .variable("x")
                    .build();
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

}
