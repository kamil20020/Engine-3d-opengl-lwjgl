package org.example;

import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Mesh {

    private final Vector3f[] vertices;
    private final Integer[] triangles;
    private final Vector2f[] textureCords;
    private final Texture texture;

    public Mesh(Vector3f[] vertices, Integer[] triangles, Vector2f[] textureCords, Texture texture){

        this.vertices = vertices;
        this.triangles = triangles;
        this.textureCords = textureCords;
        this.texture = texture;
    }

    public void draw(){

        texture.useTexture();

        glBegin(GL_QUADS);

        for(int i = 0; i < triangles.length; i += 4){

            for(int j = 0; j < 4; j++){

                int vIndex = triangles[i + j];

                Vector3f v = vertices[vIndex];
                Vector2f textureCordsV = textureCords[vIndex];

                glTexCoord2f(textureCordsV.x, textureCordsV.y);
                glVertex3f(v.x, v.y, v.z);
            }
        }

        glEnd();
    }
}
