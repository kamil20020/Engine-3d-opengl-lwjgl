package org.example.mesh;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Plant extends Mesh{

    private static final int DEFAULT_A = 16;

    private static Plant INSTANCE = getInstance();

    public static Plant getInstance(){

        if(INSTANCE == null){

            INSTANCE = new Plant();
        }

        return INSTANCE;
    }

    private Plant(){

        super(initVertices(DEFAULT_A), initQuads(), initTextureCords(), initTextureCordsForVertices());
    }

    private static Vector3f[] initVertices(int a){

        float x = 0;
        float y = 0;
        float z = 0;

        float xFar = x + a;
        float yFar = y + a;
        float zFar = z + a;

        float xFar2 = xFar / 2;
        float zFar2 = zFar / 2;

        Vector3f[] vertices = new Vector3f[8];

        vertices[0] = new Vector3f(xFar2, y, zFar);
        vertices[1] = new Vector3f(xFar2, yFar, zFar);
        vertices[2] = new Vector3f(xFar2, y, z);
        vertices[3] = new Vector3f(xFar2, yFar, z);

        vertices[4] = new Vector3f(x, y, zFar2);
        vertices[5] = new Vector3f(x, yFar, zFar2);
        vertices[6] = new Vector3f(xFar, y, zFar2);
        vertices[7] = new Vector3f(xFar, yFar, zFar2);

        return vertices;
    }

    private static Integer[] initQuads(){

        Integer[] getQuads = new Integer[]{
            0, 1, 2, 3,
            4, 5, 6, 7,
        };

        return getQuads;
    }

    private static Vector2f[] initTextureCords(){

        Vector2f[] textureCords = new Vector2f[24];

        textureCords[0] = new Vector2f(0.0f, 0.0f);
        textureCords[1] = new Vector2f(1.0f, 0.0f);
        textureCords[2] = new Vector2f(1.0f, 1.0f);
        textureCords[3] = new Vector2f(0.0f, 1.0f);

        return textureCords;
    }

    private static Integer[] initTextureCordsForVertices(){

        Integer[] textureCordsForVertices = new Integer[]{
            0, 1, 2, 3,
            0, 1, 2, 3,
        };

        return textureCordsForVertices;
    }

}
