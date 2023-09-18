package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteDataBase {
    private static final String TAG = "NoteDataBase";

    private static NoteDataBase dataBase;
    public static String DATABASE_NAME = "todo.db";
    public static String TABLE_NOTE = "NOTE";
    public static int DATABASE_VERSION = 1;

    private Context context;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;


    private NoteDataBase(Context context) {
        this.context = context;
    }

    public static NoteDataBase getInstance(Context context) {
        if (dataBase == null) {
            dataBase = new NoteDataBase(context);
        }
        return dataBase;
    }

    public Cursor rawQuery(String SQL) {

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }
        return c1;
    }

    public boolean execSQL(String SQL) {
        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }
        return true;
    }


    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name,factory,version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            String DROP_SQL = "drop table if exists " +TABLE_NOTE;

            try {
                db.execSQL(DROP_SQL);

            } catch (Exception ex){
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            String CREATE_SQL = "create table " + TABLE_NOTE + "("
                    + " _id integer NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  TODO TEXT DEFAULT '' "
                    + ")";
            try{
                db.execSQL(CREATE_SQL);
            } catch (Exception ex){
                Log.e(TAG,"Exception in CREATE_SQL", ex);
            }

            String CREATE_INDEX_SQL = "create index " + TABLE_NOTE + "_IDX ON " + TABLE_NOTE + "("
                    + "_id"
                    + ")";
            try{
                db.execSQL(CREATE_INDEX_SQL);
            } catch (Exception ex){
                Log.e(TAG, "Exception in CREATE_INDEX_SQL",ex);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    // 데이터베이스를 만드는 작업
    public boolean open() {
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    // 데이터베이스를 닫는 작업
    public void close() {
        db.close();
        dataBase = null;
    }


}
