package com.example.starfox;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Light {
    ByteBuffer vtbb;
    FloatBuffer position;
    FloatBuffer ambient;
    FloatBuffer difuse;
    FloatBuffer specular;
    GL10 gl;
    int lightid;

    FloatBuffer fb_pos;

    public Light(GL10 gl, int lightid) {
        this.gl = gl;
        this.lightid = lightid;
        gl.glEnable(lightid);
    }

    //To enable and disable the light
    public void enable() {gl.glEnable(lightid);}
    public void disable() {gl.glDisable(lightid);}

    //To position the light
    public void setPosition(float[] pos) {
        fb_pos = FloatBuffer.wrap(pos);
        gl.glLightfv(lightid, GL10.GL_POSITION, fb_pos);
    }

    public void setPosition() {
        if(fb_pos!=null){
            gl.glLightfv(lightid, GL10.GL_POSITION, fb_pos);
        }
    }


    //To set the light colors
    public void setAmbientColor(float[] color) {
        FloatBuffer fb = FloatBuffer.wrap(color);
        gl.glLightfv(lightid, GL10.GL_AMBIENT, fb);
    }

    public void setDiffuseColor(float[] color) {
        FloatBuffer fb = FloatBuffer.wrap(color);
        gl.glLightfv(lightid, GL10.GL_DIFFUSE, fb);
    }

    public void setSpecularColor(float[] color) {
        FloatBuffer fb = FloatBuffer.wrap(color);
        gl.glLightfv(lightid, GL10.GL_SPECULAR, fb);
    }

}
