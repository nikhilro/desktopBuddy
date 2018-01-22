package com.github.hermod.assistantforwarder;

import android.content.Intent;
import android.content.ServiceConnection;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Map;

public class FirebaseService extends FirebaseMessagingService {
    private final static String ACTIVATE = "mouse.activate";
    private final static String DEACTIVATE = "mouse.deactivate";
    private final static String LEFTCLICK = "mouse.leftclick";
    private final static String RIGHTCLICK = "mouse.rightclick";
    private final static String DOUBLECLICK = "mouse.doubleclick";
    private final static String CALIBRATE = "mouse.calibrate";
    private final static String ENTER = "keyboard.enter";
    private final static String TYPE = "keyboard.type";
    private final static String PAGEUP = "keyboard.pageup";
    private final static String PAGEDOWN = "keyboard.pagedown";
    private final static String SWITCHTAB = "keyboard.switchtab";
    private final static String CLOSE = "keyboard.close";
    private final static String FIND = "keyboard.find";
    private final static String UNDO = "keyboard.undo";
    private final static String ESCAPE = "keyboard.escape";
    private final static String FULLSCREEN = "keyboard.fullscreen";
    private final static String BACK = "browser.back";
    private final static String OPEN = "browser.open";
    private final static String NEWTAB = "browser.newtab";
    private final static String DRIVE = "browser.drive";
    private final static String GMAIL = "browser.gmail";
    private final static String PAUSE = "media.pause";
    private final static String PLAY = "media.play";
    private final static String SKIP = "media.skip";
    private final static String VOLUMEUP = "media.volumeup";
    private final static String VOLUMEDOWN = "media.pageup";
    private final static String CONTROL = "take.control";

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
                    forwardService.sendClick(false, false);
                    break;
                case RIGHTCLICK:
                    forwardService.sendClick(true, false);
                    break;
                case DOUBLECLICK:
                    forwardService.sendClick(false, true);
                    break;
                case CALIBRATE:
                    forwardService.calibrate();
                    break;
                case ENTER:
                    forwardService.sendKey("enter");
                    break;
                case ESCAPE:
                    forwardService.sendKey("esc");
                    break;
                case FULLSCREEN:
                    forwardService.sendHotkey("fullscreen");
                    break;
                case CLOSE:
                    forwardService.sendHotkey("close");
                    break;
                case TYPE:
                    forwardService.sendText(data.get("typed"));
                    break;
                case PAGEUP:
                    forwardService.sendKey("pgup");
                    break;
                case FIND:
                    forwardService.sendHotkey("find");
                    break;
                case SWITCHTAB:
                    forwardService.sendHotkey("switchtab");
                    break;
                case UNDO:
                    forwardService.sendHotkey("undo");
                    break;
                case PAGEDOWN:
                    forwardService.sendKey("pgdn");
                    break;
                case BACK:
                    forwardService.sendKey("browserback");
                    break;
                case OPEN:
                case DRIVE:
                case GMAIL:
                case NEWTAB:
                    forwardService.sendApplication(data.get("action"));
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
                case CONTROL:
                    String target = data.get("target").replace("'s", "").toLowerCase();
                    try {
                        if (target.equals("edwin") || target.equals("my")) {
                            forwardService.changeIP(InetAddress.getByName("100.64.135.133"));
                        } else if (target.equals("callum") || target.equals("calum")) {
                            forwardService.changeIP(InetAddress.getByName("100.64.130.123"));
                        } else if (target.equals("nikhil")) {
                            forwardService.changeIP(InetAddress.getByName("100.64.89.4"));
                        } else if (target.startsWith("d")) {
                            forwardService.changeIP(InetAddress.getByName("100.64.135.132"));
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
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
