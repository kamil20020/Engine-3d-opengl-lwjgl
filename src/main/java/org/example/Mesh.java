package org.example;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Mesh {

    private Vector3f[] vertices;
    private Integer[] triangles;

    public Mesh(Vector3f[] vertices, Integer[] triangles){

        this.vertices = vertices;
        this.triangles = triangles;
    }

    public void draw(){

        glBegin(GL_TRIANGLES);

        glColor3f(0, 1, 0);

        for(int i = 0; i < triangles.length - 3; i += 3){

            Vector3f v1 = vertices[triangles[i]];
            Vector3f v2 = vertices[triangles[i + 1]];
            Vector3f v3 = vertices[triangles[i + 2]];

            glVertex3f(v1.x, v1.y, v1.z);
            glVertex3f(v2.x, v2.y, v2.z);
            glVertex3f(v3.x, v3.y, v3.z);
        }

        glEnd();
    }
}
