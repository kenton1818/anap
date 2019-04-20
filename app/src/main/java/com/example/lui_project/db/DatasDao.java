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
        // Get a database object
        db = myDB.getWritableDatabase();
    }

    // insert data
    public long insertValue(String table, ContentValues values) {

        long result = db.insert(table, null, values);
        return result;
    }

    // update data
    public int updateValue(String table, ContentValues values, String whereClause, String[] whereArgs) {
/**
          * Table The name of the table to be modified. The modified value whereClause where the key sub-statement id=? whereArgs
   * refers to the value replaced in the whereClause
          *
          */
        int result = db.update(table, values, whereClause, whereArgs);
        return result;
    }

    // Delete
    public int deleteValue(String table, String whereClause, String[] whereArgs) {
        /**
         *Table delete the table name of the data table where Clause delete the where clause of the condition whereArgs delete the value of the condition placeholder
         */
        return db.delete(table, whereClause, whereArgs);
    }



    public Cursor selectValue(String sql, String[] selectionArgs) {
        /**
                  * Sql sql statement, sql statement do not add a semicolon, the system will automatically fill in the selectionArgs
                   * The where part of the sql statement may contain placeholders, so you need to replace the question mark with selectionArgs
                  */
        // select name from person where name =? and sex = ?
        return db.rawQuery(sql, selectionArgs);

    }

    // The second way of querying
    public Cursor selectValue2(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
                               String having, String orderBy) {
        /**
                  *Table table name columns columns to be queried selection conditions after selection where selectionArgs
                   * Replace the query conditions? The placeholder groupBy is grouped according to the touch field with having phrases, further filtering the filtered orderBy sort
                  */
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    // close
    public void close() {
        db.close();
    }

    // clear all data
    public void clear(String name) {
        db.execSQL("delete from " + name);
    }

    //Return a full query of Cursor
            public Cursor selectAll(String table){

        return selectValue2(table,null,null,null,null,null,null);
    }
    //Return the specified column of the query
    public Cursor selectColumn(String table , String[] column){

        return selectValue2(table,column,null,null,null,null,null);
    }
}
