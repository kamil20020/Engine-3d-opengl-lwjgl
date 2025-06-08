package org.example;

public class Game {

    private final Window window;
    private final Renderer renderer;
    private EventsHandler eventsHandler;

    public Game(){

        window = new Window(800, 800);
        eventsHandler = new EventsHandler(window);
        renderer = new Renderer(window, eventsHandler);
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
