package com.example.lui_project.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;


import com.example.lui_project.db.DatasDao;
import com.example.lui_project.receiver.FunctionBroadcastReceiver;
import com.example.lui_project.utils.DateUtils;
import com.example.lui_project.utils.SaveKeyValues;
import com.example.lui_project.utils.StepDetector;

/**
 * record the service
 */
public class RecordedSaveService extends Service {
    public static final String cancelSaveService = "RECORDED";
    private DatasDao datasDao;
    public RecordedSaveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("testservice","start！");
        int custom_step_length = SaveKeyValues.getIntValues("length",70);
        int custom_weight = SaveKeyValues.getIntValues("weight", 50);
        //create database tools class
        datasDao = new DatasDao(this);
        boolean result = isActivityRunning(this);
        int step;
        //Determine how to store the value
        if (result){//Get the number of steps directly
            step = StepDetector.CURRENT_SETP;
        }else {//Get the number of steps directly...
            step = StepDetector.CURRENT_SETP + SaveKeyValues.getIntValues("sport_steps" ,0);
        }
        //Calculate the amount of heat consumed and the distance traveled by the number of steps
        double distance_values = step * custom_step_length * 0.01 *0.001;//km
        String distance_Str = formatDouble(distance_values);
        double heat_values = custom_weight * distance_values * 1.036;//cls
        String heat_Str = formatDouble(heat_values);
        //get date
        Map<String,Object> map = DateUtils.getDate();
        int year = (int) map.get("year");
        int month = (int) map.get("month");
        int day = (int) map.get("day");
        String date = (String) map.get("date");
        //Deposit data
        ContentValues values = new ContentValues();
        values.put("date",date);
        values.put("year",year);
        values.put("month",month);
        values.put("day",day);
        values.put("steps",step);
        values.put("hot",heat_Str);
        values.put("length", distance_Str);
        long reBack = datasDao.insertValue("step",values);
        Log.d("sql_step","saved");
        if (reBack > 0){
            SaveKeyValues.putIntValues("sport_steps", 0 );
            StepDetector.CURRENT_SETP = 0;
            Intent bro = new Intent(this, FunctionBroadcastReceiver.class);
            bro.setAction(cancelSaveService);
            sendBroadcast(bro);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("testservice", "end！");
    }

    /**
     * Determine if the activity is running
     * @param mContext
     * @return
     */
    public static boolean isActivityRunning(Context mContext){
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if(info != null && info.size() > 0){
            ComponentName component = info.get(0).topActivity;
            if(component.getPackageName().equals(mContext.getPackageName())){
                return true;
            }
        }
        return false;
    }
    private String formatDouble(Double doubles) {
        DecimalFormat format = new DecimalFormat("####.##");
        String distanceStr = format.format(doubles);
        return distanceStr.equals("0") ? "0.00" : distanceStr;
    }
}
