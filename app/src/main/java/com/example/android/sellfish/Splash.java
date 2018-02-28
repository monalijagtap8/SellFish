package com.example.android.sellfish;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    ImageView iv;
    SharedPreferences sp;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        iv = findViewById(R.id.iv);

        Animation anim =  AnimationUtils.loadAnimation(this,R.anim.splash_anim);

        iv.setAnimation(anim);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);

        // final Intent i = new Intent(this,HomeActivity.class);

        Thread timer = new Thread()
        {
            public void run()
            {
                try {
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally {
                   /* startActivity(i);
                    finish();*/
                    if (sp.getBoolean("LOGGED_IN", false)) {
                        intent = new Intent(Splash.this, HomeActivity.class);
                        finish();
                        startActivity(intent);
                    }
                    else
                    {
                        intent = new Intent(Splash.this, TabActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }
            }
        };
        timer.start();

    }
}
