package com.example.starfox;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.media.MediaPlayer;

import java.util.Timer;
import java.util.TimerTask;

public class DialogueBox extends Texture {
    private Texture[] boxTextures;
    private Texture[][] charactersTextures;
    private Texture[] dialoguesTextures;
    private int[] dialoguesAudio;
    private int currentTextureIndex;
    private int currentCharacterIndex;
    private int currentCharacterImage;
    private final float[] verticesBox;
    private final float[] texCoordsBox;
    private final float[] verticesDialogue;
    private final float[] texCoordsDialogue;
    private boolean isVisible;
    private boolean isCharacterTalking;
    private final Timer timer;
    private TimerTask textureTask;
    private TimerTask foxTask;
    private MediaPlayer mediaPlayer;
    private Context context;


    public DialogueBox() {
        this.verticesBox = new float[]{
                -2.75f, -3.75f, 0.0f,  // 0. left-bottom
                -1.0f, -3.75f, 0.0f,  // 1. right-bottom
                -2.75f,  -1.25f, 0.0f,  // 2. left-top
                -1.0f,  -1.25f, 0.0f   // 3. right-top
        };

        this.texCoordsBox = new float[]{
                0.0f, 1.0f,  // A. left-bottom
                1.0f, 1.0f,  // B. right-bottom
                0.0f, 0.0f,  // C. left-top
                1.0f, 0.0f   // D. right-top
        };

        this.verticesDialogue = new float[]{
                -0.5f, -6.5f, 0.0f,  // 0. left-bottom
                2.75f, -6.5f, 0.0f,  // 1. right-bottom
                -0.5f,  -0.5f, 0.0f,  // 2. left-top
                2.75f,  -0.5f, 0.0f   // 3. right-top
        };

        this.texCoordsDialogue = new float[]{ // Texture coords
                0.0f, 1.0f,  // A. left-bottom
                1.0f, 1.0f,  // B. right-bottom
                0.0f, 0.0f,  // C. left-top
                1.0f, 0.0f   // D. right-top
        };

        this.isVisible = false;
        this.isCharacterTalking = false;
        this.timer = new Timer();
    }


    public void loadTexture(GL10 gl, Context context) {
        this.context = context;
        int[] textureResources = {R.raw.dialogue_box_0, R.raw.dialogue_box_1, R.raw.dialogue_box_2,
                R.raw.dialogue_box_3, R.raw.dialogue_box_4, R.raw.dialogue_box_5};

        int[][] charactersResources = {
                {R.raw.fox0, R.raw.fox1},
                {R.raw.falcon0, R.raw.falcon1},
                {R.raw.rabbit0, R.raw.rabbit1},
        };

        int[] dialoguesResources = {
                R.raw.barrel_roll,
                R.raw.move_faster,
                R.raw.obstacles_detected
        };

        int[] dialoguesAudio = {
                R.raw.dialogue_audio_fox,
                R.raw.dialogue_audio_falcon,
                R.raw.dialogue_audio_rabbit
        };

        boxTextures = new Texture[textureResources.length];
        for (int i = 0; i < textureResources.length; i++) {
            boxTextures[i] = new Texture();
            boxTextures[i].loadTexture(gl, context, textureResources[i]);
            boxTextures[i].setBuffers(verticesBox, texCoordsBox);
        }

        charactersTextures = new Texture[charactersResources.length][];
        for (int i = 0; i < charactersTextures.length; i++) {
            charactersTextures[i] = new Texture[charactersResources[i].length];
            for (int j = 0; j < charactersTextures[i].length; j++) {
                charactersTextures[i][j] = new Texture();
                charactersTextures[i][j].loadTexture(gl, context, charactersResources[i][j]);
                charactersTextures[i][j].setBuffers(verticesBox, texCoordsBox);
            }
        }

        dialoguesTextures = new Texture[dialoguesResources.length];
        for (int i = 0; i < dialoguesTextures.length; i++) {
            dialoguesTextures[i] = new Texture();
            dialoguesTextures[i].loadTexture(gl, context, dialoguesResources[i]);
            dialoguesTextures[i].setBuffers(verticesDialogue, texCoordsDialogue);
        }

        this.dialoguesAudio = dialoguesAudio;
        currentCharacterImage = 0;
        currentCharacterIndex = 0;
        currentTextureIndex = 0;
        this.mediaPlayer = MediaPlayer.create(context, dialoguesAudio[currentCharacterIndex]);
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

    private void startCharacterTextureTransition() {
        if (foxTask != null) {
            foxTask.cancel();
        }
        isCharacterTalking = true;
        foxTask = new TimerTask() {
            @Override
            public void run() {
                nextCharacterTexture();
            }
        };
        timer.schedule(foxTask, 0, 50);
    }

    private void scheduleDialogueBoxOff() {
        // Stop Fox talking 2 seconds before the DialogueBox disappears
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isCharacterTalking = false;
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

                if (currentCharacterIndex < charactersTextures.length - 1) {
                    currentCharacterIndex++;
                } else {
                    currentCharacterIndex = 0;
                }

                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    // update mediaplayer with the next character audio
                    mediaPlayer = MediaPlayer.create(context, dialoguesAudio[currentCharacterIndex]);
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
                isCharacterTalking = true;
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
                startTextureTransition(false);
                startCharacterTextureTransition();
                scheduleDialogueBoxOff();
            }
        }, 5000);
    }

   @Override
    public void draw(GL10 gl) {
        if (!isVisible) return;

        if (currentTextureIndex < boxTextures.length) {
            boxTextures[currentTextureIndex].draw(gl);
        } else {
            dialoguesTextures[currentCharacterIndex].draw(gl);
            charactersTextures[currentCharacterIndex][currentCharacterImage].draw(gl);
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

    public void nextCharacterTexture() {
        if (!isVisible || !isCharacterTalking) return;

        if (currentCharacterImage <  charactersTextures[currentCharacterIndex].length - 1) {
            currentCharacterImage++;
        } else {
            currentCharacterImage = 0;
        }

    }
}