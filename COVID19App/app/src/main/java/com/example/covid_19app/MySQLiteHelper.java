package com.example.covid_19app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "covidapp.db";
    private static final String TABLE_NAME = "stats";
    private static final String COL_1 = "country";
    private static final String COL_2 = "new_cases";
    private static final String COL_3 = "new_deaths";
    private static final String COL_4 = "active";
    private static final String COL_5 = "critical";
    private static final String COL_6 = "total_cases";
    private static final String COL_7 = "total_deaths";
    private static final String COL_8 = "total_recovered";


    public MySQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableUsers = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_1 + " Text PRIMARY KEY," +
                COL_2 + " Text NOT NULL," +
                COL_3 + " Text NOT NULL," +
                COL_4 + " Text NOT NULL," +
                COL_5 + " Text NOT NULL," +
                COL_6 + " Text NOT NULL," +
                COL_7 + " Text NOT NULL," +
                COL_8 + " Text NOT NULL)" + ";";

        Log.d("DBText","createTable: "+createTableUsers);
        db.execSQL(createTableUsers);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table IF EXISTS " + TABLE_NAME + ";");
        this.onCreate(db);
    }

    public boolean addRecord(String country, String newCases, String newDeaths, String active, String critical,
                                String totalCases, String totalDeaths, String totalRecovered){

        ContentValues values = new ContentValues();
        values.put(COL_1,country);
        values.put(COL_2,newCases);
        values.put(COL_3,newDeaths);
        values.put(COL_4,active);
        values.put(COL_5,critical);
        values.put(COL_6,totalCases);
        values.put(COL_7,totalDeaths);
        values.put(COL_8,totalRecovered);

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_NAME,null,values);

        db.close();
        if(result == 0){
            return false;
        }
        else
            return true;
    }

}
