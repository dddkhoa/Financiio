package com.example.financiio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.Runnable;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH = 2000;
    Animation animation;
    private ImageView logo;
    private TextView banner, bannerDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        logo = (ImageView) findViewById(R.id.logo);
        banner = (TextView) findViewById(R.id.banner);
        bannerDescription = (TextView) findViewById(R.id.bannerDescription);

        logo.setAnimation(animation);
        banner.setAnimation(animation);
        bannerDescription.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                SplashScreenActivity.this.finish();
            }
        }, SPLASH);
    }
}