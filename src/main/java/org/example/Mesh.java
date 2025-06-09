package org.example;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Mesh {

    private final Vector3f[] vertices;
    private final Integer[] triangles;
    private final Vector2f[] textureCords;
    private final Integer[] textureCordsForVertices;
    private final List<Texture> textures;

    public Mesh(Vector3f[] vertices, Integer[] triangles, Vector2f[] textureCords,  Integer[] textureCordsForVertices, List<Texture> textures){

        this.vertices = vertices;
        this.triangles = triangles;
        this.textureCords = textureCords;
        this.textureCordsForVertices = textureCordsForVertices;
        this.textures = textures;
    }

    public Mesh(Vector3f[] vertices, Integer[] triangles, Vector2f[] textureCords,  Integer[] textureCordsForVertices, Texture texture){

        this(vertices, triangles, textureCords, textureCordsForVertices, new ArrayList<>());

        for(int i = 0; i < triangles.length / 4; i++){

            textures.add(texture);
        }
    }

    public void draw(){

        for(int i = 0, textureIndex = 0; i < triangles.length; i += 4, textureIndex++){

            Texture texture = textures.get(textureIndex);

            texture.useTexture();

            glBegin(GL_QUADS);

            for(int j = 0; j < 4; j++){

                int vIndex = triangles[i + j];

                Vector3f v = vertices[vIndex];

                int vTextureCordsForVertexIndex = textureCordsForVertices[i + j];
                Vector2f textureCordsV = textureCords[vTextureCordsForVertexIndex];

                glTexCoord2f(textureCordsV.x, textureCordsV.y);
                glVertex3f(v.x, v.y, v.z);
            }

            glEnd();
        }
    }
}
