package com.example.lui_project.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DBHelper {
    private SQLiteDatabase db;
    public DBHelper(){
        db = SQLiteDatabase.openOrCreateDatabase(BringData.DATA_PATH+BringData.DATA_NAME,null);
    }

    /**
     * 查詢數據庫
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return
     */
    public Cursor select (String table, String[] columns,
                          String selection, String[] selectionArgs, String groupBy,
                          String having, String orderBy){
        return  db.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);


    }
    /**
     * 關閉數據庫操作
     */
   public void dbHelpclose(){
       if (db.isOpen()){
       db.close();
       }
   }

    /**
     * 按需求查詢
     * @param testType
     * @param handType
     * @return
     */
    public String getTextResult(int testType, int handType){
        Cursor cursor = this.select("test",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int test_Type = cursor.getInt(cursor.getColumnIndex("test_type"));
            int hand_Type = cursor.getInt(cursor.getColumnIndex("line_type"));
            if (testType == test_Type & handType == hand_Type){
                String text = cursor.getString(cursor.getColumnIndex("text"));
                cursor.close();
                return text;
            }
        }
        return null;
    }

    /**
     * 返回一個用於查詢所有數據的游標
     * @param name
     * @return
     */
    public Cursor selectAllDataOfTable(String name){

        return this.select(name,null,null,null,null,null,null);
    }
}
