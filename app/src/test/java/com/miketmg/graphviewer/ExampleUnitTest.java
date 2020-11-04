package com.miketmg.graphviewer;

import org.junit.Test;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import static org.junit.Assert.*;

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
}