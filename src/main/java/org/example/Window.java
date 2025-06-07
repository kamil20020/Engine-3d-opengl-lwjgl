package org.example;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final long windowId;
    private int width;
    private int height;

    public Window(int width, int height){

        this.width = width;
        this.height = height;

        if(!glfwInit()){
            throw new IllegalStateException("Could now init glfw");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        windowId = glfwCreateWindow(width, height, "Engine 3d - OpenGL from lwjgl", NULL, NULL);

        if(windowId == NULL){

            glfwTerminate();

            throw new IllegalStateException("Could not create glfw window");
        }

        glfwMakeContextCurrent(windowId);
        glfwSetFramebufferSizeCallback(windowId, (windowId, newWidth, newHeight) -> updateViewPort(width, height));
        glfwSwapInterval(1); // vertical sync
    }

    public void start(){

        glfwShowWindow(windowId);

        GL.createCapabilities(); // collaboration between lwjgl, opengl and glfw

        glClearColor(0f, 0f, 0f, 1.0f);
        updateViewPort(width, height);
    }

    public void stop(){

        glfwTerminate();
    }

    public void clearScreen(){

        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void refreshScreen(){

        glFlush();

        glfwSwapBuffers(windowId);
    }

    public void handleEvents(){

        glfwWaitEvents();
    }

    public boolean isWindowClosed(){

        return glfwWindowShouldClose(windowId);
    }

    public double getTime(){

        return glfwGetTime();
    }

    private  void updateViewPort(int newWidth, int newHeight){

        if(newWidth == 0){
            newWidth = 1;
        }

        if(newHeight == 0){
            newHeight = 1;
        }

        this.width = newWidth;
        this.height = newHeight;

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
}
