package com.miketmg.graphviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.miketmg.graphviewer.views.GraphView;



/* TODO
 Integral
 better UI, add widgets
 add reminder
*/
public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    GraphView gp;
    TextInputEditText t;
    MediaPlayer mediaPlayer;
    PopupMenu popup;
    MenuInflater inflater;

    public final static int REQUEST_CODE_SAVES = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gp = (GraphView) findViewById(R.id.GraphView);
        t = (TextInputEditText) findViewById(R.id.txtFunc);

        mediaPlayer = MediaPlayer.create(this, R.raw.king);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        popup = new PopupMenu(this, findViewById(R.id.btnMenu));
        popup.setOnMenuItemClickListener(this);
        inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());
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

    public void integralCal()
    {
        Intent i = new Intent(this, Saves.class);
        startActivity(i);
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

    public void showPopupMenu(View view) {
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.saves_menu:
                getFunctionFromDatabase();
                return true;
            case R.id.integral_menu:
                // Still dont
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}