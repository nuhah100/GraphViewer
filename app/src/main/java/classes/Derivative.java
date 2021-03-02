package classes;

import android.annotation.SuppressLint;

public class Derivative extends Function {

    @SuppressLint("DefaultLocale")
    public Derivative(String fx) {
        super(
                String.format(
                        "(%s-(%s))/%f",fx.replaceAll("x", String.format("(x+%f)", Function.DivA))
                        ,fx
                        ,Function.DivA
                )
        );
        System.out.println(String.format(
                "(%s-(%s))/%f",fx.replaceAll("x", String.format("(x+%f)", Function.DivA))
                ,fx
                ,Function.DivA
        ));
    }

    @Override
    public void updateFunction(String fx) {
        @SuppressLint("DefaultLocale")
        String divT = String.format(
                "(%s-(%s))/%f",fx.replaceAll("x", String.format("(x+%f)", Function.DivA))
                ,fx
                ,Function.DivA
        );
        super.updateFunction(divT);
        System.out.println(divT);
    }
}
