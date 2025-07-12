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
    private final Vector3f downMaxRenderPos = new Vector3f();
    private final Vector3f topMaxRenderPos = new Vector3f();
    private final Vector3f downLocalRenderPos = new Vector3f();
    private final Vector3f topLocalRenderPos = new Vector3f();
    private Chunk[][] chunks = new Chunk[DEFAULT_CHUNKS_WIDTH][DEFAULT_CHUNKS_WIDTH];

    private final CombinedGenerator combinedGenerator;

    private static final int DEFAULT_NUMBER_OF_CHUNKS = 8;
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

        float minChunksCord = -DEFAULT_NUMBER_OF_CHUNKS * Chunk.CHUNKS_2D_SIZE - Chunk.CHUNKS_2D_SIZE / 2f;

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

        downMaxRenderPos.x = minChunksCord;
        downMaxRenderPos.z = minChunksCord;

        topMaxRenderPos.x = -minChunksCord;
        topMaxRenderPos.z = -minChunksCord;

        downLocalRenderPos.x = -Chunk.CHUNKS_2D_SIZE / 2f;
        downLocalRenderPos.z = downLocalRenderPos.x;

        topLocalRenderPos.x = -downLocalRenderPos.x;
        topLocalRenderPos.z = -downLocalRenderPos.x;
    }

    private void handleChangedPosition(){

        Vector3f cameraPos = camera.getPosition();

        if(cameraPos.x < downLocalRenderPos.x){

            handleRefreshLessX();
        }
        else if(cameraPos.x > topLocalRenderPos.x){

            handleRefreshMoreX();
        }

        if(cameraPos.z < downLocalRenderPos.z){

            handleRefreshLessZ();
        }
        else if(cameraPos.z > topLocalRenderPos.z){

            handleRefreshMoreZ();
        }
    }

    private void handleRefreshLessX(){

        Chunk[][] newChunks = cloneChunks();

        downMaxRenderPos.x -= Chunk.CHUNKS_2D_SIZE;
        topMaxRenderPos.x -= Chunk.CHUNKS_2D_SIZE;

        downLocalRenderPos.x -= Chunk.CHUNKS_2D_SIZE;
        topLocalRenderPos.x -= Chunk.CHUNKS_2D_SIZE;

        for(int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

            Chunk oldChunk = chunks[DEFAULT_CHUNKS_WIDTH - 1][zI];

            oldChunk.clear();
        }

        for (int xI = 1; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            for (int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

                newChunks[xI][zI] = chunks[xI - 1][zI];
            }
        }

        Vector2f chunkPos = new Vector2f(downMaxRenderPos.x, downMaxRenderPos.z);

        for(int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            newChunks[0][zI] = chunk;

            chunkPos.y += Chunk.CHUNKS_2D_SIZE;
        }

        chunks = newChunks;
    }

    private void handleRefreshMoreX(){

        Chunk[][] newChunks = cloneChunks();

        downLocalRenderPos.x += Chunk.CHUNKS_2D_SIZE;
        topLocalRenderPos.x += Chunk.CHUNKS_2D_SIZE;

        for(int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

            Chunk oldChunk = chunks[0][zI];

            oldChunk.clear();
        }

        for (int xI = 0; xI < DEFAULT_CHUNKS_WIDTH - 1; xI++){

            for(int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

                newChunks[xI][zI] = chunks[xI + 1][zI];
            }
        }

        Vector2f chunkPos = new Vector2f(topMaxRenderPos.x, downMaxRenderPos.z);

        for(int zI = 0; zI < DEFAULT_CHUNKS_WIDTH; zI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            newChunks[DEFAULT_CHUNKS_WIDTH - 1][zI] = chunk;

            chunkPos.y += Chunk.CHUNKS_2D_SIZE;
        }

        downMaxRenderPos.x += Chunk.CHUNKS_2D_SIZE;
        topMaxRenderPos.x += Chunk.CHUNKS_2D_SIZE;

        chunks = newChunks;
    }

    private void handleRefreshLessZ(){

        Chunk[][] newChunks = cloneChunks();

        downMaxRenderPos.z -= Chunk.CHUNKS_2D_SIZE;
        topMaxRenderPos.z -= Chunk.CHUNKS_2D_SIZE;

        downLocalRenderPos.z -= Chunk.CHUNKS_2D_SIZE;
        topLocalRenderPos.z -= Chunk.CHUNKS_2D_SIZE;

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            Chunk oldChunk = chunks[xI][DEFAULT_CHUNKS_WIDTH - 1];

            oldChunk.clear();
        }

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            for (int zI = 1; zI < DEFAULT_CHUNKS_WIDTH; zI++){

                newChunks[xI][zI] = chunks[xI][zI - 1];
            }
        }

        Vector2f chunkPos = new Vector2f(downMaxRenderPos.x, downMaxRenderPos.z);

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            newChunks[xI][0] = chunk;

            chunkPos.x += Chunk.CHUNKS_2D_SIZE;
        }

        chunks = newChunks;
    }

    private void handleRefreshMoreZ(){

        Chunk[][] newChunks = cloneChunks();

        downLocalRenderPos.z += Chunk.CHUNKS_2D_SIZE;
        topLocalRenderPos.z += Chunk.CHUNKS_2D_SIZE;

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            Chunk oldChunk = chunks[xI][0];

            oldChunk.clear();
        }

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            for (int zI = DEFAULT_CHUNKS_WIDTH - 2; zI >= 0 ; zI--){

                newChunks[xI][zI] = chunks[xI][zI + 1];
            }
        }

        Vector2f chunkPos = new Vector2f(downMaxRenderPos.x, topMaxRenderPos.z);

        for(int xI = 0; xI < DEFAULT_CHUNKS_WIDTH; xI++){

            Chunk chunk = new Chunk(chunkPos, combinedGenerator);

            chunk.init();

            newChunks[xI][DEFAULT_CHUNKS_WIDTH - 1] = chunk;

            chunkPos.x += Chunk.CHUNKS_2D_SIZE;
        }

        downMaxRenderPos.z += Chunk.CHUNKS_2D_SIZE;
        topMaxRenderPos.z += Chunk.CHUNKS_2D_SIZE;

        chunks = newChunks;
    }

    private Chunk[][] cloneChunks(){

        Chunk[][] newChunks = new Chunk[chunks.length][chunks[0].length];

        for(int i = 0; i < chunks.length; i++){

            Chunk[] chunksRow = chunks[i];

            Chunk[] newChunksRow = new Chunk[chunksRow.length];

            for(int j = 0; j < chunksRow.length; j++){

                newChunksRow[j] = chunksRow[j];
            }

            newChunks[i] = newChunksRow;
        }

        return newChunks;
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
