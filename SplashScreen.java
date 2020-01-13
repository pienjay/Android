package com.esenpi.pienjay.dotdot;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {
    VideoView video;
    String videoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video);
        video=(VideoView) findViewById(R.id.videoView);
        videoPath = "android.resource://" + getPackageName() + "/" + R.raw.splash;
        video.setVideoURI(Uri.parse(videoPath));
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                startNextActivity();
            }
        });
        video.start();
    }
    private void startNextActivity()
    {
        if (isFinishing())
            return;
        startActivity(new Intent(this, Home.class));
        finish();
    }
}
