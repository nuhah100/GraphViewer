package com.miketmg.graphviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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

    // Costume view of the graph
    GraphView gp;
    // Text inputs
    TextInputEditText functionEdit, startEdit, endEdit;
    // Text field to show result
    TextView result;
    // Menu
    PopupMenu popup;
    // Object to inject menu
    MenuInflater inflater;
    // The start and end of the intergral
    String startIn,endIn;
    // Music service
    Intent musicService;
    // Codes for intents
    public final static int REQUEST_CODE_SAVES = 1;
    public final static int REQUEST_CAMERA = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get graph
        gp = (GraphView) findViewById(R.id.GraphView);
        // Get function
        functionEdit = (TextInputEditText) findViewById(R.id.txtFunc);
        // Add listner to check if the text changed
        functionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String function = charSequence.toString().toLowerCase();
                if(function.length() != 0 && MathUtility.isValidateFunction(function))
                    renderFunc(function);
            }
        });

        // Initalize menu
        popup = new PopupMenu(this, findViewById(R.id.btnMenu));
        popup.setOnMenuItemClickListener(this);
        inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());

        // Get range of intergral
        startEdit = findViewById(R.id.txtStartIn);
        endEdit = findViewById(R.id.txtEndIn);
        startEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                startIn = charSequence.toString();
                updateIntegral();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        endEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                endIn = charSequence.toString();
                updateIntegral();
            }
        });

        // Get result field
        result = findViewById(R.id.txtViewRes);

        // Keep phone from sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    //TODO add function transforming and showing.


    // Render the function
    public void renderFunc(String function) {
        String f = function.toLowerCase();
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

    // Lunch activty for getting function from database
    public void getFunctionFromDatabase()
    {
        Intent i = new Intent(this, Saves.class);
        startActivityForResult(i, REQUEST_CODE_SAVES);
    }


    // When we get back to activity with info
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SAVES: case REQUEST_CAMERA: {
                Bundle extra = data.getExtras();
                if (extra == null)
                    return;
                String function = extra.getString("Function");
                gp.updateFunc(function);
                gp.refresh();
                functionEdit.setText(function);
                break;
            }
        }
    }

    // Update result of intergral
    private void updateIntegral()
    {
        if(!MathUtility.isValidateIntegralRange(startIn,endIn))
            return;
        Double start = Double.parseDouble(startIn), end = Double.parseDouble(endIn);
        Integration i = new Integration(functionEdit.getEditableText().toString());
        result.setText(String.valueOf(i.simpson(start, end)));
    }

    // Show menu
    public void showPopupMenu(View view) {
        popup.show();
    }

    // When we click on item in the menu
    @Override
    public boolean onMenuItemClick(@org.jetbrains.annotations.NotNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.saves_menu:
                getFunctionFromDatabase();
                return true;
            case R.id.music: {
                musicService = new Intent(this, MusicService.class);
                startService(musicService);
                return true;
            }
            case R.id.qr_code: {
                Intent i = new Intent(this, QrScanner.class);
                startActivityForResult(i, REQUEST_CAMERA);
                return true;
            }
            case R.id.about: {
                Intent i = new Intent(this, About.class);
                startActivity(i);
                return true;
            }
            case R.id.instructions: {
                Intent i = new Intent(this, Instructions.class);
                startActivity(i);
                return true;
            }
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}