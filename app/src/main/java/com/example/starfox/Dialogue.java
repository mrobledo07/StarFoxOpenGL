package com.example.starfox;

public class Dialogue extends Texture{
    private int[] textures;
    public Dialogue() {
        float[] vertices = {
                -0.5f, -6.5f, 0.0f,  // 0. left-bottom
                2.75f, -6.5f, 0.0f,  // 1. right-bottom
                -0.5f,  -0.5f, 0.0f,  // 2. left-top
                2.75f,  -0.5f, 0.0f   // 3. right-top
        };

        float[] texCoords = new float[]{ // Texture coords
                0.0f, 1.0f,  // A. left-bottom
                1.0f, 1.0f,  // B. right-bottom
                0.0f, 0.0f,  // C. left-top
                1.0f, 0.0f   // D. right-top
        };

        super.setBuffers(vertices, texCoords);
    }

}
