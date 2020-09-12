package com.miketmg.graphviewer;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.miketmg.graphviewer.views.GraphView;

import classes.Interpeter;

public class MainActivity extends AppCompatActivity {
    GraphView gp;
    TextInputEditText t;
    Interpeter in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gp = (GraphView) findViewById(R.id.GraphView);
        t = (TextInputEditText) findViewById(R.id.txtFunc);
    }

    //TODO add function transforming and showing.

    public void ref(View view) {


    }

    public void renderFunc(View view) {
        String f = t.getEditableText().toString();
        gp.updateFunc(f);
        gp.refresh();
    }
}