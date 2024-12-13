package com.example.starfox;

public class CaptainPrice extends Texture{
    public CaptainPrice() {
        float[] vertices = new float[]{
                -150f, -45f, 0.0f, // A. left-bottom
                0.0f, -45f, 0.0f,  // B. right-bottom
                -150f, 45f, 0.0f,  // C. left-top
                0.0f, 45f, 0.0f    // D. right-top
        };
        float[] texCoords = new float[]{ // Texture coords
                0.0f, 1.0f,  // A. left-bottom (NEW)
                1.0f, 1.0f,  // B. right-bottom (NEW)
                0.0f, 0.0f,  // C. left-top (NEW)
                1.0f, 0.0f   // D. right-top (NEW)
        };
        super.setBuffers(vertices, texCoords);
    }
}
