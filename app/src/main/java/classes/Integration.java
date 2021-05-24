package classes;

import java.util.ArrayList;

public class Integration extends Function {

    private ArrayList<Double> ZeroPoints;


    public Integration(String fx) {
        super(fx);
        //ZeroPoints = new ArrayList<Double>() ;
        //calculateZero();
    }

    private void calculateZero() {
        for (double x = -Double.MAX_VALUE; x <= Double.MAX_VALUE; x+= 0.000000001)
            if(calculate(x) == 0)
                ZeroPoints.add(x);

    }

    @Override
    public double calculate(double x) {
        return 0;
        //return super.calculate(x);
    }

    private double integral(double a, double b, int n) {

        double h = ((b - a) / n);

        double area = 0.0;

        for (int i = 0; (i < n); i++) {

            double _f = super.calculate((a + (i * h)));

            double _multiply = (h * _f);

            double _plus = (area + _multiply);

            area = _plus;

        }
        return area;

    }

    // Default n' values.
    public double simpson(double a, double b)
    {
        return simpson(a,b,8);
    }

    // Simpson's way for calculate intergral based on n'.
    public double simpson(double a, double b, int n) {
        double h = (b - a) / n;
        double firstSeries = 0, secondSeries = 0;

        for (int j = 1; j <= (n / 2 - 1); j++)
            firstSeries += super.calculate(a + 2 * j * h);

        for (int j = 1; j <= (n / 2); j++)
            secondSeries += super.calculate(a + (2 * j - 1) * h);


        return h / 3 * (super.calculate(a) + 2 * firstSeries + 4 * secondSeries + super.calculate(b));
    }



}
