package classes;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.HashMap;


public class Function {

    String Fx;
    Expression Calc;

    public static double DivA = 0.00001;
    private HashMap<Double, Double> Cache;

    public Function(String fx)
    {
        Fx = fx;

        Cache = new HashMap<>();


        Calc = new ExpressionBuilder(fx)
                .variable("x")
                .build();
    }

    public double calculate(double x)
    {
        double y = 0;
        if (Cache.containsKey(x))
            y = Cache.get(x);
        else {
            Calc.setVariable("x", x);
            y =  Calc.evaluate();
            Cache.put(x, y);
        }
        return y;
    }


    public void updateFunction(String fx)
    {
        Cache.clear();
        Fx = fx.toLowerCase().trim();
        Calc = new ExpressionBuilder(Fx)
                .variable("x")
                .build();
        //Asim.clear();
    }

}
