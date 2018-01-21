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
    private final static String CALIBARTE = "mouse.calibrate";
    private final static String ENTER = "keyboard.enter";
    private final static String TYPE = "keyboard.type";
    private final static String PAGEUP = "keyboard.pageup";
    private final static String PAGEDOWN = "keyboard.pagedown";
    private final static String BACK = "browser.back";
    private final static String OPEN = "browser.open";
    private final static String NEWTAB = "browser.newtab";
    private final static String DRIVE = "browser.drive";
    private final static String PAUSE = "media.pause";
    private final static String PLAY = "media.play";
    private final static String SKIP = "media.skip";
    private final static String VOLUMEUP = "media.volumeup";
    private final static String VOLUMEDOWN = "media.pageup";


    private ForwardService forwardService;

    @Override
    public void onMessageReceived(RemoteMessage message){
        Map<String, String> data = message.getData();
        if (data.size() > 0) {
            switch (data.get("action")) {
                case ACTIVATE:
                    if(!forwardService.running)
                        forwardService.start();
                    break;
                case DEACTIVATE:
                    if(forwardService.running)
                        forwardService.pause();
                    break;
                case LEFTCLICK:
                    forwardService.sendClick(false);
                    break;
                case RIGHTCLICK:
                    forwardService.sendClick(true);
                    break;
                case CALIBARTE:
                    forwardService.calibrate();
                case ENTER:
                    forwardService.sendKey("enter");
                    break;
                case TYPE:
                    forwardService.sendText(data.get("typed"));
                    break;
                case PAGEUP:
                    forwardService.sendKey("pgup");
                    break;
                case PAGEDOWN:
                    forwardService.sendKey("pgdn");
                    break;
                case BACK:
                    forwardService.sendKey("browserback");
                    break;
                case OPEN:
                    forwardService.sendApplication("chrome");
                case NEWTAB:
                    forwardService.sendShortcut("ctrl+t");
                    break;
                //case DRIVE:
                //    forwardService.openURL("https://drive.google.com");
                case PAUSE:
                case PLAY:
                    forwardService.sendKey("playpause");
                    break;
                case SKIP:
                    forwardService.sendKey("nexttrack");
                    break;
                case VOLUMEUP:
                    forwardService.sendKey("volumeup");
                    break;
                case VOLUMEDOWN:
                    forwardService.sendKey("volumedown");
                    break;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        forwardService = ForwardService.sInstance;
    }
}
