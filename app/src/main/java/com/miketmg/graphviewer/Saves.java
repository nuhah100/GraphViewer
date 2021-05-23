package com.miketmg.graphviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Saves extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves_manager);

        listView = (ListView) findViewById(R.id.list_saves);


    }


    public void goBack(String function)
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Function",function);  // put data that you want returned to activity A
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}