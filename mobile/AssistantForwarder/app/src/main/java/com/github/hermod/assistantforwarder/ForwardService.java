package com.github.hermod.assistantforwarder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.net.InetAddress;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ForwardService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Socket s = new Socket(InetAddress.getByName("100.64.135.133"), 10001);
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.print("Gib data");
            pw.flush();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
