package com.example.starfox;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;


public class Loader {
    public static int loadTexture(GL10 gl, Context context, String fileName) {
        final int[] textureHandle = new int[1];
        gl.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            // Carga la imagen desde los recursos
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false; // No escalar la textura
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Asigna la textura
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureHandle[0]);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();

            // Configuración de filtros de la textura
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        }

        return textureHandle[0];
    }


    public static void loadObj(String fileName) {
        private List<Float> vertices = new ArrayList<>();
        private List<Float> normals = new ArrayList<>();
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) { // Vértices
                    String[] parts = line.split(" ");
                    vertices.add(Float.parseFloat(parts[1]));
                    vertices.add(Float.parseFloat(parts[2]));
                    vertices.add(Float.parseFloat(parts[3]));
                } else if (line.startsWith("vn ")) { // Normales
                    String[] parts = line.split(" ");
                    normals.add(Float.parseFloat(parts[1]));
                    normals.add(Float.parseFloat(parts[2]));
                    normals.add(Float.parseFloat(parts[3]));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
