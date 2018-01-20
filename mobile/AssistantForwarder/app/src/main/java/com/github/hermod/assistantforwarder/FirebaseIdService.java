package com.github.hermod.assistantforwarder;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseIdService extends FirebaseInstanceIdService {
    private static String token;

    public static String getToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        token = FirebaseInstanceId.getInstance().getToken();
        System.out.println("Refreshed token: " + token);
    }
}
