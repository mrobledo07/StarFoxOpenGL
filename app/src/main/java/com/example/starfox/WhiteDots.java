package com.example.starfox;

import javax.microedition.khronos.opengles.GL10;

public class WhiteDots {
    private float scale = 1.0f;

    public void draw(GL10 gl) {
        // Row of whitedots
        gl.glColor4f(1,1,1,1);
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 15; j++) {

            }
        }
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }
}
