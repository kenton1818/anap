package com.example.lui_project.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DatasDao {
    private DatasDB myDB;
    private SQLiteDatabase db;

    public DatasDao(Context context) {

        myDB = new DatasDB(context);
        // 得到一个數據庫object
        db = myDB.getWritableDatabase();
    }

    // 加data
    public long insertValue(String table, ContentValues values) {

        long result = db.insert(table, null, values);
        return result;
    }

    // 改data
    public int updateValue(String table, ContentValues values, String whereClause, String[] whereArgs) {
/**
          * table 要修改的table的名 values 修改後的值 whereClause where關鍵子後的語句 id=？ whereArgs
          * 指的是whereClause裡邊？替換的值
          *
          */
        int result = db.update(table, values, whereClause, whereArgs);
        return result;
    }

    // 删除
    public int deleteValue(String table, String whereClause, String[] whereArgs) {
        /**
         * table 删除数据表的表名 whereClause 删除条件的where子句 whereArgs 删除条件占位符的值
         */
        return db.delete(table, whereClause, whereArgs);
    }

    // 查询

    public Cursor selectValue(String sql, String[] selectionArgs) {
        /**
                  * sql sql語句，sql語句不要加分號，系統會給我們自動填上 selectionArgs
                  * sql語句的where部分可能包含佔位符，所以需要用selectionArgs來取代 那個問號
                  */
        // select name from person where name =? and sex = ?
        return db.rawQuery(sql, selectionArgs);

    }

    // 查詢的第二種方式
    public Cursor selectValue2(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
                               String having, String orderBy) {
        /**
                  * table 表名 columns 要查詢的列 selection where後面的查詢條件 selectionArgs
                  * 取代查詢條件中？的佔位符 groupBy 根據摸個字段來分組 having having短語，進一步過濾篩選的 orderBy 排序
                  */
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    // 關
    public void close() {
        db.close();
    }

    // 清空表中數據
    public void clear(String name) {
        db.execSQL("delete from " + name);
    }

    //返回一個全查詢的Cursor
            public Cursor selectAll(String table){

        return selectValue2(table,null,null,null,null,null,null);
    }
    //返回查詢指定列
    public Cursor selectColumn(String table , String[] column){

        return selectValue2(table,column,null,null,null,null,null);
    }
}
