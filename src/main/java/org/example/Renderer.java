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
    private final ChunksManagement chunksManagement;

    private static final int DEFAULT_NUMBER_OF_CHUNKS = 8;

    public Renderer(Window window, EventsHandler eventsHandler){

        this.window = window;
        this.camera = new Camera(new Vector3f(0, 1200, 0), eventsHandler);

        CombinedGenerator combinedGenerator = new CombinedGenerator(123);

        this.chunksManagement = new ChunksManagement(DEFAULT_NUMBER_OF_CHUNKS, combinedGenerator);
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

        chunksManagement.init();

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

        downMaxRenderPos.x -= Chunk.CHUNKS_2D_SIZE;
        topMaxRenderPos.x -= Chunk.CHUNKS_2D_SIZE;

        downLocalRenderPos.x -= Chunk.CHUNKS_2D_SIZE;
        topLocalRenderPos.x -= Chunk.CHUNKS_2D_SIZE;

        chunksManagement.handleRefreshLessX(downMaxRenderPos);
    }

    private void handleRefreshMoreX(){

        downLocalRenderPos.x += Chunk.CHUNKS_2D_SIZE;
        topLocalRenderPos.x += Chunk.CHUNKS_2D_SIZE;

        chunksManagement.handleRefreshMoreX(downMaxRenderPos, topMaxRenderPos);

        downMaxRenderPos.x += Chunk.CHUNKS_2D_SIZE;
        topMaxRenderPos.x += Chunk.CHUNKS_2D_SIZE;
    }

    private void handleRefreshLessZ(){

        downMaxRenderPos.z -= Chunk.CHUNKS_2D_SIZE;
        topMaxRenderPos.z -= Chunk.CHUNKS_2D_SIZE;

        downLocalRenderPos.z -= Chunk.CHUNKS_2D_SIZE;
        topLocalRenderPos.z -= Chunk.CHUNKS_2D_SIZE;

        chunksManagement.handleRefreshLessZ(downMaxRenderPos);
    }

    private void handleRefreshMoreZ(){

        downLocalRenderPos.z += Chunk.CHUNKS_2D_SIZE;
        topLocalRenderPos.z += Chunk.CHUNKS_2D_SIZE;

        chunksManagement.handleRefreshMoreZ(downMaxRenderPos, topMaxRenderPos);

        downMaxRenderPos.z += Chunk.CHUNKS_2D_SIZE;
        topMaxRenderPos.z += Chunk.CHUNKS_2D_SIZE;
    }

    public void render(){

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        camera.update();
        handleChangedPosition();

        chunksManagement.draw();
    }
}
