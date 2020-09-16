package com.miketmg.graphviewer;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
        //double d = stringFromJNI("3^2 + 3");
        //System.out.println(d);

    }




    //TODO add function transforming and showing.

    public void ref(View view) {


    }

    public void renderFunc(View view) {
        String f = t.getEditableText().toString();
        gp.updateFunc(f);
        gp.refresh();
    }


    // UnFocus automatic.
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}