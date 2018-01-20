package com.github.hermod.assistantforwarder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage message){
        if (message.getData().size() > 0) {
            System.out.println("Message data payload: " + message.getData());
        }
    }
}
