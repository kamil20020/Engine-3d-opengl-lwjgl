package org.example;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.glVertex3f;

public class Cube extends Mesh{

    public Cube(Vector3f pos, int a){

        super(getVertices(pos, a), getTriangles());
    }

    private static Vector3f[] getVertices(Vector3f pos, int a){

        int x = (int) pos.x;
        int y = (int) pos.y;
        int z = (int) pos.z;

        int xFar = x + a;
        int yFar = y + a;
        int zFar = z + a;

        Vector3f[] vertices = new Vector3f[8];

        vertices[0] = new Vector3f(x, y, z);
        vertices[1] = new Vector3f(x, y, zFar);
        vertices[2] = new Vector3f(xFar, y, zFar);
        vertices[3] = new Vector3f(xFar, y, z);
        vertices[4] = new Vector3f(x, yFar, z);
        vertices[5] = new Vector3f(x, yFar, zFar);
        vertices[6] = new Vector3f(xFar, yFar, zFar);
        vertices[7] = new Vector3f(xFar, yFar, z);

        return vertices;
    }

    private static Integer[] getTriangles(){

        Integer[] triangles = new Integer[]{
            0, 1, 2,
            0, 2, 3,

            4, 6, 5,
            4, 7, 6,

            1, 5, 6,
            1, 6, 2,

            0, 7, 4,
            0, 3, 7,

            0, 4, 5,
            0, 5, 1,

            3, 2, 6,
            3, 6, 7
        };

        return triangles;
    }
}
