package com.example.lui_project.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Implementing the algorithm of counting
 * There are related algorithms online
 * The first step-by-step algorithm is calculated based on the X, Y, and Z accelerations of the phone.
 * I am not familiar with related algorithms
 * So use a piece of open source code to complete the algorithm for counting
 */
public class StepDetector implements SensorEventListener {

    public static int CURRENT_SETP = 0;
    public int walk = 0;
    public static float SENSITIVITY = 8; // SENSITIVITY sensitivity

    private float mLastValues[] = new float[3 * 2];
    private float mScale[] = new float[2];
    private float mYOffset;//Displacement
    private static long mEnd = 0;//Movement time interval
    private static long mStart = 0;//Start time of exercise
    private Context context;

    /**
     * Final acceleration direction
     */
    private float mLastDirections[] = new float[3 * 2];//Final direction
    private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;

    /**
     * Constructor passed in context
     *
     * @param context
     */
    public StepDetector(Context context) {
        super();
        this.context = context;
// The value used to determine whether to count
        int h = 480;
        mYOffset = h * 0.5f;
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));//重力加速度
        mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));//地球最大磁場
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            } else {
// Determine whether the type of sensor is a gravity sensor (acceleration sensor)
                int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                if (j == 1) {
                    float vSum = 0;
// Get the acceleration of the x-axis, y-axis, and z-axis
                    for (int i = 0; i < 3; i++) {
                        final float v = mYOffset + event.values[i] * mScale[j];
                        vSum += v;
                    }
                    int k = 0;
                    float v = vSum / 3;//獲取三軸加速度的平均值
// Determine whether the person is walking, mainly from the following aspects:
// If a person walks up, they will usually take a few more steps. Therefore, if there are no 4-5 fluctuations in succession, then it is likely to be interference.
// The fluctuations of people walking are greater than the fluctuations caused by the car, so you can see the height of the wave trough and only detect the crests above a certain height.
// The reflex nerve of a person determines the limit of rapid movement of a person. It is impossible to be less than 0.2 seconds between two steps. Therefore, the wave trough of interval less than 0.2 second directly skips the induction by gravity accelerometer.
// The direction and size of gravity change. Compared with the change of gravity during normal walking or running, it is considered to be walking or running when a certain similarity is reached. It's very simple to implement, as long as the phone has a gravity sensor.
                    float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                    if (direction == -mLastDirections[k]) {
                        int extType = (direction > 0 ? 0 : 1);
                        mLastExtremes[extType][k] = mLastValues[k];
                        float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                        if (diff > SENSITIVITY) {
                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
                            boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
                            boolean isNotContra = (mLastMatch != 1 - extType);

                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                mEnd = System.currentTimeMillis();
                                // Determine whether to take a step by judging the two motion intervals
                                if (mEnd - mStart > 500) {
                                    CURRENT_SETP++;
                                    mLastMatch = extType;
                                    mStart = mEnd;
                                    // Log.e("步數", CURRENT_SETP + "");
                                }
                            } else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[k] = diff;
                    }
                    mLastDirections[k] = direction;
                    mLastValues[k] = v;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}