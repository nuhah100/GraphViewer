package com.miketmg.graphviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.miketmg.graphviewer.views.GraphView;

import classes.Integration;
import classes.MathUtility;


/* TODO
 Integral
 better UI, add widgets
 add reminder
*/
public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    GraphView gp;
    TextInputEditText functionEdit, startEdit, endEdit;
    TextView result;
    MediaPlayer mediaPlayer;
    PopupMenu popup;
    MenuInflater inflater;
    String startIn,endIn;

    public final static int REQUEST_CODE_SAVES = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gp = (GraphView) findViewById(R.id.GraphView);
        functionEdit = (TextInputEditText) findViewById(R.id.txtFunc);
        functionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String function = charSequence.toString().toLowerCase();
                if(function.length() != 0 && MathUtility.isValidateFunction(function))
                    renderFunc(function);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mediaPlayer = MediaPlayer.create(this, R.raw.king);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        popup = new PopupMenu(this, findViewById(R.id.btnMenu));
        popup.setOnMenuItemClickListener(this);
        inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());

        startEdit = findViewById(R.id.txtStartIn);
        endEdit = findViewById(R.id.txtEndIn);
        startEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                startIn = charSequence.toString();
                updateIntegral();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        endEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                endIn = charSequence.toString();
                updateIntegral();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        result = findViewById(R.id.txtViewRes);

    }

    //TODO add function transforming and showing.


    public void renderFunc(String function) {
        String f = function.toString();
        gp.updateFunc(f);
        gp.refresh();
        updateIntegral();
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

    private void updateIntegral()
    {
        if(!MathUtility.isValidateIntegralRange(startIn,endIn))
            return;
        Double start = Double.parseDouble(startIn), end = Double.parseDouble(endIn);
        Integration i = new Integration(functionEdit.getEditableText().toString());
        result.setText(String.valueOf(i.simpson(start, end)));
    }

    public void showPopupMenu(View view) {
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.saves_menu:
                getFunctionFromDatabase();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}