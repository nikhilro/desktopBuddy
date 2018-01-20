package com.github.hermod.assistantforwarder;

import android.content.Intent;
import android.content.ServiceConnection;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.nio.ByteBuffer;
import java.util.Map;

public class FirebaseService extends FirebaseMessagingService {
    private final static String ACTIVATE = "mouse.activate";
    private final static String DEACTIVATE = "mouse.deactivate";
    private final static String LEFTCLICK = "mouse.leftclick";
    private final static String RIGHTCLICK = "mouse.rightclick";
    private final static String ENTER = "keyboard.enter";
    private final static String TYPE = "keyboard.type";
    private final static String BACK = "browser.back";
    private final static String OPEN = "browser.open";
    private final static String NEWTAB = "browser.newtab";
    private final static String DRIVE = "browser.drive";
    private final static String PAUSE = "media.pause";
    private final static String PLAY = "media.play";
    private final static String SKIP = "media.skip";


    private ForwardService forwardService;

    @Override
    public void onMessageReceived(RemoteMessage message){
        Map<String, String> data = message.getData();
        if (data.size() > 0) {
            switch (data.get("command")) {
                case ACTIVATE:
                    forwardService.start();
                case DEACTIVATE:
                    forwardService.pause();
                case LEFTCLICK:
                    forwardService.sendClick(false);
                case RIGHTCLICK:
                    forwardService.sendClick(true);
                case ENTER:
                    forwardService.sendKey("enter");
                case TYPE:
                    forwardService.sendText(data.get("type"));
                case BACK:
                    forwardService.sendKey("browserback");
                //case OPEN:
                //    forwardService.openChrome();
                case NEWTAB:
                    forwardService.sendKey("ctrl+t");
                //case DRIVE:
                //    forwardService.openURL("https://drive.google.com");
                case PAUSE:
                case PLAY:
                    forwardService.sendKey("playpause");
                case SKIP:
                    forwardService.sendKey("nexttrack");
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        forwardService = ForwardService.sInstance;
    }
}
