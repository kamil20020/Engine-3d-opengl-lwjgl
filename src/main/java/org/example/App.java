package org.example;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Hello world!
 */
public class App {

    private static void startup(long windowId, int width, int height){

        GL.createCapabilities(); // collaboration between lwjgl, opengl and glfw

        glClearColor(0f, 0f, 0f, 1.0f);
        updateViewPort(windowId, width, height);
    }

    private static void shutdowm(){

        glfwTerminate();
    }

    private static void render(double time){

        glClear(GL_COLOR_BUFFER_BIT);

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

        glFlush();
    }

    private static void updateViewPort(long windowId, int width, int height){

        if(width == 0){
            width = 1;
        }

        if(height == 0){
            height = 1;
        }

        float aspectRatio = (float) width / (float) height;

        glMatrixMode(GL_PROJECTION);
        glViewport(0, 0, width, height);
        glLoadIdentity();

        if(width <= height){

            glOrtho(-100, 100, -100f / aspectRatio, 100f / aspectRatio, 1, -1);
        }
        else{

            glOrtho(-100f * aspectRatio, 100f * aspectRatio, -100, 100, 1, -1);
        }

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public static void main(String[] args) {

        System.out.println("Hello World! :D");

        if(!glfwInit()){
            throw new IllegalStateException("Could now init glfw");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        final int width = 1200;
        final int height = 800;

        long windowId = glfwCreateWindow(width, height, "Engine 3d - OpenGL from lwjgl", NULL, NULL);

        if(windowId == NULL){

            glfwTerminate();

            throw new IllegalStateException("Could not create glfw window");
        }

        glfwMakeContextCurrent(windowId);
        glfwSetFramebufferSizeCallback(windowId, App::updateViewPort);
        glfwSwapInterval(1); // vertical sync
        glfwShowWindow(windowId);

        startup(windowId, width, height);

        while(!glfwWindowShouldClose(windowId)){

            double time = glfwGetTime();

            render(time);

            glfwSwapBuffers(windowId);
            glfwWaitEvents();
        }

        shutdowm();
    }
}
