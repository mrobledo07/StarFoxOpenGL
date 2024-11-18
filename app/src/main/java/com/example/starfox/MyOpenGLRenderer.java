package com.example.starfox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import com.example.starfox.Loader;

public class MyOpenGLRenderer implements Renderer {

	public Context context;
	public MyOpenGLRenderer(Context context){
		this.context = context;
	}


	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		int backgroundTexture = Loader.loadTexture(gl, context, "corneria_bg.png");
		Loader.loadObj("starfox_ship.blend");
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

	}

	@Override
	public void onDrawFrame(GL10 gl) {

	}
}
