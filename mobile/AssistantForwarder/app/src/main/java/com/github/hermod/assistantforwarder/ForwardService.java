package com.github.hermod.assistantforwarder;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ForwardService extends Service {
    private Magnetometer magnetometer;
    private Timer timer;
    private DatagramSocket socket;
    private InetAddress dest;
    private int destPort = 12345;

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

    private byte[] serializeData(byte command, Magnetometer.Field field) {
        long millis = System.currentTimeMillis();
        return ByteBuffer.allocate(33).order(ByteOrder.LITTLE_ENDIAN).putLong(millis).put(command).putDouble(field.x).putDouble(field.y).putDouble(field.z).array();
    }

    public void sendData() {
        byte[] sendData = serializeData((byte)0, magnetometer.getNormalizedReadings());
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, dest, destPort);
        try {
            socket.send(sendPacket);
        }
        catch(Exception e) {
            System.out.println("UDP send failed");
        }
        System.out.println(magnetometer.toString());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            magnetometer = new Magnetometer();
            magnetometer.start();
            socket = new DatagramSocket(destPort);
            dest = InetAddress.getByName("100.64.135.133");
            timer = new Timer();
            timer.scheduleAtFixedRate(new MagnetometerTask(this), 1000, 100);
        }
        catch (Exception e) {System.out.println(e);}
    }
}