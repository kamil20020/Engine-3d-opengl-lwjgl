package org.example;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EventsHandler {

    private final Window window;
    private List<BiConsumer<Integer, Integer>> keyboardCallbacks = new ArrayList<>();

    public EventsHandler(Window window){

        this.window = window;

        window.setKeyboardCallback(this::handleKeyboard);
        window.setMousePosCallback(this::handleMousePos);
        window.setMouseClickCallback(this::handleMouseClick);
    }

    public void addKeyboardCallback(BiConsumer<Integer, Integer> callback){

        keyboardCallbacks.add(callback);
    }

    public void handleKeyboard(int key, int action){

        if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS){

            window.close();
        }

        keyboardCallbacks
            .forEach(callback -> callback.accept(key, action));
    }

    public void handleMousePos(double x, double y){


    }

    public void handleMouseClick(int button, int action){


    }

}
