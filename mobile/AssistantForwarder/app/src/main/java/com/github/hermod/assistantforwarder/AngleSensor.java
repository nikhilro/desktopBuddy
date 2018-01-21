package com.github.hermod.assistantforwarder;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AngleSensor implements SensorEventListener {
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];
    private SensorManager sensorManager; // Sensor manager
    private Sensor accelerometer;
    private Sensor magnetometer;

    public void start() {
        sensorManager = (SensorManager)BogusApplication.getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, this.magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
        }
        else {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    public float[] getReadings() { updateOrientationAngles(); return this.mOrientationAngles; }

    public void updateOrientationAngles() {
        sensorManager.getRotationMatrix(mRotationMatrix, null,
                mAccelerometerReading, mMagnetometerReading);
        sensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
    }
}