package com.example.bedms;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.bedms.Auth.MainActivity;
import com.example.bedms.Auth.welcome;


public class SplashScreen extends Activity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this, welcome.class);
                startActivity(intent);
                BedCache.SetupCache();
                ConditionCache.SetupCache();
                finish();
            }
        },2700);

    }
}
