package com.example.starfox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
public class HUD {
    private Life life;
    private Shield shield;
    private Boost boost;

    public HUD() {
        life = new Life();
        shield = new Shield();
        boost = new Boost();
    }


    public class Life {
        private final FloatBuffer vertexBuffer; // Buffer for vertex-array
        private final FloatBuffer texBuffer;    // Buffer for texture-coords-array (NEW)
        int[] textureIDs = new int[1];   // Array for 1 texture-ID (NEW)
        private final short[] indices = { 0, 1, 2, 1, 3, 2 };
        private final ShortBuffer indexBuffer;

        public Life() {
            int[] textureId = new int[1];
            float[] vertices = {
                    // X, Y, Z
                    -0.9f, 0.8f, 0.0f,  // Top-left
                    -0.8f, 0.8f, 0.0f,  // Top-right
                    -0.9f, 0.7f, 0.0f,  // Bottom-left
                    -0.8f, 0.7f, 0.0f   // Bottom-right
            };
            float[] texCoords = {
                    // U, V
                    0.0f, 0.0f,  // Top-left
                    1.0f, 0.0f,  // Top-right
                    0.0f, 1.0f,  // Bottom-left
                    1.0f, 1.0f   // Bottom-right
            };

            ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            vertexBuffer = vbb.asFloatBuffer();
            vertexBuffer.put(vertices);
            vertexBuffer.position(0);

            // Setup texture-coords-array buffer, in float. An float has 4 bytes (NEW)
            ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
            tbb.order(ByteOrder.nativeOrder());
            texBuffer = tbb.asFloatBuffer();
            texBuffer.put(texCoords);
            texBuffer.position(0);

            ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
            ibb.order(ByteOrder.nativeOrder());
            indexBuffer = ibb.asShortBuffer();
            indexBuffer.put(indices);
            indexBuffer.position(0);

        }

        public void loadTexture(GL10 gl, Context context, int idTexture) {
            gl.glGenTextures(1, textureIDs, 0); // Generate texture-ID array

            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);   // Bind to texture ID
            // Set up texture filters
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

            // Construct an input stream to texture image "res\drawable\nehe.png"
            InputStream istream = context.getResources().openRawResource(idTexture);

            Bitmap bitmap;
            try {
                // Read and decode input as bitmap
                bitmap = BitmapFactory.decodeStream(istream);
            } finally {
                try {
                    istream.close();
                } catch(IOException e) { }
            }

            // Build Texture from loaded bitmap for the currently-bind texture ID
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
        }

        public void draw(GL10 gl) {
            gl.glDisable(GL10.GL_DEPTH_TEST);
            gl.glDisable(GL10.GL_LIGHTING);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
            gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisable(GL10.GL_TEXTURE_2D);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
            gl.glEnable(GL10.GL_LIGHTING);
            gl.glEnable(GL10.GL_DEPTH_TEST);
        }
    }

    public class Shield {

    }

    public class Boost {

    }

    public Life getLife() {
        return life;
    }
}
