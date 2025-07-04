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
    private final List<Chunk> chunks = new ArrayList<>();

    private final CombinedGenerator combinedGenerator;

    private static final int DEFAULT_NUMBER_OF_CHUNKS = 5;

    public Renderer(Window window, EventsHandler eventsHandler){

        this.window = window;
        this.camera = new Camera(new Vector3f(0, 0, 0), eventsHandler);
        this.combinedGenerator = new CombinedGenerator(123);
    }

    public void init(){

        TexturesMap.init();
        CubeTextures.init();

        glEnable(GL_TEXTURE_2D);

        int textureId = Texture.createTexture("textures/textures.png");
        Texture.useTexture(textureId);

        int shaderProgram = ShaderUtils.load("shaders/vertex.glsl", "shaders/fragment.glsl");
        glUseProgram(shaderProgram);

        int texLocation = glGetUniformLocation(shaderProgram, "texture0");
        glUniform1i(texLocation, 0); // GL_TEXTURE0

        int numberOfChunksInOneSide = 2 * DEFAULT_NUMBER_OF_CHUNKS + 1;

        float chunk2dSize = Chunk.CHUNKS_2D_SIZE;
        float minChunksCord = -DEFAULT_NUMBER_OF_CHUNKS * chunk2dSize - chunk2dSize / 2;

        Vector2f chunkPos = new Vector2f(minChunksCord, minChunksCord);

        for(int xI = 0; xI < numberOfChunksInOneSide; xI++){

            chunkPos.y = minChunksCord;

            for(int zI = 0; zI < numberOfChunksInOneSide; zI++){

                Vector2f chunkPosCopy = new Vector2f(chunkPos);

                Chunk chunk = new Chunk(chunkPosCopy, combinedGenerator);

                chunk.init();

                chunks.add(chunk);

                chunkPos.y = chunkPos.y + chunk2dSize;
            }

            chunkPos.x = chunkPos.x + chunk2dSize;
        }
    }

    public void render(){

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        camera.update();

        for(Chunk chunk : chunks){

            chunk.draw();
        }
    }
}
