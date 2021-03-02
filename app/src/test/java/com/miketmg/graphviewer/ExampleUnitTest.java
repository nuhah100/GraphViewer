package com.miketmg.graphviewer;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.junit.Test;

import classes.Derivative;
import classes.Function;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        Expression calc = new ExpressionBuilder("acos(23)")
                .build();

        assertEquals(Math.acos(23), calc.evaluate(), 0.01d);
    }

    @Test
    public void integral_isCorrect() {
        //Integration in = new Integration("4/(1+x^2)");

        //assertEquals(in.simpson(0,1,8), 3.1416, 0.01d);
        //assertEquals(in.simpson(0,1,16), 3.151593, 0.1d);
    }

    @Test
    public void devertive()
    {
        Function div = new Derivative("x^2");

        assertEquals(div.calculate(0),0,0.000001);
        assertEquals(div.calculate(1),2,0.00001);
    }

}