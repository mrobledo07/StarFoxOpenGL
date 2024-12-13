package com.example.starfox;

public class Shield extends Texture {
    public Shield() {
        float[] vertices = {
                -4.75f, -3.75f, 0.0f,  // 0. left-bottom
                -3.0f, -3.75f, 0.0f,  // 1. right-bottom
                -4.75f,  -2.75f, 0.0f,  // 2. left-top
                -3.0f,  -2.75f, 0.0f   // 3. right-top
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
