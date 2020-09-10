package com.miketmg.graphviewer;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.miketmg.graphviewer.views.GraphView;

import classes.Interpeter;

public class MainActivity extends AppCompatActivity {
    GraphView gp;
    Interpeter in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        in = new Interpeter();
        Double d = in.proccesCode("5 + 5 - 3 + 2/2 + 3*2");
        System.out.println(d);
        gp = (GraphView) findViewById(R.id.GraphView);
    }

    public void ref(View view) {

        gp.i++;
        gp.refresh();
    }
}