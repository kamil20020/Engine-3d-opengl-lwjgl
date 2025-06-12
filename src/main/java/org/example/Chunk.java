package org.example;

import org.example.mesh.Cube;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import texture.CubeTexture;
import texture.CubeTextures;
import texture.Texture;
import texture.TexturePosition;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Chunk {

    private byte[][][] chunk;
    private Vector2f downLeftPos;

    private int vertexArraysId;
    private int vertexBufferId;
    private int visibleBlocks = 0;

    private static final Cube cube = new Cube(16);

    public static final int CHUNKS_2D_SIZE = 16 * 16;
    public static final int CHUNKS_HEIGHT = 384;
    public static final int CHUNKS_SIZE = CHUNKS_2D_SIZE * CHUNKS_HEIGHT;

    public Chunk(Vector2f downLeftPos, Generator generator){

        this.downLeftPos = downLeftPos;
        this.chunk = generator.initChunk();
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

        int y = 0;

        for(int i=0; i < chunk.length; i++){

            float x = downLeftPos.x;

            for(int j=0; j < chunk[i].length; j++){

                float z = downLeftPos.y;

                for(int k=0; k < chunk[i][j].length; k++){

                    byte blockType = chunk[i][j][k];

                    if(shouldNotDrawBlock(i, j, k)){
                        continue;
                    }

                    appendBlockToVerticesBuffer(blockType, x, y, z, vertices, quads, textureCords, texturesForVertices, verticesBuffer);

                    visibleBlocks++;

                    z += 16;
                }

                x += 16;
            }

            y += 16;
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

        int[] cubeTexturesIndexes = getCubeTexturesIndexes(blockType);

        int quadIndex = 0;

        for(int i = 0; i < 6; i++){

            int textureIndex = cubeTexturesIndexes[i];

            TexturePosition texturePosition = getTexturePosition(textureIndex);

            for(int j = 0; j < 4; j++, quadIndex++){

                Integer quadVertexIndex = quads[quadIndex];
                Vector3f vertex = vertices[quadVertexIndex];

                Integer texturesForVertex = texturesForVertices[quadIndex];
                Vector2f textureCord = textureCords[texturesForVertex];

                float u = getGlobalTextureCord(CubeTextures.COMBINED_TEXTURE_TOTAL_WIDTH, textureCord.x, texturePosition.col());
                float v = getGlobalTextureCord(CubeTextures.COMBINED_TEXTURE_TOTAL_HEIGHT, textureCord.y, texturePosition.row());

                verticesBuffer.put(vertex.x + x).put(vertex.y + y).put(vertex.z + z);
                verticesBuffer.put(textureCord.x).put(textureCord.y);
            }
        }
    }

    private boolean shouldNotDrawBlock(int i, int j, int k){

        byte blockType = chunk[i][j][k];

        return blockType == 0 || isBlockHidden(j, i, k);
    }

    private int[] getCubeTexturesIndexes(byte blockType){

        return CubeTextures.getCubeTextures(blockType);
    }

    private TexturePosition getTexturePosition(int textureIndex){

        return CubeTextures.getTexturePosition(textureIndex);
    }

    private float getGlobalTextureCord(float maxGlobalCord, float localCord, int globalCordIndex){

        return ((float) globalCordIndex * 16 + localCord * 16) / maxGlobalCord;
    }

    private boolean isBlockHidden(int x, int y, int z){

        return (x > 0 && chunk[y][x - 1][z] != 0) &&
                (x < chunk[0].length - 1 && chunk[y][x + 1][z] != 0) &&

                (y > 0 && chunk[y - 1][x][z] != 0) &&
                (y < chunk.length - 1 && chunk[y + 1][x][z] != 0) &&

                (z > 0 && chunk[y][x][z - 1] != 0) &&
                (z < chunk[0][0].length - 1 && chunk[y][x][z + 1] != 0);
    }

}
