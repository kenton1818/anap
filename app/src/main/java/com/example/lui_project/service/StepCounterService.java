package com.example.lui_project.service;




import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import java.util.Calendar;

import com.example.lui_project.utils.StepDetector;

public class StepCounterService extends Service {

    public static final String alarmSaveService = "mrkj.healthylife.SETALARM";
    private static final String TAG = "StepCounterService";
    public static Boolean FLAG = false;// 服務運行標誌

    private SensorManager mSensorManager;// 傳感器服務
    public StepDetector detector;// 傳感器監聽對象

    private PowerManager mPowerManager;// 電源管理服務
    private WakeLock mWakeLock;// 屏幕燈
    private AlarmManager alarmManager;//鬧鐘管理器
    private PendingIntent pendingIntent;//延遲意圖
    private Calendar calendar;//日期
    private Intent intent;//意圖

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "SERVICE START");
        FLAG = true;// 標記為服務正在運行

        // 創建監聽器類，實例化監聽對象
        detector = new StepDetector(this);//實例化傳感器對象
        detector.walk = 1;//設置步數從一開始
        // 獲取傳感器的服務，初始化傳感器
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        // 註冊傳感器，註冊監聽器
        mSensorManager.registerListener(detector,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        // 電源管理服務
        mPowerManager = (PowerManager) this
                .getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "S");
        //保持設備狀態
        mWakeLock.acquire();

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
}
