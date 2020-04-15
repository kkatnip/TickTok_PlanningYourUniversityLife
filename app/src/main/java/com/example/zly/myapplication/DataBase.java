package com.example.zly.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "note_db";
    public static final String TABLE_NAME = "note_table";
    public static final String ID = BaseColumns._ID;
    public static final String CONTENT = "content";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTENT + " TEXT)";
    private static final String SELECT_TABLE = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID;
    private static final String DELETE_SQL = "DELETE * FROM " + TABLE_NAME + " WHERE " + ID + " = " ;
    private static final String QUERY_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = ";



    String[] getColumns() {
        return new String[] {ID, CONTENT};
    }

    public DataBase(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
        arg0.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public void add(String content) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "insert into" + TABLE_NAME + "(_id, content) value(?, ?)";
        db.execSQL(sql, new Object[] {null, content}); // null for _id
    }
}
