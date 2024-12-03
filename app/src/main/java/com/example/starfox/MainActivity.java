package com.example.starfox;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private MyOpenGLRenderer renderer;
    private MediaPlayer mediaPlayer;
    private float previousX, previousY;
    private final float TOUCH_SCALE_FACTOR_X = 5.0f;
    private final float TOUCH_SCALE_FACTOR_Y = 10.0f;

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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                previousX = x;
                previousY = y;
                renderer.setAutoMovement(false);
                break;

            case MotionEvent.ACTION_UP:
                renderer.setAutoMovement(true);
                break;

            case MotionEvent.ACTION_MOVE:
                float aspect = (float) renderer.getWidth() / renderer.getHeight();
                float deltaX, deltaY;
                if (aspect > 1.0f) {
                    deltaX = (x - previousX) / renderer.getWidth() * TOUCH_SCALE_FACTOR_Y * 3.0f;
                    deltaY = (y - previousY) / renderer.getHeight() * TOUCH_SCALE_FACTOR_X * 2.0f;
                } else {
                    deltaX = (x - previousX) / renderer.getWidth() * TOUCH_SCALE_FACTOR_X;
                    deltaY = (y - previousY) / renderer.getHeight() * TOUCH_SCALE_FACTOR_Y;
                }


                deltaY = -deltaY;

                renderer.moveObject(deltaX, deltaY);
                previousX = x;
                previousY = y;
                break;
        }
        return true;
    }
}