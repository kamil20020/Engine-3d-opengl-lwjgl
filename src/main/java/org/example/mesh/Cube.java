package org.example.mesh;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Cube extends Mesh{

    public Cube(int a){

        super(getVertices(a), getQuads(), getTextureCords(), getTextureCordsForVertices());
    }

    public static Vector3f[] getVertices(int a){

        int x = 0;
        int y = 0;
        int z = 0;

        int xFar = x + a;
        int yFar = y + a;
        int zFar = z + a;

        Vector3f[] vertices = new Vector3f[8];

        vertices[0] = new Vector3f(x, y, zFar);
        vertices[1] = new Vector3f(x, y, z);
        vertices[2] = new Vector3f(xFar, y, zFar);
        vertices[3] = new Vector3f(xFar, y, z);

        vertices[4] = new Vector3f(x, yFar, zFar);
        vertices[5] = new Vector3f(x, yFar, z);
        vertices[6] = new Vector3f(xFar, yFar, zFar);
        vertices[7] = new Vector3f(xFar, yFar, z);

        return vertices;
    }

    public static Integer[] getQuads(){

        Integer[] getQuads = new Integer[]{
            1, 0, 2, 3, // bottom
            5, 7, 6, 4, // top
            1, 3, 7, 5, // front
            2, 0, 4, 6, // back
            0, 1, 5, 4, // right
            3, 2, 6, 7  // left
        };

        return getQuads;
    }

    public static Vector2f[] getTextureCords(){

        Vector2f[] textureCords = new Vector2f[24];

        textureCords[0]  = new Vector2f(0.0f, 0.0f);
        textureCords[1]  = new Vector2f(1.0f, 0.0f);
        textureCords[2]  = new Vector2f(1.0f, 1.0f);
        textureCords[3]  = new Vector2f(0.0f, 1.0f);

        return textureCords;
    }

    public static Integer[] getTextureCordsForVertices(){

        Integer[] textureCordsForVertices = new Integer[]{
            0, 1, 2, 3,
            0, 1, 2, 3,
            0, 1, 2, 3,
            0, 1, 2, 3,
            0, 1, 2, 3,
            0, 1, 2, 3
        };

        return textureCordsForVertices;
    }

}
