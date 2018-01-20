package com.github.hermod.assistantforwarder;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Magnetometer implements SensorEventListener {
    public class Vector {
        public double x, y, z;
        public Vector() {this.x = 0; this.y = 0; this.z = 0;}
    }

    public static int ERROR = -1;
    public static int STOPPED = 0;
    public static int STARTING = 1;
    public static int RUNNING = 2;

    private long TIMEOUT = 30000;        // in milliseconds

    private int status;
    private int calibrationCount;
    private Vector rawField;
    private Vector adjustedField;
    private Vector backgroundField;
    private Vector position;
    private SensorManager sensorManager; // Sensor manager
    private Sensor magneticSensor;               // Magnetic sensor returned by sensor manager

    public Magnetometer() {
        this.adjustedField = new Vector();
        this.rawField = new Vector();
        this.backgroundField = new Vector();
        this.position = new Vector();
        this.setStatus(Magnetometer.STOPPED);
    }

    public void onDestroy() {
        this.stop();
    }

    public void onReset() {
        this.stop();
    }

    /**
     * Start listening
     *
     * @return status of listener
     */
    public int start() {
        if ((this.status == Magnetometer.RUNNING) || (this.status == Magnetometer.STARTING)) {
            return this.status;
        }

        this.sensorManager = (SensorManager)BogusApplication.getContext().getSystemService(Context.SENSOR_SERVICE);
        this.magneticSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Register as listener if found
        if (this.magneticSensor != null) {
            this.sensorManager.registerListener(this, this.magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
            this.setStatus(Magnetometer.STARTING);
        } else {
            System.out.println("No magnetometer");
        }

        this.calibrate();

        return this.status;
    }

    private void calibrate() {
        final Timer timer = new Timer();
        final Timer cancelTimer = new Timer();
        calibrationCount = 1;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                backgroundField.x = (backgroundField.x*(calibrationCount-1) + rawField.x)/calibrationCount;
                backgroundField.y = (backgroundField.y*(calibrationCount-1) + rawField.y)/calibrationCount;
                backgroundField.z = (backgroundField.z*(calibrationCount-1) + rawField.z)/calibrationCount;
                calibrationCount++;
            }
        }, 0, 10);
        cancelTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
            }
        }, 5000);
    }

    /**
     * Stop listening to magnetic field sensor
     */
    public void stop() {
        if (this.status != Magnetometer.STOPPED) {
            this.sensorManager.unregisterListener(this);
        }
        this.setStatus(Magnetometer.STOPPED);
    }

    /**
     * Called after a delay to timeout if the listener has not attached
     */
    private void timeout() {
        if (this.status == Magnetometer.STARTING) {
            this.setStatus(Magnetometer.ERROR);
        }
    }

    /**
     * Sensor listener event to update readings
     *
     * @param event
     */
    public void onSensorChanged(SensorEvent event) {
        this.rawField.x = event.values[0];
        this.rawField.y = event.values[1];
        this.rawField.z = event.values[2];
        this.adjustedField.x = this.rawField.x - this.backgroundField.x;
        this.adjustedField.y = this.rawField.y - this.backgroundField.y;
        this.adjustedField.z = this.rawField.z - this.backgroundField.z;
    }

    /**
     * Required by SensorEventListener
     *
     * @param sensor
     * @param accuracy
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Vector getNormalizedReadings() {
        return this.adjustedField;
    }

    public Vector getPosition() {
        double dist = Math.sqrt(Math.pow(adjustedField.x, 2) + Math.pow(adjustedField.y, 2) + Math.pow(adjustedField.z, 2));
        if (dist == 0)
            return position;
        double cbdist = 1/Math.cbrt(Math.pow(adjustedField.x, 2) + Math.pow(adjustedField.y, 2) + Math.pow(adjustedField.z, 2));
        position.x = Math.cos(adjustedField.x/dist)*cbdist;
        position.y = Math.cos(adjustedField.y/dist)*cbdist;
        position.z = Math.cos(adjustedField.z/dist)*cbdist;
        return position;
    }

    @Override
    public String toString() {
        return String.format("x: %f, y: %f, z: %f", adjustedField.x, adjustedField.y, adjustedField.z);
    }
}