package org.example;

import org.example.mesh.Cube;
import org.joml.Vector3f;
import texture.CubeTextures;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private final Window window;
    private final Camera camera;

    private final Cube cube = new Cube(16);
    private byte[][][] chunk ;

    private final Generator generator;

    private static final int DEFAULT_NUMBER_OF_CHUNKS = 8;

    public Renderer(Window window, EventsHandler eventsHandler){

        this.window = window;
        this.camera = new Camera(new Vector3f(0, 0, -50), eventsHandler);
        this.generator = new Generator(1234);
    }

    public void init(){

        glEnable(GL_TEXTURE_2D);

        chunk = generator.initChunk();
    }

    private void drawTestBlock(){

        cube.draw(CubeTextures.getCubeTextures("grass"), 0, 0, 0);
    }

    private void drawChunk(){

        float y = 0;

        for(int i=0; i < chunk.length; i++){

            float x = 0;

            for(int j=0; j < chunk[i].length; j++){

                float z = 0;

                for(int k=0; k < chunk[i][j].length; k++){

                    byte blockType = chunk[i][j][k];

                    if(blockType == 0 || isBlockHidden(j, i, k)){
                        continue;
                    }

                    String textureName = CubeTextures.getTextureName(blockType);

                    cube.draw(CubeTextures.getCubeTextures(textureName), x, y, z);

                    z += 16;
                }

                x += 16;
            }

            y += 16;
        }
    }

    private boolean isBlockHidden(int x, int y, int z){

        return (x > 0 && chunk[y][x - 1][z] != 0) &&
               (x < chunk[0].length - 1 && chunk[y][x + 1][z] != 0) &&

               (y > 0 && chunk[y - 1][x][z] != 0) &&
               (y < chunk.length - 1 && chunk[y + 1][x][z] != 0) &&

               (z > 0 && chunk[y][x][z - 1] != 0) &&
               (z < chunk[0][0].length - 1 && chunk[y][x][z + 1] != 0);
    }

    public void render(){

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        camera.update();

        drawChunk();
//        drawTestBlock();
    }
}
