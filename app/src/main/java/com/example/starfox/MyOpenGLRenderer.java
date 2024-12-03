package com.example.starfox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class MyOpenGLRenderer implements Renderer {

	public Context context;
	public Object3D object3D;
	public Background background;
	public WhiteDots whiteDots;
	private float width, height, aspect, time = 0.0f;

    float Z = 1;
	private Light light;
	private boolean autoMovement = true;


	public MyOpenGLRenderer(Context context){
		this.context = context;
		this.object3D = new Object3D(context, R.raw.starfox_ship);
		this.background = new Background();
		this.whiteDots = new WhiteDots();
	}



	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);  // Set color's clear-value to black
		gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
		gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
		gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
		gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
		gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

		//Enable Lights
		gl.glEnable(GL10.GL_LIGHTING);

		// Enable Normalize
		gl.glEnable(GL10.GL_NORMALIZE);

		// Load background
		background.loadTexture(gl, context);

		light = new Light(gl, GL10.GL_LIGHT0);
		light.setPosition(new float[]{0.0f, 0.0f, 1.0f, 0.0f});

		light.setAmbientColor(new float[]{0.1f, 0.1f, 0.1f});
		light.setDiffuseColor(new float[]{1, 1, 1});
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) height = 1;   // To prevent divide by zero
		this.width = width;
		this.height = height;
		this.aspect = (float) width / height;

		background.setAspect(aspect);

		// Set the viewport (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
		gl.glLoadIdentity();                 // Reset projection matrix
		// Use perspective projection
		GLU.gluPerspective(gl, 60, aspect, 0.1f, 100.f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
		gl.glLoadIdentity();                 // Reset

		object3D.setScale(aspect < 1 ? 1.0f + 0.25f : aspect * 1.25f);
		adjustPositionToLimit();

		whiteDots.setAspect(aspect);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// Clear color and depth buffers using clear-value set earlier
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		light.setPosition(new float[]{this.getZ(), -10, -10, 0});
		float divisorX = 1.5f;
		float divisorY = 4.0f;
		if (aspect > 1.0f) {
			divisorX = 4.0f;
			divisorY = 1.5f;
		}
		GLU.gluLookAt(gl, object3D.getX() / divisorX, object3D.getY() / divisorY, 8.5f, object3D.getX() / divisorX, object3D.getY() / divisorY, 0f, 0f, 1f, 0f);

		// Draw background
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, -1.5f);
		background.draw(gl);
		gl.glPopMatrix();

		float oscillation = 0.0f;
        float oscillationSpeed = 0.02f;
		float oscillationAmplitude = 0.25f;

		if (aspect > 1.0f) {
			oscillationAmplitude = 0.5f;
		}

        if (autoMovement) {
			time += oscillationSpeed;
            oscillation = (float) Math.sin(time) * oscillationAmplitude;
		}

		moveObject(0, oscillation * oscillationSpeed);

		gl.glPushMatrix();// Reset model-view matrix ( NEW )
		gl.glTranslatef(object3D.getX(), object3D.getY(), 0);
		gl.glScalef(object3D.getScale(), object3D.getScale(), object3D.getScale());
		object3D.draw(gl);
		gl.glPopMatrix();

		whiteDots.update();
		whiteDots.draw(gl);

	}

	public float getHeight() {
		return this.height;
	}

	public float getWidth() {
		return this.width;
	}

	public float getZ() {
		return Z;
	}

	public void setZ(float z) {
		this.Z = z;
	}

	public void moveObject(float deltaX, float deltaY) {
		object3D.setPosition(object3D.getX() + deltaX, object3D.getY() + deltaY);
		adjustPositionToLimit();
	}

	public void adjustPositionToLimit() {
		float limitX, limitY;
		if (aspect > 1.0f) {
			limitX = aspect * 5;
			limitY = aspect * 3;
		} else {
			limitX = aspect * 5;
			limitY = aspect * 10;
		}

		if (object3D.getX() < -limitX)
			object3D.setX(-limitX);
		else if (object3D.getX() > limitX)
			object3D.setX(limitX);

		if (object3D.getY() < -limitY)
			object3D.setY(-limitY);
		else if (object3D.getY() > limitY)
			object3D.setY(limitY);
	}

	public void setAutoMovement(boolean autoMovement) {
		this.autoMovement = autoMovement;
	}
}
