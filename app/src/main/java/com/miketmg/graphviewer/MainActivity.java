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
        gp = (GraphView) findViewById(R.id.GraphView);
    }

    //TODO add function transforming and showing.

    public void ref(View view) {
        gp.refresh();
    }
}