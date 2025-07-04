package org.example;

import org.example.generator.CombinedGenerator;
import org.example.mesh.Cube;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import texture.CubeTextures;
import texture.TexturePosition;
import texture.TexturesMap;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Chunk {

    private final byte[][][] chunk;
    private final Vector2f downLeftPos;

    private int vertexArraysId;
    private int vertexBufferId;
    private int visibleBlocks = 0;

    public static final int CHUNKS_2D_SIZE = 16 * 16;
    public static final int CHUNKS_HEIGHT = 128;
    public static final int CHUNKS_SIZE = CHUNKS_2D_SIZE * CHUNKS_HEIGHT;

    public Chunk(Vector2f downLeftPos, CombinedGenerator combinedGenerator){

        this.downLeftPos = downLeftPos;
        this.chunk = combinedGenerator.initChunk((int) (downLeftPos.x / 16), (int) (downLeftPos.y / 16));
    }

    public void init(){

        uploadToGpu();
    }

    private void uploadToGpu(){

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(CHUNKS_SIZE * 6 * 4 * 5);

        Vector3f[] vertices = Cube.getVertices(16);
        Integer[] quads = Cube.getQuads();
        Vector2f[] textureCords = Cube.getTextureCords();
        Integer[] texturesForVertices = Cube.getTextureCordsForVertices();

        float x = downLeftPos.x;

        for(int xI=0; xI < 16; xI++){

            float z = downLeftPos.y;

            for(int zI=0; zI < 16; zI++){

                float y = 0;

                for(int yI=0; yI < CHUNKS_HEIGHT; yI++){

                    byte blockType = chunk[yI][zI][xI];

                    if(shouldNotDrawBlock(xI, yI, zI)){
                        continue;
                    }

                    appendBlockToVerticesBuffer(blockType, x, y, z, vertices, quads, textureCords, texturesForVertices, verticesBuffer);

                    visibleBlocks++;

                    y += 16;
                }

                z += 16;
            }

            x += 16;
        }

        verticesBuffer.flip();

        vertexArraysId = glGenVertexArrays();
        glBindVertexArray(vertexArraysId);

        vertexBufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        int stride = 5 * Float.BYTES;

        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 3 * Float.BYTES);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    public void draw(){

        glBindVertexArray(vertexArraysId);
        glDrawArrays(GL_QUADS, 0, visibleBlocks * 6 * 4);
    }

    public void clear(){

        glDeleteBuffers(vertexBufferId);
        glDeleteVertexArrays(vertexArraysId);
    }

    private void appendBlockToVerticesBuffer(byte blockType, float x, float y, float z, Vector3f[] vertices, Integer[] quads, Vector2f[] textureCords, Integer[] texturesForVertices, FloatBuffer verticesBuffer){

        String[] cubeTextures = CubeTextures.getCubeTextures(blockType);

        int quadIndex = 0;

        for(int i = 0; i < 6; i++){

            String cubeTexture = cubeTextures[i];

            TexturePosition texturePosition = TexturesMap.getTexturePosition(cubeTexture);

            for(int j = 0; j < 4; j++, quadIndex++){

                Integer quadVertexIndex = quads[quadIndex];
                Vector3f vertex = vertices[quadVertexIndex];

                Integer texturesForVertex = texturesForVertices[quadIndex];
                Vector2f textureCord = textureCords[texturesForVertex];

                float u = getGlobalTextureCord(CubeTextures.COMBINED_TEXTURE_TOTAL_WIDTH, textureCord.x, texturePosition.col());
                float v = getGlobalTextureCord(CubeTextures.COMBINED_TEXTURE_TOTAL_HEIGHT, textureCord.y, texturePosition.row());

                verticesBuffer.put(vertex.x + x).put(vertex.y + y).put(vertex.z + z);
                verticesBuffer.put(u).put(v);
            }
        }
    }

    private boolean shouldNotDrawBlock(int xI, int yI, int zI){

        byte blockType = chunk[yI][zI][xI];

        return blockType == 0; //|| isBlockHidden(xI, yI, zI);
    }

    private float getGlobalTextureCord(float maxGlobalCord, float localCord, int globalCordIndex){

        return ((float) globalCordIndex * 16 + localCord * 16) / maxGlobalCord;
    }

    private boolean isBlockHidden(int x, int y, int z){

        return (x > 0 && chunk[y][z][x - 1] != 0) &&
                (x < chunk[0].length - 1 && chunk[y][z][x + 1] != 0) &&

                (y > 0 && chunk[y - 1][z][x] != 0) &&
                (y < chunk.length - 1 && chunk[y + 1][z][x] != 0) &&

                (z > 0 && chunk[y][z - 1][x] != 0) &&
                (z < chunk[0][0].length - 1 && chunk[y][z + 1][x] != 0);
    }

}
