package com.github.hermod.assistantforwarder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

public class BogusActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        startService(new Intent(this, ForwardService.class));
        startService(new Intent(this, FirebaseIdService.class));
        startService(new Intent(this, FirebaseService.class));
        System.out.println(FirebaseIdService.getToken());
        finish();
    }
}