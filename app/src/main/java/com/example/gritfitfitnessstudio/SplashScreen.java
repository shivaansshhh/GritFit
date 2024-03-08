package com.example.gritfitfitnessstudio;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    ImageView logo;
    TextView gritFit;
    long animTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        to set this activity to full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

//        to create animation on objects

        ObjectAnimator animatorLogo = ObjectAnimator.ofFloat(logo,"y",400f);
        ObjectAnimator animatorName = ObjectAnimator.ofFloat(gritFit,"x", 400f);
//        set the duration of animation
        animatorLogo.setDuration(animTime);
        animatorName.setDuration(animTime);

//        to play both animation together
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorLogo,animatorName);
        animatorSet.start();

//        handler to skip this activity after some second
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }
    private void init(){
        logo = findViewById(R.id.iv_logo_splash);
        gritFit = findViewById(R.id.tv_splash_name);
    }
}