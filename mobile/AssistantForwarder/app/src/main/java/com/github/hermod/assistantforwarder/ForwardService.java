package com.github.hermod.assistantforwarder;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;

import java.net.InetAddress;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ForwardService extends Service {
    private Magnetometer magnetometer;
    private Timer timer;
    private Socket socket;
    private PrintWriter writer;

    protected class MagnetometerTask extends TimerTask {
        private ForwardService parent;
        public MagnetometerTask(ForwardService parent) {
            this.parent = parent;
        }
        @Override
        public void run() {
            parent.sendData();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendData() {
        writer.print(magnetometer.toString());
        writer.flush();
        System.out.println(magnetometer.toString());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            magnetometer = new Magnetometer();
            magnetometer.start();
            socket = new Socket(InetAddress.getByName("100.64.135.133"), 10001);
            writer = new PrintWriter(socket.getOutputStream());
            timer = new Timer();
            timer.scheduleAtFixedRate(new MagnetometerTask(this), 100, 100);
        }
        catch (Exception e) {System.out.println(e);}
    }
}
