package database;

import android.provider.BaseColumns;

public class FunctionData {

    private FunctionData() {}

    public static class FunctionEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "functions";
        public static final String COLUMN_NAME_FUNCTION = "function";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FunctionEntry.TABLE_NAME + " (" +
                    FunctionEntry._ID + " INTEGER PRIMARY KEY," +
                    FunctionEntry.COLUMN_NAME_FUNCTION + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FunctionEntry.TABLE_NAME;
}
