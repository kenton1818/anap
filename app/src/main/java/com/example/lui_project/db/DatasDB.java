package com.example.lui_project.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatasDB extends SQLiteOpenHelper {
    private static String DB_NAME = "healthy_db";// 數據庫名稱
    private static int DB_VERSION = 1;// 數據庫版本號
    public DatasDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //主鍵自增,日期,年,月,日,步數,熱量,里程
        String stepRecorded = "create table step (_id integer primary key autoincrement ," +
                " date varchar(20),year integer,month integer,day integer,steps integer,hot varchar(20),length varchar(20))";
        //主鍵自增，類型，名稱，開始年，月，日，結束年，月，日，設置時間long值,提示時間long值，提示時，提示分提示時間文字，排序 ,增加
        String sportTable = "create table plans (_id integer primary key autoincrement ,"+
                "sport_type integer,sport_name varchar(20),start_year integer,start_month integer,start_day integer,"
                +"stop_year integer,stop_month integer,stop_day integer,set_time integer,hint_time integer,hint_hour integer, hint_minute integer,hint_str varchar(20),add_24_hour integer,"
                +"number_values"+")";
        //執行SQL語句
        db.execSQL(stepRecorded);
        db.execSQL(sportTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}