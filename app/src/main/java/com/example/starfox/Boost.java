package com.example.starfox;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;

public class Boost extends Texture {
    private final BoostBar boostBar;
    private float boostAmount = 1.0f; // Full boost

    public Boost() {
        float[] vertices = {
                2.75f, -3.75f, 0.0f,  // 0. left-bottom
                4.75f, -3.75f, 0.0f,  // 1. right-bottom
                2.75f,  -2.75f, 0.0f,  // 2. left-top
                4.75f,  -2.75f, 0.0f   // 3. right-top
        };

        float[] texCoords = new float[]{ // Texture coords
                0.0f, 1.0f,  // A. left-bottom
                1.0f, 1.0f,  // B. right-bottom
                0.0f, 0.0f,  // C. left-top
                1.0f, 0.0f   // D. right-top
        };

        super.setBuffers(vertices, texCoords);
        this.boostBar = new BoostBar();
    }

    @Override
    public void loadTexture(GL10 gl, Context context, int idTexture) {
        super.loadTexture(gl, context, idTexture);
        boostBar.loadTexture(gl, context, R.raw.boost_bar);
    }

    @Override
    public void draw(GL10 gl) {
        super.draw(gl);
        boostBar.draw(gl);
    }

    public void accelerate(float boostVal) {
        boostAmount = Math.max(0.0f, Math.min(1.0f, boostAmount + boostVal));
        float[] vertices = {
                3.0f, -3.58f, 0.0f,  // 0. left-bottom
                3.0f + (boostAmount * 1.556f), -3.58f, 0.0f,  // 1. right-bottom
                3.0f,  -3.4f, 0.0f,  // 2. left-top
                3.0f + (boostAmount * 1.556f),  -3.4f, 0.0f   // 3. right-top
        };
        boostBar.setBuffers(vertices, boostBar.getTexCoords());
    }

    private static class BoostBar extends Texture {
        private float[] vertices;
        private float[] texCoords;

        public BoostBar() {
            vertices = new float[]{
                    3.0f, -3.58f, 0.0f,  // 0. left-bottom
                    4.556f, -3.58f, 0.0f,  // 1. right-bottom
                    3.0f,  -3.4f, 0.0f,  // 2. left-top
                    4.556f,  -3.4f, 0.0f   // 3. right-top
            };

            texCoords = new float[]{ // Texture coords
                    0.0f, 1.0f,  // A. left-bottom
                    1.0f, 1.0f,  // B. right-bottom
                    0.0f, 0.0f,  // C. left-top
                    1.0f, 0.0f   // D. right-top
            };

            this.setBuffers(vertices, texCoords);
        }

        public void setBuffers(float[] vertices, float[] texCoords) {
            this.vertices = vertices;
            this.texCoords = texCoords;
            super.setBuffers(vertices, texCoords);
        }

        public float[] getVertices() {
            return vertices;
        }

        public float[] getTexCoords() {
            return texCoords;
        }
    }
}