package org.example;

public class Game {

    private final Window window;
    private final Renderer renderer;

    public Game(){

        window = new Window(1200, 800);
        renderer = new Renderer(window);
    }

    public void loop(){

        window.start();

        while(!window.isWindowClosed()){

            window.clearScreen();

            double time = window.getTime();

            renderer.render();

            window.refreshScreen();
            window.handleEvents();
        }

        window.stop();
    }
}
