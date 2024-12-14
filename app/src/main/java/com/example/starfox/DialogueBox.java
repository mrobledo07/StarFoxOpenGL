package com.example.starfox;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.media.MediaPlayer;

import java.util.Timer;
import java.util.TimerTask;

public class DialogueBox extends Texture {
    private Texture[] boxTextures;
    private Texture[] foxTextures;
    private int currentTextureIndex;
    private int currentFoxIndex;
    private final float[] vertices;
    private final float[] texCoords;
    private boolean isVisible;
    private boolean isFoxTalking;
    private final Timer timer;
    private TimerTask textureTask;
    private TimerTask foxTask;
    private MediaPlayer mediaPlayer;
    private Texture dialogue;


    public DialogueBox() {
        this.vertices = new float[]{
                -2.75f, -3.75f, 0.0f,  // 0. left-bottom
                -1.0f, -3.75f, 0.0f,  // 1. right-bottom
                -2.75f,  -1.25f, 0.0f,  // 2. left-top
                -1.0f,  -1.25f, 0.0f   // 3. right-top
        };

        this.texCoords = new float[]{
                0.0f, 1.0f,  // A. left-bottom
                1.0f, 1.0f,  // B. right-bottom
                0.0f, 0.0f,  // C. left-top
                1.0f, 0.0f   // D. right-top
        };

        this.isVisible = false;
        this.isFoxTalking = false;
        this.timer = new Timer();
    }


    public void loadTexture(GL10 gl, Context context, int[] textureResources, int[] foxTextureResources, Texture dialogue) {
        boxTextures = new Texture[textureResources.length];
        this.dialogue = dialogue;
        for (int i = 0; i < textureResources.length; i++) {
            boxTextures[i] = new Texture();
            boxTextures[i].loadTexture(gl, context, textureResources[i]);
            boxTextures[i].setBuffers(vertices, texCoords);
        }
        foxTextures = new Texture[foxTextureResources.length];
        for (int i = 0; i < foxTextureResources.length; i++) {
            foxTextures[i] = new Texture();
            foxTextures[i].loadTexture(gl, context, foxTextureResources[i]);
            foxTextures[i].setBuffers(vertices, texCoords);
        }
        currentTextureIndex = 0;
        currentFoxIndex = 0;
        mediaPlayer = MediaPlayer.create(context, R.raw.dialogue_audio_fox);
        scheduleDialogueBoxOn();
    }

    private void startTextureTransition(boolean reverse) {
        if (textureTask != null) {
            textureTask.cancel();
        }
        textureTask = new TimerTask() {
            @Override
            public void run() {
                if (!reverse) {
                    nextTexture();
                } else {
                    previousTexture();
                }
            }
        };
        timer.schedule(textureTask, 0, 150);
    }

    private void startFoxTextureTransition() {
        if (foxTask != null) {
            foxTask.cancel();
        }
        isFoxTalking = true;
        foxTask = new TimerTask() {
            @Override
            public void run() {
                nextFoxTexture();
            }
        };
        timer.schedule(foxTask, 0, 50);
    }

    private void scheduleDialogueBoxOff() {
        // Stop Fox talking 2 seconds before the DialogueBox disappears
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isFoxTalking = false;
                currentFoxIndex = 0;
            }
        }, 1500);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                disappearBoxAnimation();
            }
        }, 5000);
    }

    private void disappearBoxAnimation() {
        currentTextureIndex = boxTextures.length - 1;
        startTextureTransition(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isVisible = false;
                currentTextureIndex = 0;
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                }
                scheduleDialogueBoxOn();
            }
        }, 750);
    }

    private void scheduleDialogueBoxOn() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isVisible = true;
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
                startTextureTransition(false);
                startFoxTextureTransition();
                scheduleDialogueBoxOff();
            }
        }, 10000);
    }

   @Override
    public void draw(GL10 gl) {
        if (!isVisible) return;

        if (currentTextureIndex < boxTextures.length) {
            boxTextures[currentTextureIndex].draw(gl);
        } else {
            dialogue.draw(gl);
            if (currentFoxIndex < foxTextures.length)
                foxTextures[currentFoxIndex].draw(gl);
        }
    }

    public void nextTexture() {
        if (!isVisible) return;

        if (currentTextureIndex < boxTextures.length - 1) {
            currentTextureIndex++;
        } else {
            currentTextureIndex = boxTextures.length;
        }
    }

    public void previousTexture() {
        if (!isVisible) return;

        if (currentTextureIndex > 0) {
            currentTextureIndex--;
        } else {
            currentTextureIndex = 0;
        }
    }

    public void nextFoxTexture() {
        if (!isVisible || !isFoxTalking) return;

        if (currentFoxIndex < foxTextures.length - 1) {
            currentFoxIndex++;
        } else {
            currentFoxIndex = 0;
        }

    }
}