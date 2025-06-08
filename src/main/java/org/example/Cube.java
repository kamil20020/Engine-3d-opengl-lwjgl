package org.example;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Cube extends Mesh{

    public Cube(Vector3f pos, int a, Texture texture){

        super(getVertices(pos, a), getTriangles(), getTextureCords(), texture);
    }

    private static Vector3f[] getVertices(Vector3f pos, int a){

        int x = (int) pos.x;
        int y = (int) pos.y;
        int z = (int) pos.z;

        int xFar = x + a;
        int yFar = y + a;
        int zFar = z + a;

        Vector3f[] vertices = new Vector3f[8];

        vertices[0] = new Vector3f(x, y, zFar);
        vertices[1] = new Vector3f(xFar, y, zFar);
        vertices[2] = new Vector3f(xFar, yFar, zFar);
        vertices[3] = new Vector3f(x, yFar, zFar);

        vertices[4] = new Vector3f(x, y, z);
        vertices[5] = new Vector3f(xFar, y, z);
        vertices[6] = new Vector3f(xFar, yFar, z);
        vertices[7] = new Vector3f(x, yFar, z);

        return vertices;
    }

    private static Integer[] getTriangles(){

        Integer[] triangles = new Integer[]{
            0, 1, 2, 3,    // front face
            5, 4, 7, 6,    // back face
            4, 0, 3, 7,    // left face
            1, 5, 6, 2,    // right face
            3, 2, 6, 7,    // top face
            4, 5, 1, 0     // bottom face
        };

        return triangles;
    }

    private static Vector2f[] getTextureCords(){

        Vector2f[] textureCords = new Vector2f[24];

        textureCords[0]  = new Vector2f(0.0f, 0.0f);
        textureCords[1]  = new Vector2f(1.0f, 0.0f);
        textureCords[2]  = new Vector2f(1.0f, 1.0f);
        textureCords[3]  = new Vector2f(0.0f, 1.0f);

        textureCords[4]  = new Vector2f(0.0f, 0.0f);
        textureCords[5]  = new Vector2f(1.0f, 0.0f);
        textureCords[6]  = new Vector2f(1.0f, 1.0f);
        textureCords[7]  = new Vector2f(0.0f, 1.0f);

        textureCords[8]  = new Vector2f(0.0f, 0.0f);
        textureCords[9]  = new Vector2f(1.0f, 0.0f);
        textureCords[10] = new Vector2f(1.0f, 1.0f);
        textureCords[11] = new Vector2f(0.0f, 1.0f);

        textureCords[12] = new Vector2f(0.0f, 0.0f);
        textureCords[13] = new Vector2f(1.0f, 0.0f);
        textureCords[14] = new Vector2f(1.0f, 1.0f);
        textureCords[15] = new Vector2f(0.0f, 1.0f);

        textureCords[16] = new Vector2f(0.0f, 0.0f);
        textureCords[17] = new Vector2f(1.0f, 0.0f);
        textureCords[18] = new Vector2f(1.0f, 1.0f);
        textureCords[19] = new Vector2f(0.0f, 1.0f);

        textureCords[20] = new Vector2f(0.0f, 0.0f);
        textureCords[21] = new Vector2f(1.0f, 0.0f);
        textureCords[22] = new Vector2f(1.0f, 1.0f);
        textureCords[23] = new Vector2f(0.0f, 1.0f);

        return textureCords;
    }
}
