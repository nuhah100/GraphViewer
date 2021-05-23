package com.miketmg.graphviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.miketmg.graphviewer.views.GraphView;



/* TODO
 Zoom in
 Integral
 Save functions to database with sql
 better UI, add widgets
 add music
 add reminder
 DELETE ALL STUPID COMMITS!!!
 Optimize all
*/
public class MainActivity extends AppCompatActivity {
    GraphView gp;
    TextInputEditText t;


    public final static int REQUEST_CODE_SAVES = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gp = (GraphView) findViewById(R.id.GraphView);
        t = (TextInputEditText) findViewById(R.id.txtFunc);
    }




    //TODO add function transforming and showing.


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


    public void getFunctionFromDatabase()
    {
        Intent i = new Intent(this, Saves.class);
        startActivityForResult(i, REQUEST_CODE_SAVES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SAVES:
                Bundle extra = data.getExtras();
                if(extra == null)
                    return;
                String function = extra.getString("Function");
                gp.updateFunc(function);
                gp.refresh();
                break;
        }
    }

    public void getFunctionFromData(View view) {
        getFunctionFromDatabase();
    }
}