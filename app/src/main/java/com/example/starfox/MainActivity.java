package com.example.starfox;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;


public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private MyOpenGLRenderer renderer;
    private MediaPlayer mediaPlayer;
    private float previousX, previousY;
    private GestureDetector gestureDetector;



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

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                renderer.setBoost();
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityX) > Math.abs(velocityY) && Math.abs(velocityY) < 1175) {
                    if (e2.getX() - e1.getX() > 0) {
                        renderer.doBarrelRoll(true); // Rotate to the right
                    } else {
                        renderer.doBarrelRoll(false); // Rotate to the left
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            renderer.pauseMediaPlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
            renderer.startMediaPlayer();
        }
        renderer.setAutoMovement(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            renderer.destroyMediaPlayer();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private float[] screenToWorldCoordinates(float x, float y, int screenWidth, int screenHeight) {
        float normalizedX = x / screenWidth;
        float normalizedY = y / screenHeight;

        float worldX = normalizedX * (5.0f);
        float worldY = 4.0f - normalizedY * (4.0f);

        return new float[]{worldX, worldY};
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        float TOUCH_SCALE_FACTOR = 200.0f;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float[] worldCoords = screenToWorldCoordinates(x, y, screenWidth, screenHeight);
                if (worldCoords[0] >= 4.5f && worldCoords[0] <= 5.0f &&
                        worldCoords[1] >= 3.5f && worldCoords[1] <= 4.0f) {
                    renderer.switchCamera();
                }
                previousX = x;
                previousY = y;
                renderer.setAutoMovement(false);
                break;

            case MotionEvent.ACTION_UP:
                renderer.setAutoMovement(true);
                renderer.unsetRotation();
                renderer.unsetCameraRotation();
                renderer.unsetBoost();
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaX = (x - previousX) / TOUCH_SCALE_FACTOR;
                float deltaY = (y - previousY) / TOUCH_SCALE_FACTOR;
                deltaY = -deltaY;
                renderer.moveObject(deltaX, deltaY);
                previousX = x;
                previousY = y;
                break;
        }
        return true;
    }
}