package com.github.hermod.assistantforwarder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;

public class ForwardService extends Service {
    private Magnetometer magnetometer;
    private Timer timer;
    private DatagramSocket socket;
    private InetAddress dest;
    private int destPort = 12345;

    public boolean running;
    public static ForwardService sInstance; // Forgive me, for I have sinned

    protected class MagnetometerTask extends TimerTask {
        private ForwardService parent;
        public MagnetometerTask(ForwardService parent) {
            this.parent = parent;
        }
        @Override
        public void run() {
            parent.sendPosition();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private byte[] serializeData(byte command, Magnetometer.Vector field) {
        return ByteBuffer.allocate(25).order(ByteOrder.LITTLE_ENDIAN).put(command).
                putDouble(field.x).putDouble(field.y).putDouble(field.z).array();
    }

    private void sendData(byte[] data) {
        System.out.println(data);
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, dest, destPort);
        try {
            socket.send(sendPacket);
        }
        catch(Exception e) {
            System.out.println("UDP send failed");
        }
    }

    public void sendClick(boolean right) {
        // Click (1), repetitions (1), left/right (0/1)
        this.sendData(ByteBuffer.allocate(3).put((byte)1).put((byte)1).put(right? (byte)1 : (byte)0).array());
    }

    public void sendKey(String keyname) {
        byte[] bKeyname = keyname.getBytes();
        // Key (7), keyname (bytes[])
        this.sendData(ByteBuffer.allocate(bKeyname.length + 1).put((byte)7).put(bKeyname).array());
    }

    public void sendText(String text) {
        byte[] bText = text.getBytes();
        this.sendData(ByteBuffer.allocate(bText.length + 1).put((byte)6).put(bText).array());
    }

    public void sendPosition() {
        byte[] sendData = serializeData((byte)0, magnetometer.getPosition());
        System.out.println(magnetometer.toString());
    }

    public void pause() {
        running = false;
        timer.cancel();
    }

    public void start() {
        running = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new MagnetometerTask(this), 1000, 100);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            magnetometer = new Magnetometer();
            magnetometer.start();
            socket = new DatagramSocket(destPort);
            dest = InetAddress.getByName("100.64.135.133");
            ForwardService.sInstance = this;
            this.start();
        }
        catch (Exception e) {System.out.println(e);}
    }
}