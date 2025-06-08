package org.example;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private final Window window;
    private final Camera camera;

    private final List<Cube> cubes = new ArrayList<>();

    public Renderer(Window window, EventsHandler eventsHandler){

        this.window = window;
        this.camera = new Camera(new Vector3f(0, 0, -50), eventsHandler);
    }

    public void init(){

        glEnable(GL_TEXTURE_2D);

        Texture texture = new Texture("textures/grass_block_side.png");

        float x = -10;

        for(int i=0; i < 16; i++){

            float y = -10;

            for(int j=0; j < 16; j++){

                float z = -10;

                for(int k=0; k < 16; k++){

                    cubes.add(new Cube(new Vector3f(x, y, z), 16, texture));

                    z += 16;
                }

                y += 16;
            }

            x += 16;
        }
    }

    public void render(){

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        camera.update();

        for(Cube cube : cubes){

            cube.draw();
        }
    }
}
