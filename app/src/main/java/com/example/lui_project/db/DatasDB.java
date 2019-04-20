package com.example.lui_project.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatasDB extends SQLiteOpenHelper {
    private static String DB_NAME = "healthy_db";// Name of database
    private static int DB_VERSION = 1;// Database version number
    public DatasDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Primary key increment, date, year, month, day, step, heat, mileage
        String stepRecorded = "create table step (_id integer primary key autoincrement ," +
                " date varchar(20),year integer,month integer,day integer,steps integer,hot varchar(20),length varchar(20))";
        //Primary key increment, type, name, start year, month, day, end year, month, day, set time long value, prompt time long value, prompt, prompt prompt time text, sort, increase
        String sportTable = "create table plans (_id integer primary key autoincrement ,"+
                "sport_type integer,sport_name varchar(20),start_year integer,start_month integer,start_day integer,"
                +"stop_year integer,stop_month integer,stop_day integer,set_time integer,hint_time integer,hint_hour integer, hint_minute integer,hint_str varchar(20),add_24_hour integer,"
                +"number_values"+")";
        //Execute SQL statement
        db.execSQL(stepRecorded);
        db.execSQL(sportTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}