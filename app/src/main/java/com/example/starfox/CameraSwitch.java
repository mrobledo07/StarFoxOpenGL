package com.example.starfox;

public class CameraSwitch extends Texture{
    public CameraSwitch() {
        float[] vertices = {
                4.5f, 3.5f, 0.0f,  // 0. left-bottom
                5.0f, 3.5f, 0.0f,  // 1. right-bottom
                4.5f,  4.0f, 0.0f,  // 2. left-top
                5.0f,  4.0f, 0.0f   // 3. right-top
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
