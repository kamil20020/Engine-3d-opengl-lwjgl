package org.example;

import org.example.math.PerlinNoise2d;
import org.example.math.PerlinNoise3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private final Window window;
    private final Camera camera;

    private final List<Float[]> cords = new ArrayList<>();

    private final Cube cube = new Cube(16);
    private int[][][] blocks = new int[16][32][32];

    double[][] heightNoiseValues;

    private static final int DEFAULT_NUMBER_OF_CHUNKS = 8;

    public Renderer(Window window, EventsHandler eventsHandler){

        this.window = window;
        this.camera = new Camera(new Vector3f(0, 0, -50), eventsHandler);
    }

    public void init(){

        glEnable(GL_TEXTURE_2D);

        PerlinNoise3d terrainNoise = new PerlinNoise3d(false);

        double[][][] perlinNoise3dValues = terrainNoise.perlin(32, 16, 32);

        PerlinNoise2d heightNoise = new PerlinNoise2d(false);

        heightNoiseValues = heightNoise.perlin(32, 32);

        for(int i=0; i < 16; i++){

            for(int j=0; j < 32; j++){

                for(int k=0; k < 32; k++){

                     double perlinNoise3dValue = perlinNoise3dValues[i][j][k];

                     int blockType = 0;

                     if(perlinNoise3dValue < 0.2d){

                         blockType = 0;
                     }
                     else if(perlinNoise3dValue < 0.4d){

                         blockType = 1;
                     }
                     else if(perlinNoise3dValue < 0.6d){

                         blockType = 2;
                     }
                     else if(perlinNoise3dValue < 0.8d){

                         blockType = 3;
                     }
                     else{
                         blockType = 4;
                     }

                     blocks[i][j][k] = blockType;
                }
            }
        }
    }

    private void drawChunks(){

        int maxCord = 16 * 1;

        float y = -10;

        for(int i=0; i < 16; i++){

            float x = -10;

            for(int j=0; j < 32; j++){

                float z = -10;

                for(int k=0; k < 32; k++){

                    int blockType = blocks[i][j][k];

                    String textureName = switch(blockType){

                        case 1 -> "dirt";
                        case 2 -> "sand";
                        case 3 -> "gravel";
                        case 4 -> "granite";

                        default -> "grass";
                    };

                    double height = Math.floor(heightNoiseValues[j][k] * 10) * 16;

                    cube.draw(CubeTextures.getCubeTextures(textureName), x, (float) height + y, z);

                    z += 16;
                }

                x += 16;
            }

            y += 16;
        }
    }

    public void render(){

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        camera.update();

        drawChunks();
    }
}
