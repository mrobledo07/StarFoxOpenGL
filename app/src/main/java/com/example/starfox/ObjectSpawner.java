package com.example.starfox;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class ObjectSpawner {

    private final ArrayList<Object3D> objects;
    private final Random random;
    private static final float Z_LIMIT = 30f;
    private static final float START_Z = 5f;
    private static final float limitX = 7.5f;
    private static final float MIN_DISTANCE = 2.5f;
    private static float moveSpeed = 0.1f;
    private final Context context;
    private long lastFrameTime;
    private int currentPhase = 0;
    private final float phaseDuration = 10.0f;
    private float phaseTimeElapsed = 0.0f;

    private float timeSinceLastSpawnObject = 0.0f;

    public ObjectSpawner(Context context) {
        this.objects = new ArrayList<>();
        this.random = new Random();
        this.context = context;
        lastFrameTime = System.nanoTime();
    }

    private void spawnPhase1() {
        Object3D column = new Object3D(context, R.raw.column);
        column.setX(-10.5f);
        column.setY(-3.5f);
        column.setZ(START_Z);
        objects.add(column);

        Object3D column2 = new Object3D(context, R.raw.column);
        column2.setX(10.5f);
        column2.setY(-3.5f);
        column2.setZ(START_Z);
        objects.add(column2);

        Object3D bridge = new Object3D(context, R.raw.bridge);
        setRandomPosition(bridge);
        bridge.setY(-3.5f);
        bridge.setZ(START_Z);
        objects.add(bridge);
    }

    private void spawnPhase2() {
        Object3D bigCube = new Object3D(context, R.raw.big_cube);
        bigCube.setX(-10.5f);
        bigCube.setY(-3.5f);
        bigCube.setZ(START_Z);
        objects.add(bigCube);

        Object3D bigCube2 = new Object3D(context, R.raw.big_cube);
        bigCube2.setX(10.5f);
        bigCube2.setY(-3.5f);
        bigCube2.setZ(START_Z);
        objects.add(bigCube2);

        Object3D smallCube = new Object3D(context, R.raw.small_cube);
        smallCube.setX(bigCube.getX());
        smallCube.setY(-3.5f);
        smallCube.setZ(bigCube.getZ() + 5.5f);
        objects.add(smallCube);

        Object3D smallCube2 = new Object3D(context, R.raw.small_cube);
        smallCube2.setX(bigCube.getX() + 3.5f);
        smallCube2.setY(-3.5f);
        smallCube2.setZ(bigCube.getZ() + 5.5f);
        objects.add(smallCube2);

        Object3D smallCube3 = new Object3D(context, R.raw.small_cube);
        smallCube3.setX(bigCube2.getX());
        smallCube3.setY(-3.5f);
        smallCube3.setZ(bigCube2.getZ() + 5.5f);
        objects.add(smallCube3);

        Object3D smallCube4 = new Object3D(context, R.raw.small_cube);
        smallCube4.setX(bigCube2.getX() + 3.5f);
        smallCube4.setY(-3.5f);
        smallCube4.setZ(bigCube2.getZ() + 5.5f);
        objects.add(smallCube4);

        Object3D column = new Object3D(context, R.raw.column_without_hat);
        setRandomPosition(column);
        column.setY(-3.5f);
        column.setZ(START_Z);
        objects.add(column);

        Object3D column2 = new Object3D(context, R.raw.column_without_hat);
        setRandomPosition(column2);
        column2.setY(-3.5f);
        column2.setZ(START_Z);
        objects.add(column2);
    }

    private void spawnPhase3() {
        // Spawn all (column, column_without_hat, bridge, big_cube, small_cube) objects
        Object3D column = new Object3D(context, R.raw.column);
        setRandomPosition(column);
        column.setY(-3.5f);
        column.setZ(START_Z);
        objects.add(column);

        Object3D column2 = new Object3D(context, R.raw.column);
        setRandomPosition(column2);
        column2.setY(-3.5f);
        column2.setZ(START_Z);
        objects.add(column2);

        Object3D smallCube = new Object3D(context, R.raw.small_cube);
        setRandomPosition(smallCube);
        smallCube.setY(-3.5f);
        smallCube.setZ(START_Z);
        objects.add(smallCube);

        Object3D smallCube2 = new Object3D(context, R.raw.small_cube);
        setRandomPosition(smallCube2);
        smallCube2.setY(-3.5f);
        smallCube2.setZ(START_Z);
        objects.add(smallCube2);

    }

    public void update(float deltaTime) {
        phaseTimeElapsed += deltaTime;
        timeSinceLastSpawnObject += deltaTime;

        if (phaseTimeElapsed > phaseDuration) {
            phaseTimeElapsed = 0.0f;
            currentPhase = (currentPhase + 1) % 3;
        }

        float spawnInterval = random.nextFloat() + 0.75f;
        if (timeSinceLastSpawnObject >= spawnInterval) {
            timeSinceLastSpawnObject = 0.0f;
            switch (currentPhase) {
                case 0:
                    spawnPhase1();
                    break;
                case 1:
                    spawnPhase2();
                    break;
                case 2:
                    spawnPhase3();
                    break;
            }
        }

        for (int i = objects.size() - 1; i >= 0; i--) {
            Object3D obj = objects.get(i);
            obj.setZ(obj.getZ() + moveSpeed);

            if (obj.getZ() > Z_LIMIT) {
                objects.remove(i);
            }
        }
    }

    public void draw(GL10 gl) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f;
        lastFrameTime = currentTime;

        this.update(deltaTime);

        for (Object3D obj : objects) {
            gl.glPushMatrix();
            gl.glTranslatef(obj.getX(), obj.getY(), obj.getZ());
            obj.draw(gl);
            gl.glPopMatrix();
        }
    }

    public void accelerate(float boost) {
        moveSpeed += boost;
        moveSpeed = Math.max(0.1f, Math.min(0.3f, moveSpeed));
    }

    private void setRandomPosition(Object3D obj) {
        boolean positionValid;

        do {
            float newX = random.nextFloat() * limitX * 2 - limitX;
            float newZ = START_Z;

            positionValid = true;

            for (Object3D other : objects) {
                if (distance(newX, newZ, other.getX(), other.getZ()) < MIN_DISTANCE) {
                    positionValid = false;
                    break;
                }
            }

            if (positionValid) {
                obj.setX(newX);
                obj.setZ(newZ);
            }

        } while (!positionValid);
    }

    private float distance(float x1, float z1, float x2, float z2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2));
    }
}
