package com.example.lui_project.service;




import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.example.lui_project.utils.StepDetector;
import com.example.lui_project.receiver.FunctionBroadcastReceiver;

public class StepCounterService extends Service {

    public static final String alarmSaveService = "SETALARM";
    private static final String TAG = "StepCounterService";
    public static Boolean FLAG = false;// Service running sign
    public String Flag = "0";
    private SensorManager mSensorManager;// Sensor service
    public StepDetector detector;// Sensor listener

    private PowerManager mPowerManager;// Power management service
    private WakeLock mWakeLock;// Screen light
    private AlarmManager alarmManager;//Alarm clock manager
    private PendingIntent pendingIntent;//Delayed intent
    private Calendar calendar;//date
    private Intent intent;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "SERVICE START");
        FLAG = true;// Mark as service is running

        // Create a listener class, instantiate a listener object
        detector = new StepDetector(this);//Instantiating sensor objects
        detector.walk = 1;//Set the number of steps from the beginning
        // Get sensor service, initialize sensor
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        // Register sensor, register listener
        mSensorManager.registerListener(detector,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        // Power management service
        mPowerManager = (PowerManager) this
                .getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myapp:mywakelocktag");
        //Keep device status
        mWakeLock.acquire();

        //Set up a timing service
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);//hour
        calendar.set(Calendar.MINUTE, 59);//min
        calendar.set(Calendar.SECOND, 0);//second
        calendar.set(Calendar.MILLISECOND, 0);//millisecond
        intent = new Intent(this, FunctionBroadcastReceiver.class);//send notification
        intent.setAction(alarmSaveService);//set Action
        pendingIntent = PendingIntent.getBroadcast(this, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //Set a timer
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        FLAG = false;// 服務停止
        Log.e(TAG, "後台服務停止");

        if (detector != null) {
            //取消對所有傳感器的監聽
            mSensorManager.unregisterListener(detector);
        }

        if (mWakeLock != null) {
            //釋放喚醒資源
            mWakeLock.release();
        }
    }
    public static boolean isActivityRunning(Context mContext) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if (info != null && info.size() > 0) {
            ComponentName component = info.get(0).topActivity;
            if (component.getPackageName().equals(mContext.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
