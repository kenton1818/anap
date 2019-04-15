package com.example.lui_project.application;

import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.IOException;



import com.example.lui_project.utils.BringData;
import com.example.lui_project.utils.SaveKeyValues;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //定位


        //sharedPreferences
        SaveKeyValues.createSharePreferences(this);

        //加載熱量量
        int saveDateIndex = SaveKeyValues.getIntValues("date_index",0);
        Log.e("數據庫己加載", "【" + saveDateIndex + "】");
        if (saveDateIndex == 0){
            try {
                SaveKeyValues.putIntValues("date_index", 1);
                BringData.getDataFromAssets(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        DBHelper dbHelper = new DBHelper();
//        Cursor cursor = dbHelper.selectAllDataOfTable("hot");
//        while (cursor.moveToNext()){
//            String name = cursor.getString(cursor.getColumnIndex("name"));
//            String type_name = cursor.getString(cursor.getColumnIndex("type_name"));
//            Log.e("食物","【" + name + "】" + "、"+"【" + type_name + "】");
//        }
//        cursor.close();


    }



}
