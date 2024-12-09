package com.example.starfox;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class WhiteDots {
    private static List<float[]> groundPoints;
    private static final int ROWS = 5;
    private static final int COLUMNS = 10;
    private static final float POINT_SPEED = 0.1f;
    private static final float Z_LIMIT = 30f;
    private static final float START_Z = 15f;
    private static final float limitX = 7.5f;

    public WhiteDots() {
        groundPoints = new ArrayList<>();
        initializeGroundPoints();
    }

    private void initializeGroundPoints() {
        groundPoints.clear();
        float limitXDifference = limitX * 2 / COLUMNS;
        float limitZDifference = (Z_LIMIT - START_Z) / ROWS;
        float xStart = -limitX * 2;

        for (int i = 0; i < ROWS; i++) {
            float z = START_Z + i * limitZDifference;
            for (int j = 0; j < COLUMNS; j++) {
                float x = xStart + j * limitXDifference * 4;
                groundPoints.add(new float[]{x, -3.5f, z});
            }
        }
    }

    public void draw(GL10 gl) {
        gl.glDisable(GL10.GL_LIGHTING);
        for (float[] point : groundPoints) {
            gl.glPushMatrix();
            gl.glTranslatef(point[0], point[1], point[2]);
            drawPoint(gl);
            gl.glPopMatrix();
        }
        gl.glEnable(GL10.GL_LIGHTING);
        this.update();
    }

    private void drawPoint(GL10 gl) {
        float size = 0.05f;
        float[] vertices = {
                -size, -size, 0,
                size, -size, 0,
                -size,  size, 0,
                size,  size, 0,
        };

        FloatBuffer vertexBuffer;
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    public void update() {
        for (float[] point : groundPoints) {
            point[2] += POINT_SPEED;
            if (point[2] > Z_LIMIT) {
                point[2] = START_Z;
            }
        }
    }

}
