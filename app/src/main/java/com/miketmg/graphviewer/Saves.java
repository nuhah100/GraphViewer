package com.miketmg.graphviewer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import database.FunctionData;
import database.FunctionsDbHelper;

public class Saves extends AppCompatActivity {

    // List view
    ListView listView;
    // Helper for communicating with database
    FunctionsDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves_manager);


        dbHelper = new FunctionsDbHelper(getApplicationContext());
        listView = (ListView) findViewById(R.id.list_saves);

        List<String> functions = readDataFromDb();


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.text_view, functions.toArray(new String[0]));

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                goBack(selectedItem);
            }
        });
    }

    // Get functions from databse
    private List<String> readDataFromDb()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        String[] projection = {
                FunctionData.FunctionEntry.COLUMN_NAME_FUNCTION
        };

        Cursor cursor = db.query(
                FunctionData.FunctionEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List functions = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemId = cursor.getString(
                    cursor.getColumnIndexOrThrow(FunctionData.FunctionEntry.COLUMN_NAME_FUNCTION));
            functions.add(itemId);
        }
        cursor.close();

        return functions;
    }

    // Save function to database
    private void writeDataToDb(String function)
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FunctionData.FunctionEntry.COLUMN_NAME_FUNCTION, function);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FunctionData.FunctionEntry.TABLE_NAME, null, values);
    }

    // Go back to main activity
    public void goBack(String function)
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Function",function);  // put data that you want returned to activity A
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    // Check that function is validate
    public void addFuntion(View view) {
        EditText text = (EditText) findViewById(R.id.functionAddText);
        String function = text.getText().toString();
        if(function == null || function == "")
            return;
        writeDataToDb(function);
        goBack(function);
    }
}