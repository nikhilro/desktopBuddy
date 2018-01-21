package com.github.hermod.assistantforwarder;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AngleSensor implements SensorEventListener {
    private final float[] mBackGroundAngles = new float[3];
    private final float[] mOrientationAngles = new float[3];
    private final float[] mAdjustedAngles = new float[3];
    private SensorManager sensorManager; // Sensor manager
    private Sensor orientation;
    private int calibrationCount;

    public void start() {
        sensorManager = (SensorManager)BogusApplication.getContext().getSystemService(Context.SENSOR_SERVICE);
        orientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, this.orientation, SensorManager.SENSOR_DELAY_FASTEST);
        (new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                calibrate();
            }
        }, 1000);
    }

    public void calibrate() {
        final Timer timer = new Timer();
        final Timer cancelTimer = new Timer();
        calibrationCount = 1;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mBackGroundAngles[0] = (mBackGroundAngles[0]*(calibrationCount-1) + mOrientationAngles[0])/calibrationCount;
                mBackGroundAngles[1] = (mBackGroundAngles[1]*(calibrationCount-1) + mOrientationAngles[1])/calibrationCount;
                mBackGroundAngles[2] = (mBackGroundAngles[2]*(calibrationCount-1) + mOrientationAngles[2])/calibrationCount;
                calibrationCount++;
            }
        }, 0, 10);
        cancelTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
            }
        }, 1000);
    }

    public void onSensorChanged(SensorEvent event) {
        mOrientationAngles[0] = event.values[0];
        mOrientationAngles[1] = event.values[1];
        mOrientationAngles[2] = event.values[2];
        mAdjustedAngles[0] = event.values[0] - mBackGroundAngles[0];
        mAdjustedAngles[1] = event.values[1] - mBackGroundAngles[1];
        mAdjustedAngles[2] = event.values[2] - mBackGroundAngles[2];
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    public float[] getReadings() { return this.mAdjustedAngles; }
}