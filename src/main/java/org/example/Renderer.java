package org.example;

import org.example.generator.CombinedGenerator;
import org.example.shaders.ShaderUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import texture.CubeTextures;
import texture.Texture;
import texture.TexturesMap;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Renderer {

    private final Window window;
    private final Camera camera;
    private final Vector3f downRenderPos = new Vector3f();
    private final Vector3f topRenderPos = new Vector3f();
    private Chunk[][] chunks = new Chunk[DEFAULT_CHUNKS_WIDTH][DEFAULT_CHUNKS_WIDTH];

    private final CombinedGenerator combinedGenerator;

    private static final int DEFAULT_NUMBER_OF_CHUNKS = 5;
    private static final int DEFAULT_CHUNKS_WIDTH = DEFAULT_NUMBER_OF_CHUNKS * 2 + 1;
    private static final int DEFAULT_CHUNKS_AREA = DEFAULT_CHUNKS_WIDTH * DEFAULT_CHUNKS_WIDTH;

    public Renderer(Window window, EventsHandler eventsHandler){

        this.window = window;
        this.camera = new Camera(new Vector3f(0, 1200, 0), eventsHandler);
        this.combinedGenerator = new CombinedGenerator(123);
    }

    public void init(){

        initTextures();
        initChunks();
    }

    private void initTextures(){

        TexturesMap.init();
        CubeTextures.init();

        glEnable(GL_TEXTURE_2D);

        int textureId = Texture.createTexture("textures/textures.png");
        Texture.useTexture(textureId);

        int shaderProgram = ShaderUtils.load("shaders/vertex.glsl", "shaders/fragment.glsl");
        glUseProgram(shaderProgram);

        int texLocation = glGetUniformLocation(shaderProgram, "texture0");
        glUniform1i(texLocation, 0); // GL_TEXTURE0
    }

    private void initChunks(){

        float minChunksCord = -DEFAULT_NUMBER_OF_CHUNKS * Chunk.CHUNKS_2D_SIZE - ((float) Chunk.CHUNKS_2D_SIZE) / 2;

        Vector2f chunkPos = new Vector2f(minChunksCord, minChunksCord);

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            chunkPos.y = minChunksCord;

            for(int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

                Vector2f chunkPosCopy = new Vector2f(chunkPos);

                Chunk chunk = new Chunk(chunkPosCopy, combinedGenerator);

                chunk.init();

                chunks[xI][zI] = chunk;

                chunkPos.y += Chunk.CHUNKS_2D_SIZE;
            }

            chunkPos.x += Chunk.CHUNKS_2D_SIZE;
        }

        downRenderPos.x = -((float) Chunk.CHUNKS_2D_SIZE) / 2;
        downRenderPos.z = downRenderPos.x;

        topRenderPos.x = -downRenderPos.x;
        topRenderPos.z = -downRenderPos.x;
    }

    private void handleChangedPosition(){

        Vector3f cameraPos = camera.getPosition();

        Chunk[][] newChunks = chunks.clone();

        if(cameraPos.x < downRenderPos.x){

            handleRefreshLessX(newChunks);
        }
        else if(cameraPos.x > topRenderPos.x){

            handleRefreshMoreX(newChunks);
        }

        if(cameraPos.z < downRenderPos.z){

            handleRefreshLessY(newChunks);
        }
        else if(cameraPos.z > topRenderPos.z){

            handleRefreshMoreY(newChunks);
        }

        chunks = newChunks;
    }

    private void handleRefreshLessX(Chunk[][] newChunks){

        downRenderPos.x -= Chunk.CHUNKS_2D_SIZE;

        Vector2f chunkPos = new Vector2f(downRenderPos.x, downRenderPos.z);

        for(int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

            Chunk oldChunk = chunks[DEFAULT_CHUNKS_WIDTH - 1][zI];

            oldChunk.clear();
        }

        for (int xI = 1; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            for (int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

                newChunks[xI][zI] = chunks[xI - 1][zI];
            }
        }

        for(int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            chunks[0][zI] = chunk;

            chunkPos.y += Chunk.CHUNKS_2D_SIZE;
        }
    }

    private void handleRefreshMoreX(Chunk[][] newChunks){

        downRenderPos.x += Chunk.CHUNKS_2D_SIZE;

        Vector2f chunkPos = new Vector2f(downRenderPos.x, downRenderPos.z);

        for(int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

            Chunk oldChunk = chunks[0][zI];

            oldChunk.clear();
        }

        for (int xI = DEFAULT_CHUNKS_WIDTH - 2; xI >= 0 ; xI--){

            for(int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

                newChunks[xI][zI] = chunks[xI + 1][zI];
            }
        }

        for(int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            chunks[DEFAULT_CHUNKS_WIDTH - 1][zI] = chunk;

            chunkPos.y += Chunk.CHUNKS_2D_SIZE;
        }
    }

    private void handleRefreshLessY(Chunk[][] newChunks){

        downRenderPos.z -= Chunk.CHUNKS_2D_SIZE;

        Vector2f chunkPos = new Vector2f(downRenderPos.x, downRenderPos.z);

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            Chunk oldChunk = chunks[xI][DEFAULT_CHUNKS_WIDTH - 1];

            oldChunk.clear();
        }

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            for (int zI = 1; zI < DEFAULT_CHUNKS_WIDTH; zI++){

                newChunks[xI][zI] = chunks[xI][zI - 1];
            }
        }

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            chunks[xI][0] = chunk;

            chunkPos.x += Chunk.CHUNKS_2D_SIZE;
        }
    }

    private void handleRefreshMoreY(Chunk[][] newChunks){

        downRenderPos.z += Chunk.CHUNKS_2D_SIZE;

        Vector2f chunkPos = new Vector2f(downRenderPos.x, downRenderPos.z);

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            Chunk oldChunk = chunks[xI][0];

            oldChunk.clear();
        }

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            for (int zI = DEFAULT_CHUNKS_WIDTH - 2; zI >= 0 ; zI--){

                newChunks[xI][zI] = chunks[xI][zI + 1];
            }
        }

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            chunks[xI][DEFAULT_CHUNKS_WIDTH - 1] = chunk;

            chunkPos.x += Chunk.CHUNKS_2D_SIZE;
        }
    }

    public void render(){

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        camera.update();
        handleChangedPosition();

        for(Chunk[] chunksRow : chunks){

            for(Chunk chunk : chunksRow){

                chunk.draw();
            }
        }
    }
}
