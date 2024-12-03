package com.example.starfox;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class WhiteDots {
    private float aspect = 1.0f;
    private List<float[]> groundPoints;
    private static final int ROWS = 15;
    private static final int COLUMNS = 25;
    private static final int POINT_COUNT = ROWS * COLUMNS;
    private static final float POINT_SPEED = 0.1f; // Velocidad del desplazamiento
    private static final float Z_LIMIT = 5f; // Límite detrás de la cámara
    private static final float START_Z = -20f; // Punto de inicio de los puntos
    private float limitX, limitY;

    public WhiteDots() {
        groundPoints = new ArrayList<>();
        initializeGroundPoints();
    }

    private void initializeGroundPoints() {
        groundPoints.clear();
        float limitXDifference = limitX * 2 / COLUMNS; // Separación entre columnas
        float limitZDifference = (Z_LIMIT - START_Z) / ROWS; // Separación entre filas
        float xStart = -limitX;

        for (int i = 0; i < ROWS; i++) {
            float z = START_Z + i * limitZDifference;
            for (int j = 0; j < COLUMNS; j++) {
                float x = xStart + j * limitXDifference * 10;
                groundPoints.add(new float[]{x, -limitY, z});
            }
        }
    }

    public void draw(GL10 gl) {
        gl.glDisable(GL10.GL_LIGHTING);
        gl.glPushMatrix();
        // Restablece la matriz de modelo-vista
        gl.glLoadIdentity();
        // Configura el color de los puntos (blanco)
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        for (float[] point : groundPoints) {
            gl.glPushMatrix();
            gl.glTranslatef(point[0], point[1], point[2]); // Posición del punto
            drawPoint(gl); // Dibuja un punto
            gl.glPopMatrix();
        }

        gl.glPopMatrix();
        gl.glEnable(GL10.GL_LIGHTING);
    }

    private void drawPoint(GL10 gl) {
        float size = 0.05f; // Tamaño del punto
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
            point[2] += POINT_SPEED; // Mueve el punto hacia la cámara

            // Si el punto pasa el límite, recíclalo al inicio
            if (point[2] > Z_LIMIT) {
                point[2] = START_Z; // Reposiciona al fondo
            }
        }
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
        if (aspect > 1.0f) {
            limitX = aspect * 5;
            limitY = aspect * 3;
        } else {
            limitX = aspect * 5;
            limitY = aspect * 10;
        }
        initializeGroundPoints(); // Recalcular puntos según el nuevo aspecto
    }

    public float getAspect() {
        return this.aspect;
    }
}
