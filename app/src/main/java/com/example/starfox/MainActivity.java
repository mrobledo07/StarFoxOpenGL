package com.example.starfox;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private MyOpenGLRenderer renderer;
    private MediaPlayer mediaPlayer;
    private float previousX, previousY;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView view = new GLSurfaceView(this);
        renderer = new MyOpenGLRenderer(this);
        view.setRenderer(renderer);
        setContentView(view);
        mediaPlayer = MediaPlayer.create(this, R.raw.corneria_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        renderer.setAutoMovement(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        float TOUCH_SCALE_FACTOR = 200.0f;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                previousX = x;
                previousY = y;
                renderer.setAutoMovement(false);
                System.out.println("ACTION DOWN");
                break;

            case MotionEvent.ACTION_UP:
                renderer.setAutoMovement(true);
                renderer.unsetRotation();
                renderer.unsetCameraRotation();
                System.out.println("ACTION UP");
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaX = (x - previousX) / TOUCH_SCALE_FACTOR;
                float deltaY = (y - previousY) / TOUCH_SCALE_FACTOR;
                deltaY = -deltaY;
                renderer.moveObject(deltaX, deltaY);
                previousX = x;
                previousY = y;
                System.out.println("ACTION MOVE");
                break;
        }
        return true;
    }
}