import java.util.List;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Magnetometer implements SensorEventListener  {

    public static int ERROR = -1;
    public static int STOPPED = 0;
    public static int STARTING = 1;
    public static int RUNNING = 2;

    public long TIMEOUT = 30000;        // in milliseconds

    int status;                         
    float x;                           
    float y;                            
    float z;                            
    long timeStamp;                     

    private SensorManager sensorManager;// Sensor manager
    Sensor magneticSensor;                     // Magnetic sensor returned by sensor manager

    public Magnetometer() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.timeStamp = 0;
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

        @SuppressWarnings("deprecation")
        List<Sensor> list = this.sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);

        // Register as listener if found
        if (list != null && list.size() > 0) {
            this.magneticSensor = list.get(0);
            this.sensorManager.registerListener(this, this.magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
            this.setStatus(Magnetometer.STARTING);
        }
        else {
            this.setStatus(Magnetometer.ERROR);
        }

        return this.status;
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
            this.setStatus(Magnetometer.ERROR_FAILED_TO_START);
        }
    }

    /**
     * Sensor listener event to update readings
     *
     * @param event
     */
    public void onSensorChanged(SensorEvent event) {
        this.timeStamp = System.currentTimeMillis();
        this.x = event.values[0];
        this.y = event.values[1];
        this.z = event.values[2];
    }

    /**
     * Required by SensorEventListener
     * @param sensor
     * @param accuracy
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }