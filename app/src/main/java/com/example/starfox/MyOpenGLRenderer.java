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
	private float time = 0.0f;
    private boolean autoMovement = true;
	private static final float limitX = 6.0f, limitY = 2.5f;
	private static final float ROTATION_FACTOR = 150.0f;
	private float rotationX = 0.0f, rotationY = 0.0f;

	public MyOpenGLRenderer(Context context){
		this.context = context;
		this.object3D = new Object3D(context, R.raw.starfox_ship);
		this.background = new Background();
		this.whiteDots = new WhiteDots();
	}


	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
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

		Light light = new Light(gl, GL10.GL_LIGHT0);
		light.setPosition(new float[]{0.0f, -10.0f, 10.0f, 0.0f});
		light.setAmbientColor(new float[]{1f, 1f, 1f});
		light.setDiffuseColor(new float[]{1, 1, 1});
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) height = 1;   // To prevent divide by zero
		float aspect = (float) width / height;

		// Set the viewport (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
		gl.glLoadIdentity();                 // Reset projection matrix
		// Use perspective projection
		GLU.gluPerspective(gl, 60, aspect, 0.1f, 100.f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
		gl.glLoadIdentity();                 // Reset
	}

    @Override
	public void onDrawFrame(GL10 gl) {
		// Clear color and depth buffers using clear-value set earlier
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		float divisor = 1.5f;
		GLU.gluLookAt(gl, object3D.getX() / divisor, object3D.getY() / divisor, 30, object3D.getX() / divisor, object3D.getY() / divisor, 0f, 0f, 1f, 0f);

		background.draw(gl);
		whiteDots.draw(gl);

		float oscillation = 0.0f;
		float oscillationSpeed = 0.02f;
		float oscillationAmplitude = 0.25f;

        if (autoMovement) {
			time += oscillationSpeed;
            oscillation = (float) Math.sin(time) * oscillationAmplitude;
		}

		oscillate(oscillation * oscillationSpeed);

		// Draw shadow
		gl.glPushMatrix();
		float shadowScale = Math.max(0.0f, 1.0f - (object3D.getY() / 4));
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glTranslatef(object3D.getX(), -3f, 27.0f);
		gl.glScalef(shadowScale, shadowScale, 1.0f);
		gl.glRotatef(-rotationX,0,0,1);			   // Rotation horizontal movement
		gl.glRotatef(-rotationX, 0.0f, 1.0f, 0.0f);   // Horizontal movement
		gl.glScalef(1, 0, 1);
		object3D.draw(gl);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glPopMatrix();

		// Draw object
		gl.glPushMatrix();
		gl.glTranslatef(object3D.getX(), object3D.getY(), 27.5f);
		gl.glRotatef(-rotationX,0,0,1);			   // Rotation horizontal movement
		gl.glRotatef(rotationY, 1.0f, 0.0f, 0.0f);    // Vertical movement
		gl.glRotatef(-rotationX, 0.0f, 1.0f, 0.0f);   // Horizontal movement
		object3D.draw(gl);
		gl.glPopMatrix();
	}

	public void oscillate(float deltaY) {
		object3D.setPosition(object3D.getX(), object3D.getY() + deltaY);
		adjustPositionToLimit();
	}

	public void moveObject(float deltaX, float deltaY) {
		object3D.setPosition(object3D.getX() + deltaX, object3D.getY() + deltaY);
		adjustPositionToLimit();
		rotationX = Math.max(Math.min(deltaX,25),-25) * ROTATION_FACTOR;
		rotationY = Math.max(Math.min(deltaY,25),-25) * ROTATION_FACTOR;
	}

	public void adjustPositionToLimit() {
		object3D.setX(Math.max(Math.min(object3D.getX(), limitX), -limitX));
		object3D.setY(Math.max(Math.min(object3D.getY(), limitY), -limitY));
	}

	public void setAutoMovement(boolean autoMovement) {
		this.autoMovement = autoMovement;
	}
}
