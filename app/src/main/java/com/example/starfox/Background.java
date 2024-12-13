package com.example.starfox;

public class Background extends Texture {
    public Background() {
        float[] vertices = new float[]{
                -60f, -45f, 0.0f, // A. left-bottom
                60f, -45f, 0.0f,  // B. right-bottom
                -60f, 45f, 0.0f,  // C. left-top
                60f, 45f, 0.0f    // D. right-top
        };
        float[] texCoords = new float[]{ // Texture coords
                0.0f, 1.0f,  // A. left-bottom (NEW)
                1.0f, 1.0f,  // B. right-bottom (NEW)
                0.0f, 0.35f,  // C. left-top (NEW)
                1.0f, 0.35f   // D. right-top (NEW)
        };
        super.setBuffers(vertices, texCoords);
    }
}
