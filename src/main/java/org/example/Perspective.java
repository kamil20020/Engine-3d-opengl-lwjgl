package org.example;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;

public class Perspective {

    private static final FloatBuffer floatBuffer;

    static {

        Matrix4f perspectiveMatrix = new Matrix4f().setPerspective(120, 1.0f, 0.1f, 1000);

        floatBuffer = BufferUtils.createFloatBuffer(16);
        perspectiveMatrix.get(floatBuffer);
    }

    public static void init(){

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glLoadMatrixf(floatBuffer);
    }
}
