package org.example;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private final Window window;

    public Renderer(Window window){

        this.window = window;
    }

    public void render(){

        glColor3f(0, 1, 0);
        glBegin(GL_TRIANGLES);
        glVertex2f(0, 0);
        glVertex2f(0, 50);
        glVertex2f(50, 0);
        glEnd();

        glColor3f(1, 0, 0);
        glBegin(GL_TRIANGLES);
        glVertex2f(0, 0);
        glVertex2f(0, 50);
        glVertex2f(-50, 0);
        glEnd();
    }
}
