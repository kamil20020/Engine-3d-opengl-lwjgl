package org.example;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private final Window window;
    private final Camera camera;
    private Cube cube = new Cube(new Vector3f(-10, -10, -10), 10);

    public Renderer(Window window, EventsHandler eventsHandler){

        this.window = window;
        this.camera = new Camera(new Vector3f(0, 10, -50), eventsHandler);
    }

    public void render(){

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        camera.update();

        cube.draw();
    }
}
